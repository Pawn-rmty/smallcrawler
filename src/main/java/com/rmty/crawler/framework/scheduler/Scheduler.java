package com.rmty.crawler.framework.scheduler;

import com.rmty.crawler.framework.request.Request;
import com.rmty.crawler.framework.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
public class Scheduler {

    private BlockingQueue<Request>  pending = new LinkedBlockingQueue<>();
    private BlockingQueue<Response> result  = new LinkedBlockingQueue<>();

    public void addRequest(Request request) {
        try {
            this.pending.put(request);
        } catch (InterruptedException e) {
            log.error("调度器添加 Request error", e);
        }
    }

    public void addResponse(Response response) {
        try {
            this.result.put(response);
        } catch (InterruptedException e) {
            log.error("调度器添加 Response error", e);
        }
    }

    public boolean hasRequest() {
        return pending.size() > 0;
    }

    public Request nextRequest() {
        try {
            return pending.take();
        } catch (InterruptedException e) {
            log.error("调度器获取 Request error", e);
            return null;
        }
    }

    public boolean hasResponse() {
        return result.size() > 0;
    }

    public Response nextResponse() {
        try {
            return result.take();
        } catch (InterruptedException e) {
            log.error("调度器获取 Response error", e);
            return null;
        }
    }

    public void addRequests(List<Request> requests) {
        requests.forEach(this::addRequest);
    }

    public void clear() {
        pending.clear();
    }

}
