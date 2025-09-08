package com.dh.goyeon.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dh.goyeon.auth.CustomUserDetails;
import com.dh.goyeon.board.BoardBean;
import com.dh.goyeon.board.BoardService;
import com.dh.goyeon.meeting.GroupBean;
import com.dh.goyeon.meeting.GroupRequest;
import com.dh.goyeon.meeting.MeetingRequestBean;
import com.dh.goyeon.meeting.MeetingService;
import com.dh.goyeon.notification.NotificationBean;
import com.dh.goyeon.notification.NotificationService;
import com.dh.goyeon.reply.ReplyBean;
import com.dh.goyeon.reply.ReplyService;
import com.dh.goyeon.user.UserBean;
import com.dh.goyeon.util.ApiResponse;
import com.dh.goyeon.util.Page;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/notification")
public class NotificationController {
	@Autowired
	private NotificationService notificationService;

	@GetMapping("/list")
	@ResponseBody
	public List<NotificationBean> getNotifications(@AuthenticationPrincipal CustomUserDetails userDetails) {
	    String uid = userDetails.getUsername();
	    return notificationService.getNotiList(uid);
	}



}
