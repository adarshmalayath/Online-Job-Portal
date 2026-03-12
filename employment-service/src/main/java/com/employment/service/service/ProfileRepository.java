package com.employment.service.service;

import com.employment.service.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    java.util.Optional<Profile> findByJobSeekerUserId(Long userId);
}
