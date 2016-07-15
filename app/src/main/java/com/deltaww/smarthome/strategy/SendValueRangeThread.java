package com.deltaww.smarthome.strategy;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by wenshan.lu on 2016/7/15.
 */
public class SendValueRangeThread extends Thread {

    private int type;
    private boolean bigger;
    private int value;

    public SendValueRangeThread(int type, boolean bigger, int value) {
        this.type = type;
        this.bigger = bigger;
        this.value = value;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket("192.168.16.254", 8080);
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            byte[] bytes = new byte[12];
            bytes[0] = (byte) 0x81;
            bytes[2] = 0x01;
            switch (type) {
                //温度
                case 1:
                    bytes[3] = 0x01;
                    bytes[4] = 0x10;
                    bytes[5] = 0x01;
                    break;
                case 2:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
