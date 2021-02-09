package com.gekaradchenko.coursespbandnbutestwork;

import java.math.BigDecimal;

public class PrivateBank {
    private String val;
    private String buy;
    private String sale;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getBuy() {
        return buy;
    }

    public void setBuy(String buy) {
        this.buy = buy;
    }

    public String getSale() {
        return sale;
    }

    public void setSale(String sale) {
        this.sale = sale;
    }

    public PrivateBank(String val, String buy, String sale) {
        this.val = val;
        this.buy = buy;
        this.sale = sale;
    }
}
