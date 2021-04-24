package com.bridgelabz.googlekeep.exceptions;

public class LabelException extends RuntimeException {

    public LabelException.ExceptionTypes exceptionTypes;

    public LabelException(LabelException.ExceptionTypes exceptionTypes){
        this.exceptionTypes = exceptionTypes;
    }

    public enum ExceptionTypes {

        INVAlID_TOKEN("User Token Is Invalid"),
        LABEL_EXISTS("Label already Exists"),
        LABEL_NOT_FOUND("Label Not Found");

        public String errorMessage;

        ExceptionTypes(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}
