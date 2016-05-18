package com.jdd.datacenter.com.jdd.datacenter.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 */
public class Time {
    private static AtomicBoolean simulating = new AtomicBoolean(false);
    private static volatile Map<Thread , AtomicLong> threadSleepTimes;
    private static final Object sleepTimesLock = new Object();
    private  static  AtomicLong simulatedCurrTimeMs;

    // init
    public static void startSimulating(){
        synchronized (sleepTimesLock){
            simulating.set(true);
            simulatedCurrTimeMs = new AtomicLong(0);
            threadSleepTimes = new ConcurrentHashMap<>();
        }
    }

    // stop
    public static  void stopSimulating(){
        synchronized (sleepTimesLock){
            simulating.set(false);
            threadSleepTimes = null;
        }
    }

    // simulating state
    public static  boolean isSimulating(){
        return  simulating.get();
    }
    // execute sleep
    public  static void sleepUntil(long targetTimeMS) throws InterruptedException{
        if(simulating.get()){
            try {
                synchronized (sleepTimesLock) {
                    if (threadSleepTimes == null) {
                        throw new InterruptedException();
                    }
                    threadSleepTimes.put(Thread.currentThread(), new AtomicLong(targetTimeMS));
                }

                while (simulatedCurrTimeMs.get() < targetTimeMS) {
                    synchronized (sleepTimesLock) {
                        if (threadSleepTimes == null) {
                            throw new InterruptedException();
                        }
                    }
                    Thread.sleep(10);
                }
            }finally {
                synchronized (sleepTimesLock){
                    if(simulating.get() && threadSleepTimes!= null){
                        threadSleepTimes.remove(Thread.currentThread());
                    }
                }
            }
        }
        else{
            long sleepTime = targetTimeMS - currentTimeMillis();
            if(sleepTime > 0){
                Thread.sleep(sleepTime);
            }
        }
    }

    public static  void sleep(long ms) throws InterruptedException {
        sleepUntil(ms);
    }

    public static void sleepSecs(long secs) throws InterruptedException {
        if(secs > 0){
            sleep(secs);
        }
    }

    public  static long currentTimeMillis(){
        if(simulating.get()){
            return simulatedCurrTimeMs.get();
        }
        else{
            return System.currentTimeMillis();
        }
    }

    public static long secsToMillis (int secs) {
        return 1000*(long) secs;
    }

    public static long secsToMillisLong(double secs) {
        return (long) (1000 * secs);
    }

    public static int currentTimeSecs() {
        return (int) (currentTimeMillis() / 1000);
    }

    public static int deltaSecs(int timeInSeconds) {
        return Time.currentTimeSecs() - timeInSeconds;
    }

    public static long deltaMs(long timeInMilliseconds) {
        return Time.currentTimeMillis() - timeInMilliseconds;
    }

    public static void advanceTime(long ms) {
        if(!simulating.get()) throw new IllegalStateException("Cannot simulate time unless in simulation mode");
        if(ms < 0) throw new IllegalArgumentException("advanceTime only accepts positive time as an argument");
        simulatedCurrTimeMs.set(simulatedCurrTimeMs.get() + ms);
    }

    public static boolean isThreadWaiting(Thread t) {
        if(!simulating.get()) throw new IllegalStateException("Must be in simulation mode");
        AtomicLong time;
        synchronized(sleepTimesLock) {
            time = threadSleepTimes.get(t);
        }
        return !t.isAlive() || time!=null && currentTimeMillis() < time.longValue();
    }
}
