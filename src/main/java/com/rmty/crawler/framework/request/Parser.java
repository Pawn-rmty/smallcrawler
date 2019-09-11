package com.rmty.crawler.framework.request;

import com.rmty.crawler.framework.response.Result;
import com.rmty.crawler.framework.response.Response;


public interface Parser<T> {

    Result<T> parse(Response response);

}
