package com.fighter.lancomm_demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.fighter.common.Trace;
import com.fighter.common.UIThreadUtil;
import com.fighter.lancomm.LanCommManager;
import com.fighter.lancomm.data.CommData;
import com.fighter.lancomm.data.Device;
import com.fighter.lancomm.inter.DataListener;
import com.fighter.lancomm.inter.DeviceListener;
import com.fighter.lancomm.inter.SearchListener;
import com.fighter.lancomm.ptop.Command;

import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author fighter_lee
 * @date 2020/1/13
 */
public class LancommTestActivity extends Activity {

    private static final String TAG = "LancommTestActivity";
    private RecyclerView recyclerView;
    private DeviceAdapter deviceAdapter;
    HashMap<String, Device> devices = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lancomm);

        recyclerView = findViewById(R.id.rv);

        deviceAdapter = new DeviceAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(deviceAdapter);

        deviceAdapter.setOnItemClickListener(new DeviceAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Device device) {
                sendMsg(device);
            }
        });

        LanCommManager.getReceiver().addDataListener(new DataListener() {
            @Override
            public void onBroadcastArrive(final CommData commData) {
                Trace.d(TAG, "onMessage() : " + new String(commData.getData()));
                UIThreadUtil.postUI(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LancommTestActivity.this, "接收到" + commData.getDevice().getIp() + " 发来的广播消息：" + (new String(commData.getData())), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onCommandArrive(final CommData commData) {
                //接收到点对点消息
                Trace.d(TAG, "onCommandArrive() " + new String(commData.getData()));
                UIThreadUtil.postUI(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LancommTestActivity.this, "接收到" + commData.getDevice().getIp() + " 发来的点对点消息：" + (new String(commData.getData())), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        LanCommManager.getReceiver().addDeviceListener(new DeviceListener() {
            @Override
            public void onDeviceAdd(final Device device) {
                UIThreadUtil.postUI(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LancommTestActivity.this, "设备上线了：" + device.getIp(), Toast.LENGTH_LONG).show();
                        devices.put(device.getIp(), device);
                        deviceAdapter.setData(devices);
                    }
                });
            }

            @Override
            public void onDeviceRemove(final Device device) {
                UIThreadUtil.postUI(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LancommTestActivity.this, "设备下线了：" + device.getIp(), Toast.LENGTH_LONG).show();
                        devices.remove(device.getIp());
                        deviceAdapter.setData(devices);
                    }
                });
            }
        });
    }

    public void click_broadcast(View view) {

        LanCommManager.getBroadcaster().broadcast("啦啦啦啦啦啦啦啦啦噢噢噢噢噢噢噢噢".getBytes());

    }

    public void sendMsg(Device device) {
        Command command = new Command().setDestIp(device.getIp())
                .setData("我点你了，哈哈~".getBytes())
                .setCallback(new Command.Callback() {
                    @Override
                    public void onSuccess() {
                        Trace.d(TAG, "send success");
                    }

                    @Override
                    public void onError(int code) {
                        Trace.d(TAG, "onError() " + code);
                    }

                    @Override
                    public void onReceived() {
                        Trace.d(TAG, "对方已收到");
                    }
                });
        LanCommManager.getCommunicator().sendCommand(command);
    }

    public void click_search(View view) {

        LanCommManager.getSearcher().startSearch(new SearchListener() {
            @Override
            public void onSearchStart() {
                Trace.d(TAG, "onSearchStart() ");
            }

            @Override
            public void onSearchedNewOne(Device device) {
                Trace.d(TAG, "onSearchedNewOne() " + device.toString());
            }

            @Override
            public void onSearchFinish(final HashMap<String, Device> dd) {
                devices = dd;
                Trace.d(TAG, "onSearchFinish() size:" + devices.size());
                UIThreadUtil.postUI(new Runnable() {
                    @Override
                    public void run() {
                        deviceAdapter.setData(devices);
                    }
                });
            }
        });

    }

    public void open_search(View view) {
        LanCommManager.getSearcher().setCanBeSearched(true);
    }

    public void close_search(View view) {
        LanCommManager.getSearcher().setCanBeSearched(false);
    }
}
