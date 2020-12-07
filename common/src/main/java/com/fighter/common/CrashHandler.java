package com.fighter.common;

/**
 * Created by fighter_lee on 19/08/22.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";

    private static CrashHandler INSTANCE = new CrashHandler();
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private onCrashCallback mCallback;

    private CrashHandler() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 接管未捕获异常
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //exit app
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        try {
            collectDeviceInfo(ex);
            if (mCallback != null) {
                mCallback.dealCrash(ex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void collectDeviceInfo(Throwable ex) {
        Trace.e(TAG, "collectDeviceInfo() ex:" + ex.toString());
        StringBuilder err = new StringBuilder();
        ex.printStackTrace();

        StackTraceElement[] stack = ex.getStackTrace();
        for (StackTraceElement obj : stack) {
            err.append("\tat ");
            err.append(obj.toString());
            err.append("\n");
        }
        Trace.e(TAG, "collectDeviceInfo() ex detail:" + err.toString());
        Throwable cause = ex.getCause();
        if (cause != null) {
            collectDeviceInfo(cause);
        }
    }

    public interface onCrashCallback {
        void dealCrash(Throwable ex);
    }

    /**
     * 当crash发生时，将会调用该Callback;<br/>
     * 注意：不要做耗时操作
     */
    public void setCallback(onCrashCallback callback) {
        mCallback = callback;
    }
}
