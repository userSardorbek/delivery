package com.example.delivery.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Carrier implements Comparable<Carrier> {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer CarrierId;

    private String name;

    @ManyToMany
    private List<Location> locations;


    @Override
    public int compareTo(Carrier o) {
        return this.name.compareTo(o.getName());
    }
}
