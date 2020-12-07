package com.fighter.lancomm.inter;

import com.fighter.lancomm.data.CommData;

/**
 * @author fighter_lee
 * @date 2020/1/14
 */
public interface DataListener {

    /**
     * 接收到广播数据
     *
     * @param commData
     */
    void onBroadcastArrive(CommData commData);

    /**
     * 接收到指定设备发出的点对点数据
     *
     * @param commData
     * @return 响应特定的数据
     */
    void onCommandArrive(CommData commData);

}
