package com.my51c.see51.app.bean;
public class AntennaStatusBean {
    private String code; //0 成功   -1 失败   1
    private String msg;
    private String azi;  //方位角 后面加角度符号 °
    private String elevcarr; //俯仰角(载体)  同上
    private String roll;//横滚角     同上
    private String curlon;//当前经度   同上
    private String currlat;//当前纬度    同上
    private String elev; //天线仰角   同上
    private String temp; //天线温度   加℃
    private String rssi;//RSSI数值
    private String ctrlver;//控制器版本
    private String ctrlnum; //控制器编号
    private String oduver; //天线版本
    private String odunum;//天线编号
    private String locatype;//定位方式  0 自动  1 GPS   2 北斗
    private String hot; //过温：       0 正常 -1 过温
    private String vol;  //电压故障      0 正常  -1 故障
    private String azimotor;// 方位电机故障  0 正常  -1 故障
    private String elevmotor;//俯仰电机故障   0 正常  -1 故障
    private String sendzero; //发射归零故障  0 正常  -1 故障
    private String elevzero; //俯仰归零故障  0 正常  -1 故障
    private String odutype;// 0 未知天线类型 1  V系列天线  2 S系列天线 3 C系列天线
    private String gyroscope; //陀螺仪故障    0 正常  -1 故障  V、S系列天线
    private String compass;//电子罗盘故障    0 正常  -1 故障  C系列天线
    private String rf; //射频信号故障  0 正常  -1 故障
    private String position; //位置设置错误   0 正常  -1 位置错误
    private String current;//天线过流 0 正常  -1过流
    private String zaizero;//方位归零故障 0 正常  -1故障
    private String rollzero;//横滚归零故障 0 正常 -1 故障 S6A、S9
    private String elevout;//俯仰角超出范围 0 正常 -1超出方位
    private String gpsStatus;//gps故障 0 正常 -1故障
    private String lnboscillator;//LNB 本振
    private String bucoscillator;//BUC 本振
    private String bucSwitch;//buc 开关0 buc关闭(自动模式)，1 buc开启(自动模式),2 buc关闭(手//动模式),3 buc开启(手动模式)，4 buc异常
    private String bucGain;//buc增益范围0~300

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAzi() {
        return azi;
    }

    public void setAzi(String azi) {
        this.azi = azi;
    }

    public String getElevcarr() {
        return elevcarr;
    }

    public void setElevcarr(String elevcarr) {
        this.elevcarr = elevcarr;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getCurlon() {
        return curlon;
    }

    public void setCurlon(String curlon) {
        this.curlon = curlon;
    }

    public String getCurrlat() {
        return currlat;
    }

    public void setCurrlat(String currlat) {
        this.currlat = currlat;
    }

    public String getElev() {
        return elev;
    }

    public void setElev(String elev) {
        this.elev = elev;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }

    public String getCtrlver() {
        return ctrlver;
    }

    public void setCtrlver(String ctrlver) {
        this.ctrlver = ctrlver;
    }

    public String getCtrlnum() {
        return ctrlnum;
    }

    public void setCtrlnum(String ctrlnum) {
        this.ctrlnum = ctrlnum;
    }

    public String getOduver() {
        return oduver;
    }

    public void setOduver(String oduver) {
        this.oduver = oduver;
    }

    public String getOdunum() {
        return odunum;
    }

    public void setOdunum(String odunum) {
        this.odunum = odunum;
    }

    public String getLocatype() {
        return locatype;
    }

    public void setLocatype(String locatype) {
        this.locatype = locatype;
    }

    public String getHot() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot = hot;
    }

    public String getVol() {
        return vol;
    }

    public void setVol(String vol) {
        this.vol = vol;
    }

    public String getAzimotor() {
        return azimotor;
    }

    public void setAzimotor(String azimotor) {
        this.azimotor = azimotor;
    }

    public String getElevmotor() {
        return elevmotor;
    }

    public void setElevmotor(String elevmotor) {
        this.elevmotor = elevmotor;
    }

    public String getSendzero() {
        return sendzero;
    }

    public void setSendzero(String sendzero) {
        this.sendzero = sendzero;
    }

    public String getElevzero() {
        return elevzero;
    }

    public void setElevzero(String elevzero) {
        this.elevzero = elevzero;
    }

    public String getOdutype() {
        return odutype;
    }

    public void setOdutype(String odutype) {
        this.odutype = odutype;
    }

    public String getGyroscope() {
        return gyroscope;
    }

    public void setGyroscope(String gyroscope) {
        this.gyroscope = gyroscope;
    }

    public String getCompass() {
        return compass;
    }

    public void setCompass(String compass) {
        this.compass = compass;
    }

    public String getRf() {
        return rf;
    }

    public void setRf(String rf) {
        this.rf = rf;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getZaizero() {
        return zaizero;
    }

    public void setZaizero(String zaizero) {
        this.zaizero = zaizero;
    }

    public String getRollzero() {
        return rollzero;
    }

    public void setRollzero(String rollzero) {
        this.rollzero = rollzero;
    }

    public String getElevout() {
        return elevout;
    }

    public void setElevout(String elevout) {
        this.elevout = elevout;
    }

    public String getGpsStatus() {
        return gpsStatus;
    }

    public void setGpsStatus(String gpsStatus) {
        this.gpsStatus = gpsStatus;
    }

    public String getLnboscillator() {
        return lnboscillator;
    }

    public void setLnboscillator(String lnboscillator) {
        this.lnboscillator = lnboscillator;
    }

    public String getBucoscillator() {
        return bucoscillator;
    }

    public void setBucoscillator(String bucoscillator) {
        this.bucoscillator = bucoscillator;
    }

    public String getBucSwitch() {
        return bucSwitch;
    }

    public void setBucSwitch(String bucSwitch) {
        this.bucSwitch = bucSwitch;
    }

    public String getBucGain() {
        return bucGain;
    }

    public void setBucGain(String bucGain) {
        this.bucGain = bucGain;
    }
}
