package com.gollajo.domain.auth.service;

import com.gollajo.domain.auth.dto.KakaoMemberResponse;
import com.gollajo.domain.auth.dto.OauthAccessTokenResponse;
import com.gollajo.domain.member.service.MemberService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Getter
@ConfigurationProperties(prefix = "oauth.kakao")
@Component
public class KakaoOAuthClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final MemberService memberService;
    private final MultiValueMap<String, String> info;

    @Autowired
    KakaoOAuthClient(MemberService memberService){
        this.memberService = memberService;
        this.info = new LinkedMultiValueMap<>();
    }

    public String getAccessToken(final String code){

        info.add("code", code);

        OauthAccessTokenResponse response = restTemplate.postForObject(
                "https://kauth.kakao.com/oauth/token",
                info,
                OauthAccessTokenResponse.class
        );

        return response.accessToken();
    }

    public KakaoMemberResponse getMemberInfo(final String accessToken){

        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        final HttpEntity<Void> request = new HttpEntity<>(headers);

        final KakaoMemberResponse response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                request,
                KakaoMemberResponse.class
        ).getBody();

        return response;
    }
}
