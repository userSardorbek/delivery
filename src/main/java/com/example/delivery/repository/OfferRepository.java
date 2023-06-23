package com.example.delivery.repository;

import com.example.delivery.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, String> {
    boolean existsByOfferId(String offerId);

}
