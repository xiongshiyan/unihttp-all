package top.jfunc.http;

import org.junit.Ignore;
import org.junit.Test;
import top.jfunc.http.download.Downloader;
import top.jfunc.http.download.InterruptBaseDownloadFileDownloader;
import top.jfunc.http.download.InterruptBaseConfFileDownloader;
import top.jfunc.http.download.MultiThreadDownloader;
import top.jfunc.http.request.DownloadRequest;
import top.jfunc.http.request.RequestCreator;
import top.jfunc.http.smart.*;
import top.jfunc.common.utils.CommonUtil;

import java.io.File;
import java.io.IOException;

/**
 * @author xiongshiyan at 2020/2/15 , contact me with email yanshixiong@126.com or phone 15208384257
 */
@Ignore
public class DownloadTest {
    private static final String FILE = "https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=974248807,3098128878&fm=26&gp=0.jpg";
    DownloadRequest defaultDownLoadRequest = RequestCreator.download(FILE
        , new File("C:\\Users\\xiongshiyan\\Desktop\\" + CommonUtil.randomString(16) + ".jpg"));
    @Test
    public void testCommonDownloadJDK() throws IOException{
        //1373
        JdkSmartHttpClient smartHttpClient = new JdkSmartHttpClient();
        commonDownload(smartHttpClient);
    }
    @Test
    public void testCommonDownloadApache() throws IOException{
        //5195
        ApacheSmartHttpClient smartHttpClient = new ApacheSmartHttpClient();
        commonDownload(smartHttpClient);
    }
    @Test
    public void testCommonDownloadOkHttp3() throws IOException{
        //1705
        OkHttp3SmartHttpClient smartHttpClient = new OkHttp3SmartHttpClient();
        commonDownload(smartHttpClient);
    }
    @Test
    public void testCommonDownloadJodd() throws IOException{
        //1655
        JoddSmartHttpClient smartHttpClient = new JoddSmartHttpClient();
        commonDownload(smartHttpClient);
    }
    private void commonDownload(SmartHttpClient smartHttpClient) throws IOException{
        long l = System.currentTimeMillis();
        smartHttpClient.download(defaultDownLoadRequest);
        System.out.println(System.currentTimeMillis() - l);
    }





    @Test
    public void testMultiThreadJDK() throws IOException{
        //2007
        JdkSmartHttpClient smartHttpClient = new JdkSmartHttpClient();
        multiThreadDownload(smartHttpClient);
    }
    @Test
    public void testMultiThreadApache() throws IOException{
        //3654
        ApacheSmartHttpClient smartHttpClient = new ApacheSmartHttpClient();
        multiThreadDownload(smartHttpClient);
    }
    @Test
    public void testMultiThreadOkHttp3() throws IOException{
        //7027
        OkHttp3SmartHttpClient smartHttpClient = new OkHttp3SmartHttpClient();
        multiThreadDownload(smartHttpClient);
    }
    @Test
    public void testMultiThreadJodd() throws IOException{
        //2020
        JoddSmartHttpClient smartHttpClient = new JoddSmartHttpClient();
        multiThreadDownload(smartHttpClient);
    }

    private void multiThreadDownload(SmartHttpClient smartHttpClient) throws IOException{
        long l = System.currentTimeMillis();
        MultiThreadDownloader downloader = new MultiThreadDownloader(smartHttpClient , 102400 , 10);
        downloader.download(defaultDownLoadRequest);
        System.out.println(System.currentTimeMillis() - l);
    }





    @Test
    public void testInterruptBaseConfFileDownloaderJDK() throws IOException{
        JdkSmartHttpClient smartHttpClient = new JdkSmartHttpClient();
        interruptBaseConfFileDownloader(smartHttpClient);
    }
    @Test
    public void testInterruptBaseConfFileDownloaderApache() throws IOException{
        ApacheSmartHttpClient smartHttpClient = new ApacheSmartHttpClient();
        interruptBaseConfFileDownloader(smartHttpClient);
    }
    @Test
    public void testInterruptBaseConfFileDownloaderOkHttp3() throws IOException{
        OkHttp3SmartHttpClient smartHttpClient = new OkHttp3SmartHttpClient();
        interruptBaseConfFileDownloader(smartHttpClient);
    }
    @Test
    public void testInterruptBaseConfFileDownloaderJodd() throws IOException{
        JoddSmartHttpClient smartHttpClient = new JoddSmartHttpClient();
        interruptBaseConfFileDownloader(smartHttpClient);
    }

