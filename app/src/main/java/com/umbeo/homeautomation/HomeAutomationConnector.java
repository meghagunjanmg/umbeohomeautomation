package com.umbeo.homeautomation;

import java.net.*;
import java.util.*;
import java.util.concurrent.SynchronousQueue;

public class HomeAutomationConnector
{
    HomeAutomationConnector()
    {
        loop.start();
    }
    public static SynchronousQueue<HomeAutomationOperator> sq = new SynchronousQueue<>(true);
    private DatagramSocket ds;
    private String tx_state_string="R:STATE";
    private String rx_state_string="R:STATE:";
    private int d_port=5210;
    class HomeAutomationData
    {
        HomeAutomationData(SocketAddress sa)
        {
            device_socket_address=sa;
        }
        long snp=0;
        long tx_timeout=500;
        SocketAddress device_socket_address;
    }
    class HomeAutomationState
    {
        HomeAutomationState(HomeAutomationListener a)
        {
            listnr=a;
        }
        HomeAutomationListener listnr=null;
        String stat="";
    }
    Map<String,HomeAutomationData> h_data = new HashMap<>();
    Map<SocketAddress,HomeAutomationState> r_data = new HashMap<>();
    private Thread loop = new Thread()
    {
        public void run()
        {
            try {
                ds=new DatagramSocket();
                ds.setSoTimeout(1);
                ds.setReceiveBufferSize(20480);
                ds.setSendBufferSize(20480);
                while (true)
                {
                    processReceivingData();
                    try {
                        HomeAutomationOperator h = sq.poll();
                        if (h != null) {
                            SocketAddress sa=new InetSocketAddress(h.ip,d_port);
                            if (h.cmd.equals("start")) {
                                h_data.put(h.ip, new HomeAutomationData(sa));
                                r_data.put(sa, new HomeAutomationState(h.h));
                            } else if (h.cmd.equals("stop")) {
                                h_data.remove(h.ip);
                                r_data.remove(sa);
                            }
                            else
                            {
                                sendData(h.cmd,sa);
                            }
                        }
                    }
                    catch(Exception ee){}
                    processSendingData();
                }
            }
            catch(Exception e){}
        }
        void processReceivingData()
        {
            try
            {
                while(true) {
                    DatagramPacket dp = new DatagramPacket(new byte[1024], 1024);
                    ds.receive(dp);
                    byte rdata[] = Arrays.copyOf(dp.getData(), dp.getLength());
                    String rcmd = new String(rdata, 0, rdata.length);
                    HomeAutomationState h=r_data.get(dp.getSocketAddress());
                    if(rcmd.indexOf(rx_state_string)==0 && !h.stat.equals(rcmd))
                    {
                        h.stat=rcmd;
                        h.listnr.homeAutomationState(h.stat.substring(rx_state_string.length()));
                    }
                    r_data.put(dp.getSocketAddress(),h);
                }
            }
            catch(Exception e){}
        }
        void processSendingData()
        {
            try
            {
                for(Map.Entry<String,HomeAutomationData> ent : h_data.entrySet())
                {
                    HomeAutomationData h = ent.getValue();
                    if((System.currentTimeMillis()-h.snp)>=h.tx_timeout)
                    {
                        sendData(tx_state_string,h.device_socket_address);
                        ent.getValue().snp=System.currentTimeMillis();
                    }
                }
            }
            catch (Exception e){}
        }
        void sendData(String a,SocketAddress sa)
        {
            try
            {
                DatagramPacket dp = new DatagramPacket(a.getBytes(),0,a.length());
                dp.setSocketAddress(sa);
                ds.send(dp);
            }
            catch(Exception e)
            {

            }
        }
    }
    ;
}