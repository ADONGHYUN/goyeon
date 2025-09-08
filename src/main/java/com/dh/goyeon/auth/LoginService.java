package com.dh.goyeon.auth;


public interface LoginService {
	String naVerify(String code);
	String kaVerify(String code);
	String getNaUrl();
	String getKaUrl();
	int dconKka(String userId);
	int dconNa(String userId);
}
