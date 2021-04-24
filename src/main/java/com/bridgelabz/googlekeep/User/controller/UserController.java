package com.bridgelabz.googlekeep.User.controller;

import com.bridgelabz.googlekeep.Response.Response;
import com.bridgelabz.googlekeep.Response.ResponseDTO;
import com.bridgelabz.googlekeep.Response.ResponseToken;
import com.bridgelabz.googlekeep.User.dto.EmailDTO;
import com.bridgelabz.googlekeep.User.dto.PasswordDTO;
import com.bridgelabz.googlekeep.User.dto.RegistrationDTO;
import com.bridgelabz.googlekeep.User.dto.UserDTO;
import com.bridgelabz.googlekeep.User.model.Registration;
import com.bridgelabz.googlekeep.User.repository.RegistrationRepository;
import com.bridgelabz.googlekeep.User.service.IUserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    IUserService iUserService;

    @Autowired
    RegistrationRepository registrationRepository;

    @ApiOperation("API used to register User. This API is used to\n" +
            "register user with basic details. If user does not exist with given\n" +
            "email id then user has to validate email id by clicking on the link sent in mail")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Click on the link sent in email for validation",
            response = Response.class
    ), @ApiResponse(
            code = 404,
            message = "Invalid Data Entered",
            response = Response.class
    )})
    @PostMapping("/registration")
    public ResponseEntity<ResponseDTO> userRegistration(@Valid @RequestBody RegistrationDTO registrationDTO) {
        Registration registration = iUserService.userRegistration(registrationDTO);
        ResponseDTO responseDTO = new ResponseDTO("Go to mail and click on link to validate email id", registration);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }


    @ApiOperation("API used to Validate User and Save Details in Database. This API is used to\n" +
            "validate user with given email id. If user email id is validate then user registration will be successful")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "User Registered Successfully",
            response = Response.class
    ), @ApiResponse(
            code = 404,
            message = "Validations Error message",
            response = Response.class
    )})
    @GetMapping(value = "/{token}/valid")
    public ResponseEntity<ResponseDTO> emailValidation(@PathVariable String token) {
        String registration = iUserService.emailValidation(token);
        ResponseDTO responseDTO = new ResponseDTO(registration,"User Registered Successfully");
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }


    @ApiOperation("API Used to for User Login.This API is used for user login\n"+
                    "if userId and password are valid then user will be given a token for further operations\n"+
                    "on successful login")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "User login Successful",
            response = Response.class
    ),@ApiResponse(
            code = 404,
            message = "Invalid UserId or Password",
            response = Response.class
    )})
    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> userLogin(@RequestBody UserDTO userDTO) {
        ResponseToken userData = iUserService.userLogin(userDTO);
        ResponseDTO responseDTO = new ResponseDTO("User Logged in Successfully", userData);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }


    @ApiOperation("API Used if User ForgotPassword.This API is used if user forgot password\n"+
            "user has to provide email id on which mail will be sent and user has to click on the provided link in the mail\n"+
            "to reset password.if the email is valid then user will be diverted to password reset api")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "link for password reset",
            response = Response.class
    ),@ApiResponse(
            code = 404,
            message = "invalid data entered",
            response = Response.class
    )})
    @PostMapping("/forgotPassword")
    public ResponseEntity<ResponseDTO> forgotPassword(@Valid @RequestBody EmailDTO emailDTO) {
        String userEmail = iUserService.forgotPassword(emailDTO);
        ResponseDTO responseDTO = new ResponseDTO("Go to mail and click on link to reset password", userEmail);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }


    @ApiOperation("API Used for Password Reset.This API is used for setting password\n"+
                    "user has to enter new password and also need to confirm the same and if new password and confirm password matches\n"+
                    "then new password will be set and user can use the same password for further operations")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Password reset Successful",
            response = Response.class
    ),@ApiResponse(
            code = 404,
            message = "New Password and Confirm Password does not match",
            response = Response.class
    )})
    @PostMapping(value = "/passwordReset/{token}")
    public ResponseEntity<ResponseDTO> passwordReset(@PathVariable String token,@Valid @RequestBody PasswordDTO passwordDTO) {
        String resetData = iUserService.passwordReset(token,passwordDTO);
        ResponseDTO responseDTO = new ResponseDTO("Password Reset Successfully", resetData);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }


    @ApiOperation("API Used for Setting Profile Image.This API is used for Setting Profile Image\n"+
            "user has to select the image from system storage and the same image will be set to profile image")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Profile Image Set Successfully",
            response = Response.class
    ),@ApiResponse(
            code = 404,
            message = "Error Reading File",
            response = Response.class
    )})
    @PostMapping("/uploadProfileImage")
    public ResponseEntity<ResponseDTO> uploadProfileImage(@RequestHeader String token , @RequestParam MultipartFile image) throws IOException {
        String response = iUserService.uploadProfileImage(token, image);
        ResponseDTO responseDTO = new ResponseDTO("Profile Image Uploading..", response);
        return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);
    }


    @ApiOperation("API to See Profile Image.This API is used for getting Profile Image\n"+
            "user can see the profile image here")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Get Call Successful",
            response = Response.class
    ),@ApiResponse(
            code = 404,
            message = "Error Reading File",
            response = Response.class
    )})
    @PostMapping("/getProfileImage")
    public ResponseEntity<ResponseDTO> getProfileImage(@RequestHeader String token ) {
        String response = iUserService.getProfileImage(token);
        ResponseDTO responseDTO = new ResponseDTO("Get Call Successful", response);
        return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);
    }


    @ApiOperation("API Used for Removing Profile Image.This API is used for removing Profile Image\n"+
            "user can remove the profile image ")
    @ApiResponses({@ApiResponse(
            code = 200,
            message = "Profile Image removed Successfully",
            response = Response.class
    ),@ApiResponse(
            code = 404,
            message = "Error Removing File",
            response = Response.class
    )})
    @DeleteMapping("/removeProfileImage")
    public ResponseEntity<ResponseDTO> removeProfileImage(@RequestHeader String token) {
        iUserService.removeProfileImage(token);
        ResponseDTO responseDTO = new ResponseDTO("Removing profile Image","image removed Successfully");
        return new ResponseEntity<ResponseDTO>(responseDTO,HttpStatus.OK);
    }
}
