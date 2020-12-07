package com.fighter.common.interfaces;


import com.fighter.common.Dispatcher;
import com.fighter.common.Trace;

/**
 * @author fighter_lee
 * @date 2018/3/8
 */
public abstract class NamedRunnable implements Runnable {
    protected final String name;

    public NamedRunnable(String name) {
        this.name = name;
    }

    @Override
    public final void run() {
        String oldName = Thread.currentThread().getName();
        Thread.currentThread().setName(name);
        try {
            execute();
        } catch (Exception e) {
            Trace.e("NamedCallable/" + name, "call() e = " + e);
            e.printStackTrace();
        } finally {
            Thread.currentThread().setName(oldName);
            finished();
        }
    }

    protected abstract void execute();

    private void finished() {
        Dispatcher.with().finished(this);
    }
}