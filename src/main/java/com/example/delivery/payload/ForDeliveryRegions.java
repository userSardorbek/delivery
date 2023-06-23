package com.example.delivery.payload;

import com.example.delivery.entity.Location;
import lombok.Data;

@Data
public class ForDeliveryRegions implements Comparable<ForDeliveryRegions> {

    private Integer transactionNumber;
    private Location location;

    @Override
    public int compareTo(ForDeliveryRegions o) {
        return this.transactionNumber.compareTo(o.getTransactionNumber());
    }
}
