package com.bridgelabz.googlekeep.exceptions;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.bridgelabz.googlekeep.Response.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailParseException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandlerForAll {

    public static final String message = "Exception while processing REST Request";

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        ResponseDTO responseDTO = new ResponseDTO(message, "Invalid Format entered");
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<ObjectError> errorList = exception.getBindingResult().getAllErrors();
        List<String> errMesg = errorList.stream()
                .map(objErr -> objErr.getDefaultMessage())
                .collect(Collectors.toList());
        ResponseDTO responseDTO = new ResponseDTO(message, errMesg);
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SignatureVerificationException.class)
    public ResponseEntity<ResponseDTO> handleSignatureVerificationException(SignatureVerificationException exception) {
        ResponseDTO responseDTO = new ResponseDTO(message, "Invalid Token");
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JWTDecodeException.class)
    public ResponseEntity<ResponseDTO> handleJWTDecodeException(JWTDecodeException exception) {
        ResponseDTO responseDTO = new ResponseDTO(message, "Invalid Token");
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ResponseDTO> handleTokenExpiredException(TokenExpiredException exception) {
        ResponseDTO responseDTO = new ResponseDTO(message, "Time Out Login Again to continue");
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseDTO> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception) {
        ResponseDTO responseDTO = new ResponseDTO(message, "Image size too large to store");
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ResponseDTO> handleUserException(UserException exception) {
        ResponseDTO responseDTO = new ResponseDTO(message,exception.getMessage());
        return  new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoteException.class)
    public ResponseEntity<ResponseDTO> handleNoteException(NoteException exception) {
        ResponseDTO responseDTO = new ResponseDTO(message,exception.getMessage());
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LabelException.class)
    public ResponseEntity<ResponseDTO> handleLabelException(LabelException exception) {
        ResponseDTO responseDTO = new ResponseDTO(message,exception.getMessage());
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MailParseException.class)
    public ResponseEntity<ResponseDTO> handleMailParseException(MailParseException exception) {
        ResponseDTO responseDTO = new ResponseDTO(message,"Invalid Email Id");
        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.BAD_REQUEST);
    }
}
