package com.example.courseprifs.consoleCourseWork;

import com.example.courseprifs.model.FoodOrder;
import com.example.courseprifs.model.User;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//@NoArgsConstructor

public class Wolt implements Serializable {
    private List<User> allSystemUsers;
    private List<FoodOrder> allOrders;

    public Wolt() {
        this.allOrders = new ArrayList<>();
        this.allSystemUsers = new ArrayList<>();
    }

    public List<User> getAllSystemUsers() {
        return allSystemUsers;
    }

    public void setAllSystemUsers(List<User> allSystemUsers) {
        this.allSystemUsers = allSystemUsers;
    }

    public List<FoodOrder> getAllOrders() {
        return allOrders;
    }

    public void setAllOrders(List<FoodOrder> allOrders) {
        this.allOrders = allOrders;
    }
}
