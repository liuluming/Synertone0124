package com.my51c.see51.widget;

import android.os.Bundle;

import com.synertone.commonutil.view.BaseNiceDialog;
import com.synertone.commonutil.view.ViewConvertListener;
import com.synertone.commonutil.view.ViewHolder;

public class NiceDialog extends BaseNiceDialog{
    private ViewConvertListener convertListener;
    public static NiceDialog init() {
        NiceDialog niceDialog = new NiceDialog();
        niceDialog.setMargin(30);
        return niceDialog;
    }
    @Override
    public int intLayoutId() {
        return layoutId;
    }
    public NiceDialog setLayoutId(int layoutId) {
        this.layoutId = layoutId;
        return this;
    }
    public NiceDialog setConvertListener(ViewConvertListener convertListener) {
        this.convertListener = convertListener;
        return this;
    }
    @Override
    public void convertView(ViewHolder holder, BaseNiceDialog dialog) {
        if (convertListener != null) {
            convertListener.convertView(holder, dialog);
        }
    }
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            convertListener = savedInstanceState.getParcelable("listener");
        }
    }

    /**
     * 保存接口
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("listener", convertListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        convertListener = null;
    }
}
