package com.my51c.see51.app.utils;

import android.content.Context;
import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.synertone.netAssistant.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by snt1231 on 2018/4/20.
 */

public class ViewUtil {
    public static void setSpinner(final Context mContext, final boolean enable, final Spinner... spinners) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (spinners != null && spinners.length > 0) {
                    for (int i = 0; i < spinners.length; i++) {
                        Spinner spinner = spinners[i];
                        spinner.setEnabled(enable);
                        View selectedView = spinner.getSelectedView();
                        if (selectedView != null) {
                            TextView view = (TextView) selectedView.findViewById(android.R.id.text1);
                            if (view != null) {
                                view.setTextColor(enable == false ? mContext.getResources().getColor(R.color.gray) : mContext.getResources().getColor(R.color.black
                                ));
                            }
                        }

                    }
                }
            }
        }, 400);
    }

    public static void setToggleButton(boolean enable, ToggleButton mToggleButton) {
        mToggleButton.setEnabled(enable);
    }

    /**
     * 禁止EditText输入特殊字符
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpeChat(EditText editText,int length) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat="[`~!@''#$%^&*/+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）+|{}【】‘；：”“’。，、？\"——\\\\]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) {
                    return "";
                } else{
                    return null;
                }
            }
        };
        InputFilter[] filters = {new InputFilter.LengthFilter(length),filter};
        editText.setFilters(filters);
    }
    public static void setEditextView(EditText editextView,String content){
        editextView.setText(content);
        editextView.setSelection(content.length());
    }
}