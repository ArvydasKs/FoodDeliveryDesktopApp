package com.example.courseprifs.services;

import com.example.courseprifs.model.Cuisine;

import java.util.List;

public class PriceService {
    public void applyMultiplier(List<Cuisine> cuisines, double multiplier) {
        for (Cuisine cuisine : cuisines) {
            cuisine.setPrice(cuisine.getPrice() * multiplier);
        }
    }
}
