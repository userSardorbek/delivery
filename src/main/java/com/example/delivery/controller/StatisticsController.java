package com.example.delivery.controller;

import com.example.delivery.payload.ResponseMessage;
import com.example.delivery.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    StatisticsService statisticsService;

    @GetMapping("score")
    public ResponseEntity<ResponseMessage> scorePerCarrier(){
        ResponseMessage message = statisticsService.scorePerCarrier(1);
        return ResponseEntity.ok(message);
    }
}
