package com.dh.goyeon.board;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

public class BoardBean {
    private int bnum; // 게시글 번호
    private String uid; // 사용자 ID
    private String title; // 제목
    private String subtitle;
    private String bcategory;
	private String content; // 내용
    private String writer; // 작성자
    private String bimage; // 이미지 (옵션)
    private MultipartFile bimageFile;
    private LocalDateTime created_at; // 작성일
	private LocalDateTime updated_at; // 수정일


    
    // 기본 생성자
    public BoardBean() {}



   



	// Getter and Setter methods
    public int getBnum() {
        return bnum;
    }

    public void setBnum(int bnum) {
        this.bnum = bnum;
    }

    public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }   
    
    public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

    public String getBcategory() {
		return bcategory;
	}

	public void setBcategory(String bcategory) {
		this.bcategory = bcategory;
	}

	public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBimage() {
        return bimage;
    }

    public void setBimage(String bimage) {
        this.bimage = bimage;
    }

    public MultipartFile getBimageFile() {
		return bimageFile;
	}

	public void setBimageFile(MultipartFile bimageFile) {
		this.bimageFile = bimageFile;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}

	public LocalDateTime getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(LocalDateTime updated_at) {
		this.updated_at = updated_at;
	}
    



    
}
