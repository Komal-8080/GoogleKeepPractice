package com.bridgelabz.googlekeep.Notes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class LabelDTO {

    @NotBlank(message = "LabelName Cannot be Empty")
    private String labelName;

}
