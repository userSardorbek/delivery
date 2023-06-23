package com.example.delivery.payload;

import lombok.Data;

import java.util.List;

@Data
public class OfferDto {

    private String offerId;
    private List<String> productsId;
    private String placeName;
}
