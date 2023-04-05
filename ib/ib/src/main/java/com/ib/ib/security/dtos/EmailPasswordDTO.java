package com.ib.ib.security.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class EmailPasswordDTO {
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Field email format is not valid!")
    private String email;
    @NotEmpty(message = "Field password is required!")
    private String password;

    public EmailPasswordDTO() {}
    public EmailPasswordDTO(String email, String password) {
        this.email      = email;
        this.password   = password;
    }

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
}
