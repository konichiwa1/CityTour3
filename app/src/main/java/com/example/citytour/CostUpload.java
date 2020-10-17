package com.example.citytour;

public class CostUpload {

    private String description;
    private Double cost;
    private String key;

    public CostUpload(String description, Double cost, String key) {
        this.description = description;
        this.cost = cost;
        this.key = key;
    }

    public CostUpload() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
