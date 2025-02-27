package org.ecoflow.common.models.markers;

public class Tags {

    private String amenity;
    private String name;
    private String recycling_type;

    Tags () { }

    public Tags(String amenity, String name, String recycling_type) {
        this.amenity = amenity;
        this.name = name;
        this.recycling_type = recycling_type;
    }

    public String getAmenity() {
        return amenity;
    }

    public void setAmenity(String amenity) {
        this.amenity = amenity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecycling_type() {
        return recycling_type;
    }

    public void setRecycling_type(String recycling_type) {
        this.recycling_type = recycling_type;
    }
}
