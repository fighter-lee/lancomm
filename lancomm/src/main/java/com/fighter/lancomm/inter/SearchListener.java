package com.fighter.lancomm.inter;

import com.fighter.lancomm.data.Device;

import java.util.HashMap;

/**
 * @author fighter_lee
 * @date 2020/1/15
 */
public interface SearchListener {

    /**
     * 开始搜索
     */
    void onSearchStart();

    /**
     * 发现新设备
     *
     * @param device
     */
    void onSearchedNewOne(Device device);

    /**
     * 搜索结束
     */
    void onSearchFinish(HashMap<String, Device> devices);

}
