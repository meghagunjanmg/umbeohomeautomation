package com.umbeo.homeautomation;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "devicerelays")
public class RelayModel {

    @PrimaryKey
    int pid;

    String relay_name;
    String relayState;

    public RelayModel(int pid, String relay_name, String relayState) {
        this.pid = pid;
        this.relay_name = relay_name;
        this.relayState = relayState;
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
