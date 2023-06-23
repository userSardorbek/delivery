package com.example.delivery.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Carrier carrier;

    @OneToOne
    private Offer offer;

    @OneToOne
    private Request request;

    private Integer score = 0;

    public Transaction(Carrier carrier, Offer offer, Request request) {
        this.carrier = carrier;
        this.offer = offer;
        this.request = request;
    }
}
