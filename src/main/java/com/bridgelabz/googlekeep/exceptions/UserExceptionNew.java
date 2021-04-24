package com.bridgelabz.googlekeep.exceptions;

public class UserExceptionNew extends RuntimeException {

    public ExceptionTypes exceptionTypes;

    public UserExceptionNew(ExceptionTypes exceptionTypes){
        this.exceptionTypes = exceptionTypes;
    }

    public enum ExceptionTypes {
        USER_EXISTS("User Already Registered"),
        INVALID_EMAIL("InValid EmailId Entered"),
        EMAIL_NOT_FOUND("User Email Id Not Found"),
        USER_NOT_FOUND("User Not Found"),
        INVAlID_TOKEN("User Token Is Invalid"),
        INVALID_USERID_OR_PASSWORD("UserID or Password is Invalid"),
        PASSWORD_DOES_NOT_MATCH("New password and Confirm password does not match"),
        UNABLE_TO_READ_FILE("Unable to Read File");
        public String errorMessage;

        ExceptionTypes(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}
