package com.my51c.see51.app.bean;

public class PointingStatusBean {

    /**
     * lock : 1
     * max : -3.0
     * cur : 2.5
     * err : 1
     */

    private String lock;
    private String max;
    private String cur;
    private String err;

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getCur() {
        return cur;
    }

    public void setCur(String cur) {
        this.cur = cur;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }
}
