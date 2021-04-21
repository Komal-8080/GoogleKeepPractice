package com.bridgelabz.googlekeep.Notes.dto;

import com.bridgelabz.googlekeep.Notes.model.Collaborator;
import com.bridgelabz.googlekeep.Notes.model.UserNotes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public class NoteSummary {
    private String noteId;
    private String id;
    private String title;
    private String description;
    private LocalDateTime notesCreatedOn;
    private LocalDateTime noteEditedOn;
    private String colour;
    private boolean pin;
    private boolean trash;
    private boolean archive;
    private List<Collaborator> collaborator;
    private List<String> image;
    private UUID labelId;
    private String labelName;
    private List<LocalDateTime> reminders;

    public NoteSummary(UserNotes userNotes) {
        this.noteId = userNotes.getNoteId().toString();
        this.id = userNotes.getId().toString();
        this.title = userNotes.getTitle();
        this.description = userNotes.getDescription();
        this.notesCreatedOn = userNotes.getNotesCreatedOn();
        this.noteEditedOn = userNotes.getNoteEditedOn();
        this.colour = userNotes.getColour();
        this.pin = userNotes.isPin();
        this.trash =userNotes.isTrash();
        this.archive = userNotes.isArchive();
        this.collaborator = userNotes.getCollaboratorList();
        this.image = userNotes.getImage();
        this.labelId = userNotes.getLabelId();
        this.labelName = userNotes.getLabelName();
        this.reminders = userNotes.getReminders();
    }

}
