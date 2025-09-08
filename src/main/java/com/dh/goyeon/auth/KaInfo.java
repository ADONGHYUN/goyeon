package com.dh.goyeon.auth;

public class KaInfo {
	private String id;
	private Kakao_acount kakao_account;
	
	 public static class Kakao_acount {
		 private String name;
		 private String email;
		 private String birthyear;
		 private String birthday;
		 private String gender;
		 private String phone_number;
		 
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getBirthyear() {
			return birthyear;
		}
		public void setBirthyear(String birthyear) {
			this.birthyear = birthyear;
		}
		public String getBirthday() {
			return birthday;
		}
		public void setBirthday(String birthday) {
			this.birthday = birthday;
		}
		public String getGender() {
			return gender;
		}
		public void setGender(String gender) {
			this.gender = gender;
		}
		public String getPhone_number() {
			return phone_number;
		}
		public void setPhone_number(String phone_number) {
			this.phone_number = phone_number;
		}
	 }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Kakao_acount getKakao_account() {
		return kakao_account;
	}

	public void setKakao_account(Kakao_acount kakao_account) {
		this.kakao_account = kakao_account;
	}


	public String toString() {
		return "KakaoInfoBean [id=" + id + ", kakao_account=" + kakao_account + "]";
	}
	
}
