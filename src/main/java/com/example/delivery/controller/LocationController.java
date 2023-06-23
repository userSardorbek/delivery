package com.example.delivery.controller;

import com.example.delivery.entity.Location;
import com.example.delivery.payload.EditLocationDto;
import com.example.delivery.payload.LocationDto;
import com.example.delivery.payload.OneObjectResponse;
import com.example.delivery.payload.ResponseMessage;
import com.example.delivery.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/location")
public class LocationController {

    @Autowired
    LocationService locationService;

    /**
     * (addRegion) Hudud nomini va shu hududdagi joy nomlarini tizimga
     * qo’shish. Tizimda mavjud bo'lgan joylar qo'shilmaydi. Api method
     * qo'shilgan joylar nomlarining tartiblangan ro'yxatini qaytaradi
     */
    @PostMapping("/add")
    public HttpEntity<?> addLocation(@RequestParam String regionName, @RequestParam String placeName) {
        ResponseMessage message = locationService.addLocation(regionName, placeName);
        if (message.isSuccess())
            return ResponseEntity.status(201).body(message.getObject());
        else
            return ResponseEntity.status(409).body(message.getMessage());
    }





    @PutMapping("/edit")
    public ResponseEntity<ResponseMessage> editLocation(@RequestBody EditLocationDto editLocationDto) {
        ResponseMessage responseMessage = locationService.editLocation(editLocationDto);
        return ResponseEntity.status(responseMessage.isSuccess() ? 200 : 409).body(responseMessage);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getLocations() {
        List<Location> locations = locationService.getLocations();
        return ResponseEntity.ok(locations);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseMessage> deleteLocation(@RequestBody LocationDto locationDto) {
        ResponseMessage responseMessage = locationService.deleteLocation(locationDto);
        return ResponseEntity.status(responseMessage.isSuccess() ? 200 : 409).body(responseMessage);
    }
}
