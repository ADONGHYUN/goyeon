package com.dh.goyeon.meeting.Impl;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dh.goyeon.meeting.GroupBean;
import com.dh.goyeon.meeting.GroupMemberBean;
import com.dh.goyeon.meeting.MeetingRequestBean;
import com.dh.goyeon.notification.NotificationBean;
import com.dh.goyeon.user.UserBean;

@Repository
public class MeetingDAO {
	@Autowired
	private SqlSessionTemplate mybatis;

	public int createGroup(GroupBean group) {
		System.out.println("그룹만들기 dao");
		int insertGroup = mybatis.insert("meetingDAO.insertGroup", group);
		return insertGroup;
	}

	public int insertGroupMembers(List<GroupMemberBean> members) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("members",members);
		int insertGroupMembers = mybatis.insert("meetingDAO.insertGroupMembers", map);
		return insertGroupMembers;
	}

	public int getGroupCount() {
		return mybatis.selectOne("meetingDAO.getGroupCount");
	}

	public List<GroupBean> getGroupList(int totalData) {
		return mybatis.selectList("meetingDAO.getGroupList", totalData);
	}

	public List<GroupBean> getMyGroups(String uid) {
		return mybatis.selectList("meetingDAO.getMyGroups", uid);
	}

	public int meetingRequest(MeetingRequestBean meetingRequestbean) {
		return mybatis.insert("meetingDAO.meetingRequest", meetingRequestbean);
	}

	public List<GroupMemberBean> getGroupMembers(int toGroup) {
		return mybatis.selectList("meetingDAO.getGroupMembers", toGroup);
	}

}
