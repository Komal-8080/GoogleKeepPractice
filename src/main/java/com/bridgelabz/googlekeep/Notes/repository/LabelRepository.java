package com.bridgelabz.googlekeep.Notes.repository;

import com.bridgelabz.googlekeep.Notes.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LabelRepository extends JpaRepository<Label, UUID> {

    Optional<Label> findByLabelId(UUID fromString);

    Optional<Label> findByLabelName(String labelName);
}
