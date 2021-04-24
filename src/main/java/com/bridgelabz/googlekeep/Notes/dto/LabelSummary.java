package com.bridgelabz.googlekeep.Notes.dto;

import com.bridgelabz.googlekeep.Notes.model.Label;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class LabelSummary {

    private String labelId;
    private String labelName;
    private LocalDateTime labelCreatedOn;
    private LocalDateTime labelEditedOn;

    public LabelSummary(Label userLabels) {
        this.labelId = userLabels.getLabelId().toString();
        this.labelName = userLabels.getLabelName();
        this.labelCreatedOn = userLabels.getLabelCreatedOn();
        this.labelEditedOn = userLabels.getLabelEditedOn();
    }
}
