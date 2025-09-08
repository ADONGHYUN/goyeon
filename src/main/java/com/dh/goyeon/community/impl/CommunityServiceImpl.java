package com.dh.goyeon.community.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dh.goyeon.community.CommunityBean;
import com.dh.goyeon.community.CommunityService;


@Service("communityService")
public class CommunityServiceImpl implements CommunityService{

	@Autowired
	private CommunityDAOMybaits communityDAO;
	
	@Override
	public List<CommunityBean> getSchoolPicture() {
		List<CommunityBean> schoolPicture = communityDAO.getSchoolPicture();
		return schoolPicture;
	}

	@Override
	public void insertSchoolPic(CommunityBean cBean) {
		communityDAO.insertSchoolPiccBean(cBean);		
	}
}
