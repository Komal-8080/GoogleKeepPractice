package com.bridgelabz.googlekeep.Notes.repository;

import com.bridgelabz.googlekeep.Notes.model.UserNotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotesRepository extends JpaRepository<UserNotes,UUID> {

    Optional<UserNotes> findByNoteId(UUID noteId);
}
