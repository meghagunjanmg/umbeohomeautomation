package com.umbeo.homeautomation;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RealysDao {

    @Query("SELECT * FROM devicerelays ")
    LiveData<List<RelayModel>> getAll();

    @Query("SELECT * FROM devicerelays")
    List<RelayModel> loadAll();

    @Insert
    void insertAll(List<RelayModel> entities);

    @Query("DELETE FROM devicerelays")
    void nukeTable();

    @Query("Update devicerelays SET relay_name =:dev WHERE pid =:id")
    void UpdateOne(int id, String dev);

    @Query("Update devicerelays SET relayState =:dev WHERE pid =:id")
    void UpdateState(int id, String dev);
}
