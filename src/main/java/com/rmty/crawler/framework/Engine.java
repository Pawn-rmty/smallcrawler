package com.rmty.crawler.framework;

import com.rmty.crawler.framework.config.Config;
import com.rmty.crawler.framework.download.Downloader;
import com.rmty.crawler.framework.event.Event;
import com.rmty.crawler.framework.event.EventManager;
import com.rmty.crawler.framework.pipeline.Pipeline;
import com.rmty.crawler.framework.request.Parser;
import com.rmty.crawler.framework.request.Request;
import com.rmty.crawler.framework.response.Response;
import com.rmty.crawler.framework.response.Result;
import com.rmty.crawler.framework.scheduler.Scheduler;
import com.rmty.crawler.framework.spider.Spider;
import com.rmty.crawler.framework.utils.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.rmty.crawler.framework.event.Event.SPIDER_STARTED;


@Slf4j
public class Engine {

    private List<Spider>    spiders =new ArrayList<>();;
    private Config          config= Config.me();
    private boolean         isRunning;
    private Scheduler       scheduler =new Scheduler();
    private ExecutorService executorService =  new ThreadPoolExecutor(config.parallelThreads(), config.parallelThreads(), 0, TimeUnit.MILLISECONDS,
            config.queueSize() == 0 ? new SynchronousQueue<>()
                    : (config.queueSize() < 0 ? new LinkedBlockingQueue<>()
                    : new LinkedBlockingQueue<>(config.queueSize())));

    public void add(Spider spider) {
        this.spiders.add(spider);

    }

    public void start() {
        if (isRunning) {
            throw new RuntimeException(" 已经启动");
        }

        isRunning = true;

        EventManager.fireEvent(Event.GLOBAL_STARTED, config);

        spiders.forEach(spider -> {

            Config conf = config.clone();

            log.info("Spider [{}] 启动...", spider.getName());
            log.info("Spider [{}] 配置 [{}]", spider.getName(), conf);
            spider.setConfig(conf);

            List<Request> requests = spider.getStartUrls().stream()
                    .map(spider::makeRequest).collect(Collectors.toList());

            spider.getRequests().addAll(requests);
            scheduler.addRequests(requests);

            EventManager.fireEvent(SPIDER_STARTED, conf);

        });


        Thread downloadTread = new Thread(() -> {
            while (isRunning) {
                if (!scheduler.hasRequest()) {
                    Utils.sleep(100);
                    continue;
                }
                Request request = scheduler.nextRequest();
                executorService.submit(new Downloader(scheduler, request));
                Utils.sleep(request.getSpider().getConfig().delay());
            }
        });
        downloadTread.setDaemon(true);
        downloadTread.setName("download-thread");
        downloadTread.start();

        this.complete();
    }

    private void complete() {
        while (isRunning) {
            if (!scheduler.hasResponse()) {
                Utils.sleep(100);
                continue;
            }
            Response response = scheduler.nextResponse();
            Parser   parser   = response.getRequest().getParser();
            if (null != parser) {
                Result<?>     result   = parser.parse(response);
                List<Request> requests = result.getRequests();
                if (!Utils.isEmpty(requests)) {
                    requests.forEach(scheduler::addRequest);
                }
                if (null != result.getItem()) {
                    List<Pipeline> pipelines = response.getRequest().getSpider().getPipelines();
                    pipelines.forEach(pipeline -> pipeline.process(result.getItem(), response.getRequest()));
                }
            }
        }
    }

    public void stop(){
        isRunning = false;
        scheduler.clear();
        log.info("爬虫已经停止.");
    }

}
