package com.my51c.see51.app.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.my51c.see51.adapter.SmartGatewayExpandableAdapter;

public class SystemLogInfo implements MultiItemEntity {
    public String content;

    public SystemLogInfo(String content) {
        this.content = content;
    }

    @Override
    public int getItemType() {
        return SmartGatewayExpandableAdapter.SYSTEM_LOG_INFO;
    }
}
