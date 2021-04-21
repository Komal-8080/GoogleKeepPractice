package com.bridgelabz.googlekeep.User.controller;

import com.bridgelabz.googlekeep.Response.ResponseDTO;
import com.bridgelabz.googlekeep.Response.ResponseToken;
import com.bridgelabz.googlekeep.User.dto.EmailDTO;
import com.bridgelabz.googlekeep.User.dto.PasswordDTO;
import com.bridgelabz.googlekeep.User.dto.RegistrationDTO;
import com.bridgelabz.googlekeep.User.dto.UserDTO;
import com.bridgelabz.googlekeep.User.model.Registration;
import com.bridgelabz.googlekeep.User.repository.RegistrationRepository;
import com.bridgelabz.googlekeep.User.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    IUserService iUserService;

    @Autowired
    RegistrationRepository registrationRepository;

    @PostMapping("/registration")
    public ResponseEntity<ResponseDTO> userRegistration(@Valid @RequestBody RegistrationDTO registrationDTO) {
        Registration registration = iUserService.userRegistration(registrationDTO);
        ResponseDTO responseDTO = new ResponseDTO("Go to mail and click on link to validate email id", registration);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @GetMapping(value = "/{token}/valid")
    public ResponseEntity<ResponseDTO> emailValidation(@PathVariable String token) {
        String registration = iUserService.emailValidation(token);
        ResponseDTO responseDTO = new ResponseDTO(registration,"User Registered Successfully");
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> userLogin(@RequestBody UserDTO userDTO) {
        ResponseToken userData = iUserService.userLogin(userDTO);
        ResponseDTO responseDTO = new ResponseDTO("User Logged in sucessfully", userData);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

//    @GetMapping("/refreshToken")
//    public ResponseEntity<ResponseDTO> refreshToken(@RequestHeader UUID id) {
//        ResponseToken userData = iUserService.refreshToken(id);
//        ResponseDTO responseDTO = new ResponseDTO("Get Call Successful", userData);
//        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
//    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<ResponseDTO> forgotPassword(@Valid @RequestBody EmailDTO emailDTO) {
        String userEmail = iUserService.forgotPassword(emailDTO);
        ResponseDTO responseDTO = new ResponseDTO("Go to mail and click on link to reset password", userEmail);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/passwordReset/{token}")
    public ResponseEntity<ResponseDTO> passwordReset(@PathVariable String token,@Valid @RequestBody PasswordDTO passwordDTO) {
        String resetData = iUserService.passwordReset(token,passwordDTO);
        ResponseDTO responseDTO = new ResponseDTO("Password Reset Successfully", resetData);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/uploadProfileImage")
    public ResponseEntity<ResponseDTO> uploadProfileImage(@RequestHeader String token , @RequestParam MultipartFile image) throws IOException {
        String response = iUserService.uploadProfileImage(token, image);
        ResponseDTO responseDTO = new ResponseDTO("Profile Image Uploading..", response);
        return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);
    }

    @PostMapping("/getProfileImage")
    public ResponseEntity<ResponseDTO> getProfileImage(@RequestHeader String token ) {
        String response = iUserService.getProfileImage(token);
        ResponseDTO responseDTO = new ResponseDTO("image uploaded Successfully", response);
        return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);
    }

    @DeleteMapping("/removeProfileImage")
    public ResponseEntity<ResponseDTO> removeProfileImage(@RequestHeader String token) {
        iUserService.removeProfileImage(token);
        ResponseDTO responseDTO = new ResponseDTO("Removing profile Image","image removed Successfully");
        return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);
    }
}
