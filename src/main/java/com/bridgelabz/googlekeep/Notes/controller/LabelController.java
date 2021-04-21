package com.bridgelabz.googlekeep.Notes.controller;

import com.bridgelabz.googlekeep.Notes.dto.EditLabelDTO;
import com.bridgelabz.googlekeep.Notes.dto.LabelDTO;
import com.bridgelabz.googlekeep.Notes.dto.LabelSummary;
import com.bridgelabz.googlekeep.Notes.dto.NotesDTO;
import com.bridgelabz.googlekeep.Notes.model.Label;
import com.bridgelabz.googlekeep.Notes.service.ILabelService;
import com.bridgelabz.googlekeep.Response.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/label")
public class LabelController {

    @Autowired
    ILabelService iLabelService;

    @PostMapping("/createLabel")
    public ResponseEntity<ResponseDTO> createLabel(@RequestHeader String token, @Valid @RequestBody LabelDTO labelDTO) {
        Label label = iLabelService.createLabel(token,labelDTO);
        ResponseDTO responseDTO = new ResponseDTO("New Label Created Successfully", label);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @GetMapping(value = {"/getLabels"})
    public ResponseEntity<ResponseDTO> getLabels(@RequestHeader String token,@RequestParam String label) {
        List<LabelSummary> labelList = iLabelService.getLabels(token,label);
        ResponseDTO responseDTO = new ResponseDTO("Get Call Successful", labelList);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/editLabelByLabelId/{labelId}")
    public ResponseEntity<ResponseDTO> editLabelByLabelId(@RequestHeader String token,@Valid @RequestBody EditLabelDTO editLabelDTO) {
        Label editedLabel = iLabelService.editLabelByLabelId(token,editLabelDTO);
        ResponseDTO responseDTO = new ResponseDTO("LabelName edited Successfully", editedLabel);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/deleteLabelByLabelId/{labelId}")
    public ResponseEntity<ResponseDTO> removeLabelByLabelId(@RequestHeader String token,@PathVariable UUID labelId) {
        iLabelService.removeLabelByLabelId(token,labelId);
        ResponseDTO responseDTO = new ResponseDTO("Get Call Successful", "Label with labelId "+labelId+" deleted");
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @PutMapping("/addNotesToLabel/{labelId}")
    public ResponseEntity<ResponseDTO> addNotesToLabel(@RequestHeader String token, @PathVariable UUID labelId, @Valid @RequestBody NotesDTO notesDTO) {
        Label addNotes = iLabelService.addNotesToLabel(token,labelId,notesDTO);
        ResponseDTO responseDTO = new ResponseDTO("Notes Added Successfully", addNotes);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @GetMapping("/getNotesByLabelId/{labelId}")
    public ResponseEntity<ResponseDTO> getNotesByLabelId(@RequestHeader String token,@PathVariable UUID labelId) {
        Label getNotes = iLabelService.getNotesByLabelId(token,labelId);
        ResponseDTO responseDTO = new ResponseDTO("Get Call Successful", getNotes );
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }
}
