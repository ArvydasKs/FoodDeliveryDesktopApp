package com.example.courseprifs.fxControllers;

import com.example.courseprifs.model.*;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MainFormReloadTableDataTest extends MainFormTestBase {

    private final Restaurant restaurant = new Restaurant("resto", "pw", "Resto", "Owner", "111", "Restaurant st.");
    private final BasicUser client = new BasicUser("client", "pw", "Client", "Basic", "222", "Client st.");
    private final Driver driver = new Driver("driver", "pw", "Driver", "Fast", "333", "Driver st.",
            "LIC", LocalDate.of(1990, 1, 1), VehicleType.CAR);

    @BeforeEach
    void addTabsToPane() {
        form.tabPane.getTabs().setAll(form.userTab, form.orderTab, form.foodTab,
                form.altUserTab, form.chatTab, form.reviewTab);
    }

    /** Pažymi tab'ą ir nustato prisijungusį vartotoją (restaurant arba admin). */
    private void open(Tab tab, boolean asRestaurant) throws Exception {
        form.tabPane.getSelectionModel().select(tab);
        setPrivate("currentUser", asRestaurant ? restaurant : new User("admin", "pw", "Admin", "Admin", "000"));
    }

    @Test
    void reloadTableData_userTab_fillsTableWithAllUsersAndAddresses() throws Exception {
        open(form.userTab, false);
        User plain = new User("plain", "secret", "Plain", "User", "444");
        plain.setId(7);
        when(db.getAllRecords(User.class)).thenReturn(List.of(plain, client, restaurant, driver));

        form.reloadTableData();

        ObservableList<UserTableParameters> rows = getPrivate("data");
        assertEquals(4, rows.size());
        UserTableParameters first = rows.get(0);
        assertEquals(List.of(7, "User", "plain", "secret", "Plain", "User", "444"),
                List.of(first.getId(), first.getUserType(), first.getLogin(), first.getPassword(),
                        first.getName(), first.getSurname(), first.getPhoneNumber()));
        assertNull(first.getAddress());
        assertEquals(List.of("BasicUser", "Restaurant", "Driver"),
                rows.stream().skip(1).map(UserTableParameters::getUserType).toList());
        assertEquals(List.of("Client st.", "Restaurant st.", "Driver st."),
                rows.stream().skip(1).map(UserTableParameters::getAddress).toList());
    }

    @ParameterizedTest(name = "asRestaurant={0}")
    @ValueSource(booleans = {true, false})
    void reloadTableData_orderTab_fillsOrderListsDependingOnUser(boolean asRestaurant) throws Exception {
        open(form.orderTab, asRestaurant);
        FoodOrder restaurantOrder = mock(FoodOrder.class);
        FoodOrder anyOrder = mock(FoodOrder.class);
        when(db.getRestaurantOrders(restaurant)).thenReturn(List.of(restaurantOrder));
        when(db.getAllRecords(FoodOrder.class)).thenReturn(List.of(anyOrder));
        when(db.getAllRecords(BasicUser.class)).thenReturn(List.of(client, driver));
        when(db.getAllRecords(Restaurant.class)).thenReturn(List.of(restaurant));

        form.reloadTableData();

        assertEquals(List.of(asRestaurant ? restaurantOrder : anyOrder), form.orderList.getItems());
        assertEquals(List.of(client), form.clientList.getItems());   // Driver atfiltruojamas
        assertEquals(List.of(client), form.filterClients.getItems());
        assertEquals(List.of(OrderStatus.values()), form.orderStatusField.getItems());
        assertEquals(List.of(OrderStatus.values()), form.filterStatus.getItems());
        assertEquals(List.of(restaurant), form.restaurantField.getItems());
        assertEquals(asRestaurant, form.filterRestaurants.isDisable());
        assertEquals(!asRestaurant, form.filterRestaurants.isVisible());
        assertEquals(asRestaurant ? List.of() : List.of(restaurant), form.filterRestaurants.getItems());
    }

    @ParameterizedTest(name = "asRestaurant={0}")
    @ValueSource(booleans = {true, false})
    void reloadTableData_foodTab_fillsCuisineFieldsDependingOnUser(boolean asRestaurant) throws Exception {
        open(form.foodTab, asRestaurant);
        Restaurant other = new Restaurant();
        Cuisine cuisine = new Cuisine();
        when(db.getAllRecords(Restaurant.class)).thenReturn(List.of(restaurant, other));
        when(db.getRestaurantCuisine(restaurant)).thenReturn(List.of(cuisine));

        form.reloadTableData();

        assertEquals(List.of(Allergen.values()), form.allergenCuisineField.getItems());
        if (asRestaurant) {
            assertEquals(List.of(restaurant), form.restaurantCuisineField.getItems());
            assertSame(restaurant, form.restaurantCuisineField.getSelectionModel().getSelectedItem());
            assertEquals(List.of(cuisine), form.cuisineList.getItems());
        } else {
            assertEquals(List.of(restaurant, other), form.restaurantCuisineField.getItems());
            assertTrue(form.cuisineList.getItems().isEmpty());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"altUser", "chat", "review"})
    void reloadTableData_otherTabs_loadTheirLists(String tabName) throws Exception {
        List<User> users = List.of(client);
        List<Chat> chats = List.of(mock(Chat.class));
        when(db.getAllRecords(User.class)).thenReturn(users);
        when(db.getAllRecords(Chat.class)).thenReturn(chats);
        when(db.getAllRecords(BasicUser.class)).thenReturn(List.of(client));

        switch (tabName) {
            case "altUser" -> open(form.altUserTab, false);
            case "chat" -> open(form.chatTab, false);
            default -> open(form.reviewTab, false);
        }

        form.reloadTableData();

        switch (tabName) {
            case "altUser" -> {
                assertEquals(List.of(UserType.values()), form.filterUserList.getItems());
                assertEquals(users, form.userListField.getItems());
            }
            case "chat" -> assertEquals(chats, form.chatList.getItems());
            default -> {
                assertEquals(List.of(client), form.reviewUserList.getItems());
                assertEquals(List.of(1, 2, 3, 4, 5), form.reviewRatingField.getItems());
            }
        }
    }
}
