package com.dh.goyeon.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dh.goyeon.auth.LoginService;
import com.dh.goyeon.user.UserBean;
import com.dh.goyeon.user.UserService;
import com.dh.goyeon.user.impl.State;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
	@Autowired
	UserService userService;
	@Autowired
	private LoginService loginService;
	@Autowired
	State state;
	
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@GetMapping("/")
	public String index() {
		//String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		return "index";
	}	
	
	@GetMapping("/login")
	public String login() {
		return "/user/login";
	}	
	
	/*
	 * @PostMapping("/login")
	 * 
	 * @ResponseBody public String login(@ModelAttribute UserBean user, HttpSession
	 * session) { System.out.println("user: " + user); // logger.debug("로그 user " +
	 * user); if(userService.isUser(user)) {
	 * System.out.println("isUser== true 로그인성공!"); session.setAttribute("userId",
	 * user.getUid()); System.out.println("session의 Id, user.getId()의 값"+
	 * session.getAttribute("userId") +"----"+ user.getUid()); return
	 * "/board/boardList"; } return "0"; }
	 */
	
	@PostMapping("/kakaoapi")
	@ResponseBody
	public String getKaAPI() {
		return loginService.getKaUrl()+URLEncoder.encode(state.ranString(), StandardCharsets.UTF_8);
	}
	
	@PostMapping("/naverapi")
	@ResponseBody
	public String getNaAPI() {
	    return loginService.getNaUrl()+URLEncoder.encode(state.ranString(), StandardCharsets.UTF_8);
	}
	
	@GetMapping("/kakao-login") 
	public void kakaoLogin(HttpSession session, 
			@RequestParam("code") String code,
			@RequestParam("state") String stateParam,
			@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "error_description", required = false) String error_description,
							HttpServletResponse response) throws IOException {
		apiLogin(session, code, stateParam, error, error_description, "kakao", response);
	}
	
	@GetMapping("/naver-login")
	@ResponseBody
	public void naverLogin(HttpSession session,
							@RequestParam("code") String code,
							@RequestParam("state") String stateParam,
							@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "error_description", required = false) String error_description,
							HttpServletResponse response) throws IOException {
		apiLogin(session, code, stateParam, error, error_description, "naver", response);
	}
	
	private void apiLogin(HttpSession session, String code, String stateParam, String error, String error_description,
            String loginType, HttpServletResponse response) throws IOException {
		
		response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
		if (error == null && state.stateEquals(stateParam)) {
			// 의미는 없지만 객체 재사용 여기부터는 id로 바뀜
			loginType = "naver".equals(loginType) ? loginService.naVerify(code) : loginService.kaVerify(code);
	        if (loginType != null) {
	            session.setAttribute("userId", loginType);
	            String url = (String) session.getAttribute("url");
				session.removeAttribute("url");
	            
				out.println("<html><body><script>");
	            out.println("window.opener.location.replace(" + (url == null || url.isEmpty() ? "'/'" : "'" + url + "'") + ");");
	            out.println("window.close();</script></body></html>");
	            return;
	        }
	    } else {
	        System.out.println("Error Code: " + error + "\n" + "ErrorMessage : " + error_description);
	        System.out.println("위 에러가 Null이라면 State가 다르다는 의미입니다. ");
	        logger.error("API 로그인 실패: error={}, description={}, stateValid={} ", error, error_description, state.stateEquals(stateParam));
	    }
		
		out.println("<html><body><script>");
        out.println("alert('로그인을 실패했습니다. 문의 주세요.'); window.close();");
        out.println("</script></body></html>");
	}

	@GetMapping("disconnectKakao")
	@ResponseBody
	public String dconnKkao(@ModelAttribute("userId") String userId) {
		return String.valueOf(loginService.dconKka(userId));
	}

	@GetMapping("disconnectNaver")
	@ResponseBody
	public String dconnNa(@ModelAttribute("userId") String userId) {
		return String.valueOf(loginService.dconNa(userId));
	}

}
