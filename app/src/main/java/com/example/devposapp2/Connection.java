package com.example.devposapp2;

import android.content.Context;

public class Connection extends Socketmanager {
Socketmanager mSockManager = new Socketmanager() ;



    public boolean conTest(String printerIp, int port) {
        mSockManager.mPort=port;
        mSockManager.mstrIp=printerIp;
        mSockManager.threadconnect();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (mSockManager.getIstate()) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean PrintfData(byte[]data, int size, int align ) {
        mSockManager.threadconnectwrite(data,size,align);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (mSockManager.getIstate()) {
            return true;
        }
        else {
            return false;
        }
    }

}
