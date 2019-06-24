package com.example.anonymouschat.TCP;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;

public class TCPConnection {
    private final Socket socket;
    private TCPConnectionListener eventListener;
    private BufferedReader in;
    private BufferedWriter out;
    private Thread rxThread;
    private boolean mRun = false;

    public TCPConnection(TCPConnectionListener eventListener, String ipAdr, int port) throws IOException{
        this(eventListener, new Socket(ipAdr, port));
    }

    public TCPConnection(final TCPConnectionListener ventListener, Socket socket) throws IOException{
        this.socket = socket;
        this.eventListener = ventListener;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));
        rxThread = null;
    }

    public synchronized void sendString(final String value){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    out.write(value +"\r\n");
                    out.flush();
                } catch (IOException e) {
                    eventListener.onException(TCPConnection.this, e);
                    disconnect();
                }
            }
        }).start();
    }

    public void run(){
        mRun=true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    eventListener.onConnectionReady(TCPConnection.this);

                    while (mRun){
                        eventListener.onReceiveString(in.readLine());
                    }
                } catch (Exception e){
                    eventListener.onException(TCPConnection.this, e);
                } finally {
                    eventListener.onDisconnect(TCPConnection.this);
                }
            }
        }).start();
    }

    public void stopClient() {
        mRun = false;

        if (out != null) {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
            }
        }
        eventListener = null;
        in = null;
        out = null;
    }

    public synchronized void disconnect(){
        mRun = false;
        try {
            socket.close();
        } catch (IOException e) {
            eventListener.onException(TCPConnection.this, e);
        }
    }

    @Override
    public String toString() {
        return "TCPConnection: " + socket.getInetAddress().toString() + ":" + socket.getPort();
    }
}
