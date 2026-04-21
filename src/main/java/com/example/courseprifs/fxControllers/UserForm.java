package com.example.courseprifs.fxControllers;

import com.example.courseprifs.HibernateControl.GenericHibernate;
import com.example.courseprifs.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hibernate.tool.schema.Action;
import org.mindrot.jbcrypt.BCrypt;

import java.net.URL;
import java.util.ResourceBundle;

public class UserForm implements Initializable {
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField pswField;
    @FXML
    public TextField nameField;
    @FXML
    public TextField surnameField;
    @FXML
    public TextField phoneNumField;
    @FXML
    public TextField addressField;
    @FXML
    public RadioButton userRadio;
    @FXML
    public RadioButton restaurantRadio;
    @FXML
    public RadioButton clientRadio;
    @FXML
    public RadioButton driverRadio;
    @FXML
    public TextField licenceField;
    @FXML
    public DatePicker bDateField;
    @FXML
    public ComboBox<VehicleType> vehicleTypeField;
    @FXML
    public Button createButton;
    @FXML
    public Button updateButton;

    private EntityManagerFactory entityManagerFactory;

    private GenericHibernate genericHibernate;

    private User currentUser;
    private User userToUpdate;
    private boolean register;

    public void setData(EntityManagerFactory entityManagerFactory, User user, User userToUpdate, boolean register) {
        this.entityManagerFactory = entityManagerFactory;
        this.genericHibernate = new GenericHibernate(entityManagerFactory);
        this.currentUser = user;
        this.userToUpdate = userToUpdate;
        this.register = register;
        fillFields();
    }

