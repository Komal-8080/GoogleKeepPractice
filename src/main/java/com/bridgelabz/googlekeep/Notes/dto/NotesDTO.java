package com.bridgelabz.googlekeep.Notes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class NotesDTO {

    @NotBlank(message = "Note Title Cannot be Empty")
    private String title;

    @NotBlank(message = "Note Description Cannot be Empty")
    private String description;

}
