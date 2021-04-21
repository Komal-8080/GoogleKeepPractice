package com.bridgelabz.googlekeep.Notes.repository;

import com.bridgelabz.googlekeep.Notes.model.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CollaboratorRepository extends JpaRepository<Collaborator, UUID> {

    Optional<Collaborator> findByCollaboratorAndNoteId(String emailId, UUID noteId);

    List<Collaborator> findByCollaborator(String emailId);

    Optional<Collaborator> findByCollaboratorId(UUID collaboratorId);
}
