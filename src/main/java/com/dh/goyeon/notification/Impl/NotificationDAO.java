package com.dh.goyeon.notification.Impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.dh.goyeon.notification.NotificationBean;

@Repository
public class NotificationDAO {
	@Autowired
	private SqlSessionTemplate mybatis;
	
	public void insertNotifications(List<NotificationBean> notifications) {
		mybatis.insert("notificationDAO.insertNotifications", notifications);
	}

	public List<NotificationBean> getNotiList(String uid) {
		return mybatis.selectList("notificationDAO.getNotiList", uid);
	}

}
