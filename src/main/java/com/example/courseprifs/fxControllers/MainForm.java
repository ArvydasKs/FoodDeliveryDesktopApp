package com.example.courseprifs.fxControllers;

import com.example.courseprifs.HelloApplication;
import com.example.courseprifs.HibernateControl.CustomHibernate;
import com.example.courseprifs.model.*;
import jakarta.persistence.Basic;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.hibernate.query.Order;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainForm implements Initializable {
    @FXML
    public ListView<User> userListField;
    @FXML
    public ComboBox<UserType> filterUserList;
    @FXML
    public Tab userTab;
    @FXML
    public Tab orderTab;
    @FXML
    public Tab foodTab;
    @FXML
    public Tab altUserTab;
    @FXML
    public Tab chatTab;
    @FXML
    public Tab reviewTab;
    @FXML
    public TabPane tabPane;
    //<editor-fold desc="Order Tab Elements">
    @FXML
    public ListView<FoodOrder> orderList;
    @FXML
    public ComboBox<BasicUser> clientList;
    @FXML
    public TextField titleField;
    @FXML
    public TextField priceField;
    @FXML
    public ComboBox<Restaurant> restaurantField;
    @FXML
    public ComboBox<OrderStatus> orderStatusField;
    @FXML
    public ComboBox<OrderStatus> filterStatus;
    @FXML
    public ComboBox<BasicUser> filterClients;
    @FXML
    public ListView<Cuisine> foodList;
    @FXML
    public Button viewChatButton;
    @FXML
    public ComboBox<Restaurant> filterRestaurants;
    //</editor-fold>
    //<editor-fold desc="User Tab elements">
    @FXML
    public TableView<UserTableParameters> userTable;
    @FXML
    public TableColumn<UserTableParameters, Integer> idCol;
    @FXML
    public TableColumn<UserTableParameters, String> userTypeCol;
    @FXML
    public TableColumn<UserTableParameters, String> loginCol;
    @FXML
    public TableColumn<UserTableParameters, String> passwordCol;
    @FXML
    public TableColumn<UserTableParameters, String> nameCol;
    @FXML
    public TableColumn<UserTableParameters, String> surnameCol;
    @FXML
    public TableColumn<UserTableParameters, String> phoneCol;
    @FXML
    public TableColumn<UserTableParameters, String> addressCol;

    private ObservableList<UserTableParameters> data = FXCollections.observableArrayList();
    //</editor-fold>
    //<editor-fold desc="Food Tab Elements">
    @FXML
    public ListView<Cuisine> cuisineList;
    @FXML
    public TextField titleCuisineField;
    @FXML
    public TextArea ingredientsCuisineField;
    @FXML
    public ListView<Restaurant> restaurantCuisineField;
    @FXML
    public TextField priceCuisineField;
    @FXML
    public CheckBox spicyCuisineCheck;
    @FXML
    public CheckBox veganCuisineCheck;
    @FXML
    public ListView<Allergen> allergenCuisineField;
    @FXML
    public CheckBox filterSpicyCheck;
    @FXML
    public CheckBox filterVeganCheck;
    @FXML
    public TextField multiplierField;
    //</editor-fold>
    //<editor-fold desc="Chat Tab Elements">
    @FXML
    public ListView<Chat> chatList;
    @FXML
    public ListView<Review> chatMessages;
    @FXML
    public TextArea messageTextField;
    @FXML
    public TextField chatTitleField;
    //</editor-fold>

    //<editor-fold desc="Review Tab Elements">
    @FXML
    public ListView<BasicUser> reviewUserList;
    @FXML
    public ListView<Review> usersReviewList;
    @FXML
    public TextArea reviewTextField;
    @FXML
    public ComboBox<Integer> reviewRatingField;
    @FXML
    public Button updateReviewButton;
    @FXML
    public Text reviewOwnerText;
    //</editor-fold>

    private EntityManagerFactory entityManagerFactory;
    private CustomHibernate customHibernate;
    private User currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userTable.setEditable(true);
        userTable.setItems(data);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        userTypeCol.setCellValueFactory(new PropertyValueFactory<>("userType"));
        loginCol.setCellValueFactory(new PropertyValueFactory<>("login"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        passwordCol.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordCol.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setPassword(event.getNewValue());
            User user = customHibernate.getEntityById(User.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            user.setPassword(event.getNewValue());
            customHibernate.update(user);
        });

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setName(event.getNewValue());
            User user = customHibernate.getEntityById(User.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            user.setName(event.getNewValue());
            customHibernate.update(user);
        });

        surnameCol.setCellValueFactory(new PropertyValueFactory<>("surname"));
        surnameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        surnameCol.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setSurname(event.getNewValue());
            User user = customHibernate.getEntityById(User.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            user.setSurname(event.getNewValue());
            customHibernate.update(user);
        });

        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        phoneCol.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneCol.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setPhoneNumber(event.getNewValue());
            User user = customHibernate.getEntityById(User.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            user.setPhoneNumber(event.getNewValue());
            customHibernate.update(user);
        });

        allergenCuisineField.setItems(FXCollections.observableArrayList(Allergen.values()));
        allergenCuisineField.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        foodList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        foodList.getSelectionModel().getSelectedItems().addListener((ListChangeListener<Cuisine>) change -> {
            updateOrderPriceAuto();
        });

        priceField.setEditable(false);
    }

    public void setData(EntityManagerFactory entityManagerFactory, User user) {
        this.entityManagerFactory = entityManagerFactory;
        this.customHibernate = new CustomHibernate(entityManagerFactory);
        this.currentUser = user;
        reloadTableData();
        restrictAccess();
    }

    public void reloadTableData() {
        if (userTab.isSelected()) {
            data.clear();
            //userTable.getItems().clear();

            List<User> users = customHibernate.getAllRecords(User.class);
            for (User u : users) {
                UserTableParameters userTableParameters = new UserTableParameters();
                userTableParameters.setId(u.getId());
                userTableParameters.setUserType(u.getClass().getSimpleName());
                userTableParameters.setLogin(u.getLogin());
                userTableParameters.setPassword(u.getPassword());
                userTableParameters.setName(u.getName());
                userTableParameters.setSurname(u.getSurname());
                userTableParameters.setPhoneNumber(u.getPhoneNumber());

                if (u instanceof BasicUser) {
                    userTableParameters.setAddress(((BasicUser) u).getAddress());
                }
                if (u instanceof Restaurant) {
                    userTableParameters.setAddress(((Restaurant) u).getAddress());
                }
                if (u instanceof Driver) {
                    userTableParameters.setAddress(((Driver) u).getAddress());
                }
                data.add(userTableParameters);
            }
            // userTable.getItems().addAll(data);
        }
        else if (orderTab.isSelected()) {
            clearAllOrderFields();
            List<FoodOrder> foodOrders = getFoodOrders();
            orderList.getItems().addAll(foodOrders);
            List<BasicUser> onlyBasicUsers = customHibernate.getAllRecords(BasicUser.class).stream()
                                             .filter(u -> u.getClass().equals(BasicUser.class))
                                                     .toList();
            clientList.getItems().addAll(onlyBasicUsers);
            orderStatusField.getItems().addAll(OrderStatus.values());

            if (currentUser instanceof Restaurant restaurant) {
                restaurantField.getItems().clear();
                restaurantField.getItems().add(restaurant);
            } else {
                restaurantField.getItems().addAll(customHibernate.getAllRecords(Restaurant.class));
            }

            filterStatus.getItems().addAll(OrderStatus.values());
            filterClients.getItems().addAll(onlyBasicUsers);

            if (currentUser instanceof Restaurant) {
                filterRestaurants.setDisable(true);
                filterRestaurants.setVisible(false);
            } else {
                List<Restaurant> onlyRestaurants = customHibernate.getAllRecords(Restaurant.class).stream()
                        .filter(u -> u.getClass().equals(Restaurant.class))
                        .toList();

                filterRestaurants.getItems().addAll(onlyRestaurants);
            }
        }
        else if (foodTab.isSelected()) {
            clearAllCuisineFields();
            allergenCuisineField.getItems().addAll(Allergen.values());
            if (currentUser instanceof Restaurant restaurant) {
                restaurantCuisineField.getItems().clear();
                restaurantCuisineField.getItems().add(restaurant);
                restaurantCuisineField.getSelectionModel().select(restaurant);
                loadRestaurantMenu();
            } else {
                restaurantCuisineField.getItems().addAll(customHibernate.getAllRecords(Restaurant.class));
            }
        }
        else if (altUserTab.isSelected()) {
            fillAltUserList();
        }
        else if (chatTab.isSelected()) {
            clearChatFields();
            updateChatList();
        }
        else if (reviewTab.isSelected()) {
            clearReviewFields();
            updateReviewUserList();
        }
    }

    private void restrictAccess() {
        if (currentUser instanceof Restaurant) {
            tabPane.getTabs().remove(userTab);
            tabPane.getTabs().remove(altUserTab);
            tabPane.getTabs().remove(chatTab);
        }
    }

    private boolean checkPrice(String priceText) {
        try {
            double price = Double.parseDouble(priceText);
            if (price <= 0) {
                FxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Validation error", "Price cannot be negative.");
                return false;
            }
        } catch (NumberFormatException e) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Validation error", "Please enter a number for price.");
            return false;
        }
        return true;
    }


    //<editor-fold desc="AltUser Functions">
    public void createNewUser() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-form.fxml"));
        Parent parent = fxmlLoader.load();

        UserForm userForm = fxmlLoader.getController();
        userForm.setData(entityManagerFactory, currentUser, null, false);
        userForm.setOnSaveCallback(() -> fillAltUserList());

        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setTitle("User Form");
        stage.setScene(scene);
        stage.show();
    }

    public void updateUser() throws IOException {
        User userToUpdate = userListField.getSelectionModel().getSelectedItem();
        if (userToUpdate == null) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Cannot edit user", "Please select a user from the list to update.");
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-form.fxml"));
        Parent parent = fxmlLoader.load();

        UserForm userForm = fxmlLoader.getController();
        userForm.setData(entityManagerFactory, currentUser, userToUpdate, false);
        userForm.setOnSaveCallback(() -> fillAltUserList());

        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setTitle("User Form");
        stage.setScene(scene);
        stage.show();
    }

    public void deleteUser() {
        User selectedUser = userListField.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Cannot delete user", "Please select a user from the list to delete.");
            return;
        }

        customHibernate.delete(User.class, selectedUser.getId());
        fillAltUserList();
    }

    public void fillAltUserList() {
        filterUserList.getItems().clear();
        filterUserList.getItems().addAll(UserType.values());
        userListField.getItems().clear();
        userListField.getItems().addAll(customHibernate.getAllRecords(User.class));
    }

    public void filterUsers() {
        userListField.getItems().clear();
        UserType selectedType = filterUserList.getSelectionModel().getSelectedItem();

        if (selectedType == null) {
            return;
        } else if (selectedType == UserType.All) {
            userListField.getItems().addAll(customHibernate.getAllRecords(User.class));
        } else if (selectedType == UserType.User) {
            userListField.getItems().addAll(customHibernate.getUsersByType(User.class));
        } else if (selectedType == UserType.Restaurant) {
            userListField.getItems().addAll(customHibernate.getUsersByType(Restaurant.class));
        } else if (selectedType == UserType.Client) {
            userListField.getItems().addAll(customHibernate.getUsersByType(BasicUser.class));
        } else if (selectedType == UserType.Driver) {
            userListField.getItems().addAll(customHibernate.getUsersByType(Driver.class));
        }
    }
    //</editor-fold>

    //<editor-fold desc="FoodOrder Functions">
    private void clearAllOrderFields() {
        orderList.getItems().clear();
        clientList.getItems().clear();
        titleField.clear();
        priceField.clear();
        restaurantField.getItems().clear();
        orderStatusField.getItems().clear();
        foodList.getItems().clear();
        filterStatus.getItems().clear();
        filterClients.getItems().clear();
        filterRestaurants.getItems().clear();
        clientList.setDisable(false);
        restaurantField.setDisable(false);
        priceField.setDisable(false);
        foodList.setDisable(false);
    }

    public void fillOrderLists() {
        orderList.getItems().clear();;
        orderList.getItems().addAll(getFoodOrders());
        filterStatus.getSelectionModel().clearSelection();
        filterClients.getSelectionModel().clearSelection();
        filterRestaurants.getSelectionModel().clearSelection();
    }

    private List<FoodOrder> getFoodOrders() {
        if (currentUser instanceof Restaurant) {
            return customHibernate.getRestaurantOrders((Restaurant) currentUser);
        } else {
            return customHibernate.getAllRecords(FoodOrder.class);
        }
    }

    private void updateOrderPriceAuto() {
        List<Cuisine> selected = foodList.getSelectionModel().getSelectedItems();

        if (selected == null || selected.isEmpty()) {
            priceField.setText("0.0");
            return;
        }

        double sum = selected.stream()
                .mapToDouble(Cuisine::getPrice)
                .sum();

        priceField.setText(String.valueOf(sum));
    }

    private boolean validateOrderForm() {
        String title = titleField.getText().trim();
        BasicUser client = clientList.getSelectionModel().getSelectedItem();
        String priceText = priceField.getText().trim();
        Restaurant restaurant = restaurantField.getSelectionModel().getSelectedItem();
        OrderStatus orderStatus = orderStatusField.getSelectionModel().getSelectedItem();
        List<Cuisine> selectedCuisines = foodList.getSelectionModel().getSelectedItems();

        if (title.isEmpty() || client == null || priceText.isEmpty() || restaurant == null || orderStatus == null || selectedCuisines.isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Validation error", "Please fill in all required fields and select at least one cuisine.");
            return false;
        }

        return checkPrice(priceText);
    }

    public void createOrder() {
        if (!validateOrderForm()) return;

        FoodOrder foodOrder = new FoodOrder(titleField.getText(),
                Double.parseDouble(priceField.getText()),
                clientList.getValue(),
                new ArrayList<>(foodList.getSelectionModel().getSelectedItems()),
                restaurantField.getValue(),
                orderStatusField.getValue());
        customHibernate.create(foodOrder);

        BasicUser buyer = foodOrder.getBuyer();
        buyer.setPoints(buyer.getPoints() + 1);
        customHibernate.update(buyer);

        fillOrderLists();
    }

    public void updateOrder() {
        FoodOrder foodOrder = orderList.getSelectionModel().getSelectedItem();

        if (foodOrder == null) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Cannot update order", "Please select an order to update.");
            return;
        }

        if (!validateOrderForm()) return;

        OrderStatus oldStatus = foodOrder.getOrderStatus();
        OrderStatus newStatus = orderStatusField.getValue();

        if (oldStatus == OrderStatus.PENDING) {
            foodOrder.setBuyer(clientList.getSelectionModel().getSelectedItem());
        }

        if (oldStatus == OrderStatus.PENDING || oldStatus == OrderStatus.SEEN_BY_STAFF) {
            foodOrder.setRestaurant(restaurantField.getSelectionModel().getSelectedItem());
            foodOrder.setCuisines(new ArrayList<>(foodList.getSelectionModel().getSelectedItems()));
            double newPrice = foodOrder.getCuisines()
                    .stream()
                    .mapToDouble(Cuisine::getPrice)
                    .sum();

            foodOrder.setPrice(newPrice);
            priceField.setText(String.valueOf(newPrice));
        }

        foodOrder.setOrderStatus(newStatus);
        foodOrder.setName(titleField.getText());

        customHibernate.update(foodOrder);
        fillOrderLists();
    }

    public void deleteOrder() {
        FoodOrder selectedOrder = orderList.getSelectionModel().getSelectedItem();

        if (selectedOrder == null) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Cannot delete order", "Please select an order to delete.");
            return;
        }

        selectedOrder = customHibernate.getEntityById(FoodOrder.class, selectedOrder.getId());

        Chat chat = selectedOrder.getChat();
        if (chat != null) {
            chat.setFoodOrder(null);
            selectedOrder.setChat(null);
            customHibernate.update(chat);
            customHibernate.update(selectedOrder);
            customHibernate.delete(Chat.class, chat.getId());
        }

        customHibernate.delete(FoodOrder.class, selectedOrder.getId());
        fillOrderLists();
    }

    public void loadOrderInfo() {
        FoodOrder selectedOrder = orderList.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) return;

        //selectedOrder = customHibernate.getEntityById(FoodOrder.class, selectedOrder.getId());

        clientList.getItems().stream()
                .filter(c -> c.getId() == selectedOrder.getBuyer().getId())
                .findFirst()
                .ifPresent(u -> clientList.getSelectionModel().select(u));

        titleField.setText(selectedOrder.getName());
        priceField.setText(selectedOrder.getPrice().toString());
            restaurantField.getItems().stream()
                    .filter(r -> r.getId() == selectedOrder.getRestaurant().getId())
                    .findFirst()
                    .ifPresent(u -> restaurantField.getSelectionModel().select(u));
        orderStatusField.getSelectionModel().select(selectedOrder.getOrderStatus());

        loadRestaurantMenuFromOrder();

        List<Cuisine> cuisines = customHibernate.getOrderCuisines(selectedOrder);
        foodList.getSelectionModel().clearSelection();

        for (Cuisine c : cuisines) {
            foodList.getItems().stream()
                    .filter(item -> item.getId() == c.getId())
                    .findFirst()
                    .ifPresent(item -> foodList.getSelectionModel().select(item));
        }

        disableFoodOrderFields();
    }

    private void disableFoodOrderFields() {
        OrderStatus status = orderStatusField.getSelectionModel().getSelectedItem();

        // nuo SEEN_BY_STAFF negalima keisti kliento
        boolean lockClient = (
                        status == OrderStatus.SEEN_BY_STAFF ||
                        status == OrderStatus.READY_FOR_PICKUP ||
                                status == OrderStatus.IN_DELIVERY ||
                        status == OrderStatus.COMPLETED
                );

        // nuo IN_DELIVERY negalima keisti restorano, kainos ir patiekalu
        boolean fullLock = (
                        status == OrderStatus.READY_FOR_PICKUP ||
                        status == OrderStatus.IN_DELIVERY ||
                        status == OrderStatus.COMPLETED
                );

        clientList.setDisable(lockClient);
        restaurantField.setDisable(fullLock);
        priceField.setDisable(fullLock);
        foodList.setDisable(fullLock);
    }

    public void filterOrders() {
        orderList.getItems().clear();

        OrderStatus selectedStatus = filterStatus.getSelectionModel().getSelectedItem();
        BasicUser selectedClient = filterClients.getSelectionModel().getSelectedItem();
        Restaurant restaurant;

        if (currentUser instanceof Restaurant) {
            restaurant = (Restaurant) currentUser;
        } else {
            restaurant = filterRestaurants.getSelectionModel().getSelectedItem();
        }

        List<FoodOrder> filteredOrders = customHibernate.getFilteredRestaurantOrders(restaurant, selectedClient, selectedStatus);
        orderList.getItems().addAll(filteredOrders);
    }

    public void loadRestaurantMenuFromOrder() {
        foodList.getItems().clear();
        foodList.getItems().addAll(customHibernate.getRestaurantCuisine(restaurantField.getSelectionModel().getSelectedItem()));
    }

    public void loadChatForm() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("chat-form.fxml"));
        Parent parent = fxmlLoader.load();

        FoodOrder selectedOrder = orderList.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Cannot view chat", "Please select an order to view its chat.");
            return;
        }

        orderList.getSelectionModel().clearSelection();

        ChatForm chatForm= fxmlLoader.getController();
        chatForm.setData(entityManagerFactory, currentUser, selectedOrder);

        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setTitle("Chat Form");
        stage.setScene(scene);
        stage.show();
    }
    //</editor-fold>

    //<editor-fold desc="Cuisine Functions">
    private void clearAllCuisineFields() {
        cuisineList.getItems().clear();
        titleCuisineField.clear();
        ingredientsCuisineField.clear();
        restaurantCuisineField.getItems().clear();
        priceCuisineField.clear();
        spicyCuisineCheck.setSelected(false);
        veganCuisineCheck.setSelected(false);
        allergenCuisineField.getItems().clear();
        filterSpicyCheck.setSelected(false);
        filterVeganCheck.setSelected(false);
    }

    private boolean validateCuisineForm() {
        String title = titleCuisineField.getText().trim();
        String ingredients = ingredientsCuisineField.getText().trim();
        String priceText = priceCuisineField.getText().trim();
        Restaurant restaurant = restaurantCuisineField.getSelectionModel().getSelectedItem();

        if (title.isEmpty() || ingredients.isEmpty() || priceText.isEmpty() || restaurant == null) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Validation error", "Please fill in all required fields.");
            return false;
        }

        return checkPrice(priceText);
    }

    public void createCuisine() {
        if (!validateCuisineForm()) return;

        Cuisine cuisine = new Cuisine(titleCuisineField.getText(),
                                        ingredientsCuisineField.getText(),
                                        Double.parseDouble(priceCuisineField.getText()),
                                        new ArrayList<>(allergenCuisineField.getSelectionModel().getSelectedItems()),
                                        spicyCuisineCheck.isSelected(),
                                        veganCuisineCheck.isSelected(),
                                        restaurantCuisineField.getSelectionModel().getSelectedItem());
        customHibernate.create(cuisine);
        loadRestaurantMenu();
    }

    public void updateCuisine() {
        Cuisine cuisine = cuisineList.getSelectionModel().getSelectedItem();

        if (cuisine == null) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Cannot update cuisine", "Please select a cuisine to update.");
            return;
        }

        if (!validateCuisineForm()) return;

        cuisine.setName(titleCuisineField.getText());
        cuisine.setIngredients(ingredientsCuisineField.getText());
        cuisine.setPrice(Double.valueOf(priceCuisineField.getText()));
        cuisine.setSpicy(spicyCuisineCheck.isSelected());
        cuisine.setVegan(veganCuisineCheck.isSelected());
        cuisine.setRestaurant(restaurantCuisineField.getSelectionModel().getSelectedItem());
        cuisine.setAllergens(allergenCuisineField.getSelectionModel().getSelectedItems());

        customHibernate.update(cuisine);
        loadRestaurantMenu();
    }

    public void deleteCuisine() {
        Cuisine selectedCuisine = cuisineList.getSelectionModel().getSelectedItem();

        if (selectedCuisine == null) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Cannot delete cuisine", "Please select a cuisine to delete.");
            return;
        }

        customHibernate.delete(Cuisine.class, selectedCuisine.getId());
        loadRestaurantMenu();
    }

    public void loadCuisineInfo() {
        Cuisine selectedCuisine = cuisineList.getSelectionModel().getSelectedItem();
        if (selectedCuisine == null) return;

        titleCuisineField.setText(selectedCuisine.getName());
        priceCuisineField.setText(selectedCuisine.getPrice().toString());
        ingredientsCuisineField.setText(selectedCuisine.getIngredients());
        spicyCuisineCheck.setSelected(selectedCuisine.isSpicy());
        veganCuisineCheck.setSelected(selectedCuisine.isVegan());
        restaurantCuisineField.getItems().stream()
                .filter(r -> r.getId() == selectedCuisine.getRestaurant().getId())
                .findFirst()
                .ifPresent(u -> restaurantCuisineField.getSelectionModel().select(u));

        allergenCuisineField.getSelectionModel().clearSelection();
        for (Allergen allergen : selectedCuisine.getAllergens()) {
            allergenCuisineField.getItems().stream()
                    .filter(a -> a == allergen)
                    .findFirst()
                    .ifPresent(a -> allergenCuisineField.getSelectionModel().select(a));
        }
    }

    public void loadRestaurantMenu() {
        cuisineList.getItems().clear();
        cuisineList.getItems().addAll(customHibernate.getRestaurantCuisine(restaurantCuisineField.getSelectionModel().getSelectedItem()));
    }

    public void filterCuisines() {
        Restaurant selectedRestaurant = restaurantCuisineField.getSelectionModel().getSelectedItem();

        if (selectedRestaurant == null) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Error", "Filter error", "You must select the restaurant before filtering.");
            return;
        }

        Boolean spicy = filterSpicyCheck.isSelected() ? true : null;
        Boolean vegan = filterVeganCheck.isSelected() ? true : null;

        List<Cuisine> filteredCuisines = customHibernate.getFilteredCuisines(selectedRestaurant, spicy, vegan);

        cuisineList.getItems().clear();
        cuisineList.getItems().addAll(filteredCuisines);
    }

    public void resetFilterCuisines() {
        filterSpicyCheck.setSelected(false);
        filterVeganCheck.setSelected(false);
        loadRestaurantMenu();
    }

    public void changePrices() {
        Restaurant selectedRestaurant = restaurantCuisineField.getSelectionModel().getSelectedItem();
        if (selectedRestaurant == null) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Error", "Change price error", "You must select the restaurant before changing prices.");
            return;
        }

        String multiplierText = multiplierField.getText().trim();
        if (multiplierText.isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Error", "Change price error", "Please enter a multiplier.");
            return;
        }

        double multiplier;
        try {
            multiplier = Double.parseDouble(multiplierText);
            if (multiplier <= 0) {
                FxUtils.generateAlert(Alert.AlertType.WARNING, "Error", "Change price error", "Multiplier must be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Error", "Change price error", "Please enter a valid multiplier.");
            return;
        }

        List<Cuisine> cuisines = customHibernate.getRestaurantCuisine(selectedRestaurant);
        if (cuisines.isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING,
                    "Error", "Change price error", "This restaurant has no cuisines.");
            return;
        }

        for (Cuisine cuisine : cuisines) {
            double oldPrice = cuisine.getPrice();
            double newPrice = oldPrice * multiplier;
            cuisine.setPrice(newPrice);
            customHibernate.update(cuisine);
        }

        loadRestaurantMenu();
        multiplierField.clear();
    }
    //</editor-fold>


    //<editor-fold desc="Chat Functions">
    private void clearChatFields() {
        chatList.getItems().clear();
        chatMessages.getItems().clear();
        messageTextField.clear();
        chatTitleField.clear();
    }

    private void updateChatList() {
        chatList.getItems().clear();
        chatList.getItems().addAll(customHibernate.getAllRecords(Chat.class));
    }

    public void loadChatMessages() {
        Chat chat = chatList.getSelectionModel().getSelectedItem();
        if (chat == null) return;

        chatMessages.getItems().clear();
        chatTitleField.clear();
        chatTitleField.setText(chat.getName());
        chatMessages.getItems().addAll(customHibernate.getChatMessages(chat));
    }

    public void deleteChat() {
        Chat selectedChat = chatList.getSelectionModel().getSelectedItem();
        if (selectedChat == null) return;

        FoodOrder foodOrder = customHibernate.getEntityById(FoodOrder.class, selectedChat.getFoodOrder().getId());
        if (foodOrder != null) {
            foodOrder.setChat(null);
            customHibernate.update(foodOrder);
        }

        customHibernate.delete(Chat.class, selectedChat.getId());
        clearChatFields();
        updateChatList();
    }

    public void deleteMessage() {
        Review selectedMessage = chatMessages.getSelectionModel().getSelectedItem();
        if (selectedMessage == null) return;

        customHibernate.delete(Review.class, selectedMessage.getId());
        loadChatMessages();
    }

    public void loadMessageBody() {
        Review message = chatMessages.getSelectionModel().getSelectedItem();
        if (message == null) return;

        messageTextField.clear();
        messageTextField.setText(message.getReviewText());
    }

    public void updateMessage() {
        Review message = chatMessages.getSelectionModel().getSelectedItem();
        if (message == null) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Cannot update message", "Please select a message before editing.");
            return;
        }

        if (messageTextField.getText().trim().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Error", "Cannot update message", "Message text cannot be empty.");
            return;
        }

        message.setReviewText(messageTextField.getText());
        customHibernate.update(message);
        messageTextField.clear();
        loadChatMessages();
    }

    public void updateChatTitle() {
        Chat selectedChat = chatList.getSelectionModel().getSelectedItem();

        if (selectedChat == null) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Cannot update chat title", "Please select a chat before editing.");
            return;
        }

        if (chatTitleField.getText().trim().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Error", "Cannot update chat title", "New chat title cannot be empty.");
            return;
        }

        selectedChat.setName(chatTitleField.getText());
        customHibernate.update(selectedChat);
        clearChatFields();
        updateChatList();
    }
    //</editor-fold>

    //<editor-fold desc="Review Tab Elements">
    public void deleteReview() {
        Review selectedReview = usersReviewList.getSelectionModel().getSelectedItem();
        if (selectedReview == null) return;

        if (currentUser instanceof Restaurant) {return;}

        customHibernate.delete(Review.class, selectedReview.getId());
        loadUserReviews();
    }

    public void updateReview() {
        Review review = usersReviewList.getSelectionModel().getSelectedItem();
        if (review == null) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Cannot update review", "Please select a review before editing.");
            return;
        }

        if (reviewTextField.getText().trim().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Error", "Cannot update review", "Review text cannot be empty.");
            return;
        }

        review.setReviewText(reviewTextField.getText());
        review.setRating(reviewRatingField.getSelectionModel().getSelectedItem());
        customHibernate.update(review);
        reviewTextField.clear();
        reviewRatingField.getSelectionModel().clearSelection();
        reviewOwnerText.setText("Review from: ");

        loadUserReviews();
    }

    private void clearReviewFields() {
        reviewUserList.getItems().clear();
        usersReviewList.getItems().clear();
        reviewTextField.clear();
        reviewRatingField.getItems().clear();
        reviewOwnerText.setText("Review from: ");

        reviewRatingField.getItems().addAll(1, 2, 3, 4, 5);

        if (currentUser instanceof Restaurant) {
            reviewTextField.setDisable(true);
            reviewRatingField.setDisable(true);
            updateReviewButton.setVisible(false);
            updateReviewButton.setDisable(true);
        }
    }

    private void updateReviewUserList() {
        if (currentUser instanceof Restaurant restaurant) {
            reviewUserList.getItems().add(restaurant);
            reviewUserList.getSelectionModel().select(restaurant);
            loadUserReviews();
        } else {
            List<BasicUser> users = customHibernate.getAllRecords(BasicUser.class);
            reviewUserList.getItems().addAll(users);
        }
    }

    public void loadUserReviews() {
        BasicUser selectedUser = reviewUserList.getSelectionModel().getSelectedItem();
        if (selectedUser == null) return;

        usersReviewList.getItems().clear();
        List<Review> reviews = customHibernate.getUserReviews(selectedUser);
        usersReviewList.getItems().addAll(reviews);
    }

    public void loadReviewInfo() {
        Review selectedReview = usersReviewList.getSelectionModel().getSelectedItem();
        if (selectedReview == null) return;

        reviewTextField.clear();
        reviewTextField.setText(selectedReview.getReviewText());
        reviewRatingField.getSelectionModel().select(selectedReview.getRating() - 1);
        reviewOwnerText.setText("Review from: " + selectedReview.getOwner().getLogin());
    }

    //</editor-fold>
}
