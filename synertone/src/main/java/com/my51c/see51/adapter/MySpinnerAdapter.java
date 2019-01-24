package com.my51c.see51.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.synertone.netAssistant.R;

public class MySpinnerAdapter extends ArrayAdapter<String> {
    Context context;
    String[] items;
    public MySpinnerAdapter(final Context context,
                            final int textViewResourceId, final String[] objects) {
        super(context, textViewResourceId, objects);
        this.items = objects;
        this.context = context;
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(
                    R.layout.spinner_dropdown_stytle, parent, false);
        }

        TextView tv = (TextView) convertView
                .findViewById(R.id.tv_spinner);
        tv.setText(items[position]);
        /*tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(15f);*/
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(
                    R.layout.spinner_stytle, parent, false);
        }

        TextView tv = (TextView) convertView
                .findViewById(R.id.tv_spinner);
        tv.setText(items[position]);
       /* tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(13f);*/
        return convertView;
    }

}