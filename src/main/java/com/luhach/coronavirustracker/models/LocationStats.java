package com.luhach.coronavirustracker.models;

public class LocationStats {
    private String state;
    private String country;
    private int latestTotalCases, delta;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getLatestTotalCases() {
        return latestTotalCases;
    }

    public void setLatestTotalCases(int latestTotalCases) {
        this.latestTotalCases = latestTotalCases;
    }

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    @Override
    public String toString() {
        return "LocationStats [country=" + country + ", delta=" + delta + ", latestTotalCases=" + latestTotalCases
                + ", state=" + state + "]";
    }

    
}
