package com.example.courseprifs.fxControllers;

import javafx.scene.Node;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserFormDisableFieldsTest extends UserFormTestBase {

    /** Laukas aktyvus = įjungtas ir matomas; neaktyvus = išjungtas ir paslėptas. */
    private static void assertActive(Node field, boolean active) {
        assertEquals(!active, field.isDisable());
        assertEquals(active, field.isVisible());
    }

    @ParameterizedTest(name = "{0}: address={1}, driverFields={2}")
    @CsvSource({
            "user,       false, false",
            "client,     true,  false",
            "driver,     true,  true",
            "restaurant, true,  false" // else šaka
    })
    void disableFields_showsOnlyFieldsRelevantToRole(String role, boolean addressActive, boolean driverFieldsActive) {
        selectRole(role);

        form.disableFields();

        assertActive(form.addressField, addressActive);
        assertActive(form.licenceField, driverFieldsActive);
        assertActive(form.bDateField, driverFieldsActive);
        assertActive(form.vehicleTypeField, driverFieldsActive);
    }
}
