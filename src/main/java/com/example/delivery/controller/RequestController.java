package com.example.delivery.controller;


import com.example.delivery.payload.RequestDto;
import com.example.delivery.payload.ResponseMessage;
import com.example.delivery.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/request")
public class RequestController {

    @Autowired
    RequestService requestService;

    @PostMapping("/add")
    public ResponseEntity<ResponseMessage> addRequest(@RequestParam String requestId, @RequestParam List<String> productsId, @RequestParam String placeName) {
        ResponseMessage responseMessage = requestService.addRequest(requestId, productsId, placeName);
        return ResponseEntity.status(responseMessage.isSuccess() ? 201 : 409).body(responseMessage);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ResponseMessage> editRequest(@PathVariable String id, @RequestBody RequestDto requestDto) {
        ResponseMessage responseMessage = requestService.editRequest(id, requestDto);
        return ResponseEntity.status(responseMessage.isSuccess() ? 202 : 406).body(responseMessage);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseMessage> getRequest(@PathVariable String id) {
        ResponseMessage responseMessage = requestService.getRequest(id);
        return ResponseEntity.status(responseMessage.isSuccess() ? 200 : 409).body(responseMessage);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteRequest(@PathVariable String id) {
        ResponseMessage message = requestService.delete(id);
        return ResponseEntity.status(message.isSuccess() ? 200 : 409).body(message);
    }

}
