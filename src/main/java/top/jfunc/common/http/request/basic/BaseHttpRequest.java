package top.jfunc.common.http.request.basic;

import top.jfunc.common.ChainCall;
import top.jfunc.common.http.HttpConstants;
import top.jfunc.common.http.MediaType;
import top.jfunc.common.http.ParamUtil;
import top.jfunc.common.http.base.ProxyInfo;
import top.jfunc.common.http.request.HttpRequest;
import top.jfunc.common.utils.ArrayListMultiValueMap;
import top.jfunc.common.utils.MultiValueMap;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiongshiyan at 2019/7/5 , contact me with email yanshixiong@126.com or phone 15208384257
 */
public abstract class BaseHttpRequest<THIS extends BaseHttpRequest> implements HttpRequest, ChainCall<THIS>{
    private String url;
    private String cacheFinalUrl;
    private Map<String , String> routeParams;
    private MultiValueMap<String , String> queryParams;
    private String queryParamCharset = HttpConstants.DEFAULT_CHARSET;
    private MultiValueMap<String , String> headers;
    private Map<String , String> overwriteHeaders;
    /**
     * 资源类型
     */
    private String contentType = null;
    /**
     * 连接超时时间，不设置就使用系统默认的
     * @see top.jfunc.common.http.base.Config#defaultConnectionTimeout
     */
    private Integer connectionTimeout = null;
    /**
     * 读数据超时时间，不设置就使用系统默认的
     * @see top.jfunc.common.http.base.Config#defaultReadTimeout
     */
    private Integer readTimeout = null;
    /**
     * 返回体编码，不设置就使用系统默认的
     * @see top.jfunc.common.http.base.Config#defaultResultCharset
     */
    private String resultCharset = HttpConstants.DEFAULT_CHARSET;
    /**
     * 返回结果中是否包含headers,默认不包含
     */
    private boolean includeHeaders = !INCLUDE_HEADERS;
    /**
     * 返回结果中是否忽略body,  true那么就不去读取body，提高效率, 默认不忽略
     */
    private boolean ignoreResponseBody = !IGNORE_RESPONSE_BODY;
    /**
     * 是否支持重定向
     */
    private boolean redirectable = !REDIRECTABLE;
    /**
     * 代理设置,如果有就设置
     * Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(hostName, port));
     */
    private ProxyInfo proxyInfo = null;

    /**
     * HostnameVerifier
     */
    private HostnameVerifier hostnameVerifier = null;
    /**
     * SSLContext
     */
    private SSLContext sslContext = null;
    /**
     * SSLSocketFactory
     */
    private SSLSocketFactory sslSocketFactory = null;
    /**
     * X509TrustManager
     */
    private X509TrustManager x509TrustManager = null;
    /**
     * 属性设置
     */
    private Map<String , Object> attributes;

    public BaseHttpRequest(String url){this.url = url;}
    public BaseHttpRequest(URL url){this.url = url.toString();}
    public BaseHttpRequest(){}

    @Override
    public String getUrl() {
        if(null != cacheFinalUrl){
            return cacheFinalUrl;
        }
        //处理路径参数
        cacheFinalUrl = ParamUtil.replaceRouteParamsIfNecessary(url , getRouteParams());
        //处理Query参数
        cacheFinalUrl = ParamUtil.contactUrlParams(cacheFinalUrl , getQueryParams() , getQueryParamCharset());
        return cacheFinalUrl;
    }

    @Override
    public THIS setUrl(String url) {
        this.url = url;
        return myself();
    }

    @Override
    public HttpRequest setUrl(URL url) {
        setUrl(url.toString());
        return myself();
    }

    @Override
    public Map<String, String> getRouteParams() {
        return routeParams;
    }

    @Override
    public THIS addRouteParam(String key, String value) {
        if(null == routeParams){
            routeParams = new HashMap<>(2);
        }
        routeParams.put(key, value);
        return myself();
    }

    @Override
    public MultiValueMap<String, String> getQueryParams() {
        return queryParams;
    }

    @Override
    public String getQueryParamCharset() {
        return queryParamCharset;
    }

