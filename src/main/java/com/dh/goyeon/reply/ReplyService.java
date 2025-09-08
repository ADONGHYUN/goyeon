package com.dh.goyeon.reply;

import java.util.List;


public interface ReplyService {
	public void writeReply(ReplyBean replyBean);
	public void writeRereply(ReplyBean replyBean);
	public boolean isReply(int bnum);
	public List<ReplyBean> getReply(int bnum);
	public String replyVote(ReplyBean replyBean, String rid);

}
