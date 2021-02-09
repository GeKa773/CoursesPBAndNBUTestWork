package com.gekaradchenko.coursespbandnbutestwork;

public class NBU {
    private String name;
    private String rate;
    private String val;

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

    public NBU(String name, String rate, String val) {
        this.name = name;
        this.rate = rate;
        this.val = val;
    }
}
