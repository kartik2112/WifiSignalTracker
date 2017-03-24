package com.sk.wifisignaltracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Vector;

public class ChartDisplay extends AppCompatActivity {
    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_display);

        /**
         * Github Repo Reference: https://github.com/PhilJay/MPAndroidChart
         * Complete Code Reference: https://www.studytutorial.in/android-line-chart-or-line-graph-using-mpandroid-library-tutorial
         */
        lineChart = (LineChart) findViewById(R.id.lineChart);

        /**
         * This statement will create a database with this name in case this is the first time else it will open it
         * MODE_PRIVATE is used so that the database created by this application cannot be used by any other application
         * null is used for CursorFactory which according to documentation will hold the query output
         */
        SQLiteDatabase sqlDB = openOrCreateDatabase("db123#4", MODE_PRIVATE, null);

        /**
         * This part is used to query the database i.e. for retrieving data from tables
         */
        Cursor findTimes = sqlDB.rawQuery("SELECT * FROM RSSIValRecorder ORDER BY DateTimeOfRecord DESC", null);


        Log.d("ABCABC", "AIUIGYU");
        Log.d("ABCABC", findTimes.getCount() + " , " + findTimes.getColumnIndex("rssiVal"));

        //String arr[]=new String[findTimes.getCount()];
        ArrayList<Entry> vals = new ArrayList<Entry>();  //This Entry class is defined in MPAndroidChart
        ArrayList<String> xVals = new ArrayList<String>();

        Log.d("ABCABC", findTimes.getCount() + " , " + findTimes.getColumnIndex("rssiVal"));
        try {
            if (findTimes != null) {
                if (findTimes.moveToFirst()) {
                    int tempI = 1;
                    do {
                        vals.add(new Entry(findTimes.getInt(findTimes.getColumnIndex("rssiVal")), tempI));
                        xVals.add(tempI + "");
                        //Log.d("ABCABC",findTimes.getInt( findTimes.getColumnIndex("rssiVal"))+"");
                        tempI++;
                    } while (findTimes.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.d("ABCABC", e.toString());
        } finally {
            findTimes.close();
            sqlDB.close();
        }

        Log.d("ABCABC", vals.size() + "");


        LineDataSet lineData = new LineDataSet(vals, "Signal Strength (in dBm)");
        lineData.setCircleColor(R.color.colorPrimary);
        lineData.setColor(R.color.colorPrimary);
        lineData.setFillColor(R.color.colorPrimary);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(lineData);
        LineData data = new LineData(xVals, dataSets);

        lineChart.setData(data);
        lineChart.setDescription("Variation of Wi-Fi signal strengths over 1 minute");
        lineChart.setDescriptionColor(R.color.colorPrimary);
    }
}
