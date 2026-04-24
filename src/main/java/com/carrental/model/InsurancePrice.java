package com.carrental.model;

import java.math.BigDecimal;

public class InsurancePrice {
    private int priceId;
    private int insuranceTypeId;
    private BigDecimal dailyPrice;

    public InsurancePrice() {}
    public InsurancePrice(int priceId, int insuranceTypeId, BigDecimal dailyPrice) {
        this.priceId = priceId;
        this.insuranceTypeId = insuranceTypeId;
        this.dailyPrice = dailyPrice;
    }

    public int getPriceId()                 { return priceId; }
    public void setPriceId(int v)           { this.priceId = v; }
    public int getInsuranceTypeId()         { return insuranceTypeId; }
    public void setInsuranceTypeId(int v)   { this.insuranceTypeId = v; }
    public BigDecimal getDailyPrice()       { return dailyPrice; }
    public void setDailyPrice(BigDecimal v) { this.dailyPrice = v; }

    @Override
    public String toString() {
        return String.format("InsurancePrice[typeId=%d, dailyPrice=$%.2f]", insuranceTypeId, dailyPrice);
    }
}
