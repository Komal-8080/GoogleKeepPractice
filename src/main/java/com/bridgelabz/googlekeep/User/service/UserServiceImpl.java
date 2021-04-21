package com.bridgelabz.googlekeep.User.service;

import com.bridgelabz.googlekeep.Notes.model.UserNotes;
import com.bridgelabz.googlekeep.Response.ResponseToken;
import com.bridgelabz.googlekeep.User.dto.EmailDTO;
import com.bridgelabz.googlekeep.User.dto.PasswordDTO;
import com.bridgelabz.googlekeep.User.dto.RegistrationDTO;
import com.bridgelabz.googlekeep.User.dto.UserDTO;
import com.bridgelabz.googlekeep.User.model.Registration;
import com.bridgelabz.googlekeep.User.repository.RegistrationRepository;
import com.bridgelabz.googlekeep.exceptions.NoteException;
import com.bridgelabz.googlekeep.exceptions.UserException;
import com.bridgelabz.googlekeep.utility.FileUploadUtil;
import com.bridgelabz.googlekeep.utility.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    TokenUtil tokenUtil;

    @Override
    public Registration userRegistration(RegistrationDTO registrationDTO) {
        Optional<Registration> userEmailId = registrationRepository.findByEmailId(registrationDTO.getEmailId());
        if (userEmailId.isPresent()) {
            throw new UserException("User Already Registered");
        }
        else {
            Registration registration = new Registration(registrationDTO);
            registration.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
            UUID id = registration.getId();
            String emailId = registration.getEmailId();
            String url = emailService.getUrl(id)+"/valid";
            emailService.sendMail(emailId,"Email Validation","Dear "+registration.getFirstName()+"\n"+"Kindly,Click on the below link to validate email id"+"\n"+url);
            if (registration.isVerify()) {
                Registration saveUser = registrationRepository.save(registration);
                return saveUser;
            }throw new UserException("InValid EmailId");
        }
    }

    @Override
     public String emailValidation(String token) {
        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        if (isUserExists.isPresent()){
            isUserExists.get().setVerify(true);
            registrationRepository.save(isUserExists.get());
            return "Validation Successful";
        }
        else
            throw new UserException("User Not Found");
    }

    @Override
    public ResponseToken userLogin(UserDTO userDTO) {
        Optional<Registration> userData = registrationRepository.findByEmailId(userDTO.getUserId());
        if (userData.isPresent()) {
            return authentication(userData,userDTO.getPassword());
        }
        throw new UserException("Invalid UserId Or Password");
    }

    private ResponseToken authentication(Optional<Registration> userDTO, String password) {
        ResponseToken response = new ResponseToken();
        boolean status = passwordEncoder.matches(password, userDTO.get().getPassword());
        if (status == true) {
            String token = tokenUtil.createToken(userDTO.get().getId());
            response.setToken(token);
            response.setStatusCode(200);
            response.setStatusMessage("User Logged In Successfully");
            return response;
        }
        throw new UserException("Invalid UserId Or Password");
    }

//    @Override
//    public ResponseToken refreshToken(UUID id) {
////        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
//        Optional<Registration> isUserExists = registrationRepository.findById(id);
//        if (isUserExists.isPresent()) {
//            ResponseToken response = new ResponseToken();
//            String refreshToken = tokenUtil.createToken(id);
//            response.setToken(refreshToken);
//            response.setStatusCode(200);
//            response.setStatusMessage("new token generated");
//            return response;
//        }
//        throw new UserException("Invalid Token");
//    }

    @Override
    public String forgotPassword(EmailDTO emailDTO) {
        Optional<Registration> userEmailId = registrationRepository.findByEmailId(emailDTO.getEmailId());
        if (userEmailId.isPresent()) {
            UUID id = userEmailId.get().getId();
            String emailId = emailDTO.getEmailId();
            String url = emailService.getPasswordResetUrl(id);
            emailService.sendMail(emailId,"Regarding Password Reset","Dear "+userEmailId.get().getFirstName()+"\n"+"Kindly,Click on the below link to Reset Password"+"\n"+url);
            return emailId ;
        }
        throw new UserException("EmailId Not Found");
    }

    @Override
    public String passwordReset(String token, PasswordDTO passwordDTO) {
        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        if (isUserExists.isPresent()) {
            boolean status = (passwordDTO.getNewPassword().equalsIgnoreCase(passwordDTO.getConfirmPassword()));
            if( status == true ) {
                isUserExists.get().setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
                registrationRepository.save(isUserExists.get());
                return "Password Reset Successfully";
            }else
                throw new UserException("New password and Confirm password does not match");
        } else
            throw new UserException("User Not Found");
    }

    @Override
    public String uploadProfileImage(String token, MultipartFile image) throws IOException {
        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        if (isUserExists.isPresent()) {
            String fileName = StringUtils.cleanPath(image.getOriginalFilename());
            isUserExists.get().setProfileImage(fileName);
            Registration saveImage = registrationRepository.save(isUserExists.get());
            String uploadDir = "profileImages/" + saveImage.getProfileImage();
            FileUploadUtil.saveFile(uploadDir, fileName, image);
            return "Image Uploaded Successfully";
        } throw new UserException("User Not Found");
    }

    @Override
    public String getProfileImage(String token) {
        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        if (!isUserExists.isPresent()){
            throw new UserException("User Not Found");
        }
        String profileImage = isUserExists.get().getProfileImage();
        return profileImage;
    }

    @Override
    public void removeProfileImage(String token) {
        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        if (isUserExists.isPresent()) {
            isUserExists.get().setProfileImage(null);
            registrationRepository.save(isUserExists.get());
        }
    }

}
