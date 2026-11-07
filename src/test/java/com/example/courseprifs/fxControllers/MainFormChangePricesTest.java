package com.example.courseprifs.fxControllers;

import com.example.courseprifs.model.Cuisine;
import com.example.courseprifs.model.Restaurant;
import javafx.scene.control.Alert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MainFormChangePricesTest extends MainFormTestBase {

    private final Restaurant restaurant = new Restaurant();

    private void selectRestaurant() {
        form.restaurantCuisineField.getItems().add(restaurant);
        form.restaurantCuisineField.getSelectionModel().select(restaurant);
    }

    private static Cuisine cuisine(double price) {
        Cuisine cuisine = new Cuisine();
        cuisine.setPrice(price);
        return cuisine;
    }

    @ParameterizedTest(name = "restaurant={0}, multiplier=''{1}''")
    @CsvSource({
            "false, 2,    You must select the restaurant before changing prices.",
            "true,  '  ', Please enter a multiplier.",
            "true,  0,    Multiplier must be a positive number.",
            "true,  abc,  Please enter a valid multiplier.",
            "true,  2,    This restaurant has no cuisines." // mock'as pagal nutylėjimą grąžina tuščią sąrašą
    })
    void changePrices_invalidInput_showsWarningAndUpdatesNothing(boolean restaurantSelected, String multiplier,
                                                                 String expectedMessage) {
        if (restaurantSelected) selectRestaurant();
        form.multiplierField.setText(multiplier);

        form.changePrices();

        fxUtils.verify(() -> FxUtils.generateAlert(Alert.AlertType.WARNING, "Error",
                "Change price error", expectedMessage));
        verify(db, never()).update(any());
    }

    @Test
    void changePrices_validMultiplier_updatesEveryCuisineAndReloadsMenu() {
        selectRestaurant();
        form.multiplierField.setText(" 1.5 ");
        List<Cuisine> cuisines = List.of(cuisine(10), cuisine(5));
        when(db.getRestaurantCuisine(restaurant)).thenReturn(cuisines);

        form.changePrices();

        assertEquals(15.0, cuisines.get(0).getPrice());
        assertEquals(7.5, cuisines.get(1).getPrice());
        cuisines.forEach(c -> verify(db).update(c));
        assertEquals(cuisines, form.cuisineList.getItems());
        assertEquals("", form.multiplierField.getText());
        fxUtils.verifyNoInteractions();
    }
}
