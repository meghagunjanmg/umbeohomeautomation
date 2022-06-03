package com.umbeo.homeautomation;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;

public class HomeActivity extends AppCompatActivity {
    private BottomAppBar bottomAppBar;
    private RecyclerView recyclerView;
    private Button scan;
    private DeviceScanner ds;
    private List<String> device_list = new LinkedList<>();
    private List<DeviceModel> deviceModels = new ArrayList<>();
    public static Context context;
    private DatagramSocket userv;
    DeviceAdapter adapter;
    static List<DeviceModel> deviceModelList = new ArrayList<>();
    static ConcurrentHashMap<String,String> relaystate = new ConcurrentHashMap<>();
    static AppDatabase db;
    static HomeAutomationConnector connector = new HomeAutomationConnector();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context=getApplicationContext();
        scan=(Button)findViewById(R.id.scan);
        bottomAppBar = (BottomAppBar) findViewById(R.id.bottomAppBar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        connector = new HomeAutomationConnector();
        grantPersmission();
        ds = new DeviceScanner();

        if (db == null) {
            db = AppDatabase.getInstance(getApplicationContext());
        }


        reconnect();


        db.deviceDao().getAll().observe(this, new Observer<List<DeviceModel>>() {
            @Override
            public void onChanged(List<DeviceModel> entities) {
                deviceModels = entities;
                deviceModelList = entities;

                adapter = new DeviceAdapter(HomeActivity.this, deviceModels);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                recyclerView.setAdapter(adapter);

            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                device_list.clear();
                deviceModels = new ArrayList<>();

                DeleteData();

                Set<String> scanned_dev = ds.scanDevice("D:ECHO",5210,2000,5);
                if(scanned_dev.size()>0) {
                    device_list.addAll(scanned_dev);
                    for(int i = 0; i<device_list.size();i++) {
                        deviceModels.add(new DeviceModel(i,device_list.get(i), device_list.get(i),0));
                    }

                    InsertData(deviceModels);

                    Toast.makeText(getApplicationContext(), "Device Scanned !!!",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Device Not Found !!!",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });


        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_menu:
                        return true;

                    case R.id.profile_menu:
                        return true;
                    default:
                        return false;
                }
            }
        });

    }

    private void reconnect() {

        HomeActivity.relaystate = new ConcurrentHashMap<>();

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                 deviceModels = db.deviceDao().loadAll();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        for(int i=0;i<deviceModels.size();i++){
            if(deviceModels.get(i).getDevice_status()==1){
                try
                {
                    int finalI = i;
                    connector.sq.put(new HomeAutomationOperator(deviceModels.get(finalI).getDevice_name(), "start", new HomeAutomationListener() {
                        public void homeAutomationState(String a)
                        {
                            if (a != null) {
                                HomeActivity.relaystate.remove(deviceModels.get(finalI).getDevice_name());
                                HomeActivity.relaystate.put(deviceModels.get(finalI).getDevice_name(),a);
                            }
                        }
                    }
                    ));
                }
                catch(Exception e)
                {
                }
            }
        }

    }


    static void updateName(int i , String n){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    db.deviceDao().UpdateOne(deviceModelList.get(i).getPid(),n);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    static void updateStatus(int i , int stat){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    db.deviceDao().UpdateOne2(deviceModelList.get(i).getPid(),stat);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void InsertData(List<DeviceModel> devices){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.deviceDao().insertAll(devices);
            }
        });
    }

    private void DeleteData(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                db.deviceDao().nukeTable();
            }
        });
    }



    private static final String[] REQUIRED_PERMISSIONS = new String[]
            { Manifest.permission.INTERNET};
    private static final int REQUEST_CODE_PERMISSIONS = 100;
    void grantPersmission()
    {
        for(String permission: REQUIRED_PERMISSIONS)
        {
            if(ContextCompat.checkSelfPermission(
                    getBaseContext(), permission) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,
                        REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
            }
        }
    }
}