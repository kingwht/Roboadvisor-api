package com.hsbc.roboadvisor.model.Fund;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.hsbc.roboadvisor.model.Common.CurrencyAmount;

public class Fund implements Serializable
{
    private List<AssetComposition> assetComposition;

    private Integer fundId;

    private Map<String, Double> averageReturns;

    private Integer category;

    private CurrencyAmount price;

    private String fundName;

    public Fund() {
        //empty constructor
    }

    public Fund(List<AssetComposition> assetComposition, Integer fundId, Map<String,
            Double> averageReturns, Integer category, CurrencyAmount price, String fundName) {
        this.assetComposition = assetComposition;
        this.fundId = fundId;
        this.averageReturns = averageReturns;
        this.category = category;
        this.price = price;
        this.fundName = fundName;
    }

    public List<AssetComposition> getAssetComposition() {
        return assetComposition;
    }

    public void setAssetComposition(List<AssetComposition> assetComposition) {
        this.assetComposition = assetComposition;
    }

    public Integer getFundId() {
        return fundId;
    }

    public void setFundId(Integer fundId) {
        this.fundId = fundId;
    }

    public Map<String, Double> getAverageReturns() {
        return averageReturns;
    }

    public void setAverageReturns(Map<String, Double> averageReturns) {
        this.averageReturns = averageReturns;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public CurrencyAmount getPrice() {
        return price;
    }

    public void setPrice(CurrencyAmount price) {
        this.price = price;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }
}
