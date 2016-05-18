package com.jdd.datacenter.com.jdd.datacenter.impl;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * async loop runnable
 */
public class AsyncLoopRunnable implements Runnable {
    private static AtomicBoolean shutdown = new AtomicBoolean(false);

    public static AtomicBoolean getShutdown(){
        return shutdown;
    }

    private RunnableCallback rCallback;
    private RunnableCallback killfn;
    private long lastTime = System.currentTimeMillis();

    public AsyncLoopRunnable(RunnableCallback callback , RunnableCallback killfn){
        this.rCallback = callback;
        this.killfn = killfn;
    }

    private  boolean quit(Object rtn){
        if(rtn != null){
            long sleepTime = Long.parseLong(String.valueOf(rtn));
            if(sleepTime < 0){
                return true;
            }
            else if(sleepTime > 0){
                long now = System.currentTimeMillis();
                long cost = now - lastTime;
                long sleepMs = sleepTime * 1000 - cost;
                if(sleepMs > 0){
                    lastTime = System.currentTimeMillis();
                }else{
                    lastTime = now;
                }
            }
        }
        return false;
    }

    private void shutdown(){
        rCallback.postRun();
        rCallback.shutDown();

        // System.out.println(" successfully shutdown");
    }

    @Override
    public void run() {
        if(rCallback == null){
            throw  new RuntimeException("AsyncLoopRunnable no core function ");
        }
        rCallback.preRun();

        try{
            while (shutdown.get() == false){
                Exception e = null;
                rCallback.run();

                if(shutdown.get() == true){
                    shutdown();
                    return;
                }

                e = rCallback.error();
                if(e != null){
                    throw e;
                }

                Object rtn = rCallback.getResult();
                if(this.quit(rtn)){
                    shutdown();
                    return;
                }
            }
        }catch (Throwable e){
            if(shutdown.get() == true){
                shutdown();
                return;
            }
            else{
                System.err.println("Aysnc loop died ... ");
                killfn.execute(e);
            }
        }
    }
}
