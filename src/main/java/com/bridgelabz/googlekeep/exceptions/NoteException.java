package com.bridgelabz.googlekeep.exceptions;

public class NoteException extends RuntimeException {

    public NoteException.ExceptionTypes exceptionTypes;

    public NoteException(NoteException.ExceptionTypes exceptionTypes){
        this.exceptionTypes = exceptionTypes;
    }

    public enum ExceptionTypes {
        INVAlID_TOKEN("User Token Is Invalid"),
        NOTES_NOT_FOUND("Notes Not Found"),
        UNDO_PINNED_ARCHIVED_NOTES("Note is pinned or archived,Undo it to move to Trash"),
        IMAGE_NOT_FOUND("Image not Found"),
        COLLABORATOR_NOT_ADDED("Collaborators Not Yet Added For this Notes"),
        COLLABORATOR_NOT_FOUND("Collaborator Not Found"),
        EMPTY_TRASH("Nothing to see in Trash"),
        EMPTY_PIN("Nothing to see in Pinned Notes"),
        EMPTY_ARCHIVE("Nothing to see in Archived Notes"),
        INVALID_DATE("Date should be future date");

        public String errorMessage;

        ExceptionTypes(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}
