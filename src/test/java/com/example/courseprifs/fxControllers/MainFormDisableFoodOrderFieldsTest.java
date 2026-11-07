package com.example.courseprifs.fxControllers;

import com.example.courseprifs.model.OrderStatus;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainFormDisableFoodOrderFieldsTest extends MainFormTestBase {

    @ParameterizedTest(name = "{0}: clientLocked={1}, fullLock={2}")
    @CsvSource({
            "PENDING,          false, false",
            "SEEN_BY_STAFF,    true,  false",
            "READY_FOR_PICKUP, true,  true",
            "IN_DELIVERY,      true,  true",
            "COMPLETED,        true,  true"
    })
    void disableFoodOrderFields_locksFieldsByStatus(OrderStatus status, boolean clientLocked, boolean fullLock)
            throws Exception {
        form.orderStatusField.getItems().setAll(OrderStatus.values());
        form.orderStatusField.getSelectionModel().select(status);

        invokePrivate("disableFoodOrderFields"); // metodas private

        assertEquals(clientLocked, form.clientList.isDisable());
        assertEquals(fullLock, form.restaurantField.isDisable());
        assertEquals(fullLock, form.priceField.isDisable());
        assertEquals(fullLock, form.foodList.isDisable());
    }
}
