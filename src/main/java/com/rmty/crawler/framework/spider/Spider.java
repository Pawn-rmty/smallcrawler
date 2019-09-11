package com.rmty.crawler.framework.spider;

import com.rmty.crawler.framework.config.Config;
import com.rmty.crawler.framework.event.Event;
import com.rmty.crawler.framework.event.EventManager;
import com.rmty.crawler.framework.pipeline.Pipeline;
import com.rmty.crawler.framework.request.Parser;
import com.rmty.crawler.framework.request.Request;
import com.rmty.crawler.framework.response.Response;
import com.rmty.crawler.framework.response.Result;

import lombok.Data;
import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;


@Data
@Getter
public abstract class Spider {

    protected String name;
    protected Config config;
    protected List<String>   startUrls = new ArrayList<>();
    protected List<Pipeline> pipelines = new ArrayList<>();
    protected List<Request>  requests  = new ArrayList<>();

    public Spider(String name,Config config) {
        this.name = name;
        this.config = config;
        EventManager.registerEvent(Event.SPIDER_STARTED, this::onStart);
    }

    public Spider startUrls(String... urls) {
        this.startUrls.addAll(Arrays.asList(urls));
        return this;
    }


    public void onStart(Config config) {
    }

    protected <T> Spider addPipeline(Pipeline<T> pipeline) {
        this.pipelines.add(pipeline);
        return this;
    }


    public <T> Request<T> makeRequest(String url) {
        return makeRequest(url, this::parse);
    }

    public <T> Request<T> makeRequest(String url, Parser<T> parser) {
        return new Request(this, url, parser);
    }


    protected abstract <T> Result<T> parse(Response response);


}
