package com.shaohao.mytask.utils;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

/**
 * HTTP客户端常用接口封装，简化操作，提升性能，后续支持注解
 * 参考RestTemplate
 * @version
 *
 */
public interface HTTP {

    String ReqExecute(Request request) throws IOException;
    Call ReqExecuteCall(Request request);

    /**
     *
     * GET同步方法
     *
     * @author liuyi 2016年7月17日
     * @param url
     * @return
     * @throws HttpException
     * @throws IOException
     */
    String GET(String url, String token) throws HttpException,IOException;

    /**
     * post json格式请求
     * @param baseUrl
     * @param jsonBody
     * @return
     * @throws HttpException
     * @throws IOException
     */
    String postJson(String baseUrl, String jsonBody, String token)throws IOException;

    /**
     *
     * GET同步方法
     *
     * @author liuyi 2016年7月17日
     * @param baseUrl
     * @param queryParams
     * @return
     * @throws IOException
     */
    public String GET(String baseUrl, Map<String, String> queryParams) throws  IOException;

    /**
     *
     * GET异步方法
     *
     * @author liuyi 2016年7月17日
     * @param request
     * @param responseHandle
     * @throws IOException
     */
    public  void asyncGET(Request request, Callback responseHandle) throws IOException;
    /**
     *
     * GET异步方法
     *
     * @author liuyi 2016年7月17日
     * @param baseUrl
     * @param queryParams
     * @param responseHandle
     * @throws IOException
     */
    public  void asyncGET(String baseUrl,
                          Map<String, String> queryParams, Callback responseHandle) throws IOException;

    /**
     *
     *  POST同步方法
     *
     * @author liuyi 2016年7月17日
     * @param baseUrl
     * @param bodyParams
     * @return
     * @throws IOException
     */
    public  String POST(String baseUrl, Map<String, String> bodyParams) throws  IOException;
    /**
     *
     *  POST异步方法
     *
     * @author liuyi 2016年7月17日
     * @param baseUrl
     * @param jsonBody
     * @param responseHandle
     */
    public  void asyncPOST(String baseUrl, String jsonBody, Callback responseHandle);
    /**
     *
     * POST异步方法
     *
     * @author liuyi 2016年7月17日
     * @param baseUrl
     * @param bodyParams
     * @param responseHandle
     * @throws IOException
     */
    public  void asyncPOST(String baseUrl, Map<String, String> bodyParams, Callback responseHandle) throws IOException;


}

