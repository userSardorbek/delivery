package com.example.delivery.controller;

import com.example.delivery.entity.Product;
import com.example.delivery.payload.ResponseMessage;
import com.example.delivery.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<ResponseMessage> addProduct(@RequestBody Product product) {
        ResponseMessage message = productService.addProduct(product);
        return ResponseEntity.status(message.isSuccess() ? 201 : 409).body(message);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseMessage> deleteProduct(@PathVariable String id) {
        ResponseMessage message = productService.delete(id);
        return ResponseEntity.status(message.isSuccess() ? 202 : 406).body(message);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<ResponseMessage> editProduct(@PathVariable String id, @RequestBody Product product) {
        ResponseMessage responseMessage = productService.editProduct(id, product);
        return ResponseEntity.status(responseMessage.isSuccess() ? 202 : 409).body(responseMessage);
    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseMessage> getAllProducts(){
        ResponseMessage responseMessage = productService.getAllProducts();
        return ResponseEntity.status(responseMessage.isSuccess()?200:409).body(responseMessage);
    }
}
