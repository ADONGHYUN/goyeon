package com.dh.goyeon.community;

import java.util.List;

public interface CommunityService {

	List<CommunityBean> getSchoolPicture();

	void insertSchoolPic(CommunityBean cBean);

}
