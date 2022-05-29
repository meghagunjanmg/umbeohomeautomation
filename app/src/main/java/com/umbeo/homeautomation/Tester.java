package com.umbeo.homeautomation;

class Tester
{
HomeAutomationConnector hc = new HomeAutomationConnector();
Tester()
{
try
{
hc.sq.put(new HomeAutomationOperator("127.0.0.1","start",new HomeAutomationListener()
{
public void homeAutomationState(String a)
{
System.out.println(a);
}
}
));
}
catch(Exception e)
{
}
}
}