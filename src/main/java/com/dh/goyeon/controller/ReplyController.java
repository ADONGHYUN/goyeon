package com.dh.goyeon.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dh.goyeon.reply.ReplyBean;
import com.dh.goyeon.reply.ReplyService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ReplyController {
	@Autowired
	private ReplyService replyService;
	
	@PostMapping("/reply/writeReply")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> writeReply(ReplyBean replyBean, HttpSession session) {
	    try { // replyBean에 bnum, content들어있음
	        String userId = (String) session.getAttribute("userId");
	        replyBean.setUid(userId);
	        String rid = UUID.randomUUID().toString();
	        replyBean.setRid(rid); // UUID 생성 및 설정
	        replyService.writeReply(replyBean);
	        Map<String, Object> responseData = new HashMap<>();
	        responseData.put("author", userId);
	        responseData.put("content", replyBean.getContent());
	        responseData.put("rid", rid);
	        responseData.put("replyUpvoteCount", replyBean.getReplyUpvoteCount());
	        responseData.put("replyDownvoteCount", replyBean.getReplyDownvoteCount());
	        return ResponseEntity.ok(responseData);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}
	
	@PostMapping("/reply/writeRereply")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> writeRereply(ReplyBean replyBean, HttpSession session) {
	    try { // replyBean에 bnum, content, parentReplyId들어있음
	    	System.out.println("Rereply맵핑");
	        String userId = (String) session.getAttribute("userId");
	        replyBean.setUid(userId);
	        replyService.writeRereply(replyBean);
	        Map<String, Object> responseData = new HashMap<>();
	        responseData.put("author", userId);
	        responseData.put("content", replyBean.getContent());	  
	        return ResponseEntity.ok(responseData);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}
	
	@PostMapping("/reply/vote")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> replyVote(ReplyBean replyBean, HttpSession session) {
	    Map<String, Object> response = new HashMap<>();
	    
	    try {
	        String userId = (String) session.getAttribute("userId");
	        if (userId == null) {
	            response.put("status", "error");
	            response.put("message", "User not logged in");
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	        }
	        replyBean.setUid(userId);
	        String result = replyService.replyVote(replyBean, replyBean.getRid());
	        System.out.println("result : "+ result);
	        response.put("status", "success");
	        response.put("result", result);
	        return ResponseEntity.ok(response);
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        // 서버 오류 응답 처리
	        response.put("status", "error");
	        response.put("message", "Internal server error");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

}
