package com.dh.goyeon.meeting;

import java.time.LocalDateTime;

public class MeetingRequestBean {
	private int requestId;
	private String requesterUid;
	private int fromGroup;
	private int toGroup;
	private String status;
	private LocalDateTime requestDate;
	
	public int getRequestId() {
		return requestId;
	}
	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}
	public String getRequesterUid() {
		return requesterUid;
	}
	public void setRequesterUid(String requesterUid) {
		this.requesterUid = requesterUid;
	}
	public int getFromGroup() {
		return fromGroup;
	}
	public void setFromGroup(int fromGroup) {
		this.fromGroup = fromGroup;
	}
	public int getToGroup() {
		return toGroup;
	}
	public void setToGroup(int toGroup) {
		this.toGroup = toGroup;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public LocalDateTime getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(LocalDateTime requestDate) {
		this.requestDate = requestDate;
	}	
	
}
