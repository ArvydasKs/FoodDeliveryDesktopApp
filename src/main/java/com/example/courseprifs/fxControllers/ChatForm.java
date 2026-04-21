package com.example.courseprifs.fxControllers;

import com.example.courseprifs.HibernateControl.CustomHibernate;
import com.example.courseprifs.model.*;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatForm implements Initializable {
    @FXML
    public ListView<Review> messageList;
    @FXML
    public TextArea messageBody;
    @FXML
    public Button sendMessageButton;

    private EntityManagerFactory entityManagerFactory;
    private CustomHibernate customHibernate;
    private User currentUser;
    private FoodOrder currentFoodOrder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setData(EntityManagerFactory entityManagerFactory, User currentUser, FoodOrder currentFoodOrder) {
        this.entityManagerFactory = entityManagerFactory;
        this.currentUser = currentUser;
        this.currentFoodOrder = currentFoodOrder;
        this.customHibernate = new CustomHibernate(entityManagerFactory);

        if (currentFoodOrder.getOrderStatus() == OrderStatus.COMPLETED) {
            messageBody.setVisible(false);
            sendMessageButton.setVisible(false);
        }

        showMessages();
    }

    public void showMessages() {
        FoodOrder foodOrder = customHibernate.getEntityById(FoodOrder.class, currentFoodOrder.getId());
        messageList.getItems().clear();
        messageList.getItems().addAll(customHibernate.getChatMessages(foodOrder.getChat()));
        messageBody.clear();
    }

    public void sendMessage() {
        if (messageBody.getText().trim().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Cannot send message", "The message body cannot be empty.");
            return;
        }

        if (currentFoodOrder.getChat() == null) {
            FoodOrder managedOrder = customHibernate.getEntityById(FoodOrder.class, currentFoodOrder.getId());
            Chat chat = new Chat("Chat for " + managedOrder.getName(), managedOrder);
            customHibernate.create(chat);
            managedOrder.setChat(chat);
            customHibernate.update(managedOrder);
            currentFoodOrder = managedOrder;
        }

        FoodOrder foodOrder = customHibernate.getEntityById(FoodOrder.class, currentFoodOrder.getId());
        if (currentUser instanceof BasicUser basicUser) {
            Review message = new Review(basicUser.getLogin() + ": " + messageBody.getText(),
                    basicUser,
                    foodOrder.getChat());
            customHibernate.create(message);
        }

        showMessages();
    }
}
