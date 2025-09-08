package com.dh.goyeon.notification;

import java.time.LocalDateTime;

public class NotificationBean {
    private int notificationId;      // notification_id
    private String uid;              // uid (FK)
    private String message;          // message
    private String link;             // link
    private LocalDateTime createdAt; // created_at
    private String readYn;           // read_yn

    public NotificationBean() {}

    public NotificationBean(String uid, String message, String link) {
        this.uid = uid;
        this.message = message;
        this.link = link;
        this.readYn = "N";
    }

	public int getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(int notificationId) {
		this.notificationId = notificationId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getReadYn() {
		return readYn;
	}

	public void setReadYn(String readYn) {
		this.readYn = readYn;
	}

}
