package com.bridgelabz.googlekeep.Notes.model;

import com.bridgelabz.googlekeep.Notes.dto.CollaboratorDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor
@Entity
@Table(name = "Notes_Collaborator")
public @Data class Collaborator {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    @Column(name = "collaborator_id")
    private UUID collaboratorId;
    private String collaborator;
    private UUID noteId;

    public Collaborator(CollaboratorDTO collaboratorDTO) {
        this.collaborator = collaboratorDTO.getEmailId();
        this.noteId = collaboratorDTO.getNoteId();
    }
}
