package com.bridgelabz.googlekeep.Notes.service;


import com.bridgelabz.googlekeep.Notes.dto.*;
import com.bridgelabz.googlekeep.Notes.model.Collaborator;
import com.bridgelabz.googlekeep.Notes.model.UserNotes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface INotesService {

    UserNotes createNote(String token, NotesDTO notesDTO);

    List<NoteSummary> getNotes(String token, String notes);

    UserNotes editNote(String token, EditNotesDTO editNotesDTO);

    String moveNoteToTrash(String token, UUID noteId);

    void removeNotesByNoteId(String token, UUID noteId);

    void addImageToNotes(String token, UUID noteId, MultipartFile image) throws IOException;

    void removeImageFromNotes(String token, UUID noteId);

    String pinNote(String token, UUID noteId);

    String archiveNote(String token, UUID noteId);

    String addColour(String token, ColourDTO colourDTO);

    String addCollaboratorToNotes(String token, CollaboratorDTO collaboratorDTO);

    List<NoteSummary> getAllTrashedNotes(String token);

    List<NoteSummary> getAllPinnedNotes(String token);

    List<NoteSummary> getAllArchivedNotes(String token);

    String deleteCollaboratorFromNotes(String token,UUID noteId,UUID collaboratorId);

    List<Collaborator> getCollaboratorsForNotes(String token, UUID noteId);
}
