package com.fighter.lancomm;

import com.fighter.common.Dispatcher;
import com.fighter.lancomm.broadcast.Broadcaster;
import com.fighter.lancomm.broadcast.BroadcasterImpl;
import com.fighter.lancomm.ptop.Communicator;
import com.fighter.lancomm.ptop.CommunicatorImpl;
import com.fighter.lancomm.receive.Receiver;
import com.fighter.lancomm.receive.ReceiverImpl;
import com.fighter.lancomm.search.Searcher;
import com.fighter.lancomm.search.SearcherImpl;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author fighter_lee
 * @date 2020/1/13
 */
public class LanCommManager {

    public static final ThreadPoolExecutor thread_pool =
            Dispatcher.newThreadPool("LanCommTask");

    /**
     * 获取广播器
     *
     * @return Broadcaster
     */
    public static Broadcaster getBroadcaster() {
        return BroadcasterImpl.getImpl();
    }

    /**
     * 获取接收器
     *
     * @return
     */
    public static Receiver getReceiver() {
        return ReceiverImpl.getImpl();
    }

    /**
     * 获取搜索器
     *
     * @return Searcher
     */
    public static Searcher getSearcher() {
        return SearcherImpl.getImpl();
    }

    /**
     * 获取点对点通讯器
     *
     * @return
     */
    public static Communicator getCommunicator() {
        return CommunicatorImpl.getImpl();
    }

}
