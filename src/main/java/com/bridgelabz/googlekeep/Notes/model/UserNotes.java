package com.bridgelabz.googlekeep.Notes.model;


import com.bridgelabz.googlekeep.Notes.dto.ColourDTO;
import com.bridgelabz.googlekeep.Notes.dto.EditNotesDTO;
import com.bridgelabz.googlekeep.Notes.dto.NotesDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Entity
@Table(name = "user_Notes")
public @Data class UserNotes {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    @Column(name = "noteId")
    private UUID noteId;
    private UUID id;
    private String title;
    private String description;
    private LocalDateTime notesCreatedOn;
    private LocalDateTime noteEditedOn;
    private String colour = "White";
    private String image;
    private boolean pin;
    private boolean trash;
    private boolean archive;
    private UUID labelId;
    private String labelName;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Collaborator> collaboratorList;

    public void updateNotes(EditNotesDTO editNotesDTO) {
        this.noteId = editNotesDTO.getNoteId();
        this.title = editNotesDTO.getTitle();
        this.description = editNotesDTO.getDescription();
    }

    public UserNotes(NotesDTO notesDTO) {
        this.title = notesDTO.getTitle();
        this.description = notesDTO.getDescription();
    }

    public void setColour(ColourDTO colourDTO) {
        this.noteId = colourDTO.getNoteId();
        this.colour = colourDTO.getColour();
    }
}
