package com.example.delivery.controller;


import com.example.delivery.payload.ResponseMessage;
import com.example.delivery.service.CarrierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/carrier")
public class CarrierController {

    @Autowired
    CarrierService carrierService;


    /**
     * (addCarrier) Carrier ni tizimga qo’shish, bunda carrier ning ismi
     * va u xizmat ko’rsatadigan hududlar nomlari (regionNames) tizimga
     * jo’natiladi. Tizimda mavjud bo’lmagan va duplicate hudud nomlari etiborga
     * olinmaydi. Api method carrier xizmat qiladigan hududlarning tartiblangan
     * ro’yxatini qaytaradi
     */
    @PostMapping("/add")
    public HttpEntity<?> addLocation(@RequestParam String name, @RequestParam List<String> regionNames) {
        ResponseMessage message = carrierService.addCarrier(name, regionNames);
        if (message.isSuccess())
            return ResponseEntity.status(201).body(message.getObject());
        else
            return ResponseEntity.status(409).body(message.getMessage());
    }

    /**
     * Tizimga hudud nomi (regionName) jo’natilganda shu hududga xizmat
     * qiladigan carrier lar ning tartiblangan (carrierning ismi bo’yicha)
     * ro’yxatini qaytaradi@return
     */
    @GetMapping
    public HttpEntity<?> getCarriersFromRegion(@RequestParam String region) {
        ResponseMessage message = carrierService.getCarriersFromRegion(region);
        if (message.isSuccess())
            return ResponseEntity.status(201).body(message.getObject());
        else
            return ResponseEntity.status(409).body(message.getMessage());
    }


    @GetMapping("/get")
    public ResponseEntity<ResponseMessage> getLocations(@RequestParam Integer carrierId) {
        ResponseMessage carriers = carrierService.getCarriers(carrierId);
        return ResponseEntity.status(carriers.isSuccess() ? 200 : 409).body(carriers);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteLocation(@PathVariable Integer id) {
        ResponseMessage responseMessage = carrierService.delete(id);
        return ResponseEntity.status(responseMessage.isSuccess() ? 200 : 409).body(responseMessage);
    }


}