    private Runnable onSaveCallback;

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }

    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();

        if (usernameField.getText().isBlank()) errors.append("Username cannot be empty.\n");
        if (userToUpdate == null) {
            if (pswField.getText().isBlank()) errors.append("Password cannot be empty.\n");
        }
        if (nameField.getText().isBlank()) errors.append("Name cannot be empty.\n");
        if (surnameField.getText().isBlank()) errors.append("Surname cannot be empty.\n");
        if (phoneNumField.getText().isBlank()) {
            errors.append("Phone number cannot be empty.\n");
        } else if (!phoneNumField.getText().matches("^[0-9]+$")) {
            errors.append("Invalid phone number format.\n");
        }

        if ((restaurantRadio.isSelected() || clientRadio.isSelected() || driverRadio.isSelected()) && addressField.getText().isBlank()) {
            errors.append("Address cannot be empty.\n");
        }

        if (driverRadio.isSelected()) {
            if (licenceField.getText().isBlank()) errors.append("Driver's licence cannot be empty.\n");
            if (bDateField.getValue() == null) errors.append("Birth date must be selected.\n");
            if (vehicleTypeField.getValue() == null) errors.append("Vehicle type must be selected.\n");
        }

        if (!errors.isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Validation Error", "Please fix these issues: ", errors.toString());
            return false;
        }

        if (userToUpdate == null && genericHibernate.userExists(usernameField.getText().trim())) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Validation Error", "Duplicate username", "That username is already taken.");
            return false;
        }

        return true;
    }

    private void fillFields() {
        if (userToUpdate != null) {
            usernameField.setText(userToUpdate.getLogin());
            nameField.setText(userToUpdate.getName());
            surnameField.setText(userToUpdate.getSurname());
            phoneNumField.setText(userToUpdate.getPhoneNumber());

            createButton.setDisable(true);
            createButton.setVisible(false);
            updateButton.setDisable(false);
            updateButton.setVisible(true);

            usernameField.setDisable(true);

            if (userToUpdate.getClass().equals(User.class)) {
                userRadio.setSelected(true);
            } else if (userToUpdate.getClass().equals(Restaurant.class)) {
                restaurantRadio.setSelected(true);
                addressField.setText(((Restaurant) userToUpdate).getAddress());
            } else if (userToUpdate.getClass().equals(BasicUser.class)) {
                clientRadio.setSelected(true);
                addressField.setText(((BasicUser) userToUpdate).getAddress());
            } else if (userToUpdate.getClass().equals(Driver.class)) {
                driverRadio.setSelected(true);
                addressField.setText(((Driver) userToUpdate).getAddress());
                licenceField.setText(((Driver) userToUpdate).getLicence());
                bDateField.setValue(((Driver) userToUpdate).getBDate());
                vehicleTypeField.setValue(((Driver) userToUpdate).getVehicleType());
            }

            disableFields();
            userRadio.setDisable(true);
            restaurantRadio.setDisable(true);
            clientRadio.setDisable(true);
            driverRadio.setDisable(true);


        }

        if (register) {
            restaurantRadio.setSelected(true);

            disableFields();
            userRadio.setDisable(true);
            restaurantRadio.setDisable(true);
            clientRadio.setDisable(true);
            driverRadio.setDisable(true);
        }
    }

    public void createUser(ActionEvent actionEvent) {
        if (!validateForm()) return;

        String hashedPassword = BCrypt.hashpw(pswField.getText(), BCrypt.gensalt());

        if (userRadio.isSelected()) {
            User user = new User(usernameField.getText(),
                    hashedPassword,
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumField.getText());
            genericHibernate.create(user);
        } else if (restaurantRadio.isSelected()) {
            Restaurant restaurant = new Restaurant(usernameField.getText(),
                    hashedPassword,
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumField.getText(),
                    addressField.getText());
            genericHibernate.create(restaurant);
        } else if (clientRadio.isSelected()) {
            BasicUser basicUser = new BasicUser(usernameField.getText(),
                    hashedPassword,
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumField.getText(),
                    addressField.getText());
            genericHibernate.create(basicUser);
        } else if (driverRadio.isSelected()) {
            Driver driver = new Driver(usernameField.getText(),
                    hashedPassword,
                    nameField.getText(),
                    surnameField.getText(),
                    phoneNumField.getText(),
                    addressField.getText(),
                    licenceField.getText(),
                    bDateField.getValue(),
                    vehicleTypeField.getValue());
            genericHibernate.create(driver);
        }

        if (currentUser != null)
        {
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    public void updateUser(ActionEvent actionEvent) {
        if (!validateForm()) return;

        userToUpdate.setLogin(usernameField.getText());
        userToUpdate.setName(nameField.getText());
        userToUpdate.setSurname(surnameField.getText());
        userToUpdate.setPhoneNumber(phoneNumField.getText());

        String newPassword = pswField.getText();

        if (newPassword != null && !newPassword.isBlank()) {
            String hashed = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            userToUpdate.setPassword(hashed);
        }

        if (restaurantRadio.isSelected()) {
            ((Restaurant) userToUpdate).setAddress(addressField.getText());
        }
        else if (clientRadio.isSelected()) {
            ((BasicUser) userToUpdate).setAddress(addressField.getText());
        }
        else if (driverRadio.isSelected()) {
            ((Driver) userToUpdate).setAddress(addressField.getText());
            ((Driver) userToUpdate).setLicence(licenceField.getText());
            ((Driver) userToUpdate).setBDate(bDateField.getValue());
            ((Driver) userToUpdate).setVehicleType(vehicleTypeField.getValue());
        }

        genericHibernate.update(userToUpdate);

        if (onSaveCallback != null) {
            onSaveCallback.run();
        }
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void disableFields() {
        if (userRadio.isSelected()) {
            addressField.setDisable(true);
            addressField.setVisible(false);
            licenceField.setDisable(true);
            licenceField.setVisible(false);
            bDateField.setDisable(true);
            bDateField.setVisible(false);
            vehicleTypeField.setDisable(true);
            vehicleTypeField.setVisible(false);
        }
        else if (clientRadio.isSelected()) {
            addressField.setDisable(false);
            addressField.setVisible(true);
            licenceField.setDisable(true);
            licenceField.setVisible(false);
            bDateField.setDisable(true);
            bDateField.setVisible(false);
            vehicleTypeField.setDisable(true);
            vehicleTypeField.setVisible(false);
        }
        else if (driverRadio.isSelected()) {
            addressField.setDisable(false);
            addressField.setVisible(true);
            licenceField.setDisable(false);
            licenceField.setVisible(true);
            bDateField.setDisable(false);
            bDateField.setVisible(true);
            vehicleTypeField.setDisable(false);
            vehicleTypeField.setVisible(true);
        }
        else {
            addressField.setDisable(false);
            addressField.setVisible(true);
            licenceField.setDisable(true);
            licenceField.setVisible(false);
            bDateField.setDisable(true);
            bDateField.setVisible(false);
            vehicleTypeField.setDisable(true);
            vehicleTypeField.setVisible(false);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        disableFields();
        vehicleTypeField.getItems().addAll(VehicleType.values());
    }
}
