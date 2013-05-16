
package com.examples.android.calendar;

import java.text.DateFormatSymbols;
import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CalendarHeaderAdapter extends BaseAdapter {    
    
    private Context mContext;
    private ArrayList<String> dayNames;
    
    public CalendarHeaderAdapter(Context c) {
        mContext = c;

        DateFormatSymbols symbols = new DateFormatSymbols(); 
        String[] dayNameArray = symbols.getShortWeekdays(); //returns "" in 0 position for some reason. Process here.
        validateDays(dayNameArray);
    }
    
    public void validateDays(String[] dayNameArray) {
      dayNames = new ArrayList<String>();
      for (int i = 0; i < dayNameArray.length; i++) {
        if (!TextUtils.isEmpty(dayNameArray[i])) {
          dayNames.add(dayNameArray[i]);
        }
      }
    }
    
    public int getCount() {
        return dayNames.size();
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
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.calendar_weekday_item, null);         
        }
        TextView dayView = (TextView)v.findViewById(R.id.calendar_weekday_title);
        dayView.setText(dayNames.get(position));
      
        return v;
    }

}