package com.deltaww.smarthome;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * Created by lws on 2016/5/16.
 */
public class DataReceiveThread extends Thread {
    private static final String TAG = "DataReceiveThread";

    private Socket socket;

    private Handler handler;

    public DataReceiveThread(Handler handler) {
        this.handler = handler;
        Log.d(TAG, "DataReceiveThread Created.");
    }

    @Override
    public void run() {
        Log.d(TAG, "run() called.");
        try {
            socket = new Socket("192.168.16.254", 8080);
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

            while (true) {
                Log.d(TAG, "run: Message received.");
//                if (in.readByte() == 0x7f) {
//                    Message message = new Message();
//                    message.what = in.readByte();
//                    Log.d(TAG, String.valueOf(message.what));
//
//                    byte[] value = new byte[4];
//                    in.read(value, 0, 4);
//                    message.obj = value;
//                    Log.d(TAG, value.toString());
//                    handler.sendMessage(message);
//                }
                byte[] b = new byte[8];
                in.read(b, 0, 8);
                Message message = new Message();
                message.what = 0x012;
                message.obj = b;
                handler.sendMessage(message);
                StringBuilder s = new StringBuilder();
                for (int i = 0; i < 8; i++) {
                    s.append(Integer.toHexString(b[i] & 0xFF) + " ");
                }
                Log.d(TAG, "run: " + s.toString());
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
