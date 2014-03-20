package net.alfss.smsserver.sms.interfaces;

/**
 * User: alfss
 * Date: 27.11.13
 * Time: 15:23
 */
public interface AsyncSmsServerThreadInterface{

    public void run();
    public boolean isRunning();
    public void setRunning(boolean running);
    void setThreadName();
}
