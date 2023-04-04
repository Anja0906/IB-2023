package com.ib.ib.security.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
