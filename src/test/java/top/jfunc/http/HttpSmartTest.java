package top.jfunc.http;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import top.jfunc.http.config.Config;
import top.jfunc.http.base.FormFile;
import top.jfunc.http.base.MediaType;
import top.jfunc.http.base.Method;
import top.jfunc.http.holderrequest.DefaultBodyRequest;
import top.jfunc.http.holderrequest.Request;
import top.jfunc.http.response.Response;
import top.jfunc.http.smart.*;

import java.io.File;
import java.io.IOException;

/**
 * @author 熊诗言 2017/11/24
 * @see SmartHttpClient
 */
@Ignore
public class HttpSmartTest {
    @Test
    public void testGetOkHttp3(){
        OkHttp3SmartHttpClient http = new OkHttp3SmartHttpClient();
        http.getConfig().setBaseUrl("http://localhost:8080/http-server-test/");
        testGet(http);
    }
    @Test
    public void testGetApacheHttp(){
        ApacheSmartHttpClient http = new ApacheSmartHttpClient();
        http.getConfig().setBaseUrl("http://localhost:8080/http-server-test/");
        testGet(http);
    }
    @Test
    public void testGetNativeHttp(){
        JdkSmartHttpClient http = new JdkSmartHttpClient();
        http.getConfig().setBaseUrl("http://localhost:8080/http-server-test/");
        testGet(http);
    }
    @Test
    public void testGetJoddHttp(){
        JoddSmartHttpClient http = new JoddSmartHttpClient();
        http.getConfig().setBaseUrl("http://localhost:8080/http-server-test/");
        testGet(http);
    }
    private void testGet(SmartHttpClient http){
        String url = "get/query";
        try {
            Request request1 = Request.of(url).ignoreResponseBody(false).retainResponseHeaders(true).setResultCharset("UTF-8");
            request1.headerHolder().add("saleType" , "2");
            Response response = http.get(request1);
            System.out.println(response);
            System.out.println("headerHolder:" + response.getHeaders());

            String s = http.get(url);
            System.out.println(s);

            Request request = Request.of(url).setResultCharset("UTF-8");
            request.headerHolder().add("saleType" , "2");
            byte[] bytes = http.getAsBytes(request);
            System.out.println(bytes.length);
            System.out.println(new String(bytes));

            request = Request.of(url);
            request.fileHolder().setFile(new File("C:\\Users\\xiongshiyan\\Desktop\\yyyy.txt"));
            File asFile = http.download(request);
            System.out.println(asFile.getAbsolutePath());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @Test
    public void testPostOkHttp3(){
        SmartHttpClient http = new OkHttp3SmartHttpClient();
        testPost(http);
    }
    @Test
    public void testPostApacheHttp(){
        SmartHttpClient http = new ApacheSmartHttpClient();
        testPost(http);
    }
    @Test
    public void testPostNativeHttp(){
        SmartHttpClient http = new JdkSmartHttpClient();
        testPost(http);
    }
    @Test
    public void testPostJoddHttp(){
        SmartHttpClient http = new JoddSmartHttpClient();
        testPost(http);
    }
    @Test
    public void testPostGBKOkHttp3(){
        SmartHttpClient http = new OkHttp3SmartHttpClient();
        testPostGBK(http);
    }
    @Test
    public void testPostGBKApacheHttp(){
        SmartHttpClient http = new ApacheSmartHttpClient();
        testPostGBK(http);
    }
    @Test
    public void testPostGBKNativeHttp(){
        SmartHttpClient http = new JdkSmartHttpClient();
        testPostGBK(http);
    }
    @Test
    public void testPostGBKJoddHttp(){
        SmartHttpClient http = new JoddSmartHttpClient();
        testPostGBK(http);
    }
    public void testPostGBK(SmartHttpClient http){
        try {
            String url = "http://localhost:8080/http-server-test/post/bodyGBK";
            String charset = "GBK";
            Request request = Request.of(url).setResultCharset("UTF-8").setBodyCharset(charset)
                    //设置ContentType非常重要，尤其是对于HTTPURLConnection来说，他写入的时候根据这个编码来写的
                    .setContentType(MediaType.APPLICATION_JSON.withCharset(charset));
            request.bodyHolder().setBody("熊诗言");
            Response post = http.post(request);
            System.out.println(post.getBodyAsString());
        }catch (IOException e){
            System.out.println("超时异常");
        }
    }

    public void testPost(SmartHttpClient http){
        try {
            String url = "http://localhost:8080/http-server-test/post/body";
            MediaType contentType = MediaType.APPLICATION_JSON.withCharset(Config.DEFAULT_CHARSET);
            Request request = Request.of(url).retainResponseHeaders(true).setContentType(contentType).setConnectionTimeout(10000).setReadTimeout(10000).setResultCharset("UTF-8");
            request.bodyHolder().setBody("{\"name\":\"熊诗言\"}");
            request.headerHolder().add("ss" , "ss").add("ss" , "dd");
            Response post = http.post(request);
            System.out.println(post.getBodyAsString());
            System.out.println(post.getHeaders());

            String s = http.postJson(url, "{\"name\":\"熊诗言\"}");
            System.out.println(s);

            url = "http://localhost:8080/http-server-test/post/form";
            MediaType mediaType = MediaType.APPLICATION_FORM_DATA.withCharset(Config.DEFAULT_CHARSET);
            request = Request.of(url).setContentType(mediaType);
            request.formParamHolder().add("xx" , "xx").add("yy" , "yy");
            Response response = http.post(request);
            System.out.println(response.getBodyAsString());
        }catch (IOException e){
            System.out.println("超时异常");
        }
    }

    @Test
    public void testUploadOkHttp3(){
        SmartHttpClient http = new OkHttp3SmartHttpClient();
        testUploadImpl(http);
    }
    @Test
    public void testUploadApache(){
        SmartHttpClient http = new ApacheSmartHttpClient();
        testUploadImpl(http);
    }
    @Test
    public void testUploadLocal(){
        SmartHttpClient http = new JdkSmartHttpClient();
        testUploadImpl(http);
    }
    @Test
    public void testUploadJodd(){
        SmartHttpClient http = new JoddSmartHttpClient();
        testUploadImpl(http);
    }
    private void testUploadImpl(SmartHttpClient httpClient){
        String url = "http://localhost:8080/http-server-test/upload/only";
        try {
            FormFile formFile = new FormFile(new File("E:\\BugReport.png") , "file",null);
            Request request = Request.of(url).retainResponseHeaders(true);
            request.formFileHolder().addFormFile(formFile);
            request.headerHolder().add("empCode" , "ahg0023")
                    .add("phone" , "15208384257");
            Response response = httpClient.upload(request);
            System.out.println(response.getBodyAsString());
            System.out.println(response.getHeaders());
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("超时异常");
        }
    }


    @Test
    public void testUploadWithParamsOkHttp3(){
        SmartHttpClient http = new OkHttp3SmartHttpClient();
        testUploadImplWithParams(http);
    }
    @Test
    public void testUploadWithParamsApache(){
        SmartHttpClient http = new ApacheSmartHttpClient();
        testUploadImplWithParams(http);
    }
    @Test
    public void testUploadWithParamsLocal(){
        SmartHttpClient http = new JdkSmartHttpClient();
        testUploadImplWithParams(http);
    }
    @Test
    public void testUploadWithParamsJodd(){
        SmartHttpClient http = new JoddSmartHttpClient();
        testUploadImplWithParams(http);
    }

    private void testUploadImplWithParams(SmartHttpClient httpClient){
        String url = "http://localhost:8080/http-server-test/upload/withParam";
        try {
            FormFile formFile = new FormFile(new File("E:\\BugReport.3png") , "file",null);
            FormFile formFile2 = new FormFile(new File("E:\\BugReport.png") , "file2",null);
            Request request = Request.of(url).retainResponseHeaders(true);
            request.formFileHolder().addFormFile(formFile2).addFormFile(formFile);
            request.headerHolder().add("empCode" , "ahg0023")
                    .add("phone" , "15208384257");
            request.formParamHolder().add("k1", "v1").add("k2" , "v2");
            Response response = httpClient.upload(request);
            System.out.println(response.getBodyAsString());
            System.out.println(response.getHeaders());
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("超时异常");
        }
    }

    @Test
    public void testHttpMethodOkHttp3(){
        SmartHttpClient http = new OkHttp3SmartHttpClient();
        testHttpMethod(http);
    }
    @Test
    public void testHttpMethodApacheHttp(){
        SmartHttpClient http = new ApacheSmartHttpClient();
        testHttpMethod(http);
    }
    @Test
    public void testHttpMethodNativeHttp(){
        SmartHttpClient http = new JdkSmartHttpClient();
        testHttpMethod(http);
    }
    @Test
    public void testHttpMethodJoddHttp(){
        SmartHttpClient http = new JoddSmartHttpClient();
        testHttpMethod(http);
    }
    public void testHttpMethod(SmartHttpClient http){
//        String url = "https://dzg.palmte.cn/dzdsds";
        String url = "http://localhost:8080/http-server-test/put/body";
        try {
            MediaType mediaType = MediaType.APPLICATION_JSON.withCharset(Config.DEFAULT_CHARSET);
            Request request = Request.of(url).retainResponseHeaders(true).setContentType(mediaType).setConnectionTimeout(10000).setReadTimeout(10000).setResultCharset("UTF-8");
            request.bodyHolder().setBody("{\"name\":\"熊诗言\"}");
            request.headerHolder().add("ss" , "ss").add("ss" , "dd");
            Response response = http.http(request , Method.PUT);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBodyAsString());
            System.out.println(response.getHeaders());
        }catch (IOException e){
            System.out.println("超时异常");
        }
    }



    @Test
    public void testAllApache() throws Exception{
        testAll(new ApacheSmartHttpClient());
    }
    @Test
    public void testAllNative() throws Exception{
        testAll(new JdkSmartHttpClient());
    }
    @Test
    public void testAllJodd() throws Exception{
        testAll(new JoddSmartHttpClient());
    }
    @Test
    public void testAllOkHttp() throws Exception{
        testAll(new OkHttp3SmartHttpClient());
    }
    private void testAll(SmartHttpClient smartHttpClient) throws Exception{

        MediaType mediaType = MediaType.APPLICATION_JSON.withCharset(Config.DEFAULT_CHARSET);
        top.jfunc.http.holderrequest.MutableStringBodyRequest request = DefaultBodyRequest.of("http://localhost:8080/http-server-test/post/all");
        request.setBody("xxxxx", mediaType.toString()).retainResponseHeaders(true);
        request.headerHolder().add("sale" , "2").add("ca-xx" , "ca-xx");
        request.queryParamHolder().add("sa" , "sa").add("ds" , "ds");
        Response response = smartHttpClient.post(request);
        Assert.assertEquals("success" , response.getBodyAsString());
        System.out.println(response.getHeaders());
    }


    @Test
    public void testRequest(){
        Request request = Request.of("https://wwww.baidu.com");
        request.bodyHolder().setBodyT(new SomeBean("ss"), SomeBean::getSs);
        Assert.assertEquals("ss" , request.getBody());
    }
    @Test
    public void testRequest2(){
        Request request = Request.of("https://wwww.baidu.com");
        request.bodyHolder().setBody(new SomeBean("ss"), (o)->"sssss");
        Assert.assertEquals("sssss" , request.getBody());
    }

    private static class SomeBean{
        private String ss;

        public SomeBean() {
        }

        public SomeBean(String ss) {
            this.ss = ss;
        }

        public String getSs() {
            return ss;
        }

        public void setSs(String ss) {
            this.ss = ss;
        }
    }
}
