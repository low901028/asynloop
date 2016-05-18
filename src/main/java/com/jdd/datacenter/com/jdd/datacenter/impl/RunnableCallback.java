package com.jdd.datacenter.com.jdd.datacenter.impl;

import com.jdd.datacenter.Callback;
import com.jdd.datacenter.Shutdownable;

/**
 * runnable call back
 */
public class RunnableCallback implements Runnable,Callback,Shutdownable{
    // pre run
    public void preRun(){

    }

    // run
    @Override
    public void run() {

    }

    // post Run
    public void postRun(){

    }
    // happen exception
    public Exception error(){
        return null;
    }

    // get result
    public Object getResult(){
        return null;
    }

    // shut down
    @Override
    public void shutDown() {

    }
    // execute thread name
    public String getThreadName(){
        return null;
    }

    // execute
    @Override
    public <T> Object execute(T... args) {
        return null;
    }
}