    private void interruptBaseConfFileDownloader(SmartHttpClient smartHttpClient) throws IOException{
        long l = System.currentTimeMillis();
        Downloader downloader = new InterruptBaseConfFileDownloader(smartHttpClient , 1024);
        DownloadRequest defaultDownLoadRequest = RequestCreator.download(FILE
                , new File("C:\\Users\\xiongshiyan\\Desktop\\1.jpg"));
        downloader.download(defaultDownLoadRequest);
        System.out.println(System.currentTimeMillis() - l);
    }



    @Test
    public void testInterruptBaseDownloadFileDownloaderJDK() throws IOException{
        JdkSmartHttpClient smartHttpClient = new JdkSmartHttpClient();
        interruptBaseDownloadFileDownloader(smartHttpClient);
    }
    @Test
    public void testInterruptBaseDownloadFileDownloaderApache() throws IOException{
        ApacheSmartHttpClient smartHttpClient = new ApacheSmartHttpClient();
        interruptBaseDownloadFileDownloader(smartHttpClient);
    }
    @Test
    public void testInterruptBaseDownloadFileDownloaderOkHttp3() throws IOException{
        OkHttp3SmartHttpClient smartHttpClient = new OkHttp3SmartHttpClient();
        interruptBaseDownloadFileDownloader(smartHttpClient);
    }
    @Test
    public void testInterruptBaseDownloadFileDownloaderJodd() throws IOException{
        JoddSmartHttpClient smartHttpClient = new JoddSmartHttpClient();
        interruptBaseDownloadFileDownloader(smartHttpClient);
    }

    private void interruptBaseDownloadFileDownloader(SmartHttpClient smartHttpClient) throws IOException{
        long l = System.currentTimeMillis();
        Downloader downloader = new InterruptBaseDownloadFileDownloader(smartHttpClient , 1024);
        DownloadRequest defaultDownLoadRequest = RequestCreator.download(FILE
                , new File("C:\\Users\\xiongshiyan\\Desktop\\2.jpg"));
        downloader.download(defaultDownLoadRequest);
        System.out.println(System.currentTimeMillis() - l);
    }


    @Test
    public void testGetNetFileLengthJDK() throws IOException{
        JdkSmartHttpClient smartHttpClient = new JdkSmartHttpClient();
        getNetFileLength(smartHttpClient);
    }
    @Test
    public void testGetNetFileLengthApache() throws IOException{
        ApacheSmartHttpClient smartHttpClient = new ApacheSmartHttpClient();
        getNetFileLength(smartHttpClient);
    }
    @Test
    public void testGetNetFileLengthOkHttp3() throws IOException{
        OkHttp3SmartHttpClient smartHttpClient = new OkHttp3SmartHttpClient();
        getNetFileLength(smartHttpClient);
    }
    @Test
    public void testGetNetFileLengthJodd() throws IOException{
        JoddSmartHttpClient smartHttpClient = new JoddSmartHttpClient();
        getNetFileLength(smartHttpClient);
    }

    private void getNetFileLength(SmartHttpClient smartHttpClient) throws IOException{
        Downloader downloader = new InterruptBaseDownloadFileDownloader(smartHttpClient , 1024);
        DownloadRequest defaultDownLoadRequest = RequestCreator.download(FILE
                , new File("C:\\Users\\xiongshiyan\\Desktop\\3.jpg"));
        long netFileLength = downloader.getNetFileLength(defaultDownLoadRequest);
        System.out.println(netFileLength);
    }
}
