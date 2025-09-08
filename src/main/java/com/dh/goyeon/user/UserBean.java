package com.dh.goyeon.user;

import java.sql.Date;
import java.sql.Timestamp;

import org.springframework.web.multipart.MultipartFile;

public class UserBean {
	private String uid;
	private String upw;
	private String uname;
	private String unickname;
	private String ugender;	
	private String uimage;
	private MultipartFile uimageFile;
	private String uemail;
	private Date joindate;
	private String mailcode;
	private Timestamp exdate;
	private String kapi;
	private String napi;
	private String KRToken;
	private String NRToken;
	private String npw;
	
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUpw() {
		return upw;
	}
	public void setUpw(String upw) {
		this.upw = upw;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUnickname() {
		return unickname;
	}
	public void setUnickname(String unickname) {
		this.unickname = unickname;
	}
	public String getUgender() {
		return ugender;
	}
	public void setUgender(String ugender) {
		this.ugender = ugender;
	}
	public String getUimage() {
		return uimage;
	}
	public void setUimage(String uimage) {
		this.uimage = uimage;
	}
	public MultipartFile getuimageFile() {
		return uimageFile;
	}
	public void setuimageFile(MultipartFile uimageFile) {
		this.uimageFile = uimageFile;
	}
	public String getUemail() {
		return uemail;
	}
	public void setUemail(String uemail) {
		this.uemail = uemail;
	}
	public Date getJoindate() {
		return joindate;
	}
	public void setJoindate(Date joindate) {
		this.joindate = joindate;
	}
	public String getMailcode() {
		return mailcode;
	}
	public void setMailcode(String mailcode) {
		this.mailcode = mailcode;
	}
	public Timestamp getExdate() {
		return exdate;
	}
	public void setExdate(Timestamp exdate) {
		this.exdate = exdate;
	}
	public String getKapi() {
		return kapi;
	}
	public void setKapi(String kapi) {
		this.kapi = kapi;
	}
	public String getNapi() {
		return napi;
	}
	public void setNapi(String napi) {
		this.napi = napi;
	}
	public String getKRToken() {
		return KRToken;
	}
	public void setKRToken(String kRToken) {
		KRToken = kRToken;
	}
	public String getNRToken() {
		return NRToken;
	}
	public void setNRToken(String nRToken) {
		NRToken = nRToken;
	}
	public String getNpw() {
		return npw;
	}
	public void setNpw(String npw) {
		this.npw = npw;
	}
	
	
	

}
