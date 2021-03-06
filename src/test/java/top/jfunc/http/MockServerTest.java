package top.jfunc.http;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.Header;
import org.mockserver.model.Parameter;
import top.jfunc.http.config.Config;
import top.jfunc.http.base.MediaType;
import top.jfunc.http.holderrequest.Request;
import top.jfunc.http.holderrequest.StringBodyRequest;
import top.jfunc.http.response.Response;
import top.jfunc.http.smart.*;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

/**
 * @author xiongshiyan at 2019/5/16 , contact me with email yanshixiong@126.com or phone 15208384257
 */
public class MockServerTest {
    @Rule
    public MockServerRule server = new MockServerRule(this, 50000);
    private static final String BODY = "{ message: 'incorrect username and password combination' }";

    @Test
    public void testJdk() throws Exception{
        JdkSmartHttpClient smartHttpClient = new JdkSmartHttpClient();
        testAll(smartHttpClient);
    }
    @Test
    public void testApache() throws Exception{
        ApacheSmartHttpClient smartHttpClient = new ApacheSmartHttpClient();
        testAll(smartHttpClient);
    }
    @Test
    public void testOkhttp3() throws Exception{
        OkHttp3SmartHttpClient smartHttpClient = new OkHttp3SmartHttpClient();
        testAll(smartHttpClient);
    }
    @Test
    public void testJodd() throws Exception{
        JoddSmartHttpClient smartHttpClient = new JoddSmartHttpClient();
        testAll(smartHttpClient);
    }

    private void testAll(SmartHttpClient smartHttpClient) throws Exception {
        testGet(smartHttpClient);
        testGetQueryParam(smartHttpClient);
        testPost(smartHttpClient);
        testPostForm(smartHttpClient);
        testPostFormBodyCharsetUTF8(smartHttpClient);
        testPostFormBodyCharsetGBK(smartHttpClient);
        testHeader(smartHttpClient);
    }

    private void testGet(SmartHttpClient smartHttpClient) throws Exception{
        MockServerClient mockClient = new MockServerClient("127.0.0.1", 50000);
        mockClient.when(
                request()
                        .withPath("/hello/John")
                        .withMethod("GET") )
        .respond(
                response()
                        .withStatusCode(200)
                        .withBody(BODY)
        );

        Request request = Request.of("http://localhost:50000/hello/{name}");
        request.routeParamHolder().addRouteParam("name" , "John");
        Response response = smartHttpClient.get(request);
        Assert.assertEquals(BODY, response.getBodyAsString());
    }
    private void testGetQueryParam(SmartHttpClient smartHttpClient) throws Exception{
        MockServerClient mockClient = new MockServerClient("127.0.0.1", 50000);
        mockClient.when(
                request()
                        .withPath("/hello/John")
                        .withMethod("GET")
                        .withQueryStringParameters(Parameter.param("key1" , "value1") ,
                                Parameter.param("key2" , "value2")))
        .respond(
                response()
                        .withStatusCode(200)
                        .withBody(BODY)
        );

        Request request = Request.of("http://localhost:50000/hello/{name}");
        request.routeParamHolder().addRouteParam("name" , "John");
        request.queryParamHolder().add("key1" , "value1").add("key2" , "value2");
        Response response = smartHttpClient.get(request);
        Assert.assertEquals(BODY, response.getBodyAsString());
    }
    private void testPost(SmartHttpClient smartHttpClient) throws Exception{
        MockServerClient mockClient = new MockServerClient("127.0.0.1", 50000);
        mockClient.when(
                request()
                        .withPath("/hello/John")
                        .withMethod("POST").withBody(BODY))
        .respond(
                response()
                        .withStatusCode(200)
                        .withBody(BODY)
        );

        StringBodyRequest request = Request.of("http://localhost:50000/hello/{name}").setBody(BODY);
        request.routeParamHolder().addRouteParam("name" , "John");
        Response response = smartHttpClient.post(request);
        Assert.assertEquals(BODY, response.getBodyAsString());
    }
    private void testPostForm(SmartHttpClient smartHttpClient) throws Exception{
        MockServerClient mockClient = new MockServerClient("127.0.0.1", 50000);
        mockClient.when(
                request()
                        .withPath("/hello/John")
                        .withMethod("POST")
                        .withBody("key1=value1&key2=value2")
                        .withHeader(Header.header("Content-Type" , MediaType.APPLICATION_FORM_DATA.withCharset(Config.DEFAULT_CHARSET).toString())))
        .respond(
                response()
                        .withStatusCode(200)
                        .withBody(BODY)
        );

        Request request = Request.of("http://localhost:50000/hello/{name}");
        request.routeParamHolder().addRouteParam("name" , "John");
        request.formParamHolder().add("key1" , "value1").add("key2" , "value2");
        Response response = smartHttpClient.post(request);
        Assert.assertEquals(BODY, response.getBodyAsString());
    }
    private void testPostFormBodyCharsetUTF8(SmartHttpClient smartHttpClient) throws Exception{
        MockServerClient mockClient = new MockServerClient("127.0.0.1", 50000);
        mockClient.when(
                request()
                        .withPath("/hello/John")
                        .withMethod("POST")
                        .withBody("key1=%E7%86%8A%E8%AF%97%E8%A8%80&key2=value2")
                        .withHeader(Header.header("Content-Type" , MediaType.APPLICATION_FORM_DATA.withCharset(Config.DEFAULT_CHARSET).toString())))
        .respond(
                response()
                        .withStatusCode(200)
                        .withBody(BODY)
        );

        Request request = Request.of("http://localhost:50000/hello/{name}");
        request.routeParamHolder().addRouteParam("name" , "John");
        request.formParamHolder().add("key1" , "熊诗言").add("key2" , "value2");
        Response response = smartHttpClient.post(request);
        Assert.assertEquals(BODY, response.getBodyAsString());
    }
    private void testPostFormBodyCharsetGBK(SmartHttpClient smartHttpClient) throws Exception{
        MockServerClient mockClient = new MockServerClient("127.0.0.1", 50000);
        mockClient.when(
                request()
                        .withPath("/hello/John")
                        .withMethod("POST")
                        .withBody("key1=%D0%DC%CA%AB%D1%D4&key2=value2")
                        .withHeader(Header.header("Content-Type" , "application/x-www-form-urlencoded;charset=gbk")))
        .respond(
                response()
                        .withStatusCode(200)
                        .withBody(BODY)
        );

        Request request = Request.of("http://localhost:50000/hello/{name}")
                .setBodyCharset("GBK").addFormParam("key1" , "熊诗言")
                .addFormParam("key2" , "value2");
        request.routeParamHolder().addRouteParam("name" , "John");
        Response response = smartHttpClient.post(request);
        Assert.assertEquals(BODY, response.getBodyAsString());
    }

    private void testHeader(SmartHttpClient smartHttpClient) throws Exception{
        MockServerClient mockClient = new MockServerClient("127.0.0.1", 50000);
        mockClient.reset();
        mockClient.when(
                request()
                        .withPath("/hello/John")
                        .withMethod("GET").withHeader(Header.header("sale" , "2")))
        .respond(
                response()
                        .withStatusCode(200)
                        .withHeader(Header.header("xx" , "xx"))
        );

        Request request = Request.of("http://localhost:50000/hello/{name}").retainResponseHeaders(true);
        request.routeParamHolder().addRouteParam("name" , "John");
        request.headerHolder().add("sale" , "2").add("ca-xx" , "ca-xx");
        Response response = smartHttpClient.get(request);
        Assert.assertEquals("xx" , response.getFirstHeader("xx"));
    }
}
