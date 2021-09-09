package com.emailrockets.demo.repository;

import com.emailrockets.demo.model.UserInformation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Qualifier("users")
@Repository
public interface UserInformationRepository extends JpaRepository<UserInformation, Long> {
        Optional<UserInformation> findByMail(String mail);
}