    @Override
    public THIS setQueryParamCharset(String paramCharset) {
        this.queryParamCharset = paramCharset;
        return myself();
    }

    @Override
    public THIS addQueryParam(String key, String value, String... values) {
        if(null == queryParams){
            queryParams = new ArrayListMultiValueMap<>(2);
        }
        queryParams.add(key, value, values);
        return myself();
    }

    @Override
    public MultiValueMap<String, String> getHeaders() {
        return headers;
    }

    @Override
    public THIS addHeader(String key, String value, String... values) {
        if(null == headers){
            headers = new ArrayListMultiValueMap<>(2);
        }
        headers.add(key, value, values);
        return myself();
    }

    @Override
    public Map<String, String> getOverwriteHeaders() {
        return overwriteHeaders;
    }

    @Override
    public THIS putOverwriteHeader(String key, String value) {
        if(null == overwriteHeaders){
            overwriteHeaders = new HashMap<>(2);
        }
        overwriteHeaders.put(key, value);
        return myself();
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public THIS setContentType(String contentType) {
        this.contentType = contentType;
        return myself();
    }

    @Override
    public THIS setContentType(MediaType mediaType) {
        this.contentType = mediaType.toString();
        return myself();
    }

    @Override
    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    @Override
    public THIS setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return myself();
    }

    @Override
    public Integer getReadTimeout() {
        return readTimeout;
    }

    @Override
    public THIS setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return myself();
    }

    @Override
    public String getResultCharset() {
        return resultCharset;
    }

    @Override
    public THIS setResultCharset(String resultCharset) {
        this.resultCharset = resultCharset;
        return myself();
    }

    @Override
    public boolean isIncludeHeaders() {
        return includeHeaders;
    }

    @Override
    public THIS setIncludeHeaders(boolean includeHeaders) {
        this.includeHeaders = includeHeaders;
        return myself();
    }

    @Override
    public THIS includeHeaders() {
        this.includeHeaders = INCLUDE_HEADERS;
        return myself();
    }

    @Override
    public boolean isIgnoreResponseBody() {
        return ignoreResponseBody;
    }

    @Override
    public THIS setIgnoreResponseBody(boolean ignoreResponseBody) {
        this.ignoreResponseBody = ignoreResponseBody;
        return myself();
    }

    @Override
    public THIS ignoreResponseBody() {
        this.ignoreResponseBody = IGNORE_RESPONSE_BODY;
        return myself();
    }

    @Override
    public boolean isRedirectable() {
        return redirectable;
    }

    @Override
    public THIS setRedirectable(boolean redirectable) {
        this.redirectable = redirectable;
        return myself();
    }

    @Override
    public THIS redirectable() {
        this.redirectable = REDIRECTABLE;
        return myself();
    }

    @Override
    public ProxyInfo getProxyInfo() {
        return proxyInfo;
    }

    @Override
    public THIS setProxy(ProxyInfo proxyInfo) {
        this.proxyInfo = proxyInfo;
        return myself();
    }

    @Override
    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }

    @Override
    public THIS setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
        return myself();
    }

    @Override
    public SSLContext getSslContext() {
        return sslContext;
    }

    @Override
    public THIS setSslContext(SSLContext sslContext) {
        this.sslContext = sslContext;
        return myself();
    }

    /**
     * 因为一般地 SslSocketFactory 都是从sslContext产生出来的 ， 所以如果没显式设置就从sslContext产生
     */
    @Override
    public SSLSocketFactory getSslSocketFactory() {
        if(null == sslSocketFactory && null != sslContext){
            return sslContext.getSocketFactory();
        }
        return sslSocketFactory;
    }

    @Override
    public THIS setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
        return myself();
    }

    @Override
    public X509TrustManager getX509TrustManager() {
        return x509TrustManager;
    }

    @Override
    public THIS setX509TrustManager(X509TrustManager x509TrustManager) {
        this.x509TrustManager = x509TrustManager;
        return myself();
    }

    @Override
    public THIS addAttribute(String key, String value) {
        if(null == attributes){
            attributes = new HashMap<>(2);
        }
        attributes.put(key, value);
        return myself();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}