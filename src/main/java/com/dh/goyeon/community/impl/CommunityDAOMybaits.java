package com.dh.goyeon.community.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.dh.goyeon.community.CommunityBean;



@Repository
public class CommunityDAOMybaits {
	@Autowired
	private SqlSessionTemplate mybatis;
	
	public List<CommunityBean> getSchoolPicture(){
		List<CommunityBean> schoolPicture = mybatis.selectList("communityDAO.getSchoolPicture");
		return schoolPicture;
    	   						
	}

	public void insertSchoolPiccBean(CommunityBean cBean) {
		mybatis.insert("communityDAO.insertSchoolPic", cBean);
		
	}
	

	
}
