package com.fighter.lancomm.search;

import com.fighter.lancomm.inter.SearchListener;

/**
 * @author fighter_lee
 * @date 2020/1/14
 */
public interface Searcher {

    /**
     * 开始搜索设备
     *
     * @param searchListener
     */
    void startSearch(SearchListener searchListener);

    /**
     * 设置能否被其他设备搜索到
     *
     * @param canBeSearched
     */
    void setCanBeSearched(boolean canBeSearched);

    /**
     * 是否能被其他设备搜索到
     *
     * @return
     */
    boolean isCanBeSearched();

}
