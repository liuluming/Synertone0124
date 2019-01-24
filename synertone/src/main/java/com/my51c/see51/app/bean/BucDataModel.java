package com.my51c.see51.app.bean;

import org.litepal.crud.DataSupport;

public class BucDataModel extends DataSupport {
    private String bucOscillator;
    private boolean isAdd = false;

    public String getBucOscillator() {
        return bucOscillator;
    }

    public void setBucOscillator(String bucOscillator) {
        this.bucOscillator = bucOscillator;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }
}
