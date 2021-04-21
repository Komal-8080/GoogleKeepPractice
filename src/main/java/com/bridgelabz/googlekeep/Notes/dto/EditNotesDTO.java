package com.bridgelabz.googlekeep.Notes.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Getter
@Setter
@ToString
public class EditNotesDTO {

    @NotBlank(message = "NoteId Cannot be Empty")
    private UUID noteId;

    @NotBlank(message = "Note Title Cannot be Empty")
    private String title;

    @NotBlank(message = "Note Description Cannot be Empty")
    private String description;
}
