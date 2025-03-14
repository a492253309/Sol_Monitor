package com.shaohao.mytask.utils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shaohao.mytask.enums.wofUrlEnum;
import okhttp3.*;
import okhttp3.HttpUrl.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 * 封装http协议，简化操作
 *
 * @author zwp
 */
@Service
public class HttpImpl implements HTTP {
    public final MediaType MEDIA_JSON = MediaType.parse("application/json; charset=utf-8");
    public final MediaType MEDIA_XML = MediaType.parse("application/xml; charset=utf-8");

    //最大尝试次数
    @Value("${task.http.maxRetryCount}")
    private int maxRetryCount;

    //成功状态码
    @Value("${task.http.expectedStatusCode}")
    private int expectedStatusCode;

    @Autowired
    private  OkHttpClient httpClient;

    /**
     *
     * GET极简同步方法
     *
     * @author liuyi 2016年7月17日
     * @param url
     * @return
     * @throws HttpException
     * @throws IOException
     */
    @Override
    public  String GET(String url, String token) throws HttpException,IOException {
        Request request = new Request.Builder()
                .url(url).get()
                .header("Authorization",token)
                .build();
        String result  =ReqExecute(request);

        return result;
    }

    @Override
    public String postJson(String baseUrl, String jsonBody, String token) throws IOException {

        RequestBody body = RequestBody.create(MEDIA_JSON, jsonBody);
        Request request = new Request.Builder()
                .url(baseUrl)
                .header("Authorization",token)
                .post(body)
                .build();
        String result = ReqExecute(request);

        return result;
    }

    /**
     *
     * GET 同步方法
     *
     * @author liuyi 2016年7月17日
     * @param baseUrl
     * @param queryParams
     * @return
     * @throws IOException
     */
    @Override
    public  String GET(String baseUrl,Map<String, String> queryParams) throws IOException {
        //拼装param
        Builder urlBuilder = HttpUrl.parse(baseUrl).newBuilder();
        for (Map.Entry<String, String> item : queryParams.entrySet()) {
            urlBuilder.addQueryParameter(item.getKey(), item.getValue());
        }
        HttpUrl httpUrl= urlBuilder.build();

        //发送请求
        Request request = new Request.Builder()
//                .header("id","getblock.io")
                .url(httpUrl.toString()).get()
                .build();
        return ReqExecute(request);
//        return httpUrl.toString();
    }

    /**
     *
     * POST同步方法
     *
     * @author liuyi 2016年7月17日
     * @param baseUrl
     * @param bodyParams
     * @return
     * @throws IOException
     */
    @Override
    public  String POST(String baseUrl,Map<String, String> bodyParams) throws IOException {
        //encode params
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> item : bodyParams.entrySet()) {
            bodyBuilder.addEncoded(item.getKey(), item.getValue());
        }
        FormBody formBody = bodyBuilder.build();
        //发送请求
        Request request = new Request.Builder()
                .url(baseUrl).post(formBody)
                .build();
        return ReqExecute(request);
    }


    /**
     *
     * GET异步方法
     *
     * @author liuyi 2016年7月17日
     * @param request
     * @param responseHandle
     * @throws IOException
     */
    @Override
    public  void asyncGET(Request request,Callback responseHandle) throws IOException{
        ReqExecuteCall(request).enqueue(responseHandle);
    }

    /**
     *
     * GET方法 异步请求
     *
     * @author liuyi 2016年7月17日
     * @param baseUrl
     * @param queryParams
     * @param responseHandle
     * @throws IOException
     */
    @Override
    public  void asyncGET(String baseUrl,
                          Map<String, String> queryParams,Callback responseHandle) throws IOException {
        //拼装param
        Builder urlBuilder = HttpUrl.parse(baseUrl).newBuilder();
        for (Map.Entry<String, String> item : queryParams.entrySet()) {
            urlBuilder.addQueryParameter(item.getKey(), item.getValue());
        }
        HttpUrl httpUrl= urlBuilder.build();
        //发送请求
        Request request = new Request.Builder()
                .url(httpUrl.toString()).get()
                .build();
        ReqExecuteCall(request).enqueue(responseHandle);
    }


    /**
     *
     * POST异步方法
     *
     * @author liuyi 2016年7月17日
     * @param baseUrl
     * @param jsonBody
     * @param responseHandle
     */
    @Override
    public  void asyncPOST(String baseUrl,String jsonBody,Callback responseHandle){
        RequestBody body = RequestBody.create(MEDIA_JSON, jsonBody);
        Request request = new Request.Builder()
                .url(baseUrl)
                .post(body)
                .build();
        ReqExecuteCall(request).enqueue(responseHandle);
    }

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
    @Override
    public  void asyncPOST(String baseUrl,Map<String, String> bodyParams,Callback responseHandle) throws IOException{
        //encode params
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> item : bodyParams.entrySet()) {
            bodyBuilder.addEncoded(item.getKey(), item.getValue());
        }
        FormBody formBody = bodyBuilder.build();
        //发送请求
        Request request = new Request.Builder()
                .url(baseUrl).post(formBody)
                .build();
        ReqExecuteCall(request).enqueue(responseHandle);
    }


    public void sleep(int second) {
        try {
            Thread.sleep(1000*second); // 休眠十秒（10000毫秒）
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * 请求方法
     *
     * @author liuyi 2016年7月17日
     * @param request
     * @return
     * @throws IOException
     */
    @Override
    public  String ReqExecute(Request request) throws IOException{
        int currentRetryCount = 0;
        String result = "";
        while (currentRetryCount < maxRetryCount) {
            try {
                Response response = ReqExecuteCall(request).execute();
                result = new String(response.body().bytes());

//                JSONObject jsonObject = JSONObject.parseObject(result);
//                if (jsonObject.getInteger("code") == expectedStatusCode) {
//                    // 请求成功，退出循环
                    break;
//                } else {
//                    // 响应状态码不符合预期，记录日志等
//                    currentRetryCount++;
//                    if (currentRetryCount < maxRetryCount) {
//                        sleep(1);  // 等待 1 秒
//                    }
//                }
            } catch (IOException e) {
                // 处理请求异常，记录日志等
                currentRetryCount++;
                if (currentRetryCount < maxRetryCount) {
                    sleep(1);
                }
            }
        }

        return result;
    }

    /**
     *
     * 构造CALL方法
     *
     * @author liuyi 2016年7月17日
     * @param request
     * @return
     */
    @Override
    public  Call ReqExecuteCall(Request request){
        return httpClient.newCall(request);
    }

}
