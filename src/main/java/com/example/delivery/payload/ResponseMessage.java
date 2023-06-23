package com.example.delivery.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessage {

    private String message;
    private boolean isSuccess;
    private Object object;

    public ResponseMessage(String message, boolean isSuccess) {
        this.message = message;
        this.isSuccess = isSuccess;
    }

    public ResponseMessage(Object object) {
        this.object = object;
    }

    public ResponseMessage(boolean isSuccess, Object object) {
        this.isSuccess = isSuccess;
        this.object = object;
    }
}
