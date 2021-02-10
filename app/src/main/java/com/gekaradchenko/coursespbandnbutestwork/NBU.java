package com.gekaradchenko.coursespbandnbutestwork;

public class NBU {
    private String name;
    private String rate;
    private String val;
    private boolean select;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public NBU(String name, String rate, String val, boolean select) {
        this.name = name;
        this.rate = rate;
        this.val = val;
        this.select = select;
    }
}
