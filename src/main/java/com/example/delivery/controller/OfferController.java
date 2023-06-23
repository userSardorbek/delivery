package com.example.delivery.controller;

import com.example.delivery.payload.OfferDto;
import com.example.delivery.payload.ResponseMessage;
import com.example.delivery.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offer")
public class OfferController {

    @Autowired
    OfferService offerService;

    @PostMapping("/add")
    public ResponseEntity<ResponseMessage> addOffer(@RequestParam String offerId, @RequestParam List<String> productsId, @RequestParam String placeName) {
        ResponseMessage message = offerService.addOffer(offerId, productsId, placeName);
        return ResponseEntity.status(message.isSuccess() ? 201 : 409).body(message);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseMessage> getOffer(@PathVariable String id) {
        ResponseMessage message = offerService.getOffer(id);
        return ResponseEntity.status(message.isSuccess() ? 200 : 409).body(message);
    }

    @PutMapping("/editOffer/{id}")
    public ResponseEntity<?> editOffer(@PathVariable String id, @RequestBody OfferDto offerDto) {
        ResponseMessage message = offerService.editOffer(id, offerDto);
        return ResponseEntity.status(message.isSuccess() ? 202 : 406).body(message);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteOffer(@PathVariable String id){
        ResponseMessage message = offerService.delete(id);
        return ResponseEntity.status(message.isSuccess()?200:409).body(message);
    }
}
