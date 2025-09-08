package com.dh.goyeon.meeting;

import java.time.LocalDateTime;

public class GroupBean {

    private int groupId;             // PK, auto_increment
    private String groupName;
    private String leaderUid;
    private String leaderGender;
    private int memberCount;
    private LocalDateTime createdAt;


    // Getter / Setter
    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    public String getLeaderUid() {
        return leaderUid;
    }

    public void setLeaderUid(String leaderUid) {
        this.leaderUid = leaderUid;
    }
    
    public String getLeaderGender() {
		return leaderGender;
	}

	public void setLeaderGender(String leaderGender) {
		this.leaderGender = leaderGender;
	}

	public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
