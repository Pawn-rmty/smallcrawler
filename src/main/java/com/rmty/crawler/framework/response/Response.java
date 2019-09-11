package com.rmty.crawler.framework.response;

import com.rmty.crawler.framework.request.Request;
import lombok.Getter;

import java.io.InputStream;


public class Response {

    @Getter
    private Request request;
    private Body    body;

    public Response(Request request, InputStream inputStream) {
        this.request = request;
        this.body = new Body(inputStream, request.charset());
    }

    public Body body() {
        return body;
    }

}
