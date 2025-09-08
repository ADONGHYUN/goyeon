package com.dh.goyeon.meeting.Impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dh.goyeon.meeting.GroupBean;
import com.dh.goyeon.meeting.GroupMemberBean;
import com.dh.goyeon.meeting.GroupRequest;
import com.dh.goyeon.meeting.MeetingRequestBean;
import com.dh.goyeon.meeting.MeetingService;
import com.dh.goyeon.notification.NotificationBean;
import com.dh.goyeon.notification.Impl.NotificationDAO;
import com.dh.goyeon.user.UserBean;

@Service("meetingService")
public class MeetingServiceImpl implements MeetingService {
	@Autowired
	private MeetingDAO meetingDAO;
	@Autowired
	private NotificationDAO notificationDAO;

	@Transactional
	@Override
	public void createGroup(GroupRequest groupRequest) {
		GroupBean group = new GroupBean();
		group.setLeaderUid("s111111");
		group.setGroupName("임시 그룹 이름");
		group.setMemberCount(groupRequest.getMemberCount());
		System.out.println("그룹만들기 service");

		int createGroup = meetingDAO.createGroup(group);
		if (createGroup == 0) {
			throw new IllegalStateException("그룹 생성 실패");
		}

		int groupId = group.getGroupId();

		List<GroupMemberBean> members = new ArrayList<>();
		GroupMemberBean leader = new GroupMemberBean();
		leader.setGroupId(groupId);
		leader.setMemberUid(group.getLeaderUid());
		members.add(leader);
		
		for (String uid : groupRequest.getTeamMembers()) {		
			GroupMemberBean member = new GroupMemberBean();
			member.setGroupId(groupId);
			member.setMemberUid(uid);
			members.add(member);
		}

		int memberInsertResult = meetingDAO.insertGroupMembers(members);
		if (memberInsertResult != members.size()) {
			throw new IllegalStateException("그룹 생성 실패");
		}

	}

	@Override
	public int getGroupCount() {
		return meetingDAO.getGroupCount();
	}

	@Override
	public List<GroupBean> getGroupList(int totalData) {
		return meetingDAO.getGroupList(totalData);
	}

	@Override
	public List<GroupBean> getMyGroups(String uid) {
		return meetingDAO.getMyGroups(uid);
	}

	@Override
	public void meetingRequest(MeetingRequestBean meetingRequestbean) {
		try {
			int result = meetingDAO.meetingRequest(meetingRequestbean);

			if (result == 0) {
				throw new IllegalStateException("요청 실패");
			}else {
				List<GroupMemberBean> toGroupUsers = meetingDAO.getGroupMembers(meetingRequestbean.getToGroup());
				List<NotificationBean> notifications = new ArrayList<>();
				for (GroupMemberBean member : toGroupUsers) {
				    notifications.add(new NotificationBean(member.getMemberUid(), "새로운 미팅 제안이 왔습니다!", "/"));
				}
				notificationDAO.insertNotifications(notifications); 
			}

		} catch (DuplicateKeyException e) {
			throw new IllegalArgumentException("이미 요청된 만남입니다.");
		}
	}


}
