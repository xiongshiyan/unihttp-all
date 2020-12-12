package top.jfunc.common.http;

import org.junit.Ignore;
import org.junit.Test;
import top.jfunc.common.http.base.FormFile;
import top.jfunc.common.http.holderrequest.DefaultUploadRequest;
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
        top.jfunc.common.http.request.DefaultUploadRequest defaultDefaultUploadRequest = top.jfunc.common.http.request.DefaultUploadRequest.of("sss");
        defaultDefaultUploadRequest.addFormParam("k1" , "v1");
        defaultDefaultUploadRequest.setParamCharset("dsd");
        defaultDefaultUploadRequest.addFormFile(new FormFile(new File("C:\\Users\\xiongshiyan\\Desktop\\加班.txt") , "dsad" ,"sda"));
        defaultDefaultUploadRequest.followRedirects(true);


        top.jfunc.common.http.request.DefaultUploadRequest clone = defaultDefaultUploadRequest.clone();

        clone.followRedirects(false);
        clone.retainResponseHeaders(true);
        clone.ignoreResponseBody(true);
        clone.addFormParam("k2" , "v2");
        clone.addQueryParam("dd" , "sdsad");
        System.out.println(clone);
    }
    @Test
    public void testClone2() throws Exception{
        DefaultUploadRequest defaultUploadRequest = DefaultUploadRequest.of("sss");
        defaultUploadRequest.addFormParam("k1" , "v1");
        defaultUploadRequest.setParamCharset("dsd");
        defaultUploadRequest.addFormFile(new FormFile(new File("C:\\Users\\xiongshiyan\\Desktop\\加班.txt") , "dsad" ,"sda"));
        defaultUploadRequest.followRedirects(true);


        DefaultUploadRequest clone = defaultUploadRequest.clone();

        clone.followRedirects(false);
        clone.retainResponseHeaders(true);
        clone.ignoreResponseBody(true);
        clone.addFormParam("k2" , "v2");
        clone.addQueryParam("dd" , "sdsad");
        System.out.println(clone);
    }

    @Test
    public void testClone3() throws Exception{
        UploadRequest upLoadRequest = top.jfunc.common.http.request.DefaultUploadRequest.of("sss");
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
