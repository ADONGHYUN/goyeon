package com.dh.goyeon.reply.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.dh.goyeon.reply.ReplyBean;

@Repository
public class ReplyDAOMybatis {
    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String replyCollection = "reply";
    private static final String replyLikeCollection = "replyLike";

    public boolean isReply(int bnum) {
        Query query = new Query(Criteria.where("bnum").is(bnum));
        try {
            return mongoTemplate.exists(query, replyCollection);
        } catch (Exception e) {
            System.err.println("Error checking if reply exists: " + e.getMessage());
            return false; // 기본적으로 false를 반환하여 오류 상황을 처리합니다.
        }
    }

    public List<ReplyBean> getReply(int bnum) {
        Query query = new Query(Criteria.where("bnum").is(bnum));
        try {
            return mongoTemplate.find(query, ReplyBean.class, replyCollection);
        } catch (Exception e) {
            System.err.println("Error retrieving replies: " + e.getMessage());
            return new ArrayList<>(); // 빈 리스트를 반환하여 오류 상황을 처리합니다.
        }
    }

    public void writeReply(ReplyBean replyBean) {
        replyBean.setCreatedAt(LocalDateTime.now()); // 현재 시각 설정
        replyBean.setUpdatedAt(LocalDateTime.now()); // 현재 시각 설정
        replyBean.setUpvoteId(new ArrayList<>());
        replyBean.setDownvoteId(new ArrayList<>());

        try {
            mongoTemplate.insert(replyBean, replyCollection);
        } catch (Exception e) {
            System.err.println("Error inserting reply: " + e.getMessage());
        }
    }

    public void writeRereply(ReplyBean replyBean) {
        replyBean.setRid(UUID.randomUUID().toString()); // UUID 생성 및 설정
        replyBean.setCreatedAt(LocalDateTime.now()); // 현재 시각 설정
        replyBean.setUpdatedAt(LocalDateTime.now()); // 현재 시각 설정

        try {
            mongoTemplate.insert(replyBean, replyCollection);
        } catch (Exception e) {
            System.err.println("Error inserting rereply: " + e.getMessage());
        }
    }

    public String replyVote(ReplyBean replyBean, String rid) {
        Query query = new Query(Criteria.where("rid").is(rid));
        String userId = replyBean.getUid();
        String voteType = replyBean.getVoteType();
        boolean upButtonActive = replyBean.getUpButtonActive(); // getUpButtonActive() -> isUpButtonActive()
        boolean downButtonActive = replyBean.getDownButtonActive(); // getDownButtonActive() -> isDownButtonActive()

        try {
            Update update = new Update();
            
            System.out.println("voteType + upButtonActive + downButtonActive :  " + voteType + upButtonActive + downButtonActive);

            if ("upvote".equals(voteType)) {
                if (upButtonActive) { // 좋아요 눌린상태에서 좋아요 눌렀을때
                    update.inc("replyUpvoteCount", -1).pull("upvoteId", userId);
                    mongoTemplate.updateFirst(query, update, ReplyBean.class, replyCollection);
                    return "upRemove";
                } else {             
                    if (downButtonActive) { // 싫어요 눌린상태에서 좋아요 눌렀을때
                        update.inc("replyDownvoteCount", -1).pull("downvoteId", userId).inc("replyUpvoteCount", 1).addToSet("upvoteId", userId);
                        mongoTemplate.updateFirst(query, update, ReplyBean.class, replyCollection);
                        return "downToUp";
                    }
                    // 좋아요,싫어요 둘다 눌리지 않은상태에서 좋아요를 눌렀을때
                    update.inc("replyUpvoteCount", 1).addToSet("upvoteId", userId);
                    mongoTemplate.updateFirst(query, update, ReplyBean.class, replyCollection);
                    return "up";
                }
            } else if ("downvote".equals(voteType)) {
                if (downButtonActive) { // 싫어요 버튼 눌린 상태에서 싫어요 눌렀을때
                    update.inc("replyDownvoteCount", -1).pull("downvoteId", userId);
                    mongoTemplate.updateFirst(query, update, ReplyBean.class, replyCollection);
                    return "downRemove";
                } else {
                    if (upButtonActive) { // 좋아요 버튼 눌린상태에서 싫어요 버튼 눌렀을때
                        update.inc("replyUpvoteCount", -1).pull("upvoteId", userId).inc("replyDownvoteCount", 1).addToSet("downvoteId", userId);
                        mongoTemplate.updateFirst(query, update, ReplyBean.class, replyCollection);
                        return "upToDown";
                    }
                    // 좋아요,싫어요 둘다 눌리지 않은상태에서 싫어요 눌렀을때
                    update.inc("replyDownvoteCount", 1).addToSet("downvoteId", userId);
                    mongoTemplate.updateFirst(query, update, ReplyBean.class, replyCollection);
                    return "down";
                }               
            } else {
                System.out.println("Invalid vote type: " + voteType + upButtonActive + downButtonActive);
                return "invalidVote";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }


}
