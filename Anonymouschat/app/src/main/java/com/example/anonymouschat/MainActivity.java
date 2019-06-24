package com.example.anonymouschat;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Trace;
import android.support.annotation.AnyThread;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.anonymouschat.TCP.TCPConnection;
import com.example.anonymouschat.TCP.TCPConnectionListener;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static String IP_ADDR = "46.146.137.184";
    private static final int port = 8189;
    TextView textView;
    TextView name;
    TextView message;
    TextView ip;
    ScrollView scrollView;
    private static TCPConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView10);
        ip = findViewById(R.id.editText23);
        name = findViewById(R.id.editText18);
        message = findViewById(R.id.editText21);
        scrollView = findViewById(R.id.my_scrole_view);
        ip.setText(IP_ADDR);
        new ConnectTask().execute();
    }
    public void reconnect(View view){
        connection.disconnect();
        IP_ADDR = ip.getText().toString();
        new ConnectTask().execute();

    }

    public synchronized void sendMessage(View view){
        String msg = message.getText().toString();
        if(msg.equals("")){
            return;
        }
        message.setText(null);
        String usr = name.getText().toString();
        connection.sendString(usr + ": " +msg);
    }

    private void printMessage(String msg){
        textView.append(msg + "\n");
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
    }

    public class ConnectTask extends AsyncTask<Void, String, TCPConnection> implements TCPConnectionListener {

        @SuppressLint("WrongThread")
        @Override
        protected TCPConnection doInBackground(Void... voids) {
            try {
                connection = new TCPConnection(this, IP_ADDR, port);
            } catch (IOException e) {
                onProgressUpdate("Connection exception: " + e);
            }
            connection.run();
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            printMessage(values[0]);
        }

        @Override
        public void onConnectionReady(TCPConnection tcpConnection) {
            publishProgress("Connection ready...");
        }

        @Override
        public void onReceiveString(String value) {
            publishProgress(value);
        }

        @Override
        public void onDisconnect(TCPConnection tcpConnection) {
            publishProgress("Connection close...");
        }

        @Override
        public void onException(TCPConnection tcpConnection, Exception e) {
            publishProgress("Connection exception: " + e);
        }
    }
}
