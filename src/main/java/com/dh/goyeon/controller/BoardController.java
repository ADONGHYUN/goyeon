package com.dh.goyeon.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.dh.goyeon.board.BoardBean;
import com.dh.goyeon.board.BoardService;
import com.dh.goyeon.reply.ReplyBean;
import com.dh.goyeon.reply.ReplyService;
import com.dh.goyeon.user.UserBean;
import com.dh.goyeon.util.Page;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class BoardController {
	@Autowired
	private BoardService boardService;
	@Autowired
	private ReplyService replyService;

	// 자유게시판
	@GetMapping("/board/boardList")
	public String boardList(Model model) {
		System.out.println("자유게시판 맵핑");
		int boardCount = boardService.getboardCount(1);
		Page paging = new Page(boardCount, 1, 9);
		System.out.println("boardCount: " + boardCount);
		List<BoardBean> postList = boardService.getBoardList(paging.getPosts());
		System.out.println("postList" + postList);
		model.addAttribute("postList", postList);
		model.addAttribute("paging", paging);
		return "board/boardList";
	}

	@GetMapping("/board/boardList/page/{page}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> boardListAjax(Model model, @PathVariable int page) {
		System.out.println("page값 : " + page);
		int postCount = boardService.getboardCount(page);
		System.out.println("postCount: " + postCount);
		Page paging = new Page(postCount, page, 9);
		List<BoardBean> postList = boardService.getBoardList(paging.getPosts());
		System.out.println("postList" + postList);
		Map<String, Object> response = new HashMap<>();
		response.put("postList", postList);
		response.put("paging", paging);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/board/boardList/search/{searchType}/{searchText}/page/{page}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> boardSearchList(Model model, @PathVariable String searchType,
			@PathVariable String searchText, @PathVariable int page) {
		System.out.println("값 : " + searchType + searchText);
		int count = boardService.getSearchCount(searchType, searchText);
		System.out.println("count: " + count);
		Page paging = new Page(count, page, 9);
		List<BoardBean> postList = boardService.getSearchCountList(searchType, searchText, paging.getPosts());
		Map<String, Object> response = new HashMap<>();
		response.put("postList", postList);
		response.put("paging", paging);
		return ResponseEntity.ok(response);
	}

	// bcategory 게시판
	@GetMapping("/board/boardList/{bcategory}")
	public String boardCateList(Model model, @PathVariable(value="bcategory") String bcategory) {
		System.out.println("카테고리 게시판 맵핑");
		System.out.println("카테고리 게시판 bcategory" + bcategory);
		bcategory = bcategory.trim();
		System.out.println("카테고리 게시판 trim bcategory" + bcategory);
		int postCount = boardService.getCateboardCount(bcategory);
		System.out.println("카테고리 게시판 postCount" + postCount);
		Page paging = new Page(postCount, 1, 9);
		List<BoardBean> postList = boardService.getCateBoardList(bcategory, paging.getPosts());
		model.addAttribute("postList", postList);
		model.addAttribute("paging", paging);
		return "board/myCateBoard";
	}

	@GetMapping("/board/boardList/{bcategory}/page/{page}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> boardCateListAjax(Model model, @PathVariable String bcategory,
			@PathVariable int page) {
		bcategory = bcategory.trim();
		int postCount = boardService.getCateboardCount(bcategory);
		Page paging = new Page(postCount, page, 9);
		List<BoardBean> postList = boardService.getCateBoardList(bcategory, paging.getPosts());
		Map<String, Object> response = new HashMap<>();
		response.put("postList", postList);
		response.put("paging", paging);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/board/boardList/{bcategory}/search/{searchType}/{searchText}/page/{page}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> boardCateSearchList(Model model, @PathVariable String bcategory,
			@PathVariable String searchType, @PathVariable String searchText, @PathVariable int page) {
		System.out.println("cate 검색");
		bcategory = bcategory.trim();
		int count = boardService.getCateSearchCount(bcategory, searchType, searchText);
		Page paging = new Page(count, page, 9);
		List<BoardBean> postList = boardService.getCateSearchCountList(bcategory, searchType, searchText,
				paging.get());
		Map<String, Object> response = new HashMap<>();
		response.put("postList", postList);
		response.put("paging", paging);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/board/myboard-info")
	public String myCategoryBoard(HttpSession session, Model model) {
		String userId = (String) session.getAttribute("userId");

		System.out.println("myboard-info 맵핑");
		return "/user/myBoardInfo";
	}

	@GetMapping("/board/boardDetail/bnum/{bnum}")
	public String boardDetail(@PathVariable("bnum") int bnum, Model model) {
		System.out.println("board/boardDetail 컨트롤러");
		BoardBean board = boardService.getBoard(bnum);
		boolean isReply = replyService.isReply(bnum);
		if (isReply != false) {
			List<ReplyBean> reply = replyService.getReply(bnum);
			model.addAttribute("reply", reply);
		}
		model.addAttribute("board", board);
		System.out.println(board.getCreated_at());
		return "board/boardDetail";
	}

	/*
	 * @RequestMapping(value = "/board/myBoard", method = RequestMethod.GET) public
	 * String myBoard(Model model, HttpServletRequest request) {
	 * System.out.println("board/myBoard맵핑됨"); HttpSession session =
	 * request.getSession();
	 * System.out.println("myboard의 session.getAttribute값  = "+(String)session.
	 * getAttribute("userId")); List<BoardBean> boardList =
	 * boardService.getMyBoard((String)session.getAttribute("userId"));
	 * model.addAttribute("boardList", boardList);
	 * System.out.println("myboard의 boardList = "+boardList); return
	 * "/board/myBoard"; }
	 */

	@GetMapping("/board/writeBoard")
	public String writeBoard() {
		System.out.println("board/writeBoard맵핑됨");
		return "board/writeBoard";
	}

	@DeleteMapping(value = "/board/delete/bnum/{bnum}")
	public ResponseEntity<?> deletePost(@PathVariable("bnum") int bnum) {
		try {
			boardService.deletePost(bnum); // 서비스 레이어에서 게시물 삭제 처리
			return ResponseEntity.ok().body("게시물이 삭제되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시물 삭제 중 오류가 발생했습니다.");
		}
	}

	// 사이드바
	@GetMapping("/board/get-myboard")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getMyboardCategory(HttpSession session) {
		System.out.println("side바 맵핑됨");
		String userId = (String) session.getAttribute("userId");
		List<String> getMyboardCategory = boardService.getMyboardCategory(userId);
		Map<String, Object> responseData = new HashMap<>();
		responseData.put("getMyboardCategory", getMyboardCategory);
		return ResponseEntity.ok(responseData);
	}

	// 나의게시판 생성
	@PostMapping("/board/create-board")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> createBoard(UserBean userBean, HttpSession session) {
		System.out.println("createBoard 맵핑됨");
		String userId = (String) session.getAttribute("userId");
		userBean.setUid(userId);
		int createBoard = boardService.createBoard(userBean);
		Map<String, Object> responseData = new HashMap<>();
		responseData.put("createBoard", createBoard);
		return ResponseEntity.ok(responseData);
	}

	// 나의 게시판 순서변경
	@PostMapping("/board/update-board-order")
	@ResponseBody
	public ResponseEntity<String> updateBoardOrder(@RequestBody Map<String, List<String>> request,
			HttpSession session) {
		try {
			String userId = (String) session.getAttribute("userId");
			List<String> order = request.get("order");
			System.out.println("userId :" + userId);
			System.out.println("Order: " + order);
			int result = boardService.updateBoardOrder(userId, order);
			if (result > 0) {
				return ResponseEntity.ok("Order updated successfully");
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update order");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while updating order");
		}
	}

	@GetMapping("/chat")
	public String chat() {
		return "board/chat/chat";
	}

	// 나의 게시판에서 게시판 삭제
	@DeleteMapping(value = "/board/deleteBoard/{boardName}")
	public ResponseEntity<?> deleteBoard(@PathVariable("boardName") String boardName, HttpSession session) {
		try {
			System.out.println("deleteBoard 매핑");
			System.out.println("boardName :" + boardName);
			String userId = (String) session.getAttribute("userId");
			System.out.println("userId :" + userId);

			boardService.deleteBoard(userId, boardName);

			return ResponseEntity.ok().body(boardName + "게시판이 삭제되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시판 삭제 중 오류가 발생했습니다.");
	}

	// 현재 사용자 정보를 가져오는 메서드
	public String getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			return userDetails.getUsername(); // 사용자 ID 또는 사용자 이름
		}
		return null;
	}
	
	@ControllerAdvice
	public class GlobalExceptionHandler {
	    @ExceptionHandler(Exception.class)
	    public String handleException(Exception ex) {
	        // 오류 로그 처리 및 에러 페이지 반환
	        return "errorPage";
	    }
	}

}
