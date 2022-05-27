package com.umbeo.homeautomation;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DeviceDao {

    @Query("SELECT * FROM devices")
    LiveData<List<DeviceModel>> getAll();

    @Query("SELECT * FROM devices")
    List<DeviceModel> loadAll();

    @Insert
    void insertAll(List<DeviceModel> entities);

    @Query("Update devices SET device_status =:dev WHERE pid =:id")
    void UpdateOne2(int id, int dev);

    @Query("Update devices SET new_name =:dev WHERE pid =:id")
    void UpdateOne(int id, String dev);

    @Query("DELETE FROM devices")
    void nukeTable();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DeviceModel devices);

    @Query("SELECT * from devices WHERE device_name= :dev")
    List<DeviceModel> getItemById(String dev);


}