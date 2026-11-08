package com.example.courseprifs.services;

import com.example.courseprifs.dto.UserFormData;
import com.example.courseprifs.model.BasicUser;
import com.example.courseprifs.model.Driver;
import com.example.courseprifs.model.Restaurant;
import com.example.courseprifs.model.User;

public final class UserFactory {
    private UserFactory() {}

    public static User create(UserFormData data, String hashedPassword) {
        return switch (data.role()) {
            case User -> new User(
                    data.username(), hashedPassword, data.name(), data.surname(), data.phone()
            );
            case Restaurant -> new Restaurant(
                    data.username(), hashedPassword, data.name(), data.surname(), data.phone(),
                    data.address()
            );
            case Client -> new BasicUser(
                    data.username(), hashedPassword, data.name(), data.surname(), data.phone(),
                    data.address()
            );
            case Driver -> new Driver(
                    data.username(), hashedPassword, data.name(), data.surname(), data.phone(),
                    data.address(), data.licence(), data.birthDate(), data.vehicleType()
            );
            case All -> throw new IllegalArgumentException("Cannot create user with role All");
        };
    }
}
