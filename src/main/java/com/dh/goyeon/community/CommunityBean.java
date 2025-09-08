package com.dh.goyeon.community;

public class CommunityBean {

    private int cnum;
    private String uid;
    private String introduction;
    private String ccategory;
    private java.sql.Timestamp createdAt;
    private String image;

    // 기본 생성자
    public CommunityBean() {}

    // 생성자 (필요에 따라 작성)
    public CommunityBean(int cnum, String uid, String introduction, String ccategory, java.sql.Timestamp createdAt, String image) {
        this.cnum = cnum;
        this.uid = uid;
        this.introduction = introduction;
        this.ccategory = ccategory;
        this.createdAt = createdAt;
        this.image = image;
    }

    // Getter와 Setter 메서드
    public int getCnum() {
        return cnum;
    }

    public void setCnum(int cnum) {
        this.cnum = cnum;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getCcategory() {
        return ccategory;
    }

    public void setCcategory(String ccategory) {
        this.ccategory = ccategory;
    }

    public java.sql.Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.sql.Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}

