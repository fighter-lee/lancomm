package com.fighter.lancomm.broadcast;

import com.fighter.lancomm.LanCommManager;

/**
 * @author fighter_lee
 * @date 2020/1/13
 */
public class BroadcasterImpl implements Broadcaster {

    private BroadcasterImpl() {
    }

    public static BroadcasterImpl getImpl() {
        return BroadcasterImplHolder.sInstance;
    }

    @Override
    public void broadcast(byte[] bytes) {
        LanCommManager.thread_pool.execute(new BroadcastRunnable().setData(bytes));
    }

    private static class BroadcasterImplHolder {
        private static final BroadcasterImpl sInstance = new BroadcasterImpl();
    }
}
