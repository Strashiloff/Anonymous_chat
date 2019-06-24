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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
