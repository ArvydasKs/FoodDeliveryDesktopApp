package com.example.courseprifs.fxControllers;

import com.example.courseprifs.HelloApplication;
import com.example.courseprifs.HibernateControl.CustomHibernate;
import com.example.courseprifs.model.Restaurant;
import com.example.courseprifs.model.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginForm {
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passwordField;

    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("coursework");
    private CustomHibernate customHibernate;

    public void validateAndLogin() throws IOException {
        customHibernate = new CustomHibernate(entityManagerFactory);
        User user = customHibernate.getUserByCredentials(loginField.getText(), passwordField.getText());

        if (user != null) {
            if (user.getClass().equals(User.class) || user instanceof Restaurant) {
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-form.fxml"));
                Parent parent = fxmlLoader.load();

                MainForm mainForm = fxmlLoader.getController();
                mainForm.setData(entityManagerFactory, user);

                Scene scene = new Scene(parent);
                Stage stage = (Stage) loginField.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } else {
                FxUtils.generateAlert(Alert.AlertType.WARNING, "Error", "Access denied", "You do not have permission to login.");
            }
        } else {
            FxUtils.generateAlert(Alert.AlertType.WARNING, "Error", "Invalid credentials", "Please check your username or password.");
        }


    }

    public void registerNewUser() throws IOException {
        // atidaryti user form
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-form.fxml"));
        //Stage stage = (Stage) loginField.getScene().getWindow();

        Parent parent = fxmlLoader.load();

        UserForm userForm = fxmlLoader.getController();
        userForm.setData(entityManagerFactory, null, null, true);

        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setTitle("User Form");
        stage.setScene(scene);
        stage.show();
    }
}
