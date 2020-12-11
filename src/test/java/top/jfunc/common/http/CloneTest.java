package top.jfunc.common.http;

import org.junit.Ignore;
import org.junit.Test;
import top.jfunc.common.http.base.FormFile;
import top.jfunc.common.http.holderrequest.DefaultUpLoadRequest;
import top.jfunc.common.http.request.UploadRequest;
import top.jfunc.common.utils.ObjectUtil;

import java.io.File;

/**
 * @author xiongshiyan at 2020/2/17 , contact me with email yanshixiong@126.com or phone 15208384257
 */
@Ignore
public class CloneTest {
    @Test
    public void testClone() throws Exception{
        top.jfunc.common.http.request.DefaultUpLoadRequest defaultDefaultUpLoadRequest = top.jfunc.common.http.request.DefaultUpLoadRequest.of("sss");
        defaultDefaultUpLoadRequest.addFormParam("k1" , "v1");
        defaultDefaultUpLoadRequest.setParamCharset("dsd");
        defaultDefaultUpLoadRequest.addFormFile(new FormFile(new File("C:\\Users\\xiongshiyan\\Desktop\\加班.txt") , "dsad" ,"sda"));
        defaultDefaultUpLoadRequest.followRedirects(true);


        top.jfunc.common.http.request.DefaultUpLoadRequest clone = defaultDefaultUpLoadRequest.clone();

        clone.followRedirects(false);
        clone.retainResponseHeaders(true);
        clone.ignoreResponseBody(true);
        clone.addFormParam("k2" , "v2");
        clone.addQueryParam("dd" , "sdsad");
        System.out.println(clone);
    }
    @Test
    public void testClone2() throws Exception{
        DefaultUpLoadRequest defaultUpLoadRequest = DefaultUpLoadRequest.of("sss");
        defaultUpLoadRequest.addFormParam("k1" , "v1");
        defaultUpLoadRequest.setParamCharset("dsd");
        defaultUpLoadRequest.addFormFile(new FormFile(new File("C:\\Users\\xiongshiyan\\Desktop\\加班.txt") , "dsad" ,"sda"));
        defaultUpLoadRequest.followRedirects(true);


        DefaultUpLoadRequest clone = defaultUpLoadRequest.clone();

        clone.followRedirects(false);
        clone.retainResponseHeaders(true);
        clone.ignoreResponseBody(true);
        clone.addFormParam("k2" , "v2");
        clone.addQueryParam("dd" , "sdsad");
        System.out.println(clone);
    }

    @Test
    public void testClone3() throws Exception{
        UploadRequest upLoadRequest = top.jfunc.common.http.request.DefaultUpLoadRequest.of("sss");
        upLoadRequest.addFormParam("k1" , "v1");
        upLoadRequest.setParamCharset("dsd");
        upLoadRequest.addFormFile(new FormFile(new File("C:\\Users\\xiongshiyan\\Desktop\\加班.txt") , "dsad" ,"sda"));
        upLoadRequest.followRedirects(true);


        UploadRequest clone = ObjectUtil.clone(upLoadRequest);

        upLoadRequest.setResultCharset("sss");
        clone.addAttribute("a" , "a");

        System.out.println(clone);
    }
}
