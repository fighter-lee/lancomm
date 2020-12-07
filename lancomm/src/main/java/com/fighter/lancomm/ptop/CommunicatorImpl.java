package com.fighter.lancomm.ptop;

import com.fighter.lancomm.LanCommManager;

/**
 * @author fighter_lee
 * @date 2020/1/16
 */
public class CommunicatorImpl implements Communicator {

    private CommunicatorImpl() {
    }

    public static CommunicatorImpl getImpl() {
        return CommunicatorImplHolder.sInstance;
    }

    private static class CommunicatorImplHolder {
        private static final CommunicatorImpl sInstance = new CommunicatorImpl();
    }

    @Override
    public void sendCommand(Command command) {
        LanCommManager.thread_pool.execute(new CommandRunnable(command));
    }
}
