package top.jfunc.common.http.request.holder.impl;

/**
 * 通用的代表没有更多参数的请求
 * @author xiongshiyan at 2019/5/18 , contact me with email yanshixiong@126.com or phone 15208384257
 */
public class HolderCommonRequest extends BaseHolderHttpRequest<HolderCommonRequest> {
    public HolderCommonRequest(String url){
        super(url);
    }
    public static HolderCommonRequest of(String url){
        return new HolderCommonRequest(url);
    }
}