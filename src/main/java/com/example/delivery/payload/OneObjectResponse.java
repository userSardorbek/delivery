package com.example.delivery.payload;

import lombok.Data;

@Data
public class OneObjectResponse {

    public OneObjectResponse(Object object) {
        this.object = object;
    }

    private Object object;
}
