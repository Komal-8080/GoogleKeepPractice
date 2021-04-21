package com.bridgelabz.googlekeep.User.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
public class PasswordDTO {

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}", message = "Password must be 8 characters,one uppercase,one lowercase,atleast one number and one special character")
    private String newPassword;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}", message = "Password must be 8 characters,one uppercase,one lowercase,atleast one number and one special character")
    private String confirmPassword;
}
