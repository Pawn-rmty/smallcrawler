package com.rmty.crawler.framework.pipeline;

import com.rmty.crawler.framework.request.Request;


public interface Pipeline<T> {

    void process(T item, Request request);

}
