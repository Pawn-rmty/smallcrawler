package com.rmty.crawler.framework.download;

import com.rmty.crawler.framework.request.Request;
import com.rmty.crawler.framework.response.Response;
import com.rmty.crawler.framework.scheduler.Scheduler;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;


import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


@Slf4j
public class Downloader implements Runnable {

    private final Scheduler scheduler;
    private final Request   request;

    public Downloader(Scheduler scheduler, Request request) {
        this.scheduler = scheduler;
        this.request = request;
    }

    @Override
    public void run() {
        // 获得Http客户端
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        CloseableHttpResponse responseIt = null;

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(request.getSpider().getConfig().timeout())//一、连接超时：connectionTimeout-->指的是连接一个url的连接等待时间
                .setSocketTimeout(request.getSpider().getConfig().timeout())// 二、读取数据超时：SocketTimeout-->指的是连接上一个url，获取response的返回等待时间
                .setConnectionRequestTimeout(request.getSpider().getConfig().timeout())
                .build();

        if ("get".equalsIgnoreCase(request.method())) {
            // 创建Get请求
            HttpGet httpGet = new HttpGet(request.getUrl());
            httpGet.setConfig(requestConfig);

            Map<String,String> headers = request.getHeaders();
            for(Map.Entry<String,String> e:headers.entrySet()){
                httpGet.setHeader(e.getKey(),e.getValue());
            }
            httpGet.setHeader("Content-Type",request.getContentType());

            // 由客户端执行(发送)Get请求
            try {
                responseIt = httpClient.execute(httpGet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 从响应模型中获取响应实体
            InputStream result = null;
            try {
                result = responseIt.getEntity().getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Response response = new Response(request, result);
            scheduler.addResponse(response);


        }




    }

}

