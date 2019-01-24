package com.my51c.see51.app.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.my51c.see51.adapter.SmartGatewayExpandableAdapter;

import java.util.List;

public class HardDiskStatusInfo implements MultiItemEntity{
    public String deviceCorestatusWork;
    public String deviceCorestatusDisknum;
    public List<SmartGatewayBean.DiskpartBean> diskpartBeanList;

    public HardDiskStatusInfo(String deviceCorestatusWork, String deviceCorestatusDisknum, List<SmartGatewayBean.DiskpartBean> diskpartBeanList) {
        this.deviceCorestatusWork = deviceCorestatusWork;
        this.deviceCorestatusDisknum = deviceCorestatusDisknum;
        this.diskpartBeanList=diskpartBeanList;

    }

    @Override
    public int getItemType() {
        return SmartGatewayExpandableAdapter.HARD_DISK_INFO;
    }
}
