package com.example.courseprifs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private BasicUser owner;
    @ManyToOne
    private BasicUser feedBackOwner;
    private int rating;
    private boolean isVerified;
    private String reviewText;
    private LocalDate dateCreated;
    @ManyToOne
    private Chat chat;

    public Review(String reviewText, BasicUser owner, Chat chat) {
        this.reviewText = reviewText;
        this.owner = owner;
        this.chat = chat;
    }

    @Override
    public String toString() {
        return reviewText;
    }
}
