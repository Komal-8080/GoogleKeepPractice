package com.bridgelabz.googlekeep.Notes.controller;

import com.bridgelabz.googlekeep.Notes.dto.*;
import com.bridgelabz.googlekeep.Notes.model.Collaborator;
import com.bridgelabz.googlekeep.Notes.model.UserNotes;
import com.bridgelabz.googlekeep.Notes.service.INotesService;
import com.bridgelabz.googlekeep.Response.Response;
import com.bridgelabz.googlekeep.Response.ResponseDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiOperation("API used to New Notes. This API is used to create new Notes\n" +
            "if a user is valid then user can create a new notes with Title and Description")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "New Note Created Successfully",
            response = Response.class
    ), @ApiResponse(
            code = 404,
            message = "Error Creating Notes",
            response = Response.class
    )})
    @PostMapping("/createNote")
    public ResponseEntity<ResponseDTO> createNote(@RequestHeader String token, @Valid @RequestBody NotesDTO notesDTO) {
        UserNotes userNotes = iNotesService.createNote(token,notesDTO);
        ResponseDTO responseDTO = new ResponseDTO("New Note Created Successfully", userNotes);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @ApiOperation("API used to Get Notes. This API is used to see List of Notes\n" +
                    "if all is passed then user can view list of notes and if a Note Id is passed then\n"+
                    "user can see single Notes Details")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Get Call Successful",
            response = Response.class
    ), @ApiResponse(
            code = 404,
            message = "Error getting Notes",
            response = Response.class
    )})
    @GetMapping(value = {"/getNotes"})
    public ResponseEntity<ResponseDTO> getNotes(@RequestHeader String token,@RequestParam String notes) {
        List<NoteSummary> userNotesList = iNotesService.getNotes(token,notes);
        ResponseDTO responseDTO = new ResponseDTO("Get Call Successful", userNotesList);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @ApiOperation("API used to Edit Notes. This API is used to Edit Notes\n" +
            "User can edit any Notes by passing NoteId")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Notes edited Successfully",
            response = Response.class
    ), @ApiResponse(
            code = 404,
            message = "Error Editing Notes",
            response = Response.class
    )})
    @PutMapping("/editNote/{noteId}")
    public ResponseEntity<ResponseDTO> editNotes(@RequestHeader String token,@Valid @RequestBody EditNotesDTO editNotesDTO) {
        UserNotes editedNotes = iNotesService.editNote(token,editNotesDTO);
        ResponseDTO responseDTO = new ResponseDTO("Notes edited Successfully", editedNotes);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @ApiOperation("API used to Trash Notes. This API is used move unwanted Notes to Trash \n" +
            "User can move notes to Trash by NoteId and also can restore back from Trash")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Notes Moved To Trash Successfully",
            response = Response.class
    ), @ApiResponse(
            code = 404,
            message = "Error moving Notes",
            response = Response.class
    )})
    @PutMapping("/moveNoteToTrash/{noteId}")
    public ResponseEntity<ResponseDTO> moveNoteToTrash(@RequestHeader String token,@PathVariable UUID noteId) {
        String response = iNotesService.moveNoteToTrash(token,noteId);
        ResponseDTO responseDTO = new ResponseDTO(response, noteId);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @ApiOperation("API used to Delete Notes. This API is used delete unwanted Notes permanently \n" +
            "User can delete notes permanently by NoteId and deleted notes cannot be restored")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Notes deleted Successfully",
            response = Response.class
    ), @ApiResponse(
            code = 404,
            message = "Error deleting Notes",
            response = Response.class
    )})
    @DeleteMapping("/deleteNote/{noteId}")
    public ResponseEntity<ResponseDTO> removeNotesByNoteId(@RequestHeader String token,@PathVariable UUID noteId) {
        iNotesService.removeNotesByNoteId(token,noteId);
        ResponseDTO responseDTO = new ResponseDTO("Get Call Successful", "Note with noteId "+noteId+" deleted");
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @ApiOperation("API used to Add Images to Notes. This API is used to add List of Images to Notes \n" +
            "User can add List of images to any Notes selecting from the device storage")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Image added Successfully",
            response = Response.class
    ), @ApiResponse(
            code = 404,
            message = "Error Reading File",
            response = Response.class
    )})
    @PostMapping("/addImageToNotes/{noteId}")
    public ResponseEntity<ResponseDTO> addImageToNotes(@RequestHeader String token,@PathVariable UUID noteId , @RequestParam MultipartFile image) throws IOException {
        iNotesService.addImageToNotes(token,noteId,image);
        ResponseDTO responseDTO = new ResponseDTO("image added Successfully",noteId);
        return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);
    }

    @ApiOperation("API used to Get Images from Notes. This API is used to see the List of Images stored in a Notes \n" +
            "User can see images in the Notes by noteId")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Get Call Successful",
            response = Response.class
    ), @ApiResponse(
            code = 404,
            message = "Error Reading File",
            response = Response.class
    )})
    @GetMapping("/getImagesFromNotes")
    public ResponseEntity<ResponseDTO> getImagesFromNotes(@RequestHeader String token,@RequestParam UUID noteId) {
        List<String> response = iNotesService.getImagesFromNotes(token,noteId);
        ResponseDTO responseDTO = new ResponseDTO("Images for noteId "+noteId,response);
        return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);
    }

    @ApiOperation("API used to Delete Image from Notes. This API is used to delete the Image stored in a Notes \n" +
            "User can delete the image in the Notes by noteId")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "image removed Successfully",
            response = Response.class
    ), @ApiResponse(
            code = 404,
            message = "Error Removing File",
            response = Response.class
    )})
    @DeleteMapping("/removeImageFromNotes/{noteId}")
    public ResponseEntity<ResponseDTO> removeImageFromNotes(@RequestHeader String token,@PathVariable UUID noteId,@RequestParam String image) {
        iNotesService.removeImageFromNotes(token,noteId,image);
        ResponseDTO responseDTO = new ResponseDTO("image removed Successfully",noteId);
        return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);
    }

    @ApiOperation("API used to Pin Notes. This API is used to Pin and Unpin Notes \n" +
            "User can Pin and Unpin Notes by NoteId ")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Notes Pinned",
            response = Response.class
    ), @ApiResponse(
            code = 404,
            message = "Error Saving Notes",
            response = Response.class
    )})
    @PutMapping("/pinNote/{noteId}")
    public ResponseEntity<ResponseDTO> pinNote(@RequestHeader String token,@PathVariable UUID noteId) {
        String response = iNotesService.pinNote(token,noteId);
        ResponseDTO responseDTO = new ResponseDTO(response,noteId);
        return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);
    }

    @ApiOperation("API used to archive Notes. This API is used to archive and Unarchive Notes \n" +
            "User can archive and Unarchive Notes by NoteId ")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Notes archived",
            response = Response.class
    ), @ApiResponse(
            code = 404,
            message = "Error Saving Notes",
            response = Response.class
    )})
    @PutMapping("/archiveNote/{noteId}")
    public ResponseEntity<ResponseDTO> archiveNote(@RequestHeader String token,@PathVariable UUID noteId) {
        String response = iNotesService.archiveNote(token,noteId);
        ResponseDTO responseDTO = new ResponseDTO(response, noteId);
        return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);
    }

    @ApiOperation("API used to Add Colour. This API is used to add Colour to the Notes \n" +
            "User can add Colour to the notes by noteId and Default colour to any notes will be White ")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Colour Added to Notes",
            response = Response.class
    ), @ApiResponse(
            code = 404,
            message = "Error Adding Colour",
            response = Response.class
    )})
    @PutMapping("/addColour")
    public ResponseEntity<ResponseDTO> addColour(@RequestHeader String token, @Valid @RequestBody ColourDTO colourDTO) {
        String response = iNotesService.addColour(token,colourDTO);
        ResponseDTO responseDTO = new ResponseDTO(response, colourDTO);
        return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);
    }

    @ApiOperation("API used to Add Collaborator. This API is used to add Collaborator to the Notes \n" +
            "User can collaborate and share the notes with anyone on email")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Notes Collaborated Successfully",
            response = Response.class
    ), @ApiResponse(
            code = 404,
            message = "Error While Collaborating Notes",
            response = Response.class
    )})
    @PutMapping("/addCollaboratorToNotes")
    public ResponseEntity<ResponseDTO> addCollaboratorToNotes(@RequestHeader String token,@Valid @RequestBody CollaboratorDTO collaboratorDTO) {
        String response = iNotesService.addCollaboratorToNotes(token, collaboratorDTO);
        ResponseDTO responseDTO = new ResponseDTO(response, collaboratorDTO);
        return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);
    }

    @ApiOperation("API used to get Collaborators. This API is used to get List of Collaborators\n" +
            "User can view thw list of collaborators for a particular notes by noteId")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Get Call Successful",
            response = Response.class
    ), @ApiResponse(
            code = 404,
            message = "Error While Getting Collaborators",
            response = Response.class
    )})
    @GetMapping(value={"/getCollaboratorsForNotes/{noteId}"})
    public ResponseEntity<ResponseDTO> getCollaboratorsForNotes(@RequestHeader String token,@PathVariable UUID noteId) {
        List<Collaborator> collaboratorList = iNotesService.getCollaboratorsForNotes(token,noteId);
        ResponseDTO responseDTO = new ResponseDTO("Collaborator for the Note Id: "+noteId, collaboratorList);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @ApiOperation("API used to remove Collaborators. This API is used to remove Collaborators\n" +
            "User can remove collaborator from a particular notes by noteId")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Collaborator removed Successfully",
            response = Response.class
    ), @ApiResponse(
            code = 404,
            message = "Error Removing Collaborator",
            response = Response.class
    )})
    @DeleteMapping("/deleteCollaboratorFromNotes")
    public ResponseEntity<ResponseDTO> deleteCollaboratorFromNotes(@RequestHeader String token,@RequestParam UUID noteId,@RequestParam UUID collaboratorId) {
        String response = iNotesService.deleteCollaboratorFromNotes(token,noteId,collaboratorId);
        ResponseDTO responseDTO = new ResponseDTO(response,collaboratorId);
        return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);
    }

    @ApiOperation("API used to Get Trashed Notes. This API is used to View Notes in the Trash\n" +
            "User can view List of Trashed notes")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Get Call Successful",
            response = Response.class
    ), @ApiResponse(
            code = 404,
            message = "Error Getting Trashed Notes",
            response = Response.class
    )})
    @GetMapping(value={"/getAllTrashedNotes"})
    public ResponseEntity<ResponseDTO> getAllTrashedNotes(@RequestHeader String token) {
        List<NoteSummary> userNotesList = iNotesService.getAllTrashedNotes(token);
        ResponseDTO responseDTO = new ResponseDTO("Get Call Successful", userNotesList);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @ApiOperation("API used to Get Pinned Notes. This API is used to View Pinned Notes\n" +
            "User can view List of Pinned notes")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Get Call Successful",
            response = Response.class
    ), @ApiResponse(
            code = 404,
            message = "Error Getting Pinned Notes",
            response = Response.class
    )})
    @GetMapping(value={"/getAllPinnedNotes"})
    public ResponseEntity<ResponseDTO> getAllPinnedNotes(@RequestHeader String token) {
        List<NoteSummary> userNotesList = iNotesService.getAllPinnedNotes(token);
        ResponseDTO responseDTO = new ResponseDTO("Get Call Successful", userNotesList);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @ApiOperation("API used to Get Archived Notes. This API is used to View Archived Notes\n" +
            "User can view List of Archived notes")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Get Call Successful",
            response = Response.class
    ), @ApiResponse(
            code = 404,
            message = "Error Getting Archived Notes",
            response = Response.class
    )})
    @GetMapping(value={"/getAllArchivedNotes"})
    public ResponseEntity<ResponseDTO> getAllArchivedNotes   (@RequestHeader String token) {
        List<NoteSummary> userNotesList = iNotesService.getAllArchivedNotes(token);
        ResponseDTO responseDTO = new ResponseDTO("Get Call Successful", userNotesList);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @ApiOperation("API used to Set Reminder. This API is used to set reminder and create new Notes\n" +
            "User can set future date and time to reminder to add new notes ")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Reminder Set Successfully",
            response = Response.class
    ), @ApiResponse(
            code = 404,
            message = "Error Setting Reminder",
            response = Response.class
    )})
    @PostMapping("/setReminderToNewNote")
    public ResponseEntity<ResponseDTO> setReminderToNewNote(@RequestHeader String token,@RequestParam String setReminder,@RequestBody NotesDTO notesDTO) {
        UserNotes editedNotes = iNotesService.setReminderToNewNote(token,setReminder,notesDTO);
        ResponseDTO responseDTO = new ResponseDTO("Reminder Set Successfully to New Notes", editedNotes);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @ApiOperation("API used to Set Reminder. This API is used to set reminder to the Notes\n" +
            "User can set future date and time to reminder to any notes by noteId ")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Reminder Set Successfully",
            response = Response.class
    ), @ApiResponse(
            code = 404,
            message = "Error Setting Reminder",
            response = Response.class
    )})
    @PutMapping("/setReminderToExistingNote/{noteId}")
    public ResponseEntity<ResponseDTO> setReminderToExistingNote(@RequestHeader String token,@PathVariable UUID noteId,@RequestParam String setReminder) {
        UserNotes editedNotes = iNotesService.setReminderToExistingNote(token,noteId,setReminder);
        ResponseDTO responseDTO = new ResponseDTO("Reminder Set Successfully to "+noteId, editedNotes);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @ApiOperation("API used to Remove Reminder. This API is used to remove reminder from Notes\n" +
            "User can remove set reminder from any notes by noteId ")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Reminder removed Successfully",
            response = Response.class
    ), @ApiResponse(
            code = 404,
            message = "Error removing Reminder",
            response = Response.class
    )})
    @DeleteMapping("/deleteReminderFromNotes/{noteId}")
    public ResponseEntity<ResponseDTO> deleteReminderFromNotes(@RequestHeader String token,@PathVariable UUID noteId) {
        iNotesService.deleteReminderFromNotes(token,noteId);
        ResponseDTO responseDTO = new ResponseDTO("Reminder removed Successfully",noteId);
        return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);
    }
}
