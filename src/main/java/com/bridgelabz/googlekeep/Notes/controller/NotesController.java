package com.bridgelabz.googlekeep.Notes.controller;

import com.bridgelabz.googlekeep.Notes.dto.*;
import com.bridgelabz.googlekeep.Notes.model.Collaborator;
import com.bridgelabz.googlekeep.Notes.model.UserNotes;
import com.bridgelabz.googlekeep.Notes.service.INotesService;
import com.bridgelabz.googlekeep.Response.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notes")
public class NotesController {

    @Autowired
    INotesService iNotesService;

    @PostMapping("/createNote")
    public ResponseEntity<ResponseDTO> createNote(@RequestHeader String token, @Valid @RequestBody NotesDTO notesDTO) {
        UserNotes userNotes = iNotesService.createNote(token,notesDTO);
        ResponseDTO responseDTO = new ResponseDTO("New Note Created Successfully", userNotes);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @GetMapping(value = {"/getNotes"})
    public ResponseEntity<ResponseDTO> getNotes(@RequestHeader String token,@RequestParam String notes) {
        List<NoteSummary> userNotesList = iNotesService.getNotes(token,notes);
        ResponseDTO responseDTO = new ResponseDTO("Get Call Successful", userNotesList);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/editNote/{noteId}")
    public ResponseEntity<ResponseDTO> editNotes(@RequestHeader String token,@Valid @RequestBody EditNotesDTO editNotesDTO) {
        UserNotes editedNotes = iNotesService.editNote(token,editNotesDTO);
        ResponseDTO responseDTO = new ResponseDTO("Notes edited Successfully", editedNotes);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/moveNoteToTrash/{noteId}")
    public ResponseEntity<ResponseDTO> moveNoteToTrash(@RequestHeader String token,@PathVariable UUID noteId) {
        String response = iNotesService.moveNoteToTrash(token,noteId);
        ResponseDTO responseDTO = new ResponseDTO(response, noteId);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/deleteNote/{noteId}")
    public ResponseEntity<ResponseDTO> removeNotesByNoteId(@RequestHeader String token,@PathVariable UUID noteId) {
        iNotesService.removeNotesByNoteId(token,noteId);
        ResponseDTO responseDTO = new ResponseDTO("Get Call Successful", "Note with noteId "+noteId+" deleted");
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/addImageToNotes/{noteId}")
    public ResponseEntity<ResponseDTO> addImageToNotes(@RequestHeader String token,@PathVariable UUID noteId , @RequestParam MultipartFile image) throws IOException {
        iNotesService.addImageToNotes(token,noteId,image);
        ResponseDTO responseDTO = new ResponseDTO("image added Successfully",noteId);
        return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);
    }

    @GetMapping("/getImagesFromNotes")
    public ResponseEntity<ResponseDTO> getImagesFromNotes(@RequestHeader String token,@RequestParam UUID noteId) {
        List<String> response = iNotesService.getImagesFromNotes(token,noteId);
        ResponseDTO responseDTO = new ResponseDTO("Images for noteId "+noteId,response);
        return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);
    }

    @DeleteMapping("/removeImageFromNotes/{noteId}")
    public ResponseEntity<ResponseDTO> removeImageFromNotes(@RequestHeader String token,@PathVariable UUID noteId,@RequestParam String image) {
        iNotesService.removeImageFromNotes(token,noteId,image);
        ResponseDTO responseDTO = new ResponseDTO("image removed Successfully",noteId);
        return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);
    }

    @PutMapping("/pinNote/{noteId}")
    public ResponseEntity<ResponseDTO> pinNote(@RequestHeader String token,@PathVariable UUID noteId) {
        String response = iNotesService.pinNote(token,noteId);
        ResponseDTO responseDTO = new ResponseDTO(response,noteId);
        return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);
    }

    @PutMapping("/archiveNote/{noteId}")
    public ResponseEntity<ResponseDTO> archiveNote(@RequestHeader String token,@PathVariable UUID noteId) {
        String response = iNotesService.archiveNote(token,noteId);
        ResponseDTO responseDTO = new ResponseDTO(response, noteId);
        return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);
    }

