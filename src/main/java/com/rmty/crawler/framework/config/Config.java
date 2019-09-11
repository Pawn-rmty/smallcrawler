package com.rmty.crawler.framework.config;

import lombok.ToString;

@ToString
public class Config implements Cloneable {


    private int timeout = 10000;

    private int delay = 1000;

    private int parallelThreads = Runtime.getRuntime().availableProcessors() * 2;

    private String userAgent = UserAgent.CHROME_FOR_MAC;

    private int queueSize;

    public static Config me() {
        return new Config();
    }

    public Config timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public int timeout() {
        return this.timeout;
    }

    public Config delay(int delay) {
        this.delay = delay;
        return this;
    }

    public long delay() {
        return this.delay;
    }

    public Config parallelThreads(int parallelThreads) {
        this.parallelThreads = parallelThreads;
        return this;
    }

    public int parallelThreads() {
        return this.parallelThreads;
    }

    public String userAgent() {
        return userAgent;
    }

    public Config userAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public int queueSize() {
        return queueSize;
    }

    public Config queueSize(int queueSize) {
        this.queueSize = queueSize;
        return this;
    }

    @Override
    public Config clone() {
        try {
            return (Config) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

}