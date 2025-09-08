package com.dh.goyeon.user.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;

@Component
public class Send {
	@Autowired
	private JavaMailSender mailSender;
	@Async
	public void sendMail(String uemail, String mailcode) throws Exception {
		String subject = "dhblog 인증코드입니다.";
		String content = "<html>"+
						"<body><br><br><div><h1>dhblog 인증번호</h1></div><hr><div><p>인증번호가 발급되었습니다.</p></div>"+
						"<div><p>인증번호의 유효시간은 10분입니다.</p></div>"+
						"<div><h3>"+mailcode+"</h3></div><hr> <div><p>본 메일은 발신전용입니다.</p></div></body></html>";

		String from = "abc@abc.com";
		String to = uemail;
		
		final MimeMessagePreparator preparator = new MimeMessagePreparator() {

			public void prepare(MimeMessage mimeMessage) throws Exception {
				final MimeMessageHelper mailHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
				mailHelper.setFrom(from);
				mailHelper.setTo(to);
				mailHelper.setSubject(subject);
				mailHelper.setText(content, true);
			}
		};
		try {
			mailSender.send(preparator);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
