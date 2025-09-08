package com.dh.goyeon.user.impl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dh.goyeon.auth.KakaoAPI;
import com.dh.goyeon.auth.NaverAPI;
import com.dh.goyeon.user.UserBean;
import com.dh.goyeon.user.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAOMybatis userDAO;
	@Autowired
	private SendMail sendMail;
	@Autowired
	KakaoAPI kakaoAPI;
	@Autowired
	NaverAPI naverAPI;

	@Override
	public boolean isUser(UserBean userBean) {
		return checkPW(userBean.getUpw(), userDAO.getPass(userBean.getUid()));
	}

	@Override
	public boolean getID(String userID) {
		return userDAO.getID(userID) == null ? false : true;
	}

	@Override
	public UserBean searchNickname(String unickname) {
		return userDAO.searchNickname(unickname);
	}
	
	@Override
	public String getSameMail(String uemail) {
		List<UserBean> mails = userDAO.getSameMail(uemail);
		if (mails.isEmpty()) {
			return null;
		}
		return "yes";
	}

	@Override
	public int insertUser(UserBean user) {
		user.setUpw(hashPW(user.getUpw()));
		return userDAO.insertUser(user);
	}

	// id로 유저 정보 가져옴
	@Override
	public UserBean getUser(String userId) {
		UserBean userBean = userDAO.getUser(userId);
		return userBean;
	}
	
	// 프로필사진 변경
	@Override
	public int updateUimage(String userId, String fileName) {
		return userDAO.updateUimage(userId, fileName);
	}
	
	// 정보 수정
	@Override
	public int update(UserBean userBean) {
		return userDAO.update(userBean);
	}

	// 비밀번호 수정
	@Override
	public int pwUpdate(UserBean userBean) {
		if (isUser(userBean)) {
			userBean.setNpw(hashPW(userBean.getNpw()));
			return userDAO.pwUpdate(userBean);
		} else {
			return 0;
		}
	}

	@Override
	public int delete(String userId) {
		try {
			UserBean userBean = userDAO.getUser(userId);
			if (userBean.getKapi() != null) {
				kakaoAPI.dcconKa(userBean.getKRToken());
			}
			if (userBean.getNapi() != null) {
				naverAPI.dcconNa(userBean.getNRToken());
			}
			return userDAO.delete(userId); // 이 메소드에서 예외가 발생할 수 있음
		} catch (RuntimeException e) {
			System.out.println("서비스 계층에서 예외를 받음");
			throw e; // 컨트롤러로 예외던짐
		}
	}

	@Override
	public String getMail(String uemail) throws Exception {

		List<UserBean> oldUsers = userDAO.getSameMail(uemail);
		UserBean user = new UserBean();//메일 보낼 때 쓸 빈
		if (oldUsers.isEmpty()) {
			return "no";
		}
		user.setUemail(uemail);
		
		//리스트 내용물을 담는 빈
		UserBean useris = new UserBean();
		
		for (UserBean oldUser : oldUsers) {
			if (oldUser.getUid().length()<=12) {//아이디 값이 12자리 이하면 소셜회원이 아니니 담음
				useris = oldUser;
			}
		}
		
		System.out.println(useris);
		if(useris.getUid() == null) {//소셜계정만 있는 경우
			return "api";
		}

		user = sendMail.sendMail(user);

		int codeConfirm = -1;

		if (user != null) {
			System.out.println(user.getExdate());
			codeConfirm = userDAO.updateMailCode(user);
		} else {
			return "mfail";
		}

		System.out.println(codeConfirm);

		if (codeConfirm > 0) {
			return "yes";
		} else {
			return "yesb";
		}
	}
	
	@Override
	public String getMailCode(UserBean user) {
		UserBean result = userDAO.getMailCode(user);
		if (result == null) {
			return "un";
		}

		String icode = user.getMailcode().trim();
		String rcode = result.getMailcode().trim();

		System.out.println(icode);
		System.out.println(rcode);

		if (rcode.equals(icode)) {
			return "yes";
		}
		return "no";
	}

	@Override
	public UserBean getFindId(String uemail) {
		UserBean user = new UserBean();
		user.setUemail(uemail);
		user.setMailcode("000000");
		userDAO.updateMailCode(user);// 인증완료 코드 초기화
		List<UserBean> users = userDAO.getSameMail(uemail);// 이메일이 동일한 계정들 불러오기

		for (UserBean userr : users) {
			if (userr.getUid().length() <= 12) {// 아이디가 12자리 이하면 일반회원
				user = userr;// 일반회원정보 담기
			}
		}
		return user;// 고객에게 보여줄 일반회원 정보
	}

	@Override
	public String getFid(String uid, String uemail) throws Exception {

		UserBean user = userDAO.getUser(uid);
		System.out.println(user);
		if (user == null) {
			return "no";
		} else if (!user.getUemail().equals(uemail)) {
			System.out.println(user.getUemail());
			System.out.println(uemail);
			return "nmail";
		}
		System.out.println(user);
		int codeConfirm = -1;
		user = sendMail.sendMail(user);

		if (user != null) {
			codeConfirm = userDAO.updateMailCodeId(user);
		} else {
			return "mfail";
		}

		System.out.println(codeConfirm);

		if (codeConfirm > 0) {
			return "yes";
		} else {
			return "yesb";
		}
	}

	@Override
	public String getMailCodePw(UserBean user) {
		UserBean result = userDAO.getMailCodePw(user);

		if (result == null) {
			return "un";
		}

		String icode = user.getMailcode().trim();
		String rcode = result.getMailcode().trim();

		System.out.println(icode);
		System.out.println(rcode);

		if (rcode.equals(icode)) {
			return "yes";
		}
		return "no";
	}

	@Override
	public String getFindPw(String uid) {
		UserBean user = new UserBean();
		user.setUid(uid);
		user.setMailcode("000000");
		int codeConfirm = userDAO.updateMailCodeId(user);
		if (codeConfirm > 0) {
			return "yes";
		}
		return "no";
	}

	@Override
	public String pwChange(UserBean user) {
		user.setUpw(hashPW(user.getUpw()));
		int confirm = userDAO.pwChange(user);
		if (confirm > 0) {
			return "yes";
		}
		return "no";
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	public String hashPW(String upw) {
		return BCrypt.hashpw(upw, BCrypt.gensalt());
	}

	// 비밀번호 확인
	public boolean checkPW(String upw, String dbpw) {
		if (dbpw == null) {
			return false;
		}
		return BCrypt.checkpw(upw, dbpw);
	}

	

}
