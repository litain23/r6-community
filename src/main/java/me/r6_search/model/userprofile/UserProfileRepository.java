package me.r6_search.model.userprofile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUsername(String username);
    UserProfile findByUsernameAndEmailAuthenticateCode(String username, String emailCode);
}
