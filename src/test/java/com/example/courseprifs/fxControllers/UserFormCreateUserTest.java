package com.example.courseprifs.fxControllers;

import com.example.courseprifs.model.BasicUser;
import com.example.courseprifs.model.Driver;
import com.example.courseprifs.model.Restaurant;
import com.example.courseprifs.model.User;
import com.example.courseprifs.model.VehicleType;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserFormCreateUserTest extends UserFormTestBase {

    @ParameterizedTest
    @ValueSource(strings = {"user", "restaurant", "client", "driver"})
    void createUser_validForm_persistsCorrectEntityWithHashedPassword(String role) {
        selectRole(role);
        initForm(null, null);

        form.createUser(new ActionEvent());

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(db).create(captor.capture());
        User saved = (User) captor.getValue();

        assertEquals("jonas", saved.getLogin());
        assertEquals("Jonas", saved.getName());
        assertEquals("Jonaitis", saved.getSurname());
        assertEquals("123456", saved.getPhoneNumber());
        assertNotEquals("secret", saved.getPassword());
        assertTrue(BCrypt.checkpw("secret", saved.getPassword()));

        switch (role) {
            case "user" -> assertEquals(User.class, saved.getClass());
            case "restaurant" -> {
                assertEquals(Restaurant.class, saved.getClass());
                assertEquals("Vilnius", ((Restaurant) saved).getAddress());
            }
            case "client" -> {
                assertEquals(BasicUser.class, saved.getClass());
                assertEquals("Vilnius", ((BasicUser) saved).getAddress());
            }
            default -> {
                Driver driver = assertInstanceOf(Driver.class, saved);
                assertEquals("Vilnius", driver.getAddress());
                assertEquals("LIC-1", driver.getLicence());
                assertEquals(BIRTH_DATE, driver.getBDate());
                assertEquals(VehicleType.CAR, driver.getVehicleType());
            }
        }
    }

    @Test
    void createUser_invalidForm_doesNotPersist() {
        selectRole("user");
        form.usernameField.setText("");
        initForm(null, null);

        form.createUser(new ActionEvent());

        verify(db, never()).create(any());
    }

    @Test
    void createUser_whenLoggedIn_runsCallbackAndClosesWindow() {
        selectRole("user");
        initForm(new User(), null);
        Runnable callback = mock(Runnable.class);
        form.setOnSaveCallback(callback);

        Node source = mock(Node.class);
        Scene scene = mock(Scene.class);
        Stage stage = mock(Stage.class);
        when(source.getScene()).thenReturn(scene);
        when(scene.getWindow()).thenReturn(stage);

        form.createUser(new ActionEvent(source, null));

        verify(db).create(any(User.class));
        verify(callback).run();
        verify(stage).close();
    }
}
