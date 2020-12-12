package top.jfunc.http;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;
import top.jfunc.http.config.Config;
import top.jfunc.http.base.HttpHeaders;
import top.jfunc.http.holderrequest.DefaultRequest;
import top.jfunc.http.request.HttpRequest;
import top.jfunc.http.response.Response;
import top.jfunc.http.smart.*;

import java.io.IOException;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

/**
 * @author xiongshiyan at 2019/5/16 , contact me with email yanshixiong@126.com or phone 15208384257
 */
public class Non200Test {
    @Rule
    public MockServerRule server = new MockServerRule(this, 50000);

    private static final String BODY = "{ message: 'incorrect username and password combination' }";

    @Test
    public void testJdk() throws Exception{
        JdkSmartHttpClient smartHttpClient = new JdkSmartHttpClient();
        test400(smartHttpClient);
        test301(smartHttpClient);
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
        test400(smartHttpClient);
        test301(smartHttpClient);
        test301Redirect(smartHttpClient);
    }

    private void test400(SmartHttpClient smartHttpClient) throws IOException {
        MockServerClient mockClient = new MockServerClient("127.0.0.1", 50000);
        mockClient.when(
                request()
                        .withPath("/hello/400")
                        .withMethod("GET") )
                .respond(
                        response()
                                .withStatusCode(400)
                                .withBody(BODY)
                );

        HttpRequest request = DefaultRequest.of("http://localhost:50000/hello/400");
        Response response = smartHttpClient.get(request);
        Assert.assertEquals(BODY , response.getBodyAsString());
        Assert.assertEquals(400 , response.getStatusCode());
    }
    private void test301(SmartHttpClient smartHttpClient) throws IOException {
        MockServerClient mockClient = new MockServerClient("127.0.0.1", 50000);
        mockClient.when(
                request()
                        .withPath("/hello/301")
                        .withMethod("GET") )
                .respond(
                        response()
                                .withStatusCode(301)
                                .withHeader("k1" , "v1")
                                .withBody(BODY)
                );

        HttpRequest request = DefaultRequest.of("http://localhost:50000/hello/301");

        request.retainResponseHeaders(Config.RETAIN_RESPONSE_HEADERS);

        Response response = smartHttpClient.get(request);
        Assert.assertEquals(BODY , response.getBodyAsString());
        Assert.assertEquals(301 , response.getStatusCode());
        Assert.assertEquals("v1" , response.getFirstHeader("k1"));
    }
    private void test301Redirect(SmartHttpClient smartHttpClient) throws IOException {
        MockServerClient mockClient = new MockServerClient("127.0.0.1", 50000);
        mockClient.when(
                request()
                        .withPath("/hello301/redirect")
                        .withMethod("GET") )
                .respond(
                        response()
                                .withStatusCode(301)
                                .withHeader(HttpHeaders.LOCATION, "https://www.baidu.com")
                                .withBody(BODY)
                );

        HttpRequest request = DefaultRequest.of("http://localhost:50000/hello301/redirect");

        request.followRedirects(Config.FOLLOW_REDIRECTS);

        Response response = smartHttpClient.get(request);
        Assert.assertEquals(200 , response.getStatusCode());
    }
}
