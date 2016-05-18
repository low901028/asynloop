package com.jdd.datacenter.com.jdd.datacenter.impl;

import com.jdd.datacenter.Utils;

/**
 * default async loop
 */
public class AsyncLoopDefaultKill extends RunnableCallback {
    public <T> Object execute(T... args){
        Exception e = (Exception) args[0];
        Utils.halt_process(1, "Async loop died!");
        return  e;
    }

    public void run(){
        Utils.halt_process(1, "Async loop died!");
    }
}
