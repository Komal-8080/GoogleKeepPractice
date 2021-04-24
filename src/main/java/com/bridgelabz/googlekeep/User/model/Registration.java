package com.bridgelabz.googlekeep.User.model;

import com.bridgelabz.googlekeep.Notes.model.Label;
import com.bridgelabz.googlekeep.Notes.model.UserNotes;
import com.bridgelabz.googlekeep.User.dto.RegistrationDTO;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "registration_details")
public @Data class Registration {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    @Column(name = "userId")
    private UUID id;
    private String firstName;
    private String lastName;
    private String emailId;
    private String password;
    private String mobileNumber;
    private boolean isVerify;
    private LocalDateTime registrationDate = LocalDateTime.now();
    private String profileImage;

    @OneToMany(cascade = CascadeType.ALL)
    private List<UserNotes> notes;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Label> labels;

    public Registration(RegistrationDTO registrationDTO) {
        this.firstName = registrationDTO.getFirstName();
        this.lastName = registrationDTO.getLastName();
        this.emailId = registrationDTO.getEmailId();
        this.mobileNumber =registrationDTO.getMobileNumber();
        this.password = registrationDTO.getPassword();
    }

    public Registration() { }
}
