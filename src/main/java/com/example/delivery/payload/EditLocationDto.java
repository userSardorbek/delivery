package com.example.delivery.payload;

import lombok.Data;

@Data
public class EditLocationDto {

    private String region;
    private String placeName;
    private String editedRegion;
    private String editedPlaceName;
}
