package com.example.delivery.payload;

import lombok.Data;

import java.util.List;

@Data
public class RequestDto {

    private String requestId;
    private List<String> productsId;
    private String placeName;
}