    @PutMapping("/addColour")
    public ResponseEntity<ResponseDTO> addColour(@RequestHeader String token, @Valid @RequestBody ColourDTO colourDTO) {
        String response = iNotesService.addColour(token,colourDTO);
        ResponseDTO responseDTO = new ResponseDTO(response, colourDTO);
        return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);
    }

    @PutMapping("/addCollaboratorToNotes")
    public ResponseEntity<ResponseDTO> addCollaboratorToNotes(@RequestHeader String token,@Valid @RequestBody CollaboratorDTO collaboratorDTO) {
        String response = iNotesService.addCollaboratorToNotes(token, collaboratorDTO);
        ResponseDTO responseDTO = new ResponseDTO(response, collaboratorDTO);
        return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);
    }

    @GetMapping(value={"/getCollaboratorsForNotes/{noteId}"})
    public ResponseEntity<ResponseDTO> getCollaboratorsForNotes(@RequestHeader String token,@PathVariable UUID noteId) {
        List<Collaborator> collaboratorList = iNotesService.getCollaboratorsForNotes(token,noteId);
        ResponseDTO responseDTO = new ResponseDTO("Collaborator for the Note Id: "+noteId, collaboratorList);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/deleteCollaboratorFromNotes")
    public ResponseEntity<ResponseDTO> deleteCollaboratorFromNotes(@RequestHeader String token,@RequestParam UUID noteId,@RequestParam UUID collaboratorId) {
        String response = iNotesService.deleteCollaboratorFromNotes(token,noteId,collaboratorId);
        ResponseDTO responseDTO = new ResponseDTO(response,collaboratorId);
        return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);
    }

    @GetMapping(value={"/getAllTrashedNotes"})
    public ResponseEntity<ResponseDTO> getAllTrashedNotes(@RequestHeader String token) {
        List<NoteSummary> userNotesList = iNotesService.getAllTrashedNotes(token);
        ResponseDTO responseDTO = new ResponseDTO("Get Call Successful", userNotesList);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @GetMapping(value={"/getAllPinnedNotes"})
    public ResponseEntity<ResponseDTO> getAllPinnedNotes(@RequestHeader String token) {
        List<NoteSummary> userNotesList = iNotesService.getAllPinnedNotes(token);
        ResponseDTO responseDTO = new ResponseDTO("Get Call Successful", userNotesList);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @GetMapping(value={"/getAllArchivedNotes"})
    public ResponseEntity<ResponseDTO> getAllArchivedNotes   (@RequestHeader String token) {
        List<NoteSummary> userNotesList = iNotesService.getAllArchivedNotes(token);
        ResponseDTO responseDTO = new ResponseDTO("Get Call Successful", userNotesList);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/setReminderToNewNote")
    public ResponseEntity<ResponseDTO> setReminderToNewNote(@RequestHeader String token,@RequestParam String setReminder,@RequestBody NotesDTO notesDTO) {
        UserNotes editedNotes = iNotesService.setReminderToNewNote(token,setReminder,notesDTO);
        ResponseDTO responseDTO = new ResponseDTO("Reminder Set Successfully to New Notes", editedNotes);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/setReminderToExistingNote/{noteId}")
    public ResponseEntity<ResponseDTO> setReminderToExistingNote(@RequestHeader String token,@PathVariable UUID noteId,@RequestParam String setReminder) {
        UserNotes editedNotes = iNotesService.setReminderToExistingNote(token,noteId,setReminder);
        ResponseDTO responseDTO = new ResponseDTO("Reminder Set Successfully to "+noteId, editedNotes);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/deleteReminderFromNotes/{noteId}")
    public ResponseEntity<ResponseDTO> deleteReminderFromNotes(@RequestHeader String token,@PathVariable UUID noteId) {
        iNotesService.deleteReminderFromNotes(token,noteId);
        ResponseDTO responseDTO = new ResponseDTO("Reminder removed Successfully",noteId);
        return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);
    }
}
