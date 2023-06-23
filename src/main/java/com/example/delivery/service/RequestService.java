package com.example.delivery.service;

import com.example.delivery.entity.Location;
import com.example.delivery.entity.Product;
import com.example.delivery.entity.Request;
import com.example.delivery.payload.RequestDto;
import com.example.delivery.payload.ResponseMessage;
import com.example.delivery.repository.LocationRepository;
import com.example.delivery.repository.ProductRepository;
import com.example.delivery.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RequestService {

    @Autowired
    RequestRepository requestRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    ProductRepository productRepository;

    public ResponseMessage addRequest(String requestId, List<String> productsId, String placeName) {

        boolean exists = requestRepository.existsByRequestId(requestId);
        if (exists)
            return new ResponseMessage(requestId + " Bunday so'rov kodi mavjud", false);

        Optional<Location> optionalLocation = locationRepository.findByPlaceName(placeName);
        if (optionalLocation.isEmpty())
            return new ResponseMessage("Bunday hudud topilmadi", false);

        List<Product> products = new ArrayList<>();
        for (String id : productsId) {
            Optional<Product> optionalProduct = productRepository.findById(id);
            optionalProduct.ifPresent(products::add);
        }
        if (products.size() == 0)
            return new ResponseMessage("Bunday mahsulotlar topilmadi", false);


        Location location = optionalLocation.get();

        Request request = new Request();
        request.setRequestId(requestId);
        request.setLocation(location);
        request.setProducts(products);
        Request save = requestRepository.save(request);

        return new ResponseMessage("So'rov qabul qilindi", true, save);
    }

    public ResponseMessage editRequest(String id, RequestDto dto) {
        Optional<Request> optionalRequest = requestRepository.findById(id);
        if (optionalRequest.isEmpty())
            return new ResponseMessage(id + " Bunday so'rov topilmadi", false);

        Optional<Location> optionalLocation = locationRepository.findByPlaceName(dto.getPlaceName());
        if (optionalLocation.isEmpty())
            return new ResponseMessage("Bunday hudud topilmadi", false);

        List<Product> products = new ArrayList<>();
        for (String s : dto.getProductsId()) {
            Optional<Product> optionalProduct = productRepository.findById(s);
            if (optionalProduct.isPresent())
                products.add(optionalProduct.get());
        }
        if (products.size() == 0)
            return new ResponseMessage("Bunday mahsulotlar topilmadi", false);
        Location location = optionalLocation.get();

        Request request = new Request();
        request.setRequestId(dto.getRequestId());
        request.setLocation(location);
        request.setProducts(products);
        Request save = requestRepository.save(request);
        return new ResponseMessage("Tahrirlandi", true, save);
    }


    public ResponseMessage getRequest(String id) {
        Optional<Request> byId = requestRepository.findById(id);
        if (byId.isEmpty())
            return new ResponseMessage(id + " Bunday kodli so'rov topilmadi", false);
        Request request = byId.get();
        return new ResponseMessage("Sizning buyurtmangiz", true, request);
    }

    public ResponseMessage delete(String id) {
        requestRepository.deleteById(id);
        return new ResponseMessage(id + " So'rov o'chirildi", true);
    }
}
