package com.my51c.see51.app.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.my51c.see51.adapter.SmartGatewayExpandableAdapter;

public class SmartGatewayLevel0Item extends AbstractExpandableItem<MultiItemEntity> implements MultiItemEntity {
    public String name;

    public SmartGatewayLevel0Item(String name) {
        this.name = name;
    }

    @Override
    public int getLevel() {
        return 0;
    }

    @Override
    public int getItemType() {
        return SmartGatewayExpandableAdapter.TYPE_LEVEL_0;
    }
}
