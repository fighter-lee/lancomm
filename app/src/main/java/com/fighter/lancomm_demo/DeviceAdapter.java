package com.fighter.lancomm_demo;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fighter.lancomm.data.Device;

import java.util.HashMap;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author fighter_lee
 * @date 2020/1/16
 */
public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.MyViewHolder> {

    private HashMap<String, Device> devices = new HashMap<>();
    private String[] mKeys = new String[]{};

    public void setData(HashMap<String, Device> devices) {
        this.devices = devices;
        Set<String> strings = devices.keySet();
        this.mKeys = strings.toArray(mKeys);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_device, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final Device device = devices.get(mKeys[i]);
        myViewHolder.deviceName.setText(device.getIp());
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(device);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView deviceName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.tv_device_name);
        }
    }

    private ItemClickListener mItemClickListener;

    public interface ItemClickListener {
        public void onItemClick(Device device);
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;

    }
}
