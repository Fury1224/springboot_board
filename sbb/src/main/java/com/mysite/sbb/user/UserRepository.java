package com.mysite.sbb.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
	Optional<SiteUser> findByUsername(String username);
	Optional<SiteUser> findById(Long userId);
	List<SiteUser> findByProfileImageUrlIsNull();
}
