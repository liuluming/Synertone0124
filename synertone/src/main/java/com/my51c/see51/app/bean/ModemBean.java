package com.my51c.see51.app.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

public class ModemBean extends DataSupport implements Serializable {
   /* pro_mgid=2&pro_terminal=9&pro_ob_freq=12236000&pro_ob_symbolrate=60000&pro_adv_lnblo=10600000&
    pro_adv_clustergroup=1&pro_buclo=13050000&pro_10Mhz=1&pro_audio=0&pro_opmode=1*/
    private String mgid;
    private String terminal;
    private String beam;
    private String freq;
    private String symbolrate;
    private String lnblo;
    private String buclo;
    private String cluster;
    private String clustergroup;

    public String getMgid() {
        return mgid;
    }

    public void setMgid(String mgid) {
        this.mgid = mgid;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getBeam() {
        return beam;
    }

    public void setBeam(String beam) {
        this.beam = beam;
    }

    public String getFreq() {
        return freq;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }

    public String getSymbolrate() {
        return symbolrate;
    }

    public void setSymbolrate(String symbolrate) {
        this.symbolrate = symbolrate;
    }

    public String getLnblo() {
        return lnblo;
    }

    public void setLnblo(String lnblo) {
        this.lnblo = lnblo;
    }

    public String getBuclo() {
        return buclo;
    }

    public void setBuclo(String buclo) {
        this.buclo = buclo;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getClustergroup() {
        return clustergroup;
    }

    public void setClustergroup(String clustergroup) {
        this.clustergroup = clustergroup;
    }
}
