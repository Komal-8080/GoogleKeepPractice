package com.bridgelabz.googlekeep.Notes.controller;

import com.bridgelabz.googlekeep.Notes.dto.EditLabelDTO;
import com.bridgelabz.googlekeep.Notes.dto.LabelDTO;
import com.bridgelabz.googlekeep.Notes.dto.LabelSummary;
import com.bridgelabz.googlekeep.Notes.dto.NotesDTO;
import com.bridgelabz.googlekeep.Notes.model.Label;
import com.bridgelabz.googlekeep.Notes.service.ILabelService;
import com.bridgelabz.googlekeep.Response.Response;
import com.bridgelabz.googlekeep.Response.ResponseDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiOperation("API used to create label. This API is used to create new label\n" +
            "which will be unique and same label name will not be accepted ")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Label Created Successfully",
            response = Response.class
    ), @ApiResponse(
            code = 404,
            message = "Unable to Create Label",
            response = Response.class
    )})
    @PostMapping("/createLabel")
    public ResponseEntity<ResponseDTO> createLabel(@RequestHeader String token, @Valid @RequestBody LabelDTO labelDTO) {
        Label label = iLabelService.createLabel(token,labelDTO);
        ResponseDTO responseDTO = new ResponseDTO("New Label Created Successfully", label);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @ApiOperation("API used to get label list. This API is used to get label list\n" +
                    "when user enters all list of labels will be displayed. if user enters label id then\n"+
                    "user can see single label details")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Get Call Successful",
            response = Response.class
    ), @ApiResponse(
            code = 404,
            message = "Error Getting Label",
            response = Response.class
    )})
    @GetMapping(value = {"/getLabels"})
    public ResponseEntity<ResponseDTO> getLabels(@RequestHeader String token,@RequestParam String label) {
        List<LabelSummary> labelList = iLabelService.getLabels(token,label);
        ResponseDTO responseDTO = new ResponseDTO("Get Call Successful", labelList);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @ApiOperation("API used to Edit label. This API is used to edit label\n" +
            "Label can be edited and saved using label Id")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Label edited Successfully",
            response = Response.class
    ), @ApiResponse(
            code = 404,
            message = "Error Getting Label",
            response = Response.class
    )})
    @PutMapping("/editLabelByLabelId")
    public ResponseEntity<ResponseDTO> editLabelByLabelId(@RequestHeader String token,@Valid @RequestBody EditLabelDTO editLabelDTO) {
        Label editedLabel = iLabelService.editLabelByLabelId(token,editLabelDTO);
        ResponseDTO responseDTO = new ResponseDTO("LabelName edited Successfully", editedLabel);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @ApiOperation("API used to Delete label. This API is used to Delete label\n" +
            "Label can be deleted by labelId but the notes inside the label will be saved,Only label name will be removed")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Label deleted Successfully",
            response = Response.class
    ), @ApiResponse(
            code = 404,
            message = "Error deleting Label",
            response = Response.class
    )})
    @DeleteMapping("/deleteLabelByLabelId/{labelId}")
    public ResponseEntity<ResponseDTO> removeLabelByLabelId(@RequestHeader String token,@PathVariable UUID labelId) {
        iLabelService.removeLabelByLabelId(token,labelId);
        ResponseDTO responseDTO = new ResponseDTO("Get Call Successful", "Label with labelId "+labelId+" deleted");
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @ApiOperation("API used to Add Notes to label. This API is used add Notes to the label\n" +
            "For a particular label new Notes can be added by label Id and Notes will be saved inside the Label")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Notes Added To Label Successfully",
            response = Response.class
    ), @ApiResponse(
            code = 404,
            message = "Error Adding Notes",
            response = Response.class
    )})
    @PutMapping("/addNotesToLabel/{labelId}")
    public ResponseEntity<ResponseDTO> addNotesToLabel(@RequestHeader String token, @PathVariable UUID labelId, @Valid @RequestBody NotesDTO notesDTO) {
        Label addNotes = iLabelService.addNotesToLabel(token,labelId,notesDTO);
        ResponseDTO responseDTO = new ResponseDTO("Notes Added Successfully", addNotes);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @ApiOperation("API used to Get Notes By label. This API is used to see Notes by label Idl\n" +
            "if a valid Label Id is passed then the list of notes inside the label can be viewed")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Get Call Successful",
            response = Response.class
    ), @ApiResponse(
            code = 404,
            message = "Error Getting label",
            response = Response.class
    )})
    @GetMapping("/getNotesByLabelId/{labelId}")
    public ResponseEntity<ResponseDTO> getNotesByLabelId(@RequestHeader String token,@PathVariable UUID labelId) {
        Label getNotes = iLabelService.getNotesByLabelId(token,labelId);
        ResponseDTO responseDTO = new ResponseDTO("Get Call Successful", getNotes );
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }
}
