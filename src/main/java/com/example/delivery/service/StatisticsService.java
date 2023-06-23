package com.example.delivery.service;

import com.example.delivery.entity.Transaction;
import com.example.delivery.payload.ResponseMessage;
import com.example.delivery.repository.CarrierRepository;
import com.example.delivery.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsService {

    @Autowired
    CarrierRepository carrierRepository;
    @Autowired
    TransactionRepository transactionRepository;

    public ResponseMessage scorePerCarrier(Integer min) {
        return new ResponseMessage("", true);
    }
}
