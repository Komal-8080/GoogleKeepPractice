package com.bridgelabz.googlekeep.Notes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.UUID;

@Getter
@Setter
@ToString
public class ColourDTO {

    @NotNull(message = "NoteId Cannot be Empty")
    private UUID noteId;

    @Pattern(regexp = "White|Red|Yellow|Blue|Green|Purple|Pink", message = "you can add only this colors white,red,yellow,blue,green,purple,pink")
    private String colour = "White";

}
