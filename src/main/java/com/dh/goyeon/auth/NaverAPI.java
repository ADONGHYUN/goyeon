package com.dh.goyeon.auth;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.dh.goyeon.user.UserBean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.handler.timeout.TimeoutException;


@Configuration
public class NaverAPI {
	private final WebClient webClient;
    private final String client_id;
    private final String loginURI;
    private final String clientSecret;
	
    
    public NaverAPI(WebClient.Builder webClientBuilder,
            @Value("${naver.api.client_id}") String client_id,
            @Value("${naver.api.loginURI}") String loginURI,
            @Value("${naver.api.clientSecret}") String clientSecret) {
    		this.webClient = webClientBuilder.build();  // WebClient 인스턴스 생성
    		this.client_id = client_id;
    		this.loginURI = loginURI;
    		this.clientSecret = clientSecret;
    }

    
    public String getClient_id() {
        return client_id;
    }
    public String getLoginURI() {
        return loginURI;
    }
    
    private void handleException(Exception e) {
        if (e instanceof WebClientResponseException) {
            WebClientResponseException webClientException = (WebClientResponseException) e;
            System.err.println("상태코드에러: " + webClientException.getStatusCode().value() + " - " + webClientException.getStatusText());
            System.err.println("에러 메시지: " + webClientException.getMessage());
        } else if (e instanceof TimeoutException) {
            System.err.println("시간초과로 데이터를 가져오지 못했습니다. " + e.getMessage());
        } else if (e instanceof IOException) {
            System.err.println("JSON 파싱 오류. " + e.getMessage());
        } else {
            System.err.println("로그를 확인해야 알 수 있는 에러. " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public String getAccessToken(String code, UserBean userBean) {
    	try {
	    	String Token = webClient.get()
				        .uri(uriBuilder -> uriBuilder
				        .scheme("https")
				        .host("nid.naver.com")
				        .path("/oauth2.0/token")
				        .queryParam("grant_type", "authorization_code")
				        .queryParam("client_id", client_id)
				        .queryParam("client_secret", clientSecret)
				        .queryParam("code", code)
				        .build())
				        .retrieve()
				        .bodyToMono(String.class)
				        .block();
	    	
	        if (Token != null && !Token.isEmpty()) {
	        		ObjectMapper objectMapper = new ObjectMapper();
	        		JsonNode node = objectMapper.readTree(Token);
	        		String accessToken = node.path("access_token").asText(null);
	        		String refreshToken = node.path("refresh_token").asText(null);
	        		
	        		if (accessToken == null || accessToken.isEmpty()) {
	        		    throw new RuntimeException("토큰 응답에 access_token 없음: " + Token);
	        		}
	        		if (refreshToken == null || refreshToken.isEmpty()) {
	        		    throw new RuntimeException("토큰 응답에 refresh_token 없음: " + Token);
	        		}
	        		
	        		userBean.setNRToken(refreshToken);
	        		System.out.println("리프레쉬토큰 : "+ node.path("refresh_token").asText()+"어세스토큰 : "+node.path("access_token").asText());
	        		return accessToken;	        		
	        	}
    	} catch (Exception e) {
        	handleException(e);
        } 
        return null;
    }
    
    public void getProfile(UserBean userBean, String accessToken) {
    	if (accessToken == null || accessToken.isEmpty()) {
            throw new IllegalArgumentException("Access token이 유효하지 않습니다.");
        }

    	try {
    		System.out.println("accessToken ::"+ accessToken);
	    	String profile = webClient.get()
				        .uri(uriBuilder -> uriBuilder
				        .scheme("https")
				        .host("openapi.naver.com")
				        .path("/v1/nid/me")
				        .build())
				        .header("Authorization", "Bearer " + accessToken)
				        .retrieve()
				        .bodyToMono(String.class)
				        .block();
	    	
	    	if (profile == null || profile.isEmpty()) {
	            throw new RuntimeException("네이버 프로필 응답이 비어있습니다.");
	        }
	    	ObjectMapper objectMapper = new ObjectMapper();
    		JsonNode node = objectMapper.readTree(profile).path("response");
    		
    		String name = node.path("name").asText(null);
            String email = node.path("email").asText(null);
            String id = node.path("id").asText(null);

            if (name == null || email == null || id == null) {
                throw new RuntimeException("프로필 정보 누락: " + profile);
            }

            userBean.setUname(name);
            userBean.setUemail(email);
            userBean.setNapi(id);

            System.out.println("userBean:: " + userBean);
	        		
	        	
    	} catch (Exception e) {
        	handleException(e);
        } 
    }

    
    private String getAToken(String RefreshToken) {
    	if (RefreshToken == null || RefreshToken.isEmpty()) {
            System.err.println("Refresh token이 유효하지 않습니다.");
            return null;
        }
    	
    	try {
	        String response = webClient.post()
	                .uri("https://nid.naver.com/oauth2.0/token")
	                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
	                .bodyValue("grant_type=refresh_token&" + 
		                		"client_id=" + client_id + "&" +
		                		"client_secret=" + clientSecret + "&" +
		                		"refresh_token=" + RefreshToken)
	                .retrieve()
	                .bodyToMono(String.class)
	                .block();
	
	        if (response != null && !response.isEmpty()) {
	            ObjectMapper objectMapper = new ObjectMapper();
	            JsonNode node = objectMapper.readTree(response);
	            System.out.println("\n\n\n\n\n\n\n "+node.path("access_token").asText());
	            return node.path("access_token").asText();   
	        }
	    } catch (Exception e) {
	    	handleException(e);
	    }
    	return null;
    }
    
    public boolean dcconNa(String RefreshToken) {
    	 if (RefreshToken == null || RefreshToken.isEmpty()) {
             System.err.println("Refresh token이 유효하지 않습니다.");
             return false;
         }
        try {
        	URI uri = new URI("https://nid.naver.com/oauth2.0/token?"
                    + "grant_type=delete"
                    + "&client_id=" + URLEncoder.encode(client_id, StandardCharsets.UTF_8.toString())
                    + "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8.toString())
                    + "&access_token=" + URLEncoder.encode(getAToken(RefreshToken), StandardCharsets.UTF_8.toString()));   
        	String response = webClient.get()
        		    .uri(uri)
            	    .retrieve()
            	    .bodyToMono(String.class)
            	    .block();

            if (response != null && !response.isEmpty()) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode node = objectMapper.readTree(response);
                System.out.println("\n\n\n\n\n\n\n "+node.path("result").asText());
                return "success".equals(node.path("result").asText());
            }
        } catch (Exception e) {
        	handleException(e);
        }
        return false;
    }
}
