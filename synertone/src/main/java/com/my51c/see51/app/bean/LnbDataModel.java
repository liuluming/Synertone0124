package com.my51c.see51.app.bean;
import org.litepal.crud.DataSupport;
public class LnbDataModel extends DataSupport {
    private String lnb;
    private boolean isAdd = false;
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLnb() {
        return lnb;
    }

    public void setLnb(String lnb) {
        this.lnb = lnb;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LnbDataModel that = (LnbDataModel) o;
        return (lnb == that.lnb) || (lnb != null && lnb.equals(that.lnb));
    }

    @Override
    public int hashCode() {
        return lnb.hashCode();
    }
}
