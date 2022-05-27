package com.umbeo.homeautomation;

public class OperateData {
    String cmd;
    DeviceState ds;

    public OperateData(String cmd, DeviceState ds) {
        this.cmd = cmd;
        this.ds = ds;
    }
}
