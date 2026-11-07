package com.example.courseprifs.fxControllers;

import com.example.courseprifs.HibernateControl.GenericHibernate;
import com.example.courseprifs.model.User;
import com.example.courseprifs.model.VehicleType;
import jakarta.persistence.EntityManagerFactory;
import javafx.scene.control.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedConstruction;

import java.time.LocalDate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;

/** Bendras UserForm testų setup'as: užpildyta forma ir GenericHibernate mock'as. */
abstract class UserFormTestBase extends FxTestBase {

    protected static final LocalDate BIRTH_DATE = LocalDate.of(1990, 1, 1);

    protected UserForm form;
    protected GenericHibernate db;

    @BeforeEach
    void setUpForm() {
        form = new UserForm();
        form.usernameField = new TextField("jonas");
        form.pswField = new PasswordField();
        form.pswField.setText("secret");
        form.nameField = new TextField("Jonas");
        form.surnameField = new TextField("Jonaitis");
        form.phoneNumField = new TextField("123456");
        form.addressField = new TextField("Vilnius");
        form.licenceField = new TextField("LIC-1");
        form.bDateField = new DatePicker(BIRTH_DATE);
        form.vehicleTypeField = new ComboBox<>();
        form.vehicleTypeField.setValue(VehicleType.CAR);
        form.userRadio = new RadioButton();
        form.restaurantRadio = new RadioButton();
        form.clientRadio = new RadioButton();
        form.driverRadio = new RadioButton();
        form.createButton = new Button();
        form.updateButton = new Button();
    }

    /** Pažymi radio mygtuką pagal rolę: user, restaurant, client arba driver. */
    protected void selectRole(String role) {
        switch (role) {
            case "user" -> form.userRadio.setSelected(true);
            case "restaurant" -> form.restaurantRadio.setSelected(true);
            case "client" -> form.clientRadio.setSelected(true);
            default -> form.driverRadio.setSelected(true);
        }
    }

    /** Inicializuoja formą per setData, pakeičiant GenericHibernate mock'u. */
    protected void initForm(User currentUser, User userToUpdate) {
        try (MockedConstruction<GenericHibernate> mocked = mockConstruction(GenericHibernate.class)) {
            form.setData(mock(EntityManagerFactory.class), currentUser, userToUpdate, false);
            db = mocked.constructed().get(0);
        }
    }
}
