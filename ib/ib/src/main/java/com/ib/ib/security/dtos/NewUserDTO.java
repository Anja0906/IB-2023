package com.ib.ib.security.dtos;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class NewUserDTO {
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Field email format is not valid!")
    private String email;
    private String firstName;
    private String lastName;
    @NotEmpty(message = "Field phone is required!")
    private String telephoneNumber;
    @NotEmpty(message = "Field password is required!")
    private String password;
    @NotEmpty(message = "Field passwordConfirmation is required!")
    private String passwordConfirmation;


    public NewUserDTO() {}
    public NewUserDTO(String email, String firstName, String lastName, String telephoneNumber, String password, String passwordConfirmation) {
        this.email                = email;
        this.firstName            = firstName;
        this.lastName             = lastName;
        this.telephoneNumber      = telephoneNumber;
        this.password             = password;
        this.passwordConfirmation = passwordConfirmation;
    }

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}

    public String getLastName() {return lastName;}
    public void setLastName(String lastName) {this.lastName = lastName;}

    public String getTelephoneNumber() {return telephoneNumber;}
    public void setTelephoneNumber(String telephoneNumber) {this.telephoneNumber = telephoneNumber;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getPasswordConfirmation() {return passwordConfirmation;}
    public void setPasswordConfirmation(String passwordConfirmation) {this.passwordConfirmation = passwordConfirmation;}
}
