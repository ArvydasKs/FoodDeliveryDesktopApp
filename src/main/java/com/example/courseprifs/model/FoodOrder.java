package com.example.courseprifs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class FoodOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private Double price;
    @ManyToOne
    private BasicUser buyer;
    @ManyToMany
    private List<Cuisine> cuisines;
    @OneToOne
    private Chat chat;
    @ManyToOne
    private Restaurant restaurant;
    @ManyToOne
    private Driver driver;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private LocalDate dateCreated;
    private LocalDate dateUpdated;

    public FoodOrder(String name, Double price, BasicUser buyer, Restaurant restaurant, OrderStatus orderStatus) {
        this.name = name;
        this.price = price;
        this.buyer = buyer;
        this.restaurant = restaurant;
        this.orderStatus = orderStatus;
    }

    public FoodOrder(String name, Double price, BasicUser buyer, List<Cuisine> cuisines, Restaurant restaurant, OrderStatus orderStatus) {
        this.name = name;
        this.price = price;
        this.buyer = buyer;
        this.cuisines = cuisines;
        this.restaurant = restaurant;
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return name + " - " + price + ", " + buyer.getName() + " " + buyer.getSurname();
    }
}
