package com.jdd.datacenter.com.jdd.datacenter.impl;

import com.jdd.datacenter.SmartThread;
import com.jdd.datacenter.Utils;
import com.jdd.datacenter.com.jdd.datacenter.util.Time;

/**
 * Created by Administrator on 2016/5/18.
 */
public class AsynLoopThread implements SmartThread{
    // inside thread
    private Thread thread;
    // call back
    private RunnableCallback callback;

    public AsynLoopThread(RunnableCallback callback){
        this.init(callback , false, Thread.NORM_PRIORITY,true);
    }

    public AsynLoopThread(RunnableCallback callback, boolean daemon,int priority, boolean start){
        this.init(callback , daemon, priority,start);
    }

    public AsynLoopThread(RunnableCallback callback,boolean daemon,RunnableCallback killfn,int priority, boolean start){
        this.init(callback , daemon, killfn, priority,start);
    }

    public void init(RunnableCallback callback, boolean daemon, int priority, boolean start){
        RunnableCallback killfn = new AsyncLoopDefaultKill();
        this.init(callback, daemon, killfn, priority, start);
    }

    private void init(RunnableCallback callback, boolean daemon, RunnableCallback killfn, int priority, boolean start){
        if(killfn==null){
            killfn = new AsyncLoopDefaultKill();
        }

        Runnable runnable = new AsyncLoopRunnable(callback , killfn);
        thread = new Thread(runnable);
        String threadName = callback.getThreadName();
        if(threadName == null){
            threadName = callback.getClass().getSimpleName();
        }
        thread.setName(threadName);
        thread.setDaemon(daemon);
        thread.setPriority(priority);
        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.err.println(" uncaught exception ");
                Utils.halt_process(1, "UncaughtException");
            }
        });

        this.callback = callback;
        if(start){
            start();
        }
    }
    @Override
    public void start() {
        thread.start();
    }

    @Override
    public void join() throws InterruptedException {
        thread.join();
    }

    @Override
    public void interrupt() {
        thread.interrupt();
    }

    @Override
    public Boolean isSleeping() {
        return Time.isThreadWaiting(thread);
    }

    @Override
    public void cleanup() {
        callback.shutDown();
    }

    public Thread getThread(){
        return  thread;
    }
}
