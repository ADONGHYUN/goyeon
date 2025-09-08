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
import com.dh.goyeon.reply.ReplyBean;
import com.dh.goyeon.reply.ReplyService;
import com.dh.goyeon.user.UserBean;
import com.dh.goyeon.util.ApiResponse;
import com.dh.goyeon.util.Page;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/meeting")
public class MeetingController {
	@Autowired
	private MeetingService meetingService;

	// 과팅 시작하기
	@GetMapping("/create")
	public String createGroupdate() {
		System.out.println("과팅 시작하기 맵핑");

		return "meeting/group-create";
	}

	@PostMapping("/create")
	@ResponseBody
	public ResponseEntity<ApiResponse> createGroup(@RequestBody GroupRequest groupRequest,
			Authentication authentication) {

		try {
			meetingService.createGroup(groupRequest);
			return ResponseEntity.ok(new ApiResponse(true, "그룹 생성 성공"));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "그룹 생성 실패"));
		}
	}

	@GetMapping("group-list")
	public String joinMeeting(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum, Model model) {
		System.out.println("group-list/page/1 맵핑성공");

		int dataCount = meetingService.getGroupCount();
		Page page = new Page(dataCount, pageNum, 9);
		List<GroupBean> groupList = meetingService.getGroupList(page.getOffset());

		model.addAttribute("groupList", groupList);
		model.addAttribute("page", page);

		/*
		 * Authentication authentication =
		 * SecurityContextHolder.getContext().getAuthentication(); if (authentication !=
		 * null && authentication.isAuthenticated() && authentication.getPrincipal()
		 * instanceof CustomUserDetails) { CustomUserDetails userDetails =
		 * (CustomUserDetails) authentication.getPrincipal(); String gender =
		 * userDetails.getUgender(); // UserBean에서 getUgender() 구현되어 있어야 함
		 * model.addAttribute("userGender", gender); model.addAttribute("a", "a"); }
		 */

		System.out.println("group-list/page/1 맵핑아래부분 성공");
		return "meeting/group-list";
	}

	@GetMapping("/my-groups")
	public ResponseEntity<List<GroupBean>> getMyGroups(@AuthenticationPrincipal CustomUserDetails userDetails) {
		System.out.println("/my-groups 맵핑");
		String uid = userDetails.getUsername();
		System.out.println("/my-groups if문");
		System.out.println("/my-groups if문 uid:" + uid);
		List<GroupBean> myGroups = meetingService.getMyGroups(uid);
		return ResponseEntity.ok(myGroups);
	}

	@PostMapping("/meeing-request")
	public ResponseEntity<ApiResponse> requestMeeting(@RequestBody MeetingRequestBean meetingRequestbean,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		String requesterUid = userDetails.getUsername();
		meetingRequestbean.setRequesterUid(requesterUid);

		try {
			meetingService.meetingRequest(meetingRequestbean);
			return ResponseEntity.ok(new ApiResponse(true, "미팅 신청 완료"));
		} catch (IllegalArgumentException e) {
			// 중복 요청 시
			return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse(false, "서버 오류가 발생했습니다."));
		}
	}

}
