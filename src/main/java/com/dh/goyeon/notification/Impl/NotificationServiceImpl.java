package com.dh.goyeon.notification.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dh.goyeon.notification.NotificationBean;
import com.dh.goyeon.notification.NotificationService;

@Service("notificationService")
public class NotificationServiceImpl implements NotificationService{
	@Autowired
	NotificationDAO notificationDAO;

	@Override
	public List<NotificationBean> getNotiList(String uid) {
		return notificationDAO.getNotiList(uid);
	}

}
