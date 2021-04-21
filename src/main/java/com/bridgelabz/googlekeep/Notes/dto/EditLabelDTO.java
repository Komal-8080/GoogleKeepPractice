package com.bridgelabz.googlekeep.Notes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@ToString
public class EditLabelDTO {

    @NotNull(message = "LabelId Cannot be Empty")
    private UUID labelId;

    @NotBlank(message = "LabelName Cannot be Empty")
    private String labelName;
}
