package com.hashimte.hashbus1.ui.search;

public class SearchData {
    private String startLocation;
    private String endLocation;
    private String waitMinTime;
    private Integer waitTime;

    public SearchData(String startLocation, String endLocation,  Integer waitTime,String waitMinTime) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.waitMinTime = waitMinTime;
        this.waitTime = waitTime;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getWaitMinTime() {
        return waitMinTime;
    }

    public void setWaitMinTime(String waitMinTime) {
        this.waitMinTime = waitMinTime;
    }

    public Integer getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(Integer waitTime) {
        this.waitTime = waitTime;
    }
}
