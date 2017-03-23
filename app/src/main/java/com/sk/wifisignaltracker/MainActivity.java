package com.sk.wifisignaltracker;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements Runnable{
    TextView sStrength;
    Button display,refresh;
    Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sStrength=(TextView)findViewById(R.id.sigStrength);
        display=(Button)findViewById(R.id.displayButton);
        refresh=(Button)findViewById(R.id.refreshButton);

        WifiManager wifi=(WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo=wifi.getConnectionInfo();

        sStrength.setText(wifiInfo.getRssi()+" dBm");

        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),DisplayRSSI.class);
                startActivity(i);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thread=new Thread(MainActivity.this);
                thread.start();
            }
        });
    }

    @Override
    public void run() {
        SQLiteDatabase sqlDB=openOrCreateDatabase("db123#4",MODE_PRIVATE,null);
        //sqlDB.execSQL("DROP TABLE RSSIValRecorder");
        sqlDB.execSQL("CREATE TABLE IF NOT EXISTS RSSIValRecorder(EventNo int AUTO_INCREMENT,SubEventNo int AUTO_INCREMENT,DateTimeOfRecord varchar(30),rssiVal int)");
        for(int i=0;i<20;i++){
            try{
                Thread.sleep(500);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SQLiteDatabase sqlDB=openOrCreateDatabase("db123#4",MODE_PRIVATE,null);

                        WifiManager wifi=(WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        WifiInfo wifiInfo=wifi.getConnectionInfo();

                        sStrength.setText(wifiInfo.getRssi()+" dBm");
                        sqlDB.execSQL("INSERT INTO RSSIValRecorder(rssiVal,DateTimeOfRecord) values("+wifiInfo.getRssi()+",'"+ new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance().getTime())+"')");

                        Log.d("ABCABC","Found new Signal Strength");
                    }
                });

            }
            catch (Exception e){
                Log.d("ABCABC",e.toString());
            }
        }


    }
}
