package com.gollajo.domain.auth.service;

import com.gollajo.domain.auth.dto.KakaoMemberResponse;
import com.gollajo.domain.auth.dto.OauthAccessTokenResponse;
import com.gollajo.domain.member.service.MemberService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Getter
//@ConfigurationProperties(prefix = "oauth.kakao")
@Component
@Slf4j
public class KakaoOAuthClient {


    private final RestTemplate restTemplate = new RestTemplate();
    private final MemberService memberService;
    private final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    private final HttpHeaders headers = new HttpHeaders();



    @Autowired
    KakaoOAuthClient(MemberService memberService){
        this.memberService = memberService;
        //TODO: 이 값 숨겨야됨.
        this.params.add("grant_type", "authorization_code");
        this.params.add("client_id", "8b4a681f5fac9ed686cd100ad58313a0");
        this.params.add("redirect_uri", "https://gollajo-fe-home.vercel.app/oauth");

        this.headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");
        this.headers.add(HttpHeaders.ACCEPT,"application/json");
    }
    public String getAccessToken(final String code){

        params.remove("code");
        params.add("code",code);

        final HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        final OauthAccessTokenResponse response = restTemplate.postForEntity(
                "https://kauth.kakao.com/oauth/token",
                httpEntity,
                OauthAccessTokenResponse.class
        ).getBody();

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
