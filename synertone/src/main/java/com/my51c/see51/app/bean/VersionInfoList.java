package com.my51c.see51.app.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.my51c.see51.adapter.SmartGatewayExpandableAdapter;

import java.util.List;

public class VersionInfoList implements MultiItemEntity {
    public List<VersionInfo> infoList;

    public VersionInfoList(List<VersionInfo> infoList) {
        this.infoList = infoList;
    }

    @Override
    public int getItemType() {
        return SmartGatewayExpandableAdapter.VERSION_INFO;
    }
}
