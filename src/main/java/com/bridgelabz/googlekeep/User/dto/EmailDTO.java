package com.bridgelabz.googlekeep.User.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
public class EmailDTO {

    @Pattern(regexp = "^([a-zA-a0-9\\.\\-\\+]+)@([a-zA-Z0-9\\.]{1,5})([a-zA-Z\\.]+){1,3}([a-zA-Z]{1,3})$", message = "Invalid Email Id")
    private String emailId;
}
