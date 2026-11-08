package com.example.courseprifs.services;

import com.example.courseprifs.HibernateControl.GenericHibernate;
import com.example.courseprifs.dto.UserFormData;
import com.example.courseprifs.model.User;

public class UserService {
    private final GenericHibernate genericHibernate;
    private final PasswordService passwordService;

    public UserService(GenericHibernate genericHibernate, PasswordService passwordService) {
        this.genericHibernate = genericHibernate;
        this.passwordService = passwordService;
    }

    public void createUser(UserFormData data) {
        String hashedPassword = passwordService.hash(data.password());
        User user = UserFactory.create(data, hashedPassword);
        genericHibernate.create(user);
    }
}
