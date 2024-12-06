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
public class customOAuth2UserService extends DefaultOAuth2UserService  {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String username = "";
        String profileImage = "";
        Long kakaoId = null;

        // 카카오 로그인인 경우
        if ("kakao".equals(provider)) {
        	KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);
        	kakaoId = kakaoUserInfo.getId();
        	username = kakaoUserInfo.getNickname();
            profileImage = kakaoUserInfo.getProfileImage();
        }

        Optional<SiteUser> user = userRepository.findByUsername(username + "_" + provider);
        SiteUser siteUser;
        if (user.isPresent()) {
            siteUser = user.get();
        } else {
            siteUser = new SiteUser();
            siteUser.setUsername(username + "_" + provider);
            siteUser.setProvider(provider);
            siteUser.setSocialId(kakaoId);
            userRepository.save(siteUser);
        }

        return new CustomOAuth2User(oAuth2User, siteUser.getUsername(), profileImage,
                userRequest.getAccessToken().getTokenValue(), provider);
    }
}
