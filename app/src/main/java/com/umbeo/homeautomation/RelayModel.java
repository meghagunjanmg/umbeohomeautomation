package com.umbeo.homeautomation;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "devicerelays")
public class RelayModel {

    @PrimaryKey
    int pid;

    String relay_name;
    String relayState;
    String deviceip;
    String ds;

    public RelayModel(int pid, String relay_name, String relayState, String deviceip, String ds) {
        this.pid = pid;
        this.deviceip = deviceip;
        this.relay_name = relay_name;
        this.relayState = relayState;
        this.ds = ds;
    }

    public String getDs() {
        return ds;
    }

    public void setDs(String ds) {
        this.ds = ds;
    }

    public String getDeviceip() {
        return deviceip;
    }

    public void setDeviceip(String deviceip) {
        this.deviceip = deviceip;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getRelay_name() {
        return relay_name;
    }

    public void setRelay_name(String relay_name) {
        this.relay_name = relay_name;
    }

    public String getRelayState() {
        return relayState;
    }

    public void setRelayState(String relayState) {
        this.relayState = relayState;
    }
}
