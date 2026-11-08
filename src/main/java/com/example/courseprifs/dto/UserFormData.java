package com.example.courseprifs.dto;

import com.example.courseprifs.model.UserType;
import com.example.courseprifs.model.VehicleType;

import java.time.LocalDate;

public record UserFormData(
   String username,
   String password,
   String name,
   String surname,
   String phone,
   String address,
   String licence,
   LocalDate birthDate,
   VehicleType vehicleType,
   UserType role,
   boolean createMode
) {}
