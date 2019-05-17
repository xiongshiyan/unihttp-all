package top.jfunc.common.http.smart;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import top.jfunc.common.http.Method;
import top.jfunc.common.http.ParamUtil;
import top.jfunc.common.http.base.Config;
import top.jfunc.common.http.base.ContentCallback;
import top.jfunc.common.http.base.ResultCallback;
import top.jfunc.common.http.basic.OkHttp3Client;
import top.jfunc.common.utils.IoUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * @author xiongshiyan at 2018/1/11
 */
public class OkHttp3SmartHttpClient extends OkHttp3Client implements SmartHttpClient , SmartHttpTemplate<okhttp3.Request.Builder>{

    @Override
    public OkHttp3SmartHttpClient setConfig(Config config) {
        super.setConfig(config);
        return this;
    }

    @Override
    public <R> R template(top.jfunc.common.http.smart.Request request, Method method , ContentCallback<okhttp3.Request.Builder> contentCallback , ResultCallback<R> resultCallback) throws IOException {
        okhttp3.Response response = null;
        InputStream inputStream = null;
        try {
            String completedUrl = handleUrlIfNecessary(request.getUrl() , request.getRouteParams() ,request.getParams() , request.getBodyCharset() , method);

            //1.构造OkHttpClient
            OkHttpClient.Builder clientBuilder = new OkHttpClient().newBuilder()
                    .connectTimeout(getConnectionTimeoutWithDefault(request.getConnectionTimeout()), TimeUnit.MILLISECONDS)
                    .readTimeout(getReadTimeoutWithDefault(request.getReadTimeout()), TimeUnit.MILLISECONDS);
            //1.1如果存在就设置代理
            if(null != request.getProxyInfo()){
                clientBuilder.proxy(request.getProxyInfo().getProxy());
            }

            ////////////////////////////////////ssl处理///////////////////////////////////
            if(ParamUtil.isHttps(completedUrl)){
                initSSL(clientBuilder , RequestSSLUtil.getHostnameVerifier(request , getHostnameVerifier()) ,
                        RequestSSLUtil.getSSLSocketFactory(request , getSSLSocketFactory()) ,
                        RequestSSLUtil.getX509TrustManager(request , getX509TrustManager()));
            }
            ////////////////////////////////////ssl处理///////////////////////////////////

            //给子类复写的机会
            doWithBuilder(clientBuilder , ParamUtil.isHttps(completedUrl));

            OkHttpClient client = clientBuilder.build();

            doWithClient(client);

            //2.1设置URL
            okhttp3.Request.Builder builder = new okhttp3.Request.Builder().url(completedUrl);

            //2.2设置headers
            setRequestHeaders(builder , request.getContentType() , mergeDefaultHeaders(request.getHeaders()));

            //2.3处理请求体
            if(null != contentCallback && method.hasContent()){
                contentCallback.doWriteWith(builder);
            }

            //3.构造请求
            okhttp3.Request okRequest = builder.build();

            //4.执行请求
            response = client.newCall(okRequest).execute();

            //5.获取响应
            inputStream = getStreamFrom(response , request.isIgnoreResponseBody());

            int statusCode = response.code();
            return resultCallback.convert(statusCode , inputStream, getResultCharsetWithDefault(request.getResultCharset()), parseHeaders(response , request.isIncludeHeaders()));
        } catch (IOException e) {
            throw e;
        } catch (Exception e){
            throw new RuntimeException(e);
        } finally {
            IoUtil.close(inputStream);
            IoUtil.close(response);
        }
    }



    @Override
    public Response get(Request req) throws IOException {
        Request request = beforeTemplate(req);
        Response response = template(request , Method.GET , null , Response::with);
        return afterTemplate(request , response);
    }
    /**
     * @param req 请求体的编码，不支持，需要在contentType中指定
     */
    @Override
    public Response post(Request req) throws IOException {
        Request request = beforeTemplate(req);
        String body = request.getBodyIfNullWithParams();
        Response response = template(request, Method.POST ,
                d -> setRequestBody(d, Method.POST, stringBody(body, request.getContentType())), Response::with);
        return afterTemplate(request , response);
    }


    @Override
    public Response httpMethod(Request req, Method method) throws IOException {
        Request request = beforeTemplate(req);
        ContentCallback<okhttp3.Request.Builder> contentCallback = null;
        if(method.hasContent()){
            String body = request.getBodyIfNullWithParams();
            contentCallback = d -> setRequestBody(d, method, stringBody(body, request.getContentType()));
        }
        Response response = template(request, method , contentCallback , Response::with);
        return afterTemplate(request , response);
    }


    @Override
    public byte[] getAsBytes(Request req) throws IOException {
        Request request = beforeTemplate(req);
        return template(request , Method.GET , null , (s, b, r, h)-> IoUtil.stream2Bytes(b));
    }

    @Override
    public File getAsFile(Request req) throws IOException {
        Request request = beforeTemplate(req);
        return template(request , Method.GET , null , (s, b, r, h)-> IoUtil.copy2File(b, request.getFile()));
    }

    @Override
    public Response upload(Request req) throws IOException {
        Request request = beforeTemplate(req);
        MultipartBody requestBody = filesBody(request.getParams() , request.getFormFiles());
        Response response = template(request, Method.POST ,
                d -> setRequestBody(d, Method.POST, requestBody) ,
                Response::with);
        return afterTemplate(request , response);
    }

    @Override
    public Response afterTemplate(Request request, Response response) throws IOException{
        if(request.isRedirectable() && response.needRedirect()){
            return get(Request.of(response.getRedirectUrl()));
        }
        return response;
    }
}
