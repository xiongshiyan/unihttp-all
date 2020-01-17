package top.jfunc.common.http.smart;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import top.jfunc.common.http.base.ContentCallback;
import top.jfunc.common.http.base.ResultCallback;
import top.jfunc.common.http.component.*;
import top.jfunc.common.http.component.okhttp3.*;
import top.jfunc.common.http.request.HttpRequest;
import top.jfunc.common.utils.MultiValueMap;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * 使用OkHttp3 实现的Http请求类
 * @author xiongshiyan at 2018/1/11
 */
public class OkHttp3SmartHttpClient extends AbstractImplementSmartHttpClient<Request.Builder> {


    private RequesterFactory<OkHttpClient> okHttpClientFactory;
    private RequesterFactory<Request.Builder> requestBuilderFactory;
    private HeaderHandler<Request.Builder> requestBuilderHeaderHandler;
    private RequestExecutor<OkHttpClient , Request , Response> requestExecutor;
    private StreamExtractor<Response> responseStreamExtractor;
    private HeaderExtractor<Response> responseHeaderExtractor;

    private Closer responseCloser;

    public OkHttp3SmartHttpClient(){
        setBodyContentCallbackCreator(new DefaultOkHttp3BodyContentCallbackCreator());
        setUploadContentCallbackCreator(new DefaultOkHttp3UploadContentCallbackCreator());

        setOkHttpClientFactory(new DefaultOkHttp3ClientFactory());
        setRequestBuilderFactory(new DefaultOkHttp3RequestBuilderFactory());
        setRequestBuilderHeaderHandler(new DefaultOkHttp3HeaderHandler());
        setRequestExecutor(new DefaultOkHttp3RequestExecutor());
        setResponseStreamExtractor(new DefaultOkHttp3StreamExtractor());
        setResponseHeaderExtractor(new DefaultOkHttp3HeaderExtractor());

        setResponseCloser(new DefaultCloser());
    }


    @Override
    protected <R> R doInternalTemplate(HttpRequest httpRequest , ContentCallback<Request.Builder> contentCallback , ResultCallback<R> resultCallback) throws Exception {
        Response response = null;
        InputStream inputStream = null;
        try {
            //1.创建并配置OkHttpClient
            OkHttpClient client = getOkHttpClientFactory().create(httpRequest);

            //2.1设置URL
            Request.Builder builder = getRequestBuilderFactory().create(httpRequest);

            //2.2处理请求体
            getContentCallbackHandler().handle(builder , contentCallback , httpRequest);

            //2.3设置headers
            handleHeaders(builder , httpRequest);

            //3.执行请求
            response = execute(client, httpRequest, builder);

            //4.获取响应
            inputStream = getResponseStreamExtractor().extract(response , httpRequest);

            //5.处理header，包括Cookie的处理
            MultiValueMap<String, String> responseHeaders = getResponseHeaderExtractor().extract(response, httpRequest);

            //6.处理Cookie
            getCookieAccessor().saveCookieIfNecessary(httpRequest , responseHeaders);

            return resultCallback.convert(response.code(), inputStream,
                    getConfig().getResultCharsetWithDefault(httpRequest.getResultCharset()),
                    responseHeaders);
        } finally {
            closeInputStream(inputStream);
            closeResponse(response);
        }
    }

    private void handleHeaders(Request.Builder builder , HttpRequest httpRequest) throws IOException {
        getCookieAccessor().addCookieIfNecessary(httpRequest);
        getRequestBuilderHeaderHandler().configHeaders(builder , httpRequest);
    }

    protected Response execute(OkHttpClient client, HttpRequest httpRequest, Request.Builder builder) throws IOException {
        return getRequestExecutor().execute(client , builder.build() , httpRequest);
    }

    protected void closeResponse(Response response) throws IOException {
        getResponseCloser().close(response);
    }

    protected void closeInputStream(InputStream inputStream) throws IOException {
        getInputStreamCloser().close(inputStream);
    }


    public RequesterFactory<OkHttpClient> getOkHttpClientFactory() {
        return okHttpClientFactory;
    }

    public void setOkHttpClientFactory(RequesterFactory<OkHttpClient> okHttpClientFactory) {
        this.okHttpClientFactory = Objects.requireNonNull(okHttpClientFactory);
    }

    public RequesterFactory<Request.Builder> getRequestBuilderFactory() {
        return requestBuilderFactory;
    }

    public void setRequestBuilderFactory(RequesterFactory<Request.Builder> requestBuilderFactory) {
        this.requestBuilderFactory = Objects.requireNonNull(requestBuilderFactory);
    }

    public HeaderHandler<Request.Builder> getRequestBuilderHeaderHandler() {
        return requestBuilderHeaderHandler;
    }

    public void setRequestBuilderHeaderHandler(HeaderHandler<Request.Builder> requestBuilderHeaderHandler) {
        this.requestBuilderHeaderHandler = Objects.requireNonNull(requestBuilderHeaderHandler);
    }

    public RequestExecutor<OkHttpClient, Request, Response> getRequestExecutor() {
        return requestExecutor;
    }

    public void setRequestExecutor(RequestExecutor<OkHttpClient, Request, Response> requestExecutor) {
        this.requestExecutor = Objects.requireNonNull(requestExecutor);
    }

    public StreamExtractor<Response> getResponseStreamExtractor() {
        return responseStreamExtractor;
    }

    public void setResponseStreamExtractor(StreamExtractor<Response> responseStreamExtractor) {
        this.responseStreamExtractor = Objects.requireNonNull(responseStreamExtractor);
    }

    public HeaderExtractor<Response> getResponseHeaderExtractor() {
        return responseHeaderExtractor;
    }

    public void setResponseHeaderExtractor(HeaderExtractor<Response> responseHeaderExtractor) {
        this.responseHeaderExtractor = Objects.requireNonNull(responseHeaderExtractor);
    }

    public Closer getResponseCloser() {
        return responseCloser;
    }

    public void setResponseCloser(Closer responseCloser) {
        this.responseCloser = Objects.requireNonNull(responseCloser);
    }

    @Override
    public String toString() {
        return "SmartHttpClient implemented by square's OkHttp3";
    }
}
