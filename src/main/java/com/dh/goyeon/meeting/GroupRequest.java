package com.dh.goyeon.meeting;

import java.util.List;

public class GroupRequest {
	private String leaderUid;
    private String meetingName;
	private int memberCount;
    private List<String> teamMembers;

    public String getLeaderUid() {
		return leaderUid;
	}

	public void setLeaderUid(String leaderUid) {
		this.leaderUid = leaderUid;
	}

	public String getMeetingName() {
		return meetingName;
	}

	public void setMeetingName(String meetingName) {
		this.meetingName = meetingName;
	}
	
    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public List<String> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<String> teamMembers) {
        this.teamMembers = teamMembers;
    }

}
