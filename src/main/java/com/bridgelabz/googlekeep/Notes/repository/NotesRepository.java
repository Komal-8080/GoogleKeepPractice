package com.bridgelabz.googlekeep.Notes.repository;

import com.bridgelabz.googlekeep.Notes.model.UserNotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotesRepository extends JpaRepository<UserNotes,UUID> {

    Optional<UserNotes> findByNoteId(UUID noteId);

//    List<UserNotes> findByTrash();

//    List<UserNotes> findById(UUID id);

//    Optional<UserNotes> findByIdAndNoteId(UUID id, UUID noteId);



//    UserNotes deleteByNoteId(UserNotes userNotes);

//    Optional<UserNotes> findByIdAndNoteId(UUID id, UUID noteId);
}
