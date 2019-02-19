package com.hsbc.roboadvisor.model;

public class Holding
{
    private Integer fundId;

    private Integer units;

    private Volume balance;

    private Volume avgUnitPrice;

    public Holding() {
        //empty constructor
    }

    public Holding(Integer fundId, Integer units, Volume balance, Volume avgUnitPrice) {
        this.fundId = fundId;
        this.units = units;
        this.balance = balance;
        this.avgUnitPrice = avgUnitPrice;
    }

    public Integer getFundId() {
        return fundId;
    }

    public void setFundId(Integer fundId) {
        this.fundId = fundId;
    }

    public Integer getUnits() {
        return units;
    }

    public void setUnits(Integer units) {
        this.units = units;
    }

    public Volume getBalance() {
        return balance;
    }

    public void setBalance(Volume balance) {
        this.balance = balance;
    }

    public Volume getAvgUnitPrice() {
        return avgUnitPrice;
    }

    public void setAvgUnitPrice(Volume avgUnitPrice) {
        this.avgUnitPrice = avgUnitPrice;
    }
}
