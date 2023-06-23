package com.example.delivery.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode
public class Location implements Comparable<Location>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer locationId;

    private String region;

    private String placeName;


    @Override
    public int compareTo(Location o) {
        return this.getPlaceName().compareTo(o.getPlaceName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (!locationId.equals(location.locationId)) return false;
        if (!region.equals(location.region)) return false;
        return placeName.equals(location.placeName);
    }

    @Override
    public int hashCode() {
        int result = locationId.hashCode();
        result = 31 * result + region.hashCode();
        result = 31 * result + placeName.hashCode();
        return result;
    }
}
