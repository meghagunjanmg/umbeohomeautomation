package com.umbeo.homeautomation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.SynchronousQueue;

public class MainActivity extends AppCompatActivity {
    private DeviceScanner ds;
    private List<String> device_list = new LinkedList<>();
    private ArrayAdapter<String> aa;
    private Button scan,connect,disconnect,r1,r2,r3,r4;
    private Spinner ips;
    public static Context context;
    private DatagramSocket userv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=getApplicationContext();
        grantPersmission();
        loop.start();
        ds = new DeviceScanner();
        scan=(Button)findViewById(R.id.scan);
        ips=(Spinner)findViewById(R.id.ips);
        connect=(Button)findViewById(R.id.connect);
        disconnect=(Button)findViewById(R.id.disconnect);
        r1=(Button)findViewById(R.id.r1);
        r2=(Button)findViewById(R.id.r2);
        r3=(Button)findViewById(R.id.r3);
        r4=(Button)findViewById(R.id.r4);

        aa=new ArrayAdapter<>
                (context,android.R.layout.simple_spinner_item,device_list);
        aa.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        ips.setAdapter(aa);


        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                device_list.clear();
                aa.notifyDataSetChanged();
                Set<String> scanned_dev = ds.scanDevice("D:ECHO",5210,2000,5);
                if(scanned_dev.size()>0) {
                    device_list.addAll(scanned_dev);
                    aa.notifyDataSetChanged();
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
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sq.put("start:"+(String)ips.getSelectedItem());
                }
                catch(Exception e)
                {

                }

            }
        });
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sq.put("stop");
                }
                catch(Exception e)
                {

                }
            }
        });
        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sq.put("r1");
                }
                catch(Exception e)
                {

                }
            }
        });
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sq.put("r2");
                }
                catch(Exception e)
                {

                }
            }
        });
        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sq.put("r3");
                }
                catch(Exception e)
                {

                }
            }
        });
        r4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sq.put("r4");
                }
                catch(Exception e)
                {

                }
            }
        });
    }
    private SynchronousQueue<String> sq = new SynchronousQueue<>(true);
    private Thread loop = new Thread()
    {
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
                            r1.setBackgroundColor(r_stat[0]? Color.GREEN:Color.MAGENTA);
                            r_stat[1]=stat.charAt(1)=='1';
                            r2.setBackgroundColor(r_stat[1]? Color.GREEN:Color.MAGENTA);
                            r_stat[2]=stat.charAt(2)=='1';
                            r3.setBackgroundColor(r_stat[2]? Color.GREEN:Color.MAGENTA);
                            r_stat[3]=stat.charAt(3)=='1';
                            r4.setBackgroundColor(r_stat[3]? Color.GREEN:Color.MAGENTA);
                        }
                    }
                    catch(Exception ee)
                    {
                    }
                    try {
                        String cmd=sq.poll();
                        if(cmd!=null)
                        {
                            if(cmd.indexOf("start:")==0)
                            {
                                dest=new InetSocketAddress(cmd.substring(6),5210);
                                connect_flg=true;
                            }
                            else if(cmd.equals("stop"))
                            {
                                connect_flg=false;
                            }
                            else if(connect_flg && cmd.equals("r1"))
                            {
                                sendPacket((r_stat[0])?"R:10":"R:11");
                            }
                            else if(connect_flg && cmd.equals("r2"))
                            {
                                sendPacket((r_stat[1])?"R:20":"R:21");
                            }
                            else if(connect_flg && cmd.equals("r3"))
                            {
                                sendPacket((r_stat[2])?"R:30":"R:31");
                            }
                            else if(connect_flg && cmd.equals("r4"))
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