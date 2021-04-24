package com.bridgelabz.googlekeep.User.service;

import com.bridgelabz.googlekeep.Response.Response;
import com.bridgelabz.googlekeep.Response.ResponseToken;
import com.bridgelabz.googlekeep.User.dto.EmailDTO;
import com.bridgelabz.googlekeep.User.dto.PasswordDTO;
import com.bridgelabz.googlekeep.User.dto.RegistrationDTO;
import com.bridgelabz.googlekeep.User.dto.UserDTO;
import com.bridgelabz.googlekeep.User.model.Registration;
import com.bridgelabz.googlekeep.User.repository.RegistrationRepository;
import com.bridgelabz.googlekeep.configuration.ApplicationConfiguration;
import com.bridgelabz.googlekeep.exceptions.UserExceptionNew;
import com.bridgelabz.googlekeep.utility.FileUploadUtil;
import com.bridgelabz.googlekeep.utility.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
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
        if (!userEmailId.isPresent()) {
            Registration registration = new Registration(registrationDTO);
            registration.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
            UUID id = registration.getId();
            String emailId = registration.getEmailId();
            String url = emailService.getUrl(id)+"/valid";
            emailService.sendMail(emailId,"Email Validation","Dear "+registration.getFirstName()+"\n"+"Kindly,Click on the below link to validate email id"+"\n"+url);
            if (registration.isVerify()) {
                Registration saveUser = registrationRepository.save(registration);
                return saveUser;
            }throw new UserExceptionNew(UserExceptionNew.ExceptionTypes.INVALID_EMAIL);
        } throw new UserExceptionNew(UserExceptionNew.ExceptionTypes.USER_EXISTS);
    }

    @Override
     public String emailValidation(String token) {
        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        if (isUserExists.isPresent()){
            isUserExists.get().setVerify(true);
            registrationRepository.save(isUserExists.get());
            return ApplicationConfiguration.getMessageAccessor().getMessage("101");
        } throw new UserExceptionNew(UserExceptionNew.ExceptionTypes.USER_NOT_FOUND);
    }

    @Override
    public ResponseToken userLogin(UserDTO userDTO) {
        Optional<Registration> userData = registrationRepository.findByEmailId(userDTO.getUserId());
        if (userData.isPresent()) {
            return authentication(userData,userDTO.getPassword());
        } throw new UserExceptionNew(UserExceptionNew.ExceptionTypes.INVALID_USERID_OR_PASSWORD);
    }

    private ResponseToken authentication(Optional<Registration> userDTO, String password) {
        ResponseToken response = new ResponseToken();
        boolean status = passwordEncoder.matches(password, userDTO.get().getPassword());
        if (status == true) {
            String token = tokenUtil.createToken(userDTO.get().getId());
            response.setToken(token);
            response.setStatusCode(200);
            response.setStatusMessage(ApplicationConfiguration.getMessageAccessor().getMessage("102"));
            return response;
        } throw new UserExceptionNew(UserExceptionNew.ExceptionTypes.INVALID_USERID_OR_PASSWORD);
    }

    @Override
    public String forgotPassword(EmailDTO emailDTO) {
        Optional<Registration> userEmailId = registrationRepository.findByEmailId(emailDTO.getEmailId());
        if (userEmailId.isPresent()) {
            UUID id = userEmailId.get().getId();
            String emailId = emailDTO.getEmailId();
            String url = emailService.getPasswordResetUrl(id);
            emailService.sendMail(emailId,"Regarding Password Reset","Dear "+userEmailId.get().getFirstName()+"\n"+"Kindly,Click on the below link to Reset Password"+"\n"+url);
            return emailId ;
        } throw new UserExceptionNew(UserExceptionNew.ExceptionTypes.EMAIL_NOT_FOUND);
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
                return ApplicationConfiguration.getMessageAccessor().getMessage("103");
            } throw new UserExceptionNew(UserExceptionNew.ExceptionTypes.PASSWORD_DOES_NOT_MATCH);
        } throw new UserExceptionNew(UserExceptionNew.ExceptionTypes.INVAlID_TOKEN);
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
            return ApplicationConfiguration.getMessageAccessor().getMessage("104");
        } throw new UserExceptionNew(UserExceptionNew.ExceptionTypes.INVAlID_TOKEN);
    }

    @Override
    public String getProfileImage(String token) {
        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        if (isUserExists.isPresent()){
            String profileImage = isUserExists.get().getProfileImage();
            return profileImage;
        } throw new UserExceptionNew(UserExceptionNew.ExceptionTypes.INVAlID_TOKEN);
    }

    @Override
    public void removeProfileImage(String token) {
        UUID id = UUID.fromString(tokenUtil.decodeToken(token));
        Optional<Registration> isUserExists = registrationRepository.findById(id);
        if (isUserExists.isPresent()) {
            isUserExists.get().setProfileImage(null);
            registrationRepository.save(isUserExists.get());
        } throw new UserExceptionNew(UserExceptionNew.ExceptionTypes.INVAlID_TOKEN);
    }

}
