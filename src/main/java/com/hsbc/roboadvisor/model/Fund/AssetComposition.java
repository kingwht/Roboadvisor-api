package com.hsbc.roboadvisor.model.Fund;

public class AssetComposition
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
}
