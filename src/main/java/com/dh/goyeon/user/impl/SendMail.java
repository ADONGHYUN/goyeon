package com.dh.goyeon.user.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dh.goyeon.user.UserBean;


@Component
public class SendMail {
	@Autowired
	private Send send;
	
	public UserBean sendMail(UserBean user) throws Exception {
		String mailCode = getCode();
		
		LocalDateTime now = LocalDateTime.now();
        LocalDateTime exd = now.plus(10, ChronoUnit.MINUTES);

        Timestamp exdate = Timestamp.valueOf(exd);
		user.setMailcode(mailCode);
		user.setExdate(exdate);
		
		send.sendMail(user.getUemail(), mailCode);
		
		return user;
	}
	
	private String getCode() {
		Random random = new Random();
		int min = 100000; // 최소 6자리 숫자
		int max = 999999; // 최대 6자리 숫자
		return Integer.toString(random.nextInt(max - min + 1) + min);
	}
	
	public String getRId() {
		return getCode()+getCode()+getCode();
	}
}
