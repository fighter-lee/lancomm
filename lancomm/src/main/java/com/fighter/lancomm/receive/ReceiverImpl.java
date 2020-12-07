package com.fighter.lancomm.receive;

import com.fighter.lancomm.inter.DataListener;
import com.fighter.lancomm.inter.DeviceListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fighter_lee
 * @date 2020/1/14
 */
public class ReceiverImpl implements Receiver {

    private List<DataListener> receiverList = new ArrayList();

    public List<DeviceListener> getDeviceListeners() {
        return deviceListenerList;
    }

    private List<DeviceListener> deviceListenerList = new ArrayList<>();

    private ReceiverImpl() {
    }

    public static ReceiverImpl getImpl() {
        return ReceiverImplHolder.sInstance;
    }

    private static class ReceiverImplHolder {
        private static final ReceiverImpl sInstance = new ReceiverImpl();
    }

    public List<DataListener> getReceivers() {
        return receiverList;
    }

    @Override
    public void addDataListener(DataListener dataListener) {
        if (!receiverList.contains(dataListener)) {
            receiverList.add(dataListener);
        }
        openReceiverThread();
    }

    @Override
    public void addDeviceListener(DeviceListener deviceListener) {
        if (!deviceListenerList.contains(deviceListener)) {
            deviceListenerList.add(deviceListener);
        }
        openReceiverThread();
    }

    @Override
    public void removeDataListener(DataListener dataListener) {
        if (receiverList.contains(dataListener)) {
            receiverList.remove(dataListener);
        }
        closeReceiverThread();
    }

    @Override
    public void removeDeviceListener(DeviceListener deviceListener) {
        if (deviceListenerList.contains(deviceListener)) {
            deviceListenerList.remove(deviceListener);
        }
        closeReceiverThread();
    }

    private void openReceiverThread() {
        if (receiverList.size() > 0 || deviceListenerList.size() > 0) {
            //开启broadcast数据接收
            BroadcastReceiverThread.open();
        }
        if (receiverList.size() > 0) {
            //开启点对点数据接收
            CommandReceiverThread.open();
        }
    }

    private void closeReceiverThread() {
        if (receiverList.size() == 0 && deviceListenerList.size() == 0) {
            //关闭broadcast数据接收
            BroadcastReceiverThread.close();
        }
        if (receiverList.size() == 0) {
            //关闭点对点数据接收
            CommandReceiverThread.close();
        }
    }
}
