package com.fighter.common;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

/**
 * Created by fighter_lee on 20/04/27.
 */
public class Timer {
    private Timer.ICondition mCondition;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private long count = 0L;
    private Timer.IntervalListener intervalListener;
    private long mPeriod = 1000L;
    private long mTimeout = 5000L;
    private long firstTimeDelay = 0L;
    private long minTime = 0L;

    private final ListenerHandlerThread mHandlerThread;

    private Runnable runnable = new Runnable() {
        public void run() {
            long runningTime = (count + 1L) * mPeriod + firstTimeDelay;
            if (runningTime > mTimeout) {
                mHandlerThread.end();
                if (intervalListener != null) {
                    intervalListener.onTimeOut();
                }
            } else if (runningTime > minTime && mCondition.onConditionComplete()) {
                mHandlerThread.end();
                if (intervalListener != null) {
                    intervalListener.onComplete();
                }
            } else {
                if (intervalListener != null) {
                    intervalListener.onNext(count);
                }
                count++;
                start(mPeriod);
            }
        }
    };
    /**
     * 可用来绑定一个元素，或者标识该Timer
     */
    public Object tag = null;

    /**
     * 取消定时器
     * 注意：该接口会终止定时器，并不会调用任何接口通知
     */
    public void cancel() {
        handler.removeCallbacks(runnable);
    }

    private long getMinTime() {
        return this.minTime;
    }

    /**
     * 最小条件到达时间，若再该时间内，条件到达，也会被判断为没有到达
     *
     * @param minTime 最小完成时间，单位：mills
     */
    public Timer setMinTime(long minTime) {
        this.minTime = minTime;
        return this;
    }

    public static Timer newInstance() {
        return new Timer();
    }

    private Timer() {
        mHandlerThread = ListenerHandlerThread.create("Timer", false);
        mHandlerThread.start();
    }

    /**
     * 两次触发onNext()回调的时间间隔
     *
     * @param period 心跳周期，单位:mills，默认1000
     */
    public Timer setPeriod(long period) {
        this.mPeriod = period;
        return this;
    }

    /**
     * 第一次执行
     *
     * @param timeout 超时时间，单位:mills，默认5000
     */
    public Timer setTimeout(long timeout) {
        this.mTimeout = timeout;
        return this;
    }

    public Timer setICondition(Timer.ICondition iCondition) {
        this.mCondition = iCondition;
        return this;
    }

    public Timer setFirstTimeDelay(long firstTimeDelay) {
        this.firstTimeDelay = firstTimeDelay;
        return this;
    }

    public void intervalTask(@NonNull Timer.IntervalListener intervalListener) {
        this.intervalListener = intervalListener;
        this.start(this.firstTimeDelay);
    }

    private void start(long delay) {
        this.mHandlerThread.postDelayed(this.runnable, delay);
    }

    public interface IntervalListener {
        void onNext(Long var1);

        void onComplete();

        void onTimeOut();
    }

    public interface ICondition {
        boolean onConditionComplete();
    }
}
