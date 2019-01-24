package com.my51c.see51.app.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.my51c.see51.adapter.SmartGatewayExpandableAdapter;

public class FirewallInfo implements MultiItemEntity {
    public String content;

    public FirewallInfo(String content) {
        this.content = content;
    }

    @Override
    public int getItemType() {
        return SmartGatewayExpandableAdapter.FIRE_WALL_INFO;
    }
}
