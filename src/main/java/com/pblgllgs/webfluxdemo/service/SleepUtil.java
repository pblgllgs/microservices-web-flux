package com.pblgllgs.webfluxdemo.service;
/*
 *
 * @author pblgl
 * Created on 05-12-2023
 *
 */

public class SleepUtil {
    public static void sleepSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
