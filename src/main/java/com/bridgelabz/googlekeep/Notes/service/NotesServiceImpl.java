package com.bridgelabz.googlekeep.Notes.service;


import com.bridgelabz.googlekeep.Notes.dto.*;
import com.bridgelabz.googlekeep.Notes.model.Collaborator;
import com.bridgelabz.googlekeep.Notes.model.UserNotes;
import com.bridgelabz.googlekeep.Notes.repository.CollaboratorRepository;
import com.bridgelabz.googlekeep.Notes.repository.NotesRepository;
import com.bridgelabz.googlekeep.User.model.Registration;
import com.bridgelabz.googlekeep.User.repository.RegistrationRepository;
import com.bridgelabz.googlekeep.User.service.EmailService;
import com.bridgelabz.googlekeep.exceptions.NoteException;
import com.bridgelabz.googlekeep.utility.FileUploadUtil;
import com.bridgelabz.googlekeep.utility.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotesServiceImpl implements INotesService {

    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    NotesRepository notesRepository;

    @Autowired
    CollaboratorRepository collaboratorRepository;

    @Autowired
    TokenUtil tokenUtil;

    @Autowired
    EmailService emailService;

    @Override
    public UserNotes createNote(String token, NotesDTO notesDTO) {
        UUID id = UUID.fromString((tokenUtil.decodeToken(token)));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        if (isUserExists.isPresent()) {
            UserNotes userNotes = new UserNotes(notesDTO);
            userNotes.setId(id);
            userNotes.setNotesCreatedOn(LocalDateTime.now());
            UserNotes save = notesRepository.save(userNotes);
            return save;
        } else
            throw new NoteException("User Not Found");
    }

    @Override
    public List<NoteSummary> getNotes(String token, String notes) {
        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        if (isUserExists.isPresent()) {
            if (notes.equalsIgnoreCase("all")) {
                return notesRepository.findAll().stream().map(userNotes -> new NoteSummary(userNotes)).collect(Collectors.toList());
            }
            Optional<UserNotes> byNoteId = notesRepository.findByNoteId(UUID.fromString(notes));
            if (byNoteId.isPresent()){
                return Collections.singletonList(new NoteSummary(byNoteId.get()));
            }
            throw new NoteException("Notes Not Found");
        }
        throw new NoteException("User Not Found");
    }

    @Override
    public UserNotes editNote(String token, EditNotesDTO editNotesDTO) {
        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        Optional<UserNotes> userNotes = notesRepository.findByNoteId(editNotesDTO.getNoteId());
        if (userNotes.isPresent()) {
            userNotes.get().updateNotes(editNotesDTO);
            userNotes.get().setNoteEditedOn(LocalDateTime.now());
        }
            return notesRepository.save(userNotes.get());
    }

    @Override
    public String moveNoteToTrash(String token, UUID noteId) {
        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        Optional<UserNotes> userNotes = notesRepository.findByNoteId(noteId);
        if (isUserExists.isPresent() && userNotes.isPresent()) {
            if(!userNotes.get().isTrash() && !userNotes.get().isPin() && !userNotes.get().isArchive()) {
                userNotes.get().setTrash(true);
                userNotes.get().setNoteEditedOn(LocalDateTime.now());
                notesRepository.save(userNotes.get());
                return "Notes Moved To Trash";
            }
           else if (userNotes.get().isTrash()) {
                userNotes.get().setTrash(false);
                userNotes.get().setNoteEditedOn(LocalDateTime.now());
                notesRepository.save(userNotes.get());
                return "Notes restored back to its location";
            }
            else throw new NoteException("Note is pinned or archived,Undo it to move to Trash");
        }
        throw new NoteException("Notes Not Found");
    }

    @Override
    public void removeNotesByNoteId(String token, UUID noteId) {
        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        Optional<UserNotes> userNotes = notesRepository.findByNoteId(noteId);
        if (isUserExists.isPresent() && userNotes.isPresent()) {
            if(!userNotes.get().isPin() || !userNotes.get().isArchive()) {
                notesRepository.delete(userNotes.get());
            }
            else throw new NoteException("Note is pinned or archived,Undo it to delete");
        }
    }

    @Override
    public void addImageToNotes(String token, UUID noteId, MultipartFile image) throws IOException {
        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        Optional<UserNotes> userNotes = notesRepository.findByNoteId(noteId);
        if (isUserExists.isPresent() && userNotes.isPresent()) {
            String fileName = StringUtils.cleanPath(image.getOriginalFilename());

            userNotes.get().setImage(fileName);
            UserNotes saveImage = notesRepository.save(userNotes.get());
            System.out.println("FileName: "+fileName);
            String uploadDir = "user-photos/" + saveImage.getNoteId();
            System.out.println("FileName: "+uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, image);
        }
    }

    @Override
    public void removeImageFromNotes(String token, UUID noteId) {
        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        Optional<UserNotes> userNotes = notesRepository.findByNoteId(noteId);
        if (isUserExists.isPresent() && userNotes.isPresent()) {
            userNotes.get().setImage(null);
            notesRepository.save(userNotes.get());
        }
    }

    @Override
    public String pinNote(String token, UUID noteId) {
        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        Optional<UserNotes> userNotes = notesRepository.findByNoteId(noteId);
        if (isUserExists.isPresent() && userNotes.isPresent()) {
            if (!userNotes.get().isPin()) {
                userNotes.get().setPin(true);
                userNotes.get().setNoteEditedOn(LocalDateTime.now());
                notesRepository.save(userNotes.get());
                return "Notes Pinned Successfully";
            }else {
                userNotes.get().setPin(false);
                userNotes.get().setNoteEditedOn(LocalDateTime.now());
                notesRepository.save(userNotes.get());
                return "Notes UnPinned Successfully";
            }
        }
        throw new NoteException("Notes Not Found");
    }

    @Override
    public String archiveNote(String token, UUID noteId) {
        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        Optional<UserNotes> userNotes = notesRepository.findByNoteId(noteId);
        if (isUserExists.isPresent() && userNotes.isPresent()) {
            if (!userNotes.get().isArchive()) {
                userNotes.get().setArchive(true);
                userNotes.get().setNoteEditedOn(LocalDateTime.now());
                notesRepository.save(userNotes.get());
                return "Notes Archived Successfully";
            }else {
                userNotes.get().setArchive(false);
                userNotes.get().setNoteEditedOn(LocalDateTime.now());
                notesRepository.save(userNotes.get());
                return "Notes UnArchived Successfully";
            }
        }
        throw new NoteException("Notes Not Found");
    }

    @Override
    public String addColour(String token, ColourDTO colourDTO) {
        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        Optional<UserNotes> userNotes = notesRepository.findByNoteId(colourDTO.getNoteId());
        if (isUserExists.isPresent() && userNotes.isPresent()) {
            userNotes.get().setColour(colourDTO);
            userNotes.get().setNoteEditedOn(LocalDateTime.now());
            notesRepository.save(userNotes.get());
            return "Colour Changed Successfully";
        }
        throw new NoteException("Notes Not Found");
    }

    @Override
    public String addCollaboratorToNotes(String token, CollaboratorDTO collaboratorDTO) {
        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        Optional<UserNotes> userNotes = notesRepository.findByNoteId(collaboratorDTO.getNoteId());
        if (isUserExists.isPresent() && userNotes.isPresent()) {
            if ( userNotes.get().isTrash()) {
                return "This Note is Trashed restore back to its location and try again";
            }else {
                Collaborator collaborator = new Collaborator(collaboratorDTO);
                userNotes.get().setNoteEditedOn(LocalDateTime.now());
                userNotes.get().getCollaboratorList().add(collaborator);
                collaboratorRepository.save(collaborator);
                notesRepository.save(userNotes.get());
                emailService.sendMail(collaboratorDTO.getEmailId(), "Note shared with you: " + userNotes.get().getTitle(), isUserExists.get().getFirstName() + "(" + isUserExists.get().getEmailId() + ") Shared a note with you" + "\n" + "Notes Title: " + userNotes.get().getTitle() + "\n" + "Notes Description: " + userNotes.get().getDescription());
                return "Notes Collaborated Successfully";
            }
        } throw new NoteException("Notes Not Found");
    }

    @Override
    public List<Collaborator> getCollaboratorsForNotes(String token, UUID noteId) {
        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        Optional<UserNotes> byNoteId = notesRepository.findByNoteId(noteId);
        if (isUserExists.isPresent() && byNoteId.isPresent()) {
            return byNoteId.get().getCollaboratorList();
        }
        throw new NoteException("Collaborators Not Yet Added For this Notes");
    }

    @Override
    public String deleteCollaboratorFromNotes(String token,UUID noteId,UUID collaboratorId) {
        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        Optional<UserNotes> byNoteId = notesRepository.findByNoteId(noteId);
        if (isUserExists.isPresent() && byNoteId.isPresent()) {
            Optional<Collaborator> byCollaboratorId = collaboratorRepository.findByCollaboratorId(collaboratorId);
            if (byCollaboratorId.isPresent()) {
                byNoteId.get().getCollaboratorList().remove(byCollaboratorId.get());
                notesRepository.save(byNoteId.get());
                return "Collaborator deleted Successfully";
            }throw new NoteException("Collaborator Not Found");
        }throw new NoteException("Notes Not Found");
    }

    @Override
    public List<NoteSummary> getAllTrashedNotes(String token) {
        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        if (isUserExists.isPresent()) {
            List<NoteSummary> noteSummaryList = notesRepository.findAll().stream().map(userNotes -> new NoteSummary(userNotes)).collect(Collectors.toList());
            List<NoteSummary> trashedNotes = noteSummaryList.stream().filter(userNotes -> (userNotes.isTrash() == true)).collect(Collectors.toList());
            if (trashedNotes.isEmpty())
                throw new NoteException("Nothing to see in Trash");
            else return trashedNotes;
        }
        throw new NoteException("User Not Found");
    }

    @Override
    public List<NoteSummary> getAllPinnedNotes(String token) {
        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        if (isUserExists.isPresent()) {
            List<NoteSummary> noteSummaryList = notesRepository.findAll().stream().map(userNotes -> new NoteSummary(userNotes)).collect(Collectors.toList());
            List<NoteSummary> pinnedNotes = noteSummaryList.stream().filter(userNotes -> (userNotes.isPin() == true && userNotes.isTrash() == false)).collect(Collectors.toList());
            if (pinnedNotes.isEmpty())
                throw new NoteException("Nothing to see in Pinned Notes");
            else return pinnedNotes;
        }
        throw new NoteException("User Not Found");
    }

    @Override
    public List<NoteSummary> getAllArchivedNotes(String token) {
        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        if (isUserExists.isPresent()) {
            List<NoteSummary> noteSummaryList = notesRepository.findAll().stream().map(userNotes -> new NoteSummary(userNotes)).collect(Collectors.toList());
            List<NoteSummary> archivedNotes = noteSummaryList.stream().filter(userNotes -> (userNotes.isArchive() == true && userNotes.isTrash() == false)).collect(Collectors.toList());
            if (archivedNotes.isEmpty())
                throw new NoteException("Nothing to see in Archived Notes");
            else return archivedNotes;
        }
        throw new NoteException("User Not Found");
    }


}
