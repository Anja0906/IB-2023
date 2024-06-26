package com.ib.ib.model;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private String email;
    private String firstName;
    private String lastName;
    private String telephoneNumber;
    private Boolean isAdmin;
    public User() {}

    public User(String email, String firstName, String lastName, String telephoneNumber, Boolean isAdmin) {
        this.email           = email;
        this.firstName       = firstName;
        this.lastName        = lastName;
        this.telephoneNumber = telephoneNumber;
        this.isAdmin         = isAdmin;
    }


    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}


    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {return lastName;}
    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getTelephoneNumber() {return telephoneNumber;}
    public void setTelephoneNumber(String telephoneNumber) {this.telephoneNumber = telephoneNumber;}

    public Boolean getIsAdministrator() {return isAdmin;}
    
    /**
     * @deprecated I accidentally named it not as code style require, lol. Please use getIsAdmin...
     */
    @Deprecated
    public Boolean IsAdministrator() {
        return isAdmin;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                '}';
    }
}
