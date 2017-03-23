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

        SQLiteDatabase sqlDB=openOrCreateDatabase("db123#4",MODE_PRIVATE,null);
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
                        if(!findTimes.isNull(findTimes.getInt( findTimes.getColumnIndex("rssiVal")
                        ))){
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

        ArrayAdapter arrayAdapter=new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,arr);
        listView.setAdapter(arrayAdapter);
    }
}
