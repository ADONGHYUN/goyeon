package com.dh.goyeon.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.dh.goyeon.community.CommunityBean;
import com.dh.goyeon.community.CommunityService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class CommunityController {
	private static final Logger logger = LoggerFactory.getLogger(CommunityController.class);
	@Autowired CommunityService communityService;
	
	
	@GetMapping("/community/school-picture")
	@ResponseBody
	public List<CommunityBean> getSchoolPicture(Model model) {
		System.out.println("/school-picture 맵핑");
		return communityService.getSchoolPicture();
	}
	
	@PostMapping("/community/school-picture")
	@ResponseBody
	public ResponseEntity<String> schoolPicture(
	        @ModelAttribute CommunityBean cBean, 
	        @RequestParam("schoolPicture") MultipartFile schoolPicture, 
	        HttpServletRequest request, 
	        HttpSession session) {
		// MIME 타입 체크 (image로 시작하는지 확인)
	    if (!schoolPicture.getContentType().startsWith("image/")) {
	        return ResponseEntity.badRequest().body("이미지 파일만 업로드할 수 있습니다.");
	    }
		
	    String uploadDir = request.getServletContext().getRealPath("/resources/schoolPictures/");
	    File dir = new File(uploadDir);
	    if (!dir.exists()) {
	        dir.mkdirs();
	    }
	    logger.info("Upload 폴더 ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇaaㅇ"+ uploadDir+"aaa");
	    //C:\Users\스터디룸 PC 3\AppData\Local\Temp\tomcat-docbase.8080.638586614424918892\resources\schoolPictures
	    String userId = (String) session.getAttribute("userId");
	    cBean.setUid(userId);

	    try {
	        // 파일 저장
	        String fileName = System.currentTimeMillis() + "_" + schoolPicture.getOriginalFilename();
	        File destinationFile = new File(uploadDir + fileName);
	        schoolPicture.transferTo(destinationFile);
	        cBean.setImage(fileName);

	        // 데이터베이스에 저장
	        communityService.insertSchoolPic(cBean);
	        return ResponseEntity.ok("사진 등록이 완료되었습니다.");
	    } catch (IOException e) {
	        e.printStackTrace();
	        logger.error("사진 등록 중 오류가 발생했습니다.", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("사진 등록 중 오류가 발생했습니다.");
	    }
	}

}
