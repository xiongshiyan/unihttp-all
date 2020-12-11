package top.jfunc.common.http;

import org.junit.Assert;
import org.junit.Test;
import top.jfunc.common.http.base.Config;
import top.jfunc.common.http.request.DefaultRequest;
import top.jfunc.common.utils.ObjectUtil;

public class BooleanTest {
    @Test
    public void testBoolean(){
        DefaultRequest request = DefaultRequest.of();
        Config config = Config.defaultConfig();
        //Request未指定的时候使用Config的
        Assert.assertFalse(ObjectUtil.defaultIfNull(request.retainResponseHeaders() , config.retainResponseHeaders()));

        //request指定了使用Request的
        request.retainResponseHeaders(Config.RETAIN_RESPONSE_HEADERS);
        Assert.assertTrue(ObjectUtil.defaultIfNull(request.retainResponseHeaders() , config.retainResponseHeaders()));

        //Request未指定的时候使用Config的，Config改变过的
        config.retainResponseHeaders(Config.RETAIN_RESPONSE_HEADERS);
        DefaultRequest defaultRequest = DefaultRequest.of();
        Assert.assertTrue(ObjectUtil.defaultIfNull(defaultRequest.retainResponseHeaders() , config.retainResponseHeaders()));
    }
}
