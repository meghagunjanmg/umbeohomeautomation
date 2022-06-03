package com.umbeo.homeautomation;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {DeviceModel.class,RelayModel.class}, version = 2)
    public abstract class AppDatabase extends RoomDatabase {

    public abstract DeviceDao deviceDao();
    public abstract RealysDao realysDao();

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "devices";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, AppDatabase.DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return sInstance;
    }

}
