package top.jfunc.http.holderrequest;

import top.jfunc.http.holder.DefaultFileHolder;
import top.jfunc.http.holder.FileHolder;

import java.io.File;
import java.net.URL;

/**
 * 下载为文件的请求
 * @author xiongshiyan at 2019/5/18 , contact me with email yanshixiong@126.com or phone 15208384257
 */
public class DefaultDownloadRequest extends BaseHttpRequest<DefaultDownloadRequest> implements DownloadRequest {
    public DefaultDownloadRequest(String url){
        super(url);
    }

    public DefaultDownloadRequest(URL url){
        super(url);
    }
    public DefaultDownloadRequest(){
    }

    public static DownloadRequest of(){
        return new DefaultDownloadRequest();
    }
    public static DownloadRequest of(URL url){
        return new DefaultDownloadRequest(url);
    }
    public static DownloadRequest of(String url){
        return new DefaultDownloadRequest(url);
    }
    public static DownloadRequest of(String url , String filePath){
        DefaultDownloadRequest downLoadFileRequest = new DefaultDownloadRequest(url);
        downLoadFileRequest.fileHolder().setFile(filePath);
        return downLoadFileRequest;
    }
    public static DownloadRequest of(String url , File file){
        DefaultDownloadRequest downLoadFileRequest = new DefaultDownloadRequest(url);
        downLoadFileRequest.fileHolder().setFile(file);
        return downLoadFileRequest;
    }

    private FileHolder fileHolder = new DefaultFileHolder();

    @Override
    public FileHolder fileHolder() {
        return this.fileHolder;
    }

    public DownloadRequest setFileHolder(FileHolder fileHolder) {
        this.fileHolder = fileHolder;
        return myself();
    }
}
