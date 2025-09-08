package com.dh.goyeon.user.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dh.goyeon.user.UserBean;

@Repository
public class UserDAOMybatis {
	@Autowired
	private SqlSessionTemplate mybatis;

	 public boolean isUser(UserBean userBean) {
	       System.out.println("isUser MyBatis 호출 성공!!");
	       UserBean result = mybatis.selectOne("userDAO.isUser", userBean);
	       System.out.println("result: "+ result);
	       if(result != null) {
	    	   System.out.println("userDAO  return true;");
	    	   return true;
	       }
	       System.out.println("userDAO  return false;");
	       return false;
	    	   							// 결과가 null이면 false, 그렇지 않으면 true를 반환
	}

	public String getPass(String uid) {
		return mybatis.selectOne("userDAO.getPass", uid);
	}
	
	public String getID(String userID) {
		return this.mybatis.selectOne("userDAO.getID", userID);
	}
	
	public UserBean searchNickname(String unickname) {
		return this.mybatis.selectOne("userDAO.searchNickname", unickname);
	}
	
	public List<UserBean> getSameMail(String uemail) { // 이메일 중복체크
		return this.mybatis.selectList("userDAO.getSameMail", uemail);
	}
	
	public int insertUser(UserBean user) {
		System.out.println("UserDAO insert");
		return mybatis.insert("userDAO.insertUser",user);
	}
	
	public UserBean getUser(String userId) {
		return mybatis.selectOne("userDAO.getUser", userId);
	}
	
	public UserBean getUserDetails(String userId) {
		return mybatis.selectOne("userDAO.getUser", userId);
	}
	
	public int updateUimage(String userId, String fileName) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("fileName", fileName);
		return mybatis.update("userDAO.updateUimage", params);
		
	}
	
	public int update(UserBean userBean) {
		return mybatis.update("userDAO.update", userBean);
	}

	public int pwUpdate(UserBean userBean) {
		return mybatis.update("userDAO.pwUpdate", userBean);
	}
	
	@Transactional
	public int delete(String uId) {
	    int deleteUser = mybatis.delete("userDAO.delete", uId);
	    if (deleteUser > 1) {
	        throw new RuntimeException("삭제된 레코드 수가 1보다 큽니다. 롤백합니다.");
	    }
	    return deleteUser;
	}
	
	public String searchAPI(UserBean userBean) {
		return this.mybatis.selectOne("userDAO.searchAPI", userBean);
	}
	
	public int setKAPI(UserBean userBean) {
		return this.mybatis.update("userDAO.setKAPI", userBean);
	}
	
	public int setNAPI(UserBean userBean) {
		return this.mybatis.update("userDAO.setNAPI", userBean);
	}
	
	// 아디,비번찾기
	public int updateMailCode(UserBean user) {
		System.out.println("UserDAO updateMailCode");
		return mybatis.update("userDAO.updateMailCode",user);
	}

	public UserBean getMailCode(UserBean user) {
		System.out.println("UserDAO getMailCode");
		return mybatis.selectOne("userDAO.getMailCode",user);
	}
	
	public int updateMailCodeId(UserBean user) {
		System.out.println("UserDAO updateMailCodeId");
		return mybatis.update("userDAO.updateMailCodeId",user);
	}
	
	public UserBean getMailCodePw(UserBean user) {
		System.out.println("UserDAO getMailCodePw");
		return mybatis.selectOne("userDAO.getMailCodePw",user);
	}
	
	public int pwChange(UserBean user) {
		System.out.println("UserDAO pwChange");
		return mybatis.update("userDAO.pwChange",user);
	}

	
	
}
