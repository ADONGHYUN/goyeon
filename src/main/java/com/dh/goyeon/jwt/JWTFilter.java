package com.dh.goyeon.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dh.goyeon.auth.CustomUserDetails;
import com.dh.goyeon.entity.UserEntity;
import com.dh.goyeon.user.UserBean;

import org.springframework.security.core.Authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTFilter extends OncePerRequestFilter {

	private final JWTUtil jwtUtil;

	public JWTFilter(JWTUtil jwtUtil) {

		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// request에서 Authorization 헤더를 찾음
		String authorization = request.getHeader("Authorization");

		System.out.println("jwt필터 : authorization값 : " + authorization);
		System.out.println("jwt필터2 : authorization값 : " + authorization);

		// Authorization 헤더 검증
		if (authorization == null || !authorization.startsWith("Bearer ")) {

			System.out.println("jwt필터 : token null");
			filterChain.doFilter(request, response);

			// 조건이 해당되면 메소드 종료 (필수)
			return;
		}

		// Bearer 중복 제거
	    String token = authorization.replaceFirst("^Bearer\\s+", "").trim();
	    if (token.startsWith("Bearer ")) {
	        token = token.replaceFirst("^Bearer\\s+", "").trim();
	    }

		try {
			// 토큰 소멸 시간 검증
			if (jwtUtil.isExpired(token)) {

				System.out.println("token expired");
				filterChain.doFilter(request, response);

				// 조건이 해당되면 메소드 종료 (필수)
				return;
			}

			// 토큰에서 username과 role 획득
			String username = jwtUtil.getUsername(token);
			String role = jwtUtil.getRole(token);

			UserBean userBean = new UserBean();
			userBean.setUid(username);
			userBean.setUpw("temppassword");
			userBean.setUgender("임시 Ugender");

			System.out.println("SecurityContextHolder 인증 주입 완료: " + token);
			

			// UserDetails에 회원 정보 객체 담기
			CustomUserDetails customUserDetails = new CustomUserDetails(userBean);

			// 스프링 시큐리티 인증 토큰 생성
			Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null,
					customUserDetails.getAuthorities());
			// 세션에 사용자 등록
			SecurityContextHolder.getContext().setAuthentication(authToken);
			System.out.println("현재 인증 객체: " + SecurityContextHolder.getContext().getAuthentication());

			filterChain.doFilter(request, response);
		} catch (Exception e) {
			System.out.println("JWT 처리 실패: " + e.getMessage());
			filterChain.doFilter(request, response);
		}
	}
}
