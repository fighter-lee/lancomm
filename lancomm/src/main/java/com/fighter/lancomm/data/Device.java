package com.fighter.lancomm.data;

/**
 * @author fighter_lee
 * @date 2020/1/13
 */
public class Device {

    /**
     * ip地址
     */
    private String ip;

    public Device(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "Device{" +
                "ip='" + ip + '\'' +
                '}';
    }
}
