package top.jfunc.common.http;

import org.junit.Test;

public class DelegateTest {
    @Test
    public void testDelegate(){
        SmartHttpClient delegate = HttpDelegate.delegate();
        System.out.println(delegate);
    }
}
