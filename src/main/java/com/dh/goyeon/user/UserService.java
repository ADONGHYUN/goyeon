package com.dh.goyeon.user;


public interface UserService {
	boolean isUser(UserBean user);
	public int insertUser(UserBean user);
	public boolean getID(String userId);
	UserBean searchNickname(String nickname);
	String getSameMail(String uemail);
	public UserBean getUser(String userId);
	int update(UserBean userBean);
	int pwUpdate(UserBean userBean);
	int delete(String userId);
	String getMail(String uemail) throws Exception;
	String getMailCode(UserBean user);
	UserBean getFindId(String uemail);
	String getFid(String uid, String uemail) throws Exception;
	String getMailCodePw(UserBean user);
	String getFindPw(String uid);
	String pwChange(UserBean user);
	int updateUimage(String userId, String fileName);
	

}
