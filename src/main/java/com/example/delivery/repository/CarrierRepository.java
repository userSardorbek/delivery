package com.example.delivery.repository;

import com.example.delivery.entity.Carrier;
import com.example.delivery.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("ALL")
public interface CarrierRepository extends JpaRepository<Carrier, Integer> {

    @Query(value = "select c from Carrier c where c.locations=?1")
    List<Carrier> getCarrierByLocations(List<Location> locations);

    List<Carrier> findByLocationsEquals(Location location);
}
