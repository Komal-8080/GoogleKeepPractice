package com.bridgelabz.googlekeep.Notes.service;


import com.bridgelabz.googlekeep.Notes.dto.EditLabelDTO;
import com.bridgelabz.googlekeep.Notes.dto.LabelDTO;
import com.bridgelabz.googlekeep.Notes.dto.LabelSummary;
import com.bridgelabz.googlekeep.Notes.dto.NotesDTO;
import com.bridgelabz.googlekeep.Notes.model.Label;

import java.util.List;
import java.util.UUID;

public interface ILabelService {

    Label createLabel(String token, LabelDTO labelDTO);

    List<LabelSummary> getLabels(String token, String label);

    Label editLabelByLabelId(String token, EditLabelDTO editLabelDTO);

    void removeLabelByLabelId(String token, UUID labelId);

    Label addNotesToLabel(String token, UUID labelId, NotesDTO notesDTO);

    Label getNotesByLabelId(String token, UUID labelId);
}
