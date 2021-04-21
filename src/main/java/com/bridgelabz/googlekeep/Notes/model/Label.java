package com.bridgelabz.googlekeep.Notes.model;

import com.bridgelabz.googlekeep.Notes.dto.EditLabelDTO;
import com.bridgelabz.googlekeep.Notes.dto.LabelDTO;
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
@Table(name = "Label")
public @Data class Label {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    @Column(name = "labelId")
    private UUID labelId;
    private String labelName;
    private LocalDateTime labelCreatedOn;
    private LocalDateTime labelEditedOn;

    @OneToMany(cascade = CascadeType.ALL)
    private List<UserNotes> notes;

    public Label(LabelDTO labelDTO) {
        this.labelName = labelDTO.getLabelName();
    }

    public void updateLabel(EditLabelDTO editLabelDTO) {
        this.labelId = editLabelDTO.getLabelId();
        this.labelName = editLabelDTO.getLabelName();
    }
}
