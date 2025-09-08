package com.dh.goyeon.reply.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dh.goyeon.reply.ReplyBean;
import com.dh.goyeon.reply.ReplyService;
import com.dh.goyeon.reply.ReplyVoteBean;

@Service("replyService")
public class ReplyServiceImpl implements ReplyService{
	
	@Autowired
	private ReplyDAOMybatis replyDAO;
	
	@Override
	public boolean isReply(int bnum) {
		boolean isReply = replyDAO.isReply(bnum);
		return isReply;
	}
	
	@Override
	public List<ReplyBean> getReply(int bnum) {			
		return replyDAO.getReply(bnum);
	}
	
	@Override
	public void writeReply(ReplyBean replyBean) {
		replyDAO.writeReply(replyBean);
	}

	public void writeRereply(ReplyBean replyBean) {
		replyDAO.writeRereply(replyBean);
	}

	public String replyVote(ReplyBean replyBean, String rid) {
		return replyDAO.replyVote(replyBean, rid);
	}
}
