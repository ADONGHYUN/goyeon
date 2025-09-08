package com.dh.goyeon.auth.impl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dh.goyeon.auth.KakaoAPI;
import com.dh.goyeon.auth.LoginService;
import com.dh.goyeon.auth.NaverAPI;
import com.dh.goyeon.user.UserBean;
import com.dh.goyeon.user.UserService;
import com.dh.goyeon.user.impl.SendMail;
import com.dh.goyeon.user.impl.UserDAOMybatis;
import com.dh.goyeon.util.NicknameGenerator;

@Service("loginService")
public class LoginServiceImpl implements LoginService {

	@Autowired
	private UserDAOMybatis userDAO;
	@Autowired
	private SendMail sendMail;
	@Autowired
	KakaoAPI kakaoAPI;
	@Autowired
	NaverAPI naverAPI;

	
	// kakao url 가져오기
	@Override
	public String getKaUrl() {
		return "https://kauth.kakao.com/oauth/authorize?client_id="
				+ URLEncoder.encode(kakaoAPI.getKey(), StandardCharsets.UTF_8) + "&redirect_uri="
				+ URLEncoder.encode(kakaoAPI.getLoginURI(), StandardCharsets.UTF_8) + "&response_type=code&state=";
	}

	// naver url 가져오기 , naverAPI.getClient_id(), getLoginURI()의 값은 서블렛xml파일에 있는 값:
	// http://localhost:8080/naver-login
	@Override
	public String getNaUrl() {
		return "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id="
				+ URLEncoder.encode(naverAPI.getClient_id(), StandardCharsets.UTF_8) + "&redirect_uri="
				+ URLEncoder.encode(naverAPI.getLoginURI(), StandardCharsets.UTF_8) + "&state=";
	}

	// 카카오 회원 검증
	@Override
	public String kaVerify(String code) {
		UserBean userBean = new UserBean();
		userBean = kakaoAPI.getProfile(userBean, kakaoAPI.getAccessToken(code, userBean));
		UserBean userInfo = userDAO.getUser(userBean.getKapi());
		if (userInfo == null) {
			String id = userDAO.searchAPI(userBean);
			if (id == null) {
				while (true) {
					id = sendMail.getRId();
					if (userDAO.getID(id) == null) {
						userBean.setUid(sendMail.getRId());
						System.out.println("uid : " + id);
						System.out.println("uid : " + userBean.getUid());
						userBean.setUpw("0000");
						userDAO.insertUser(userBean);
						return userBean.getUid();
					}
				}
			} else {
				userBean.setUid(id);
			}
		} else {
			userBean.setUid(userInfo.getUid());
		}
		userDAO.setKAPI(userBean);
		return userBean.getUid();
	}

	// 네이버 회원 검증
	@Override
	public String naVerify(String code) {
	    // 유저 정보 객체 생성
	    UserBean userBean = new UserBean();

	    // 액세스 토큰 발급
	    String accessToken = naverAPI.getAccessToken(code, userBean);
	    if (accessToken == null || accessToken.isEmpty()) {
	        throw new IllegalArgumentException("유효하지 않은 access token");
	    }

	    // 유저 프로필 정보 세팅
	    naverAPI.getProfile(userBean, accessToken);

	    // 이미 등록된 유저인지 확인
	    UserBean userInfo = userDAO.getUser(userBean.getNapi());

	    if (userInfo == null) { // 네이버 연동 안된 회원
	        // 연동된 UID가 있는지 확인
	        String existingUid = userDAO.searchAPI(userBean);

	        if (existingUid == null) { // 완전 새로운 회원
	            // 새로운 UID 생성
	            String newUid;
	            do {
	                newUid = sendMail.getRId();	                
	            } while (userDAO.getID(newUid) != null);

	            userBean.setUid(newUid);
	            userBean.setUnickname(NicknameGenerator.generate()); // 랜덤 닉네임 설정
	            userBean.setUpw("0000"); // 기본 비밀번호 설정
	            userDAO.insertUser(userBean);
	        } else { // 네이버 연동 안된 기존 회원
	            userBean.setUid(existingUid);
	        }

	    } else {
	        // 기존 네이버 연동된 회원
	        userBean.setUid(userInfo.getUid());
	    }

	    // 사용자 API 연동 정보 업데이트
	    userDAO.setNAPI(userBean);

	    return userBean.getUid();
	}


	// 카카오 연동 해제
	@Override
	public int dconKka(String userId) {
		UserBean userBean = userDAO.getUser(userId);
		if (kakaoAPI.dcconKa(userBean.getKRToken())) {
			userBean.setKapi(null);
			userBean.setKRToken(null);
			userDAO.setKAPI(userBean);
			return 1;
		}
		return 4;
	}

	// naver 연동 해제
	@Override
	public int dconNa(String userId) {
		UserBean userBean = userDAO.getUser(userId);
		if (naverAPI.dcconNa(userBean.getNRToken())) {
			userBean.setNapi(null);
			userBean.setNRToken(null);
			userDAO.setNAPI(userBean);
			return 2;
		}
		return 4;
	}

}
