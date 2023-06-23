package com.example.delivery.payload;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class CarrierDto {

    private String name;

    private List<String> regionNames;
}
