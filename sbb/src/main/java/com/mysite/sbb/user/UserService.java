package com.mysite.sbb.user;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public SiteUser create(String username, String email, String password) {
		SiteUser user = new SiteUser();
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		user.setProfileImageUrl("https://fury-spring.s3.ap-southeast-2.amazonaws.com/default_user.jpg");
		this.userRepository.save(user);
		return user;
	}
	
	public SiteUser getUser(String username) {
		Optional<SiteUser> siteUser = this.userRepository.findByUsername(username);
		if(siteUser.isPresent()) {
			return siteUser.get();
		}
		else {
			throw new DataNotFoundException("site user not exist");
		}
	}
	
	@Transactional
	public SiteUser getUserById(Long userId) {
		Optional<SiteUser> siteUser = this.userRepository.findById(userId);
		if(siteUser.isPresent()) {
			return siteUser.get();
		}
		else {
			throw new DataNotFoundException("site user not exist");
		}
	}
	
	public SiteUser update(SiteUser user, String newPassword) {
		user.setPassword(this.passwordEncoder.encode(newPassword));
		this.userRepository.save(user);
		return user;
	 }
	
	public SiteUser updateProfile(SiteUser user, String image) {
		user.setProfileImageUrl(image);
		return userRepository.save(user);
	 }

	public boolean isMatch(String rawPassword, String encodedPassword) {
		return this.passwordEncoder.matches(rawPassword, encodedPassword);
    }

	/* 기존에 프로필 이미지가 null 이었던 유저들 기본 프로필로 지정하기 위해 사용했던 코드
	@Transactional
    public void updateNullProfileImagesToDefault() {
        String defaultImageUrl = "https://fury-spring.s3.ap-southeast-2.amazonaws.com/default_user.jpg";

        // profile_image_url이 null인 사용자들 조회
        List<SiteUser> usersWithoutProfileImage = userRepository.findByProfileImageUrlIsNull();

        for (SiteUser user : usersWithoutProfileImage) {
            user.setProfileImageUrl(defaultImageUrl); // 기본값 설정
        }

        // 변경된 사용자 저장
        userRepository.saveAll(usersWithoutProfileImage);
    }
	
 	@PostConstruct
    public void updateDefaultProfileImageForExistingUsers() {
        updateNullProfileImagesToDefault();
    }
	*/
}
