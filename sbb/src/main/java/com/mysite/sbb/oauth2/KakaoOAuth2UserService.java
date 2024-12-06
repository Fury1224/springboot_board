package com.mysite.sbb.oauth2;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoOAuth2UserService extends DefaultOAuth2UserService {
	private final UserRepository userRepository;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		Map<String, Object> attributes = oAuth2User.getAttributes();
		KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);
		String nickname = kakaoUserInfo.getNickname();
		String profileImage = kakaoUserInfo.getProfileImage();
		Optional<SiteUser> user = userRepository.findByUsername(nickname + "_kakao");
		SiteUser siteUser;
		if (user.isPresent()) {
			siteUser = user.get();
		}
		else {
			siteUser = new SiteUser();
			siteUser.setUsername(nickname + "_kakao");
			siteUser.setProvider("kakao");
			siteUser.setSocialId(kakaoUserInfo.getId());
			userRepository.save(siteUser);
		}
		return new KakaoOAuth2User(
				oAuth2User,
				siteUser.getUsername(),
				profileImage,
				userRequest.getAccessToken().getTokenValue(),
				"kakao");
	}
}
