package com.example.delivery.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
@Data
public class Request {

    @Id
    private String requestId;

    @ManyToMany
    private List<Product> products;

    @ManyToOne
    private Location location;

}
