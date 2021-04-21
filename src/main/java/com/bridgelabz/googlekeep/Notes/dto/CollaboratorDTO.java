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
public class CollaboratorDTO {

    @NotNull(message = "NoteId Cannot be Empty")
    private UUID noteId;

    @Pattern(regexp = "^([a-zA-a0-9\\.\\-\\+]+)@([a-zA-Z0-9\\.]{1,5})([a-zA-Z\\.]+){2,3}([a-zA-Z]{2,3})$", message = "Invalid Email Id")
    private String emailId;

}
