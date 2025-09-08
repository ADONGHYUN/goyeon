package com.dh.goyeon.controller;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.dh.goyeon.board.BoardBean;
import com.dh.goyeon.board.BoardService;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@Controller
public class TinyController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/board/tiny/free")
    public String tinyFree(HttpSession session,Model model) {
    	String userId = (String)session.getAttribute("userId");
    	model.addAttribute("userId", userId);
    	model.addAttribute("bcategory", "자유게시판 자유게시판");
        return "/board/tiny";
    }
    
    @GetMapping("/board/tiny/{bcategory}")
    public String tiny(@PathVariable String bcategory, HttpSession session,Model model) {
    	String userId = (String)session.getAttribute("userId");
    	model.addAttribute("userId", userId);
    	model.addAttribute("bcategory", bcategory);    	
        return "/board/tiny";
    }

    
    @PostMapping("/board/uploadImage")
    public void uploadImage(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 세션에서 사용자 ID 가져오기
        String userId = (String) session.getAttribute("userId");
        
        // 클라이언트에서 전송된 파일 가져오기
        Part filePart = request.getPart("file"); 
        String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        
        // 파일명 설정 (사용자 ID + 원본 파일명)
        String fileName = userId + "_" + originalFileName;
        
        // 업로드할 디렉토리 경로 설정
        String uploadDir = request.getServletContext().getRealPath("/resources/uploads");
        
        // 디렉토리가 없으면 생성
        File uploadFileDir = new File(uploadDir);
        if (!uploadFileDir.exists()) {
            uploadFileDir.mkdirs();
        }
        
        // 파일 저장
        filePart.write(uploadDir + File.separator + fileName);
        
        // 저장된 파일의 URL 생성
        String fileUrl = "/resources/uploads/" + fileName;
        System.out.println("fileUrl::"+fileUrl);
        // TinyMCE에 응답으로 이미지 URL 전송
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"location\": \"" + fileUrl + "\"}");
    }



    @PostMapping("/board/writeBoard")
    public String writeBoard(@ModelAttribute("boardBean") BoardBean boardBean, Model model, 
    		HttpSession session, HttpServletRequest request) throws IOException, ServletException {
    	try {
    	System.out.println("맵핑성공");
        System.out.println("model 값 :" + model);
        System.out.println("request 값 :" + request);
        System.out.println("getBcategory:"+boardBean.getBcategory());
        
        String saveFolder = "/resources/Thumbnail";
        ServletContext context = request.getServletContext();
        String realFolder = context.getRealPath(saveFolder);

        // Create upload directory if it does not exist
        File uploadDir = new File(realFolder);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        MultipartFile bimageFile = boardBean.getBimageFile();
        if (bimageFile != null && !bimageFile.isEmpty()) {
            // 파일 이름 얻기
            String originalFilename = bimageFile.getOriginalFilename();

            // 파일명 중복 방지를 위해 사용자 ID와 시간 추가
            String userId = (String) session.getAttribute("userId");
            String fileName = userId + "_" + System.currentTimeMillis() + "_" + originalFilename;
            
            System.out.println("fileName:"+fileName);
            // 파일을 저장할 전체 경로
            String filePath = realFolder + File.separator + fileName;
            
            // 파일 저장
            File dest = new File(filePath);
            bimageFile.transferTo(dest); // 파일을 지정된 경로에 저장

            // boardBean에 파일명 설정
            boardBean.setBimage(fileName);
        } else {
            System.out.println("이미지 파일이 업로드되지 않았습니다.");
        }
        
        boardBean.setUid((String)session.getAttribute("userId"));
        boardService.writeBoard(boardBean);
        if(boardBean.getBcategory().equals("자유게시판 자유게시판")) {
        	return "redirect:/board/boardList/page/1";
        }else {
        String encodedBcategory;		
		encodedBcategory = new URI(null, null, boardBean.getBcategory(), null).toASCIIString();	
		return "redirect:/board/boardList/" + encodedBcategory;
        }
        } catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }

    @GetMapping("/board/update/{bnum}")
	public String editForm(@PathVariable("bnum") int bnum, Model model) {
    	System.out.println("update:: 맵핑"+"bnum::"+bnum);
	    BoardBean post = boardService.getBoard(bnum);

	    model.addAttribute("post", post);
	    return "/board/postUpdate";  // 수정 폼으로 이동
	}	
    
    @PostMapping(value = "/board/update/{bnum}")
    public String updatePost(@ModelAttribute("boardBean") BoardBean boardBean, Model model, 
                             HttpSession session, HttpServletRequest request) throws IOException, ServletException {
    	try {
        	System.out.println("맵핑성공");
            System.out.println("model 값 :" + model);
            System.out.println("request 값 :" + request);
            System.out.println("getBcategory:"+boardBean.getBcategory());
            System.out.println("getBimage ::"+boardBean.getBimage());
            String saveFolder = "/resources/Thumbnail";
            ServletContext context = request.getServletContext();
            String realFolder = context.getRealPath(saveFolder);
            System.out.println("realFolder ::"+realFolder);
            // Create upload directory if it does not exist
            File uploadDir = new File(realFolder);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

         // 기존 이미지 삭제 로직
            String existingImage = boardBean.getBimage(); // 기존 이미지 파일명
            if (existingImage != null && !existingImage.isEmpty()) {
                File oldImageFile = new File(realFolder + File.separator + existingImage);
                if (oldImageFile.exists()) {
                    boolean deleted = oldImageFile.delete(); // 기존 이미지 파일 삭제
                    if (deleted) {
                        System.out.println("기존 이미지 파일 삭제 성공: " + existingImage);
                    } else {
                        System.out.println("기존 이미지 파일 삭제 실패: " + existingImage);
                    }
                }
            }
            
            MultipartFile bimageFile = boardBean.getBimageFile();
            if (bimageFile != null && !bimageFile.isEmpty()) {
            	System.out.println("if문 ::"+bimageFile);
                // 파일 이름 얻기
                String originalFilename = bimageFile.getOriginalFilename();

                // 파일명 중복 방지를 위해 사용자 ID와 시간 추가
                String userId = (String) session.getAttribute("userId");
                String fileName = userId + "_" + System.currentTimeMillis() + "_" + originalFilename;

                // 파일을 저장할 전체 경로
                String filePath = realFolder + File.separator + fileName;
                System.out.println("filePath ::"+filePath);
                // 파일 저장
                File dest = new File(filePath);
                bimageFile.transferTo(dest); // 파일을 지정된 경로에 저장
                System.out.println("fileName ::"+fileName);
                // boardBean에 파일명 설정
                boardBean.setBimage(fileName);
                
                
            } else {
                System.out.println("이미지 파일이 업로드되지 않았습니다.");
            }
            
            boardBean.setUid((String)session.getAttribute("userId"));

            // 게시글 업데이트
            boardService.updatePost(boardBean);

            // 카테고리에 따라 페이지 리다이렉트
            if (boardBean.getBcategory().equals("자유게시판 자유게시판")) {
                return "redirect:/board/boardList/page/1";
            } else {
                // URI 인코딩 처리
                String encodedBcategory = new URI(null, null, boardBean.getBcategory(), null).toASCIIString();
                return "redirect:/board/boardList/" + encodedBcategory;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    
}
