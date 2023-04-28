package com.ib.ib.model;

import javax.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "passwords")
public class Password {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    private LocalDateTime validTo;
    private String currentPassword;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Password() {}
    public Password(Integer id, LocalDateTime validTo, String currentPassword, User user) {
        this.id                 = id;
        this.validTo            = validTo;
        this.currentPassword    = currentPassword;
        this.user               = user;
    }

    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}

    public LocalDateTime getValidTo() {return validTo;}
    public void setValidTo(LocalDateTime validTo) {this.validTo = validTo;}

    public String getCurrentPassword() {return currentPassword;}
    public void setCurrentPassword(String currentPassword) {this.currentPassword = currentPassword;}

    @Override
    public String toString() {
        return "Password{" +
                "id=" + id +
                ", validTo=" + validTo +
                ", currentPassword='" + currentPassword + '\'' +
                ", user=" + user +
                '}';
    }
}
