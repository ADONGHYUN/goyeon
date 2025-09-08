package com.dh.goyeon.user.controller;

import java.io.File;
import java.io.IOException;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import com.dh.goyeon.user.UserBean;
import com.dh.goyeon.user.UserService;
import com.dh.goyeon.util.NicknameGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
@SessionAttributes("userId")
public class UserClientController {
	@Autowired
	private UserService userService;	

	@GetMapping("/join")
	public String join(HttpSession session) {
		if (session.getAttribute("userId") != null) {
			String str = "redirect:/";
			return str;
		}
		return "/user/join";
	}

	// id 중복체크
	@PostMapping("/join/getId")
	@ResponseBody
	public String getId(@RequestParam(value="uid") String uid) {
		System.out.println("/join/getId 매핑");
		if (userService.getID(uid)) {
			return "yes";
		}
		return "no";
	}

	// 이메일 증복체크
	@PostMapping("/join/getMail")
	@ResponseBody
	public String getMail(@RequestParam(value="uemail") String uemail) {
		System.out.println(uemail);
		String user = userService.getSameMail(uemail);
		if (user != null) {
			return "yes";
		}
		return "no";
	}

	@PostMapping("/join")
	@ResponseBody
	public String join(@RequestBody UserBean user) {
		System.out.println("회원가입하기");
		user.setUnickname(NicknameGenerator.generate());
		System.out.println(user);
		int success = userService.insertUser(user);
		if (success > 0) {
			System.out.println("성공");
			return "yes";
		}
		System.out.println("실패");
		return "no";
	}

	@PostMapping("/findGetMail")
	@ResponseBody
	public String getIdFind(@RequestParam String uemail) throws Exception {
		System.out.println("입력받은 이메일: " + uemail);
		return userService.getMail(uemail);
	}
	
	@GetMapping("/findId")
	public String toIdFind() {
		System.out.println("아이디찾기폼으로");
		return "/user/findId";
	}

	@GetMapping("/findPw")
	public String toPwFind() {
		System.out.println("비밀번호찾기폼으로");
		return "/user/findPw";
	}
	
	@PostMapping("/findId")
	@ResponseBody
	public String getIdFindCode(@RequestBody UserBean user) {

		System.out.println("아이디 찾기");
		System.out.println(user);

		return userService.getMailCode(user);
	}
	
	@GetMapping("/searchNickname")
	@ResponseBody
	public UserBean searchNickname(@RequestParam("nickname") String nickname) {
	    System.out.println("닉네임 찾기: " + nickname);
	    return userService.searchNickname(nickname);
	}

	@PostMapping("/findIdResult")
	public String toFindIdResult(String uemail, Model model) throws Exception {
		System.out.println("이메일: " + uemail);
		UserBean list = userService.getFindId(uemail);
		model.addAttribute("userList", list);
		return "/user/findIdResult";
	}

	@PostMapping("/findGetFid")
	@ResponseBody
	public String getPwFind(@RequestParam String uid, @RequestParam String uemail) throws Exception {
		System.out.println("입력받은 아이디: " + uid);
		System.out.println("입력받은 이메일: " + uemail);
		return userService.getFid(uid,uemail);
	}
	
	@PostMapping("/findPw")
	@ResponseBody
	public String getPwFindCode(@RequestBody UserBean user) {

		System.out.println("패스워드 찾기");
		System.out.println(user);

		return userService.getMailCodePw(user);
	}

	@PostMapping("/findPwResult")
	public String toFindPwResult(String uid, Model model) throws Exception {
		System.out.println("아이디: " + uid);
		String conf = userService.getFindPw(uid);
		if(conf.equals("yes")) {
			model.addAttribute("uid", uid);
		}
		return "/user/findPwResult";
	}
	
	@PostMapping("/findPwChange")
	@ResponseBody
	public String pwChange(@RequestBody UserBean user) {

		System.out.println("비밀번호 찾기/변경");
		System.out.println(user);

		return userService.pwChange(user);
	}
	// 내정보
	@GetMapping("/myinfo")
	public String myInfo(@ModelAttribute("userId") String userId, Model model) {
		System.out.println();
		UserBean userBean = userService.getUser(userId);
		if (userBean != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode node = objectMapper.createObjectNode();
			if (userBean.getKapi() != null) {
				node.put("kakao", 1);
			}
			if (userBean.getNapi() != null) {
				node.put("naver", 1);
			}
			if (userBean.getUid().length() <= 12) {
				node.put("id", 1);
			}
			model.addAttribute("type", node.toString());
			model.addAttribute("myinfo", userBean);
			return "/user/myInfo";
		}
		return "redirect:/logout";
	}

	@PostMapping("/myInfo")
	@ResponseBody
	public String myInfoUpdate(@ModelAttribute UserBean userBean) {
		int updateResult = userService.update(userBean);
		return updateResult == 1 ? "1" : "0";
	}

	@GetMapping("/changepw")
	public String changePW(@ModelAttribute("userId") String userId, Model model) {
		model.addAttribute("userId", userId);
		return "/user/changepw";
	}

	@PostMapping("/pwUpdate")
	@ResponseBody
	public String pwUpdate(UserBean userBean) {
		int updateResult = userService.pwUpdate(userBean);
		return updateResult == 1 ? "1" : "0";
	}


	@GetMapping("/delete")
	public String delete(@ModelAttribute("userId") String userId, Model model) {
		model.addAttribute("userId", userId);
		return "/user/delete";
	}

	@PostMapping("delete")
	@ResponseBody
	public String delete2(UserBean userBean, HttpSession session, SessionStatus sessionStatus) {
		try {
		int delete = userService.delete(userBean.getUid());
		System.out.println("de: "+ delete);
		if (delete == 1) {
			sessionStatus.setComplete();
			session.invalidate();
			return "1";
		} else if(delete == 0){
			return "0";
		}else {
			return "2";
		}
		}catch (Exception e) {
			System.out.println("컨트롤러에서 받은 오류");
			e.printStackTrace();
			return "2";
		}
	}

	@PostMapping("/update-uimage")
	@ResponseBody
	public String updateUimage(UserBean userBean, HttpServletRequest request) throws IOException {
	    System.out.println("맵핑은성공");
	    String uploadDir = request.getServletContext().getRealPath("/resources/useruimages");
	    System.out.println("uploadDir: "+uploadDir);
	    System.out.println("userBean.uid"+userBean.getUid());
	    System.out.println("userBean.getuimageFile()"+userBean.getuimageFile());
	    File uploadDirectory = new File(uploadDir);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }
 
	    MultipartFile uimageFile = userBean.getuimageFile();

	    String userId = userBean.getUid();
	        // 파일명 생성 (사용자 ID 기반)
	        String fileName = userId+"_"+uimageFile.getOriginalFilename();
	        File uploadFile = new File(uploadDirectory, fileName);

	        // 파일 저장
	        uimageFile.transferTo(uploadFile);
	        userService.updateUimage(userId,fileName);
	        
	        
	        return "1";
	}

	


	
}
