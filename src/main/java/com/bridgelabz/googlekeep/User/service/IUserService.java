package com.bridgelabz.googlekeep.User.service;


import com.bridgelabz.googlekeep.Response.ResponseToken;
import com.bridgelabz.googlekeep.User.dto.EmailDTO;
import com.bridgelabz.googlekeep.User.dto.PasswordDTO;
import com.bridgelabz.googlekeep.User.dto.RegistrationDTO;
import com.bridgelabz.googlekeep.User.dto.UserDTO;
import com.bridgelabz.googlekeep.User.model.Registration;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IUserService {

    Registration userRegistration(RegistrationDTO registrationDTO);

    String emailValidation(String token);

    ResponseToken userLogin(UserDTO userDTO);

    String forgotPassword(EmailDTO emailDTO);

    String passwordReset(String token, PasswordDTO passwordDTO);

    String uploadProfileImage(String token, MultipartFile image) throws IOException;

    String getProfileImage(String token);

    void removeProfileImage(String token);

//    ResponseToken refreshToken(UUID id);
}
