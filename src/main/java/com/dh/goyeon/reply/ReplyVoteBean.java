package com.dh.goyeon.reply;

import java.util.Objects;

public class ReplyVoteBean {

	private String voteUid;
	private String vote;
	
	 public ReplyVoteBean() {
	    }
	
	public ReplyVoteBean(String voteUid, String vote) {
		this.voteUid = voteUid;
		this.vote = vote;
	}
	public String getVoteUid() {
		return voteUid;
	}
	public void setVoteUid(String voteUid) {
		this.voteUid = voteUid;
	}
	public String getVote() {
		return vote;
	}
	public void setVote(String vote) {
		this.vote = vote;
	}
	
	
	// equals 메서드
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReplyVoteBean that = (ReplyVoteBean) o;
        return Objects.equals(voteUid, that.voteUid) &&
               Objects.equals(vote, that.vote);
    }

    // hashCode 메서드
    @Override
    public int hashCode() {
        return Objects.hash(voteUid, vote);
    }
}
