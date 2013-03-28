/*
 * Copyright 2011 Lauri Nevala.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.examples.android.calendar;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CalendarAdapter extends BaseAdapter {
  static final int FIRST_DAY_OF_WEEK =0; // Sunday = 0, Monday = 1


  private Context mContext;

  private Calendar month;
  private Calendar selectedDate;
  private Calendar todayDate;
  private Calendar week;
  private ArrayList<String> items;
  private TextView monthTitle;

  public CalendarAdapter(Context c, Calendar monthCalendar, Calendar weekCalendar, TextView monthTitle) {
    month = monthCalendar;
    week = weekCalendar;
    selectedDate = (Calendar)monthCalendar.clone();
    selectedDate.set(0, 0, 0);
    todayDate = Calendar.getInstance();
    mContext = c;
    month.set(Calendar.DAY_OF_MONTH, 1);
    week.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
    this.items = new ArrayList<String>();
    this.monthTitle = monthTitle;
  }

  public void setItems(ArrayList<String> items) {
    for(int i = 0;i != items.size();i++){
      if(items.get(i).length()==1) {
        items.set(i, "0" + items.get(i));
      }
    }
    this.items = items;
    notifyDataSetInvalidated();
    notifyDataSetChanged();
  }


  public int getCount() {
    return days.length;
  }

  public Object getItem(int position) {
    return null;
  }

  public long getItemId(int position) {
    return 0;
  }

  // create a new view for each item referenced by the Adapter
  public View getView(int position, View convertView, ViewGroup parent) {
    View v = convertView;
    TextView dayView;
    if (convertView == null) {  // if it's not recycled, initialize some attributes
      LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      v = vi.inflate(R.layout.calendar_item, null);
    }

    dayView = (TextView)v.findViewById(R.id.date);
    dayView.setTextColor(Color.parseColor("#AAAAAA"));
    RelativeLayout container = (RelativeLayout)v.findViewById(R.id.item_content_container);
    // disable empty days from the beginning
    if(days[position].equals("")) {
      dayView.setClickable(false);
      dayView.setFocusable(false);
      container.setBackgroundResource(android.R.color.transparent);
    }
    else {
      // mark current day as focused
      if(month.get(Calendar.YEAR)== selectedDate.get(Calendar.YEAR) && month.get(Calendar.MONTH)== selectedDate.get(Calendar.MONTH) && days[position].equals(""+selectedDate.get(Calendar.DAY_OF_MONTH))) {
        container.setBackgroundResource(R.drawable.calendar_item_focused_background);
      } else {
        container.setBackgroundResource(android.R.color.transparent);
      }
      
      //today is persistent color
      if(month.get(Calendar.YEAR)== todayDate.get(Calendar.YEAR) && month.get(Calendar.MONTH)== todayDate.get(Calendar.MONTH) && days[position].equals(""+todayDate.get(Calendar.DAY_OF_MONTH))) {
        container.setBackgroundResource(R.drawable.calendar_item_today_background);
        dayView.setTextColor(Color.parseColor("#70B700"));
      }
    }
    dayView.setText(days[position]);

    // create date string for comparison
    String date = days[position];

    if(date.length()==1) {
      date = "0"+date;
    }
    String monthStr = ""+(month.get(Calendar.MONTH)+1);
    if(monthStr.length()==1) {
      monthStr = "0"+monthStr;
    }

    // show icon if date is not empty and it exists in the items array
    ImageView iw = (ImageView)v.findViewById(R.id.date_marker);
    if(date.length()>0 && items!=null && items.contains(date)) {        	
      iw.setVisibility(View.VISIBLE);
    }
    else {
      iw.setVisibility(View.INVISIBLE);
    }
    return v;
  }
  
  public void setSelectedDate(Calendar selectedDate) {
    this.selectedDate = selectedDate;
    notifyDataSetChanged();
  }

  public void refreshDays()
  {
    // clear items
    items.clear();

    monthTitle.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
    int lastDay = month.getActualMaximum(Calendar.DAY_OF_MONTH);
    int firstDay = (int)month.get(Calendar.DAY_OF_WEEK);

    // figure size of the array
    if(firstDay==1){
      days = new String[lastDay+(FIRST_DAY_OF_WEEK*6)];
    }
    else {
      days = new String[lastDay+firstDay-(FIRST_DAY_OF_WEEK+1)];
    }

    int j=FIRST_DAY_OF_WEEK;

    // populate empty days before first real day
    if(firstDay>1) {
      for(j=0;j<firstDay-FIRST_DAY_OF_WEEK;j++) {
        days[j] = "";
      }
    }
    else {
      for(j=0;j<FIRST_DAY_OF_WEEK*6;j++) {
        days[j] = "";
      }
      j=FIRST_DAY_OF_WEEK*6+1; // sunday => 1, monday => 7
    }

    // populate days
    int dayNumber = 1;
    for(int i=j-1;i<days.length;i++) {
      days[i] = ""+dayNumber;
      dayNumber++;
    }
    
    //add in buffer days at the end to complete grid
    int numberOfItemsToCompleteGrid = 7 * (int)Math.ceil(days.length / 7.0);
    String[] tempDay = new String[numberOfItemsToCompleteGrid];
    for(int i = 0; i<days.length; i++) {
      tempDay[i] = days[i]; 
    }
    for(int i = days.length; i<numberOfItemsToCompleteGrid;i++ ) {
      tempDay[i] = ""; 
    }
    days = tempDay;
  }
  
  public void refreshDaysForWeek() {
    // clear items
    items.clear();

    monthTitle.setText("Week of " + android.text.format.DateFormat.format("MMMM d, yyyy", week));
    days = new String[7];
    
    Calendar tempCalendar = (Calendar)week.clone();
    for (int i = 0; i < 7; i++) {
      days[i]= String.valueOf(tempCalendar.get(Calendar.DAY_OF_MONTH));
      tempCalendar.add(Calendar.DATE, 1);
    }
    // populate days
    
  }

  // references to our items
  public String[] days;
}