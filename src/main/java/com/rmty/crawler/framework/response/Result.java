package com.rmty.crawler.framework.response;

import com.rmty.crawler.framework.request.Request;
import com.rmty.crawler.framework.utils.Utils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Result<T> {

    private List<Request> requests = new ArrayList<>();
    private T item;

    public Result(T item) {
        this.item = item;
    }

    public Result addRequest(Request request) {
        this.requests.add(request);
        return this;
    }

    public Result addRequests(List<Request> requests) {
        if (!Utils.isEmpty(requests)) {
            this.requests.addAll(requests);
        }
        return this;
    }

}
