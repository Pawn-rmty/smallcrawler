package com.rmty.crawler.demo.examples;

import com.rmty.crawler.framework.Engine;
import com.rmty.crawler.framework.config.Config;
import com.rmty.crawler.framework.pipeline.Pipeline;
import com.rmty.crawler.framework.response.Response;
import com.rmty.crawler.framework.response.Result;
import com.rmty.crawler.framework.spider.Spider;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.stream.Collectors;


public class News163Example {

    @Slf4j
    static class News163Spider extends Spider {
        public News163Spider(String name,Config config) {
            super(name,config);
            this.startUrls(
                    "http://news.163.com/special/0001386F/rank_news.html",
                    "http://news.163.com/special/0001386F/rank_ent.html", // 娱乐
                    "http://news.163.com/special/0001386F/rank_sports.html", // 体育
                    "http://news.163.com/special/0001386F/rank_tech.html", // 科技
                    "http://news.163.com/special/0001386F/game_rank.html", //游戏
                    "http://news.163.com/special/0001386F/rank_book.html"); // 读书
        }

        @Override
        public void onStart(Config config) {
            this.addPipeline((Pipeline<List<String>>) (item, request) -> item.forEach(System.out::println));
            this.requests.forEach(request -> {
                request.contentType("text/html; charset=gb2312");
                request.charset("gb2312");
            });
        }

        @Override
        protected Result parse(Response response) {
            List<String> titles = response.body().css("div.areabg1 .area-half.left div.tabContents td a").stream()
                    .map(Element::text)
                    .collect(Collectors.toList());

            return new Result(titles);
        }
    }

    public static void main(String[] args) {
        Engine e = new Engine();
        e.add(new News163Spider("网易新闻",Config.me()));
        e.start();
    }

}
