package org.ecoflow.common.models.markers;

public class Marker {

    private long id;
    private double lat;
    private double lon;
    private String name;
    private Tags tags;
    private String address;

    public Marker() { }

    public Marker(int id,String address, Tags tags, String name, double lon, double lat) {
        this.id = id;
        this.address = address;
        this.tags = tags;
        this.name = name;
        this.lon = lon;
        this.lat = lat;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tags getTags() {
        return tags;
    }

    public void setTags(Tags tags) {
        this.tags = tags;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
