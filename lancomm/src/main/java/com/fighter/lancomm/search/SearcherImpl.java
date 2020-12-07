package com.fighter.lancomm.search;

import com.fighter.common.Trace;
import com.fighter.lancomm.LanCommManager;
import com.fighter.lancomm.broadcast.BroadcastRunnable;
import com.fighter.lancomm.data.Const;
import com.fighter.lancomm.inter.SearchListener;

/**
 * @author fighter_lee
 * @date 2020/1/14
 */
public class SearcherImpl implements Searcher {

    private static final String TAG = "SearcherImpl";
    private boolean canBeSearched = false;

    private SearcherImpl() {
    }

    public static SearcherImpl getImpl() {
        return SearcherImplHolder.sInstance;
    }

    private static class SearcherImplHolder {
        private static final SearcherImpl sInstance = new SearcherImpl();
    }

    @Override
    public void startSearch(SearchListener searchListener) {
        LanCommManager.thread_pool.execute(new SearchRunnable(searchListener));
    }

    @Override
    public void setCanBeSearched(boolean canBeSearched) {
        Trace.d(TAG, "setCanBeSearched() " + canBeSearched);
        if (this.canBeSearched != canBeSearched) {
            if (canBeSearched) {
                //设备上线->广播通知其他设备
                LanCommManager.thread_pool.execute(new BroadcastRunnable().setType(Const.PACKET_TYPE_DEVICE_ADD));
            } else {
                //设备下线->广播通知其他设备
                LanCommManager.thread_pool.execute(new BroadcastRunnable().setType(Const.PACKET_TYPE_DEVICE_REMOVE));
            }
        }
        this.canBeSearched = canBeSearched;
        if (canBeSearched) {
            SearchRspThread.open();
        } else {
            SearchRspThread.close();
        }
    }

    @Override
    public boolean isCanBeSearched() {
        return canBeSearched;
    }
}
