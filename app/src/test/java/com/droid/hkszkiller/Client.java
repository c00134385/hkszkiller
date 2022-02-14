package com.droid.hkszkiller;

import com.droid.hkszkiller.http.Configure;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    Socket socket;
    BufferedReader in;
    BufferedWriter out;
    BufferedReader wt;

    Thread readThread;

    private boolean isRunning = true;

    Client() {
        try {
            InetAddress inetAddress = InetAddress.getByName(Configure.ip);
            socket = new Socket(inetAddress, Configure.port);

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            wt = new BufferedReader(new InputStreamReader(System.in));

            readThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Thread: " + Thread.currentThread().getName() + " running");
                    while(isRunning) {
                        char[] buffer = new char[128];
//                        StringBuilder sb = new StringBuilder();
                        while (true) {
                            try {
                                int count = in.read(buffer);
                                if(count < 0) {
                                    break;
                                }
                                System.out.print(String.valueOf(buffer, 0, count));
//                                sb.append(String.valueOf(buffer, 0, count));
                            } catch (IOException e) {
                                e.printStackTrace();
                                break;
                            }
                        }

                    }
                    System.out.println("Thread: " + Thread.currentThread().getName());
                }
            });
            readThread.setName("thead-" + socket.getPort() + "");
            readThread.start();
            System.out.println("test");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int write(String data) {
        System.out.println(data);
        System.out.println("length: " + data.length());
        try {
            out.write(data, 0, data.length());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        return 0;
    }

    public void start() {

    }

    public void release() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isRunning = false;
        readThread.interrupt();
        try {
            readThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
