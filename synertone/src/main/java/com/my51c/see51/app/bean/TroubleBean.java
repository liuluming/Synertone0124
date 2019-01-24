package com.my51c.see51.app.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.my51c.see51.adapter.ExpandableItemAdapter;

public class TroubleBean implements MultiItemEntity {
    private String name;
    private String info;

    public TroubleBean(String name, String info) {
        this.name = name;
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    @Override
    public int getItemType() {
        return  ExpandableItemAdapter.TYPE_TRUOBLE;
    }
}
