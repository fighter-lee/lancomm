package com.fighter.lancomm.inter;

import com.fighter.lancomm.data.Device;

/**
 * @author fighter_lee
 * @date 2020/1/14
 */
public interface DeviceListener {

    void onDeviceAdd(Device device);

    void onDeviceRemove(Device device);

}
