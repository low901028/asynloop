package com.jdd.datacenter;

/**
 * Thread interface
 */
public interface SmartThread{
    /**
     * thread start
     */
    public void start();

    /**
     * thread join
     * @throws InterruptedException
     */
    public void join() throws  InterruptedException;

    /**
     * thread interrupt
     */
    public void interrupt();

    /**
     * thread is sleep
     * @return
     */
    public Boolean isSleeping();

    /**
     * clean up thread
     */
    public void cleanup();
}
