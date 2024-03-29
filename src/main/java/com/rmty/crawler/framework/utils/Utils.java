package com.rmty.crawler.framework.utils;

import java.util.Collection;
import java.util.concurrent.TimeUnit;


public class Utils {

    public static void sleep(long time){
        try {
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static <E> boolean isEmpty(Collection<E> collection){
        return null == collection || collection.size() == 0;
    }

}
