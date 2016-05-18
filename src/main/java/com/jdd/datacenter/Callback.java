package com.jdd.datacenter;

/**
 * call back
 */
public interface Callback {
    public <T> Object execute(T... args);
}
