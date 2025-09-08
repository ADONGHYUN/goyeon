package com.dh.goyeon.reply;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reply")
public class ReplyBean {

    @Id
    private String rid;  // reply 고유번호, uuid
    private String uid; // 작성자 id
    private int bnum;
    private String content;
    private String voteType;
    private boolean upButtonActive; 
    private boolean downButtonActive;
    private int replyUpvoteCount;
    private int replyDownvoteCount;
    private List<String> upvoteId = new ArrayList<>(); // 빈 리스트로 초기화
    private List<String> downvoteId = new ArrayList<>(); // 빈 리스트로 초기화
    private String parentReplyId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // 기본 생성자
    public ReplyBean() {
    }
    
    // Getter와 Setter 메서드들
    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getBnum() {
        return bnum;
    }

    public void setBnum(int bnum) {
        this.bnum = bnum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVoteType() {
        return voteType;
    }

    public void setVoteType(String voteType) {
        this.voteType = voteType;
    }
    
    public boolean getUpButtonActive() {
		return upButtonActive;
	}

	public void setUpButtonActive(boolean upButtonActive) {
		this.upButtonActive = upButtonActive;
	}

	public boolean getDownButtonActive() {
		return downButtonActive;
	}

	public void setDownButtonActive(boolean downButtonActive) {
		this.downButtonActive = downButtonActive;
	}

	public int getReplyUpvoteCount() {
        return replyUpvoteCount;
    }

    public void setReplyUpvoteCount(int replyUpvoteCount) {
        this.replyUpvoteCount = replyUpvoteCount;
    }

    public int getReplyDownvoteCount() {
        return replyDownvoteCount;
    }

    public void setReplyDownvoteCount(int replyDownvoteCount) {
        this.replyDownvoteCount = replyDownvoteCount;
    }

    public List<String> getUpvoteId() {
        return upvoteId;
    }

    public void setUpvoteId(List<String> upvoteId) {
        this.upvoteId = upvoteId;
    }

    public List<String> getDownvoteId() {
        return downvoteId;
    }

    public void setDownvoteId(List<String> downvoteId) {
        this.downvoteId = downvoteId;
    }

    public String getParentReplyId() {
        return parentReplyId;
    }

    public void setParentReplyId(String parentReplyId) {
        this.parentReplyId = parentReplyId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
