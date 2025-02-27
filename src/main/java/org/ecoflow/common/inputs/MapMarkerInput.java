package org.ecoflow.common.inputs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MapMarkerInput  implements Serializable {

    @JsonProperty("lat")
    private float lat;

    @JsonProperty("lon")
    private float lon;

    @JsonProperty("amenities")
    private List<String> amenities;

    @JsonProperty("recyclingFilters")
    private List<String> recyclingFilters;

    public MapMarkerInput() { }

    public MapMarkerInput(float lat, float lon, List<String> amenities, List<String> recyclingFilters) {
        this.lat = lat;
        this.lon = lon;
        this.amenities = amenities;
        this.recyclingFilters = recyclingFilters;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public List<String> getRecyclingFilters() {
        return recyclingFilters;
    }

    public void setRecyclingFilters(List<String> recyclingFilters) {
        this.recyclingFilters = recyclingFilters;
    }
}
