package com.bridgelabz.googlekeep.User.repository;

import com.bridgelabz.googlekeep.User.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    Optional<Registration> findByEmailId(String emailId);

    Optional<Registration> findById(UUID id);
}
