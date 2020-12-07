package com.fighter.common.interfaces;

import com.fighter.common.Trace;

import java.util.concurrent.Callable;

/**
 * @author fighter_lee
 * @date 2018/3/8
 */
public abstract class NamedCallable<T> implements Callable<T> {
    protected final String name;

    public NamedCallable(String name) {
        this.name = name;
    }

    @Override
    public T call() throws Exception {
        String oldName = Thread.currentThread().getName();
        Thread.currentThread().setName(name);
        T t = null;
        try {
            t = execute();
        } catch (Exception e) {
            Trace.e("NamedCallable/" + name, "call() e = " + e);
            e.printStackTrace();
        } finally {
            Thread.currentThread().setName(oldName);
        }
        return t;
    }

    protected abstract T execute();
}