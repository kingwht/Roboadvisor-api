package com.hsbc.roboadvisor.model.Fund;

import java.io.Serializable;

public class AssetComposition implements Serializable
{
    private String gics;

    private Integer percentage;

    public AssetComposition() {
        // empty constructor
    }

    public AssetComposition(String gics, Integer percentage) {
        this.gics = gics;
        this.percentage = percentage;
    }

    public String getGics() {
        return gics;
    }

    public void setGics(String gics) {
        this.gics = gics;
    }

    public Integer getPercentage() {
        return percentage;
    }

    public void setPercentage(Integer percentage) {
        this.percentage = percentage;
    }
}
