package com.umbeo.homeautomation;

public class HomeAutomationOperator
{
    String ip="";
    String cmd="";
    HomeAutomationListener h=null;
    HomeAutomationOperator(String a, String b, HomeAutomationListener c)
    {
        ip=a;
        cmd=b;
        h=c;
    }
}