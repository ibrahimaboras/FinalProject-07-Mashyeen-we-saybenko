package com.example.user.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;


@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    private UUID userId;

    private String nationality;

    private String passportNumber;

    private String gender;

    private LocalDate dateOfBirth;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @JsonBackReference // Prevent serialization of the "User" side
    private User user;

    // Constructors
    public UserProfile() {}

    public UserProfile(String nationality, String passportNumber, String gender, LocalDate dateOfBirth) {
        this.nationality = nationality;
        this.passportNumber = passportNumber;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

    // Getters and Setters
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "nationality='" + nationality + '\'' +
                ", passportNumber='" + passportNumber + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }

}