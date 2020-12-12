package top.jfunc.http;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import top.jfunc.http.HttpDelegate;
import top.jfunc.http.holderrequest.Request;
import top.jfunc.http.response.Response;

/**
 * @author xiongshiyan at 2019/5/16 , contact me with email yanshixiong@126.com or phone 15208384257
 */
@Ignore
public class RouteParamTest {
    @Test
    public void testRouteParam() throws Exception{
        Request request = Request.of("http://localhost:8080/http-server-test/get/{do}");
        request.routeParamHolder().put("do", "query");
        Response response = HttpDelegate.delegate().get(request);
        Assert.assertEquals("success" , response.getBodyAsString());
    }
}
