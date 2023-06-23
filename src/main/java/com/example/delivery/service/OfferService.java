package com.example.delivery.service;

import com.example.delivery.entity.Location;
import com.example.delivery.entity.Offer;
import com.example.delivery.entity.Product;
import com.example.delivery.payload.OfferDto;
import com.example.delivery.payload.ResponseMessage;
import com.example.delivery.repository.LocationRepository;
import com.example.delivery.repository.OfferRepository;
import com.example.delivery.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OfferService {

    @Autowired
    OfferRepository offerRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    ProductRepository productRepository;


    public ResponseMessage addOffer(String offerId, List<String> productsId, String placeName) {
        boolean exists = offerRepository.existsByOfferId(offerId);
        if (exists)
            return new ResponseMessage("Bunday offer id mavjud", false);

        Optional<Location> optionalLocation = locationRepository.findByPlaceName(placeName);
        if (optionalLocation.isEmpty())
            return new ResponseMessage("Bunday hudud topilmadi", false);

        List<Product> products = new ArrayList<>();
        for (String id :productsId) {
            Optional<Product> optionalProduct = productRepository.findById(id);
            if (optionalProduct.isPresent())
                products.add(optionalProduct.get());
        }

        if (products.isEmpty())
            return new ResponseMessage("Birorta mahsulot topilmadi", false);
        Location location = optionalLocation.get();
        Offer offer = new Offer();
        offer.setOfferId(offerId);
        offer.setLocation(location);
        offer.setProducts(products);

        Offer save = offerRepository.save(offer);
        return new ResponseMessage("offer saved", true, save);
    }

    public ResponseMessage getOffer(String id) {
        Optional<Offer> optionalOffer = offerRepository.findById(id);
        if (optionalOffer.isEmpty())
            return new ResponseMessage("Bunday taklif topilmadi", false);
        return new ResponseMessage("Taklif", true, optionalOffer.get());
    }

    public ResponseMessage editOffer(String id, OfferDto offerDto) {
        Optional<Offer> optionalOffer = offerRepository.findById(id);
        if (optionalOffer.isEmpty())
            return new ResponseMessage("Bunday taklif topilmadi", false);

        Optional<Location> optionalLocation = locationRepository.findByPlaceName(offerDto.getPlaceName());
        if (optionalLocation.isEmpty())
            return new ResponseMessage("Bunday hudud topilmadi", false);

        List<Product> products = new ArrayList<>();
        for (String s : offerDto.getProductsId()) {
            Optional<Product> optionalProduct = productRepository.findById(s);
            if (optionalProduct.isPresent())
                products.add(optionalProduct.get());
        }

        if (products.isEmpty())
            return new ResponseMessage("Birorta mahsulot topilmadi", false);

        Location location = optionalLocation.get();
        Offer offer = optionalOffer.get();
        offer.setLocation(location);
        offer.setProducts(products);

        Offer save = offerRepository.save(offer);
        return new ResponseMessage("Taklif tahrirlandi", true, save);
    }

    public ResponseMessage delete(String id) {
        offerRepository.deleteById(id);
        return new ResponseMessage("Deleted", true);
    }
}
