package top.jfunc.http;

import org.junit.Test;
import top.jfunc.http.HttpDelegate;
import top.jfunc.http.SmartHttpClient;

public class DelegateTest {
    @Test
    public void testDelegate(){
        SmartHttpClient delegate = HttpDelegate.delegate();
        System.out.println(delegate);
    }
}
