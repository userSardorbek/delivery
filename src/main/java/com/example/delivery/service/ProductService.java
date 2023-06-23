package com.example.delivery.service;

import com.example.delivery.entity.Product;
import com.example.delivery.payload.ResponseMessage;
import com.example.delivery.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public ResponseMessage addProduct(Product product) {
        if (productRepository.existsById(product.getId()))
            return new ResponseMessage(product.getId() + " Bunday id mavjud emas", false);
        Product save = productRepository.save(product);
        return new ResponseMessage("Mahsulot saqlandi", true, save);
    }

    public ResponseMessage delete(String id) {
        productRepository.deleteById(id);
        return new ResponseMessage("Deleted", true);
    }

    public ResponseMessage editProduct(String id, Product editingProduct) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty())
            return new ResponseMessage(id + " Bunday mahsulot yo'q", false);
        Product product = optionalProduct.get();
//        product.setId(editingProduct.getId());
        product.setName(editingProduct.getName());
        Product save = productRepository.save(product);
        return new ResponseMessage("Tahrirlandi", true, save);
    }

    public ResponseMessage getAllProducts() {
        List<Product> all = productRepository.findAll();
        return new ResponseMessage("Barcha mahsulotlar ro'yxati", true, all);
    }
}
