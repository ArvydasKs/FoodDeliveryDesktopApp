package com.example.courseprifs.fxControllers;

import com.example.courseprifs.model.User;
import javafx.scene.control.Alert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserFormValidateFormTest extends UserFormTestBase {

    /** validateForm yra private, todėl kviečiamas per reflection. */
    private boolean validate() throws Exception {
        Method method = UserForm.class.getDeclaredMethod("validateForm");
        method.setAccessible(true);
        return (boolean) method.invoke(form);
    }

    /** Grąžina validacijos klaidų tekstą, parodytą alert'e. */
    private String shownErrors() {
        ArgumentCaptor<String> content = ArgumentCaptor.forClass(String.class);
        fxUtils.verify(() -> FxUtils.generateAlert(eq(Alert.AlertType.ERROR), eq("Validation Error"),
                eq("Please fix these issues: "), content.capture()));
        return content.getValue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"user", "restaurant", "client", "driver"})
    void validateForm_validFields_returnsTrue(String role) throws Exception {
        selectRole(role);
        initForm(null, null);

        assertTrue(validate());
        verify(db).userExists("jonas");
        fxUtils.verifyNoInteractions();
    }

    @Test
    void validateForm_allDriverFieldsBlank_returnsFalseWithEveryError() throws Exception {
        selectRole("driver");
        form.usernameField.setText(" ");
        form.pswField.setText("");
        form.nameField.setText("");
        form.surnameField.setText("");
        form.phoneNumField.setText("");
        form.addressField.setText("");
        form.licenceField.setText("");
        form.bDateField.setValue(null);
        form.vehicleTypeField.setValue(null);
        initForm(null, null);

        assertFalse(validate());
        assertEquals("""
                Username cannot be empty.
                Password cannot be empty.
                Name cannot be empty.
                Surname cannot be empty.
                Phone number cannot be empty.
                Address cannot be empty.
                Driver's licence cannot be empty.
                Birth date must be selected.
                Vehicle type must be selected.
                """, shownErrors());
        verify(db, never()).userExists(anyString());
    }

    @Test
    void validateForm_nonNumericPhone_returnsFalse() throws Exception {
        selectRole("user");
        form.phoneNumField.setText("12ab");
        initForm(null, null);

        assertFalse(validate());
        assertEquals("Invalid phone number format.\n", shownErrors());
    }

    @Test
    void validateForm_duplicateUsername_returnsFalse() throws Exception {
        selectRole("user");
        initForm(null, null);
        when(db.userExists("jonas")).thenReturn(true);

        assertFalse(validate());
        fxUtils.verify(() -> FxUtils.generateAlert(Alert.AlertType.ERROR, "Validation Error",
                "Duplicate username", "That username is already taken."));
    }

    @Test
    void validateForm_updateMode_skipsPasswordAndDuplicateChecks() throws Exception {
        User existing = new User("jonas", "hash", "Jonas", "Jonaitis", "123456");
        initForm(null, existing); // setData užpildo laukus iš existing ir pažymi userRadio
        form.pswField.setText("");

        assertTrue(validate());
        verify(db, never()).userExists(anyString());
        fxUtils.verifyNoInteractions();
    }
}
