package com.example.delivery.controller;

import com.example.delivery.entity.Carrier;
import com.example.delivery.entity.ForReturn;
import com.example.delivery.payload.ResponseMessage;
import com.example.delivery.payload.TransactionDto;
import com.example.delivery.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/add")
    public ResponseEntity<ResponseMessage> addTransaction(@RequestParam Integer carrierId, @RequestParam String requestId, @RequestParam String offerId) {
        ResponseMessage message = transactionService.addTransaction(carrierId, requestId, offerId);
        return ResponseEntity.status(message.isSuccess() ? 201 : 409).body(message);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ResponseMessage> edit(@PathVariable Integer id, @RequestBody TransactionDto transactionDto) {
        ResponseMessage message = transactionService.edit(id, transactionDto);
        return ResponseEntity.status(message.isSuccess() ? 202 : 409).body(message);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> get(@PathVariable Integer id) {
        ResponseMessage message = transactionService.get(id);
        return ResponseEntity.status(message.isSuccess() ? 200 : 409).body(message);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        ResponseMessage message = transactionService.delete(id);
        return ResponseEntity.status(message.isSuccess() ? 200 : 409).body(message);
    }


    /**
     * (evaluateTransaction) Tizimga tranzaksiyaning kodi (transactionId) va ball
     * (int score) jo’natish orqali transaksiyaga ball beriladi. Yangi transaksiya
     * Yaratilganda tranzaksiya 0 ga teng ballga ega bo'ladi. Agar ball 1 va 10
     * orasida bo'lmasa (ekstremallar kiradi) false qaytariladi, aks holda true
     * qaytariladi.
     */
    @PostMapping("/evaluate")
    public ResponseEntity<?> evaluate(@RequestParam Integer id, int score) {
        ResponseMessage message = transactionService.evaluate(id, score);
        return ResponseEntity.ok(message);
    }


    /**
     * Tizimga minimumScore (int minimumScore) qiymat jo’natish orqali tashuvchilar (carriers)
     * va bir xil tashuvchiga (carrier ga) tegishli tranzaksiyalarning umumiy bali qaytariladi.
     * bali berilgan minimal balldan (minimumScore) past boʻlgan tranzaksiya eʼtiborga
     * olinmaydi. Tashuvchilar (carriers) alifbo tartibida saralangan holda qaytariladi.
     */
    @GetMapping("/scores")
    public ResponseEntity<?> scorePerCarrier(@RequestParam Integer minScore) {
        Map<Carrier, Integer> map = transactionService.scorePerCarrier(minScore);
        return ResponseEntity.ok(map);
    }

    /**
     * har bir masulot uchun (productId) uchun tranzaktsiyalar sonini (faqat 0 dan katta bo'lsa)
     * qaytaradi, masulot identificatori bo’yicha saralangan holatda qaytishi kerak.
     */
    @GetMapping
    public ForReturn perProduct() {
        return transactionService.perProduct();
    }

    @GetMapping("/deliveryLoc")
    public ForReturn deliveryLocPer() {
        return transactionService.deliveryRegionsPerNT();
    }

}
