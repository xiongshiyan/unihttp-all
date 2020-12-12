package top.jfunc.http;

import org.junit.Assert;
import org.junit.Test;
import top.jfunc.http.base.Config;
import top.jfunc.http.base.MediaType;
import top.jfunc.http.holderrequest.Request;

/**
 * @author xiongshiyan at 2019/6/21 , contact me with email yanshixiong@126.com or phone 15208384257
 */
public class GenericTest {
    /**
     * 测试方法连缀
     */
    @Test
    public void testThis(){
        Request request = Request.of("https://somedomain:port/get/{id}").addHeader("","")
                .addFormParam("k1","v1").addRouteParam("id","3");
    }
    @Test
    public void testMediaType(){
        Assert.assertEquals("application/json" , MediaType.APPLICATION_JSON.toString());
        Assert.assertEquals("application/json;charset=utf-8" , MediaType.APPLICATION_JSON.withCharset(Config.DEFAULT_CHARSET).toString());
        Assert.assertEquals("application/x-www-form-urlencoded" , MediaType.APPLICATION_FORM_DATA.toString());
        Assert.assertEquals("application/x-www-form-urlencoded;charset=utf-8" , MediaType.APPLICATION_FORM_DATA.withCharset(Config.DEFAULT_CHARSET).toString());
    }
}
