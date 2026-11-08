package com.example.courseprifs.validators;

import com.example.courseprifs.dto.UserFormData;
import com.example.courseprifs.model.UserType;

public final class UserFormValidator {
    private UserFormValidator() {}

    public static String validate(UserFormData data) { // klaidu string builderis
        StringBuilder errors = new StringBuilder();
        validateCommonFields(data, errors);
        validateRoleFields(data, errors);
        return errors.toString();
    }



    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static void requireText(String value, String message, StringBuilder errors) {
        if (isBlank(value)) errors.append(message).append('\n');
    }

    private static void requireValue(Object value, String message, StringBuilder errors) {
        if (value == null) errors.append(message).append('\n');
    }

    private static void validateCommonFields(UserFormData data, StringBuilder errors) {
        requireText(data.username(), "Username cannot be empty.", errors); // username
        if (data.createMode()) requireText(data.password(), "Password cannot be empty.", errors); // password, jei kuriamas naujas vartotojas
        requireText(data.name(), "Name cannot be empty.", errors); // name
        requireText(data.surname(), "Surname cannot be empty.", errors); // surname
        validatePhone(data.phone(), errors); // telefono numeris
    }

    private static void validatePhone(String phone, StringBuilder errors) {
        if (isBlank(phone)) errors.append("Phone number cannot be empty.\n"); // telefono numeris ne tuscias
        else if (!phone.matches("\\d+")) errors.append("Invalid phone number format.\n"); // telefono numeris atitinka formata
    }

    private static void validateRoleFields(UserFormData data, StringBuilder errors) {
        if (data.role() != UserType.User) requireText(data.address(), "Address cannot be empty.", errors); // adresas, jei ne admin
        if (data.role() == UserType.Driver) validateDriverFields(data, errors); // jei driver
    }

    private static void validateDriverFields(UserFormData data, StringBuilder errors) {
        requireText(data.licence(), "Driver's licence cannot be empty.", errors); // licence
        requireValue(data.birthDate(), "Birth date must be selected.", errors); // birth date
        requireValue(data.vehicleType(), "Vehicle type must be selected.", errors); // vehicle type
    }
}
