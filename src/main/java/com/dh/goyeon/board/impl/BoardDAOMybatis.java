package com.dh.goyeon.board.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.dh.goyeon.board.BoardBean;
import com.dh.goyeon.user.UserBean;

@Repository
public class BoardDAOMybatis {
	@Autowired
	private SqlSessionTemplate mybatis;

	public int getboardCount(int page) {
		int getboardCount = mybatis.selectOne("boardMapper.getboardCount", page);
		return getboardCount;
	} 
	
	public List<BoardBean> getBoardList(int posts){
		List<BoardBean> boardList = mybatis.selectList("boardMapper.getBoardList", posts);
		return boardList;
    	   						
	}
	 
	public int getSearchCount(String searchType, String searchText) {
		Map<String, Object> params = new HashMap<>();
		params.put("searchType", searchType);	   
		params.put("searchText", searchText);	 
		return mybatis.selectOne("boardMapper.getSearchCount", params);
	}

	public List<BoardBean> getSearchCountList(String searchType, String searchText, int posts) {
		Map<String, Object> params = new HashMap<>();
		params.put("searchType", searchType);	   
		params.put("searchText", searchText);	
		params.put("posts", posts);
		return mybatis.selectList("boardMapper.getSearchCountList", params);
	}
	
	public int getCateboardCount(String bcategory) {
		System.out.println("bcategory::"+bcategory);
		int getCateboardCount = mybatis.selectOne("boardMapper.getCateboardCount",bcategory);
		System.out.println("getCateboardCount::"+getCateboardCount);
		return getCateboardCount;
	} 
	
	public List<BoardBean> getCateBoardList(String bcategory, int posts){
		Map<String, Object> params = new HashMap<>();
		params.put("bcategory", bcategory);
		params.put("posts", posts);
		System.out.println("posts:"+posts);
		List<BoardBean> boardList = mybatis.selectList("boardMapper.getCateBoardList", params);
		return boardList;
    	   						
	}
	 
	public int getCateSearchCount(String bcategory, String searchType, String searchText) {
		Map<String, Object> params = new HashMap<>();
		params.put("bcategory", bcategory);
		params.put("searchType", searchType);	   
		params.put("searchText", searchText);	 
		return mybatis.selectOne("boardMapper.getCateSearchCount", params);
	}

	public List<BoardBean> getCateSearchCountList(String bcategory, String searchType, String searchText, int posts) {
		Map<String, Object> params = new HashMap<>();
		params.put("bcategory", bcategory);
		params.put("searchType", searchType);	   
		params.put("searchText", searchText);	
		params.put("posts", posts);
		return mybatis.selectList("boardMapper.getCateSearchCountList", params);
	}
	
	
	 public BoardBean getBoard(int bnum){
		System.out.println("BoardDAOMybatis의 getBoard 왔음");
		BoardBean board = mybatis.selectOne("boardMapper.getBoard", bnum);	
		return board;    	   					
	}
	 
	 public void writeBoard(BoardBean boardBean) {
		 mybatis.insert("boardMapper.writeBoard", boardBean);		
		}

	 public void updatePost(BoardBean boardBean) {
		 mybatis.update("boardMapper.updatePost", boardBean);				
		}
	 
	public void deletePost(int bnum) {
		mybatis.delete("boardMapper.deletePost", bnum);	
	}

	public List<BoardBean> getMyBoard(String uid) {
		List<BoardBean> boardList = mybatis.selectList("boardMapper.getMyBoard", uid);
		System.out.println("BoardDAOMybatis의 getMyBoard의 boardList값 = "+ boardList);
		return boardList;
	}



	

	public int createBoard(UserBean userBean) {
        return mybatis.update("boardMapper.createBoard", userBean);
	}

	public List<String> getMyboardCategory(String userId) {
		String bcategory = mybatis.selectOne("boardMapper.getMyBoardCategory", userId);
		if (bcategory != null && !bcategory.isEmpty()) {
            return Arrays.asList(bcategory.split(","));
        }
        return Collections.emptyList();
    }

	public List<String> myCategoryBoard(String userId, String bcategory) {
		UserBean userBean = new UserBean();
		userBean.setUid(userId);
		//userBean.setBcategory(bcategory);
		return mybatis.selectList("boardMapper.myCategoryBoard", userBean);
	}

	public int updateBoardOrder(String userId, List<String> order) {
	    Map<String, Object> params = new HashMap<>();
	    String bcategoryOrder = String.join(", ", order);
	    System.out.println("bcategoryOrder: "+ bcategoryOrder);
	    params.put("uid", userId);	   
	    params.put("bcategoryOrder", bcategoryOrder);	    
	    return mybatis.update("boardMapper.updateBoardOrder", params);
	}

	public void deleteBoard(String userId, String boardName) {
		Map<String, Object> params = new HashMap<>();
		params.put("uid", userId);	   
	    params.put("boardName", boardName);	  
	    System.out.println(boardName);
		mybatis.update("userDAO.deleteBoard", params);
		mybatis.delete("boardMapper.deleteBoard", params);	
	}

	

	
	

}
