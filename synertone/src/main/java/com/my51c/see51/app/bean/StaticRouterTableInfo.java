package com.my51c.see51.app.bean;

import com.my51c.see51.app.annotation.FieldTitle;

public class StaticRouterTableInfo extends Object {
    @FieldTitle(value = "网络目标", index = 0)
    public String networkDestination;
    @FieldTitle(value ="网关",index = 1)
    public String gateWay;
    @FieldTitle(value ="掩码", index = 2)
    public String maskCode;
    @FieldTitle(value ="路由标识", index = 3)
    public String routerSign;
    @FieldTitle(value ="越点数/引用数/查找数", index = 4)
    public String allNumber;//格式：越点数/引用数/查找数
    @FieldTitle(value ="接口", index = 5)
    public String interfaceName;

    public StaticRouterTableInfo() {
    }

    public StaticRouterTableInfo(String networkDestination, String gateWay, String maskCode, String routerSign, String allNumber, String interfaceName) {
        this.networkDestination = networkDestination;
        this.gateWay = gateWay;
        this.maskCode = maskCode;
        this.routerSign = routerSign;
        this.allNumber = allNumber;
        this.interfaceName = interfaceName;
    }
}
