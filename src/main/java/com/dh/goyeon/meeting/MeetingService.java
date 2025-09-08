package com.dh.goyeon.meeting;

import java.util.List;

public interface MeetingService {
	public void createGroup(GroupRequest groupRequest);
	public int getGroupCount();
	public List<GroupBean> getGroupList(int totalData);
	public List<GroupBean> getMyGroups(String uid);
	public void meetingRequest(MeetingRequestBean meetingRequestbean);
}
