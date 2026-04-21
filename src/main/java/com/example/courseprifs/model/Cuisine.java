package com.example.courseprifs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Cuisine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String ingredients;
    private Double price;
    private List<Allergen> allergens;
    private boolean isSpicy;
    private boolean isVegan;
    @ManyToMany(mappedBy = "cuisines", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<FoodOrder> orders;
    @ManyToOne
    private Restaurant restaurant;

    public Cuisine(String name, String ingredients, Double price, List<Allergen> allergens, boolean isSpicy, boolean isVegan, Restaurant restaurant) {
        this.name = name;
        this.ingredients = ingredients;
        this.price = price;
        this.allergens = allergens;
        this.isSpicy = isSpicy;
        this.isVegan = isVegan;
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return name + " - " + price + ", " + restaurant.getLogin();
    }
}
