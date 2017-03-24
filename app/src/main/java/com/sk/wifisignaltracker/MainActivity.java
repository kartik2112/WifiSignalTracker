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
import java.util.concurrent.RunnableFuture;

public class MainActivity extends AppCompatActivity implements Runnable{
    TextView sStrength, countDownValText;
    Button display, refresh, displayChart;
    Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //This sets the layout to be activity_main


        /**
         * Here the references of elements in layout are found are stored in their corresponding objects
         */
        sStrength=(TextView)findViewById(R.id.sigStrength);
        display=(Button)findViewById(R.id.displayButton);
        displayChart = (Button) findViewById(R.id.displayButtonChart);
        refresh=(Button)findViewById(R.id.refreshButton);
        countDownValText = (TextView) findViewById(R.id.countDownValTxt);


        /**
         * This is the part that is used to find the wifi rssi value
         */
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
                refresh.setEnabled(false);
                display.setEnabled(false);
                displayChart.setEnabled(false);

                countDownValText.setText("120");

                thread=new Thread(MainActivity.this);
                thread.start();
            }
        });

        displayChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ChartDisplay.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void run() {
        /**
         * This statement will create a database with this name in case this is the first time else it will open it
         * MODE_PRIVATE is used so that the database created by this application cannot be used by any other application
         * null is used for CursorFactory which according to documentation will hold the query output
         */
        SQLiteDatabase sqlDB=openOrCreateDatabase("db123#4",MODE_PRIVATE,null);

        //sqlDB.execSQL("DROP TABLE RSSIValRecorder");

        /**
         * This execSQL method will create a table if doesn't already exist
         * execSQL method is used for those SQL queries where the output is not expected.
         * E.g. INSERT, UPDATE, DELETE, CREATE TABLE
         */
        sqlDB.execSQL("CREATE TABLE IF NOT EXISTS RSSIValRecorder(EventNo int AUTO_INCREMENT,SubEventNo int AUTO_INCREMENT,DateTimeOfRecord varchar(30),rssiVal int)");

        /**
         * Since 120 values are recorded each time the table is cleaned so that it doesn't contain huge list of values
         */
        sqlDB.execSQL("DELETE FROM RSSIValRecorder");  //Surprisingly this is correct syntax for DELETE queries in SQLite
        for (int i = 1; i <= 120; i++) {  //This will make the thread run for 1 minute
            try{
                final int tempI = i;
                Thread.sleep(500); //Make this thread asleep for 500 ms

                /**
                 * This is used as referred from StackOverflow
                 * If you don't use this then an error occurs
                 * A thread that has created the layout only can change its properties
                 * Hence, this method is used for asking the UI Thread to make these changes
                 */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        countDownValText.setText(120 - tempI + "");


                        SQLiteDatabase sqlDB=openOrCreateDatabase("db123#4",MODE_PRIVATE,null);


                        /**
                         * This is the part that is used to find the wifi rssi value
                         */
                        WifiManager wifi=(WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        WifiInfo wifiInfo=wifi.getConnectionInfo();
                        sStrength.setText(wifiInfo.getRssi()+" dBm");


                        /**
                         * execSQL statement as mentioned before is used for running SQL queries where O/P isn't expected
                         */
                        sqlDB.execSQL("INSERT INTO RSSIValRecorder(rssiVal,DateTimeOfRecord) values("+wifiInfo.getRssi()+",'"+ new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance().getTime())+"')");

                        Log.d("ABCABC","Found new Signal Strength");
                    }
                });

            }
            catch (Exception e){
                Log.d("ABCABC",e.toString());
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refresh.setEnabled(true);
                display.setEnabled(true);
                displayChart.setEnabled(true);
            }
        });

    }
}
