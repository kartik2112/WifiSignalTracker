package com.sk.wifisignaltracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DisplayRSSI extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_rssi);

        listView=(ListView)findViewById(R.id.listVw);


        /**
         * This statement will create a database with this name in case this is the first time else it will open it
         * MODE_PRIVATE is used so that the database created by this application cannot be used by any other application
         * null is used for CursorFactory which according to documentation will hold the query output
         */
        SQLiteDatabase sqlDB=openOrCreateDatabase("db123#4",MODE_PRIVATE,null);

        /**
         * This part is used to query the database i.e. for retrieving data from tables
         */
        Cursor findTimes=sqlDB.rawQuery("SELECT * FROM RSSIValRecorder",null);


        Log.d("ABCABC","AIUIGYU");
        Log.d("ABCABC",findTimes.getCount()+" , "+findTimes.getColumnIndex("rssiVal"));

        String arr[]=new String[findTimes.getCount()];
        int tempI=0;
        Log.d("ABCABC",findTimes.getCount()+" , "+findTimes.getColumnIndex("rssiVal"));
        try{
            if(findTimes!=null){
                if(findTimes.moveToFirst()){
                    do{
                        if(!findTimes.isNull(findTimes.getInt( findTimes.getColumnIndex("rssiVal")))){
                            arr[tempI]=findTimes.getInt( findTimes.getColumnIndex("rssiVal"))+"";
                        }

                    }while (findTimes.moveToNext());
                }
            }
        }
        catch(Exception e){
            Log.d("ABCABC",e.toString());
        }
        finally {
            findTimes.close();
            sqlDB.close();
        }

        Log.d("ABCABC",arr.length+"");

        ArrayAdapter arrayAdapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,arr);
        listView.setAdapter(arrayAdapter);
    }
}
