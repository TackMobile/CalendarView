package com.examples.android.calendar;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;


public class CalendarWeekView extends Activity {

    public Calendar week;
    public CalendarAdapter adapter;
    public Handler handler;
    public ArrayList<String> items; // container to store some random calendar items
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        week = Calendar.getInstance();
        onNewIntent(getIntent());
        
        items = new ArrayList<String>();
        adapter = new CalendarAdapter(this, week);
        
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(adapter);
                
        TextView title  = (TextView) findViewById(R.id.title);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", week));
        
        TextView previous  = (TextView) findViewById(R.id.previous);
        previous.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
              if (week.get(Calendar.DAY_OF_YEAR) <= 7) {
                week.add(Calendar.YEAR, -1);
                week.set(Calendar.DAY_OF_YEAR, week.getActualMaximum(Calendar.DAY_OF_YEAR)- (7-week.get(Calendar.DAY_OF_YEAR)));
              } else {
                week.add(Calendar.DAY_OF_YEAR, -7);
              }
                refreshCalendar();
            }
        });
        
        TextView next  = (TextView) findViewById(R.id.next);
        next.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
              if (week.get(Calendar.DAY_OF_YEAR) + 7 > week.getActualMaximum(Calendar.DAY_OF_YEAR)) {
                week.set(Calendar.DAY_OF_YEAR, (7-(week.getActualMaximum(Calendar.DAY_OF_YEAR)-week.get(Calendar.DAY_OF_YEAR))));
                week.add(Calendar.YEAR, 1);
              } else {
                week.add(Calendar.DAY_OF_YEAR, 7);
              }
                refreshCalendar();
            }
        });
        
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                TextView date = (TextView)v.findViewById(R.id.date);
                if(date instanceof TextView && !date.getText().equals("")) {
                    
                    Intent intent = new Intent();
                    String day = date.getText().toString();
                    if(day.length()==1) {
                        day = "0"+day;
                    }
                    // return chosen date as string format 
                    intent.putExtra("date", android.text.format.DateFormat.format("yyyy-MM", week)+"-"+day);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                
            }
        });
    }
    
    public void refreshCalendar()
    {
        TextView title  = (TextView) findViewById(R.id.title);
        
        adapter.refreshDaysForWeek();
        adapter.notifyDataSetChanged();             
        
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", week));
    }
    
    public void onNewIntent(Intent intent) {
        String date = intent.getStringExtra("date");
        String[] dateArr = date.split("-"); // date format is yyyy-mm-dd
        week.set(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1]), Integer.parseInt(dateArr[2]));
    }
}
