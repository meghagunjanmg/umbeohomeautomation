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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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

    static AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context=getApplicationContext();
        scan=(Button)findViewById(R.id.scan);
        bottomAppBar = (BottomAppBar) findViewById(R.id.bottomAppBar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        grantPersmission();
        loop.start();
        ds = new DeviceScanner();

        if (db == null) {
            db = AppDatabase.getInstance(getApplicationContext());
        }

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
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        db.deviceDao().nukeTable();
                    }
                });



                //adapter.notifyDataSetChanged();
                Set<String> scanned_dev = ds.scanDevice("D:ECHO",5210,2000,5);
                if(scanned_dev.size()>0) {
                    device_list.addAll(scanned_dev);
                    for(int i = 0; i<device_list.size();i++) {
                        deviceModels.add(new DeviceModel(i,device_list.get(i), device_list.get(i),0));
                    }
                    //adapter = new DeviceAdapter(HomeActivity.this,deviceModels);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                    recyclerView.setAdapter(adapter);

                    DeleteData();
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



    static SynchronousQueue<OperateData> sq = new SynchronousQueue<>(true);
    private Thread loop = new Thread()
    {
        DeviceState ds;
        boolean connect_flg=false;
        SocketAddress dest;
        long snp=System.currentTimeMillis();
        long timeout=500;
        boolean r_stat[] = new boolean[4];
        public void run()
        {
            try {
                userv=new DatagramSocket();
                userv.setSoTimeout(1);
                while(true)
                {
                    try {
                        DatagramPacket dp = new DatagramPacket(new byte[1024],1024);
                        userv.receive(dp);
                        byte rdata[] = Arrays.copyOf(dp.getData(),dp.getLength());
                        String rcmd = new String(rdata,0,rdata.length);
                        if(rcmd.indexOf("R:STATE:")==0)
                        {
                            String stat=rcmd.substring(8);
                            r_stat[0]=stat.charAt(0)=='1';
                            //r1.setBackgroundColor(r_stat[0]? Color.GREEN:Color.MAGENTA);
                            r_stat[1]=stat.charAt(1)=='1';
                            //r2.setBackgroundColor(r_stat[1]? Color.GREEN:Color.MAGENTA);
                            r_stat[2]=stat.charAt(2)=='1';
                            //r3.setBackgroundColor(r_stat[2]? Color.GREEN:Color.MAGENTA);
                            r_stat[3]=stat.charAt(3)=='1';
                            //r4.setBackgroundColor(r_stat[3]? Color.GREEN:Color.MAGENTA);

                            ds.relayState(rcmd.substring(8));

                        }
                    }
                    catch(Exception ee)
                    {
                    }
                    try {
                        OperateData cmd=sq.poll();
                        if(cmd!=null)
                        {
                            if(cmd.cmd.indexOf("start:")==0)
                            {
                                dest=new InetSocketAddress(cmd.cmd.substring(6),5210);
                                ds=cmd.ds;
                                connect_flg=true;
                            }
                            else if(cmd.cmd.equals("stop"))
                            {
                                connect_flg=false;
                            }
                            else if(connect_flg && cmd.cmd.equals("r1"))
                            {
                                sendPacket((r_stat[0])?"R:10":"R:11");
                            }
                            else if(connect_flg && cmd.cmd.equals("r2"))
                            {
                                sendPacket((r_stat[1])?"R:20":"R:21");
                            }
                            else if(connect_flg && cmd.cmd.equals("r3"))
                            {
                                sendPacket((r_stat[2])?"R:30":"R:31");
                            }
                            else if(connect_flg && cmd.cmd.equals("r4"))
                            {
                                sendPacket((r_stat[3])?"R:40":"R:41");
                            }
                        }
                    }
                    catch(Exception ee)
                    {

                    }
                    if(connect_flg && (System.currentTimeMillis()-snp)>=timeout)
                    {
                        sendPacket("R:STATE");
                        snp=System.currentTimeMillis();
                    }
                }
            }
            catch(Exception e){}
        }
        void sendPacket(String dta)
        {
            try
            {
                DatagramPacket dp = new DatagramPacket(dta.getBytes(),0,dta.length());
                dp.setSocketAddress(dest);
                userv.send(dp);
            }
            catch(Exception e)
            {

            }
        }
    }
            ;
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