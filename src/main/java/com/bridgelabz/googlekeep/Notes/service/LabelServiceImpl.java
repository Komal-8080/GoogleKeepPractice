package com.bridgelabz.googlekeep.Notes.service;


import com.bridgelabz.googlekeep.Notes.dto.EditLabelDTO;
import com.bridgelabz.googlekeep.Notes.dto.LabelDTO;
import com.bridgelabz.googlekeep.Notes.dto.LabelSummary;
import com.bridgelabz.googlekeep.Notes.dto.NotesDTO;
import com.bridgelabz.googlekeep.Notes.model.Label;
import com.bridgelabz.googlekeep.Notes.model.UserNotes;
import com.bridgelabz.googlekeep.Notes.repository.LabelRepository;
import com.bridgelabz.googlekeep.Notes.repository.NotesRepository;
import com.bridgelabz.googlekeep.User.model.Registration;
import com.bridgelabz.googlekeep.User.repository.RegistrationRepository;
import com.bridgelabz.googlekeep.exceptions.LabelException;
import com.bridgelabz.googlekeep.utility.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LabelServiceImpl implements ILabelService{

    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    NotesRepository notesRepository;

    @Autowired
    LabelRepository labelRepository;

    @Autowired
    TokenUtil tokenUtil;

    @Override
    public Label createLabel(String token, LabelDTO labelDTO) {
        UUID id = UUID.fromString((tokenUtil.decodeToken(token)));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        Optional<Label> byLabelName = labelRepository.findByLabelName(labelDTO.getLabelName());
        if (isUserExists.isPresent()) {
            Label label = new Label(labelDTO);
            if (!byLabelName.isPresent()) {
                label.setLabelCreatedOn(LocalDateTime.now());
                return labelRepository.save(label);
            }else throw new LabelException("Label already Exists");
        } else throw new LabelException("User Not Found");
    }

    @Override
    public List<LabelSummary> getLabels(String token, String label) {
        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        if (isUserExists.isPresent()) {
            if (label.equalsIgnoreCase("all")) {
                return labelRepository.findAll().stream().map(userLabels -> new LabelSummary(userLabels)).collect(Collectors.toList());
            }
            Optional<Label> byLabelId = labelRepository.findByLabelId(UUID.fromString(label));
            if (byLabelId.isPresent()){
                return Collections.singletonList(new LabelSummary(byLabelId.get()));
            }
            throw new LabelException("Label Not Found");
        }
        throw new LabelException("User Not Found");
    }

    @Override
    public Label editLabelByLabelId(String token, EditLabelDTO editLabelDTO) {
        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        Optional<Label> byLabelId = labelRepository.findByLabelId(editLabelDTO.getLabelId());
        if (byLabelId.isPresent()) {
            byLabelId.get().updateLabel(editLabelDTO);
            byLabelId.get().setLabelEditedOn(LocalDateTime.now());
            return labelRepository.save(byLabelId.get());
        }
        throw new LabelException("Label Not Found");
    }

    @Override
    public void removeLabelByLabelId(String token, UUID labelId) {
        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        Optional<Label> byLabelId = labelRepository.findByLabelId(labelId);
        if (isUserExists.isPresent() && byLabelId.isPresent()) {
                labelRepository.delete(byLabelId.get());
        }
    }

    @Override
    public Label addNotesToLabel(String token, UUID labelId, NotesDTO notesDTO) {
        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        Optional<Label> byLabelId = labelRepository.findByLabelId(labelId);
        if (isUserExists.isPresent() && byLabelId.isPresent()) {
            UserNotes userNotes = new UserNotes(notesDTO);
            userNotes.setId(id);
            userNotes.setNotesCreatedOn(LocalDateTime.now());
            userNotes.setLabelId(labelId);
            userNotes.setLabelName(byLabelId.get().getLabelName());
            byLabelId.get().getNotes().add(userNotes);
            notesRepository.save(userNotes);
            Label save = labelRepository.save(byLabelId.get());
            return save;
        } else throw new LabelException("Label Not Found");
    }

    @Override
    public Label getNotesByLabelId(String token, UUID labelId) {
        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        Optional<Label> byLabelId = labelRepository.findByLabelId(labelId);
        if (isUserExists.isPresent() && byLabelId.isPresent()) {
            return byLabelId.get();
        }else throw new LabelException("Label Not Found");
    }

}
