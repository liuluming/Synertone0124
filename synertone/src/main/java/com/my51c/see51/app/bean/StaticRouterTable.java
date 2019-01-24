package com.my51c.see51.app.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.my51c.see51.adapter.SmartGatewayExpandableAdapter;

public class StaticRouterTable implements MultiItemEntity{
    public String content;

    public StaticRouterTable(String content) {
        this.content = content;
    }

    @Override
    public int getItemType() {
        return SmartGatewayExpandableAdapter.STATIC_ROUTER_INFO;
    }
}
