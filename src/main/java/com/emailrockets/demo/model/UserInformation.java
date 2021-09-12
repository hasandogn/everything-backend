package com.emailrockets.demo.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor
@Table(name = "user_information")
public class UserInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String mail;
    private String firstName;
    private String lastName;
    private String middleName;
    private boolean isActive;

    public UserInformation(Long id, String mail, String firstName, String lastName, String middleName, Boolean isActive) {
        this.id = id;
        this.mail = mail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.isActive = isActive;
    }


    public UserInformation(Long id, String mail, String firstName, String lastName, String middleName) {
        this.id = id;
        this.mail = mail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
    }

    public UserInformation(String mail, String firstName, String lastName, String middleName, Boolean isActive) {
        this.mail = mail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.isActive = isActive;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public boolean getActive() {
        return isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInformation that = (UserInformation) o;
        return isActive == that.isActive && Objects.equals(id, that.id) && Objects.equals(mail, that.mail) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(middleName, that.middleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mail, firstName, lastName, middleName, isActive);
    }
}
