package com.example.delivery.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Place {

    @Id
    private String id;

    private String name;
}
