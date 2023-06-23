package com.example.delivery.service;

import com.example.delivery.entity.Location;
import com.example.delivery.payload.EditLocationDto;
import com.example.delivery.payload.LocationDto;
import com.example.delivery.payload.OneObjectResponse;
import com.example.delivery.payload.ResponseMessage;
import com.example.delivery.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    @Autowired
    LocationRepository locationRepository;

    public ResponseMessage addLocation(String regionName, String placeName) {
        Optional<Location> optionalLocation = locationRepository.findByRegionAndPlaceName(regionName, placeName);
        if (optionalLocation.isPresent())
            return new ResponseMessage("Bunday region va place mavjud", false);

        Location location = new Location();
        location.setRegion(regionName);
        location.setPlaceName(placeName);

        locationRepository.save(location);
        List<Location> all = locationRepository.findAll();
        all.sort(Comparator.comparing(Location::getRegion));
        return new ResponseMessage("",true,all);
    }

    public ResponseMessage editLocation(EditLocationDto editLocationDto) {
        Optional<Location> optionalLocation = locationRepository.findByRegionAndPlaceName(editLocationDto.getRegion(), editLocationDto.getPlaceName());

        boolean exists = locationRepository.existsByPlaceNameAndRegion(editLocationDto.getEditedPlaceName(), editLocationDto.getEditedRegion());

        if (exists)
            return new ResponseMessage("Siz kiritgan yangi hudud allaqachon mavjud", false);

        if (optionalLocation.isEmpty())
            return new ResponseMessage("Bunday hudud topilmadi", false);
        Location location = optionalLocation.get();

        location.setRegion(editLocationDto.getEditedRegion());
        location.setPlaceName(editLocationDto.getEditedPlaceName());

        Location save = locationRepository.save(location);
        return new ResponseMessage("Muvofaqiyatli tahrirlandi", true, save);
    }

    public List<Location> getLocations() {
        List<Location> all = locationRepository.findAll();
        all.sort(Comparator.comparing(Location::getPlaceName));
        return all;
    }

    public ResponseMessage deleteLocation(LocationDto locationDto) {

        Optional<Location> optionalLocation = locationRepository.findByRegionAndPlaceName(locationDto.getRegion(), locationDto.getPlaceName());
        if (optionalLocation.isEmpty())
            return null;
        locationRepository.delete(optionalLocation.get());

        return new ResponseMessage("Hudud muvofaqiyatli o'chirildi", true);
    }
}
