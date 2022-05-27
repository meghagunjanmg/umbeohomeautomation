package com.umbeo.homeautomation;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "devices")
public class DeviceModel {
    @PrimaryKey
    int pid;
    String device_name;
    String new_name;
    int device_status;

    public DeviceModel(int pid,String device_name, String new_name, int device_status) {
        this.pid = pid;
        this.device_name = device_name;
        this.new_name = new_name;
        this.device_status = device_status;

    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public String getNew_name() {
        return new_name;
    }

    public void setNew_name(String new_name) {
        this.new_name = new_name;
    }

    public int getDevice_status() {
        return device_status;
    }

    public void setDevice_status(int device_status) {
        this.device_status = device_status;
    }
}
