package com.dh.goyeon.board;

import java.util.List;

import com.dh.goyeon.user.UserBean;

public interface BoardService {
	public List<BoardBean> getBoardList(int posts);
	public void writeBoard(BoardBean boardBean);
	public BoardBean getBoard(int bnum);
	public void updatePost(BoardBean boardBean);
	public void deletePost(int bnum);
	public List<BoardBean> getMyBoard(String uid);
	public int getboardCount(int page);
	public int createBoard(UserBean userBean);
	public List<String> getMyboardCategory (String userId);
	public List<String> myCategoryBoard(String userId, String bcategory);
	public int updateBoardOrder(String userId, List<String> order);
	public int getSearchCount(String searchType, String searchText);
	public List<BoardBean> getSearchCountList(String searchType, String searchText, int posts);
	public int getCateboardCount(String bcategory);
	public List<BoardBean> getCateBoardList(String bcategory, int posts);
	public int getCateSearchCount(String bcategory, String searchType, String searchText);
	public List<BoardBean> getCateSearchCountList(String bcategory, String searchType, String searchText, int posts);
	public void deleteBoard(String userId, String boardName);
	
	
}
