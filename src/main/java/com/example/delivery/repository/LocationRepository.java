package com.example.delivery.repository;

import com.example.delivery.entity.Carrier;
import com.example.delivery.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {


    Optional<Location> findByRegionAndPlaceName(String region, String placeName);

    boolean existsByPlaceNameAndRegion(String placeName, String region);

    List<Location> findByRegion(String region);

    Optional<Location> findByPlaceName(String placeName);


}
