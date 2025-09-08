package com.dh.goyeon.auth;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.dh.goyeon.user.UserBean;



public class KakaoAPI {
    private final String key;
    private final String loginURI;
    private final String clientSecret;
    //처음 커밋됐나?
    
    public KakaoAPI(String key, String loginURI, String clientSecret) {
        this.key = key;
        this.loginURI = loginURI;
        this.clientSecret = clientSecret;
    }
    
    public String getKey() {
        return key;
    }
    
    public String getLoginURI() {
        return loginURI;
    }
    
    
    public String getAccessToken(String code, UserBean userBean) {
		String qs = "grant_type=authorization_code"
				+ "&client_id="+key
				+ "&redirect_uri="+loginURI
				+ "&code="+ code
				+ "&client_secret="+clientSecret;
        
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);
        ResponseEntity<KaToken> response = new RestTemplate().exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, new HttpEntity<>(qs,headers), KaToken.class);
        userBean.setKRToken(response.getBody().getRefresh_token());
        return response.getBody().getAccess_token();
    }
    
    public UserBean getProfile(UserBean userBean, String accessToken) {
        // 헤더 설정
        HttpHeaders headers2 = new HttpHeaders();
        headers2.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);
        headers2.set("Authorization", "Bearer "+accessToken);

        HttpEntity<String> entity2 = new HttpEntity<>(headers2);
        ResponseEntity<KaInfo> response2 = new RestTemplate().exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST, entity2, KaInfo.class);
        // 헤더 설정
        HttpHeaders headers3 = new HttpHeaders();
        headers3.set("Authorization", "Bearer "+accessToken);
        
        userBean.setKapi(response2.getBody().getId());
        userBean.setUname(response2.getBody().getKakao_account().getName());
        userBean.setUemail(response2.getBody().getKakao_account().getEmail());

        return userBean;
    }
    
    
    
    public String getAToken(String RefreshToken) {
    	LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
    	body.add("grant_type", "refresh_token");
        body.add("client_id", key);
        body.add("refresh_token", RefreshToken);
        body.add("client_secret", clientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);
        return new RestTemplate().exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, new HttpEntity<>(body, headers), KaToken.class).getBody().getAccess_token();
    }
    
    public boolean dcconKa(String RefreshToken) {	
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+ getAToken(RefreshToken));
        return new RestTemplate().exchange("https://kapi.kakao.com/v1/user/unlink", HttpMethod.POST, new HttpEntity<>(headers), String.class).getBody().trim().startsWith("{\"id\":") ? true : false;
    }
}
