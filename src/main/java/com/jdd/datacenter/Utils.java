package com.jdd.datacenter;

/**
 * Created by Administrator on 2016/5/19.
 */
public class Utils {
    public static void halt_process(int val, String msg) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        haltProcess(val);
    }

    public static void haltProcess(int val) {
        Runtime.getRuntime().halt(val);
    }
}
