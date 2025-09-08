package com.dh.goyeon.board.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dh.goyeon.board.BoardBean;
import com.dh.goyeon.board.BoardService;
import com.dh.goyeon.user.UserBean;
@Service("boardService")
public class BoardServiceImpl implements BoardService{

		@Autowired
		private BoardDAOMybatis boardDAO;

		@Override
		public int getboardCount(int page) {
			int getboardCount = boardDAO.getboardCount(page);
			return getboardCount;
		}
		
		@Override
		public List<BoardBean> getBoardList(int posts) {
			List<BoardBean> boardList = boardDAO.getBoardList(posts);
			return boardList;
		}
		
		@Override
		public int getSearchCount(String searchType, String searchText) {
			return boardDAO.getSearchCount(searchType, searchText);
		}
		
		@Override
		public List<BoardBean> getSearchCountList(String searchType, String searchText, int posts) {
			return boardDAO.getSearchCountList(searchType, searchText, posts);
		}
	
		
		@Override
		public int getCateboardCount(String bcategory) {
			int getboardCount = boardDAO.getCateboardCount(bcategory);
			return getboardCount;
		}
		
		@Override
		public List<BoardBean> getCateBoardList(String bcategory, int posts) {
			List<BoardBean> boardList = boardDAO.getCateBoardList(bcategory, posts);
			return boardList;
		}
		
		@Override
		public int getCateSearchCount(String bcategory, String searchType, String searchText) {
			return boardDAO.getCateSearchCount(bcategory, searchType, searchText);
		}
		
		@Override
		public List<BoardBean> getCateSearchCountList(String bcategory, String searchType, String searchText, int posts) {
			return boardDAO.getCateSearchCountList(bcategory, searchType, searchText, posts);
		}
		
		
		
		
		
		
		
		@Override
		public BoardBean getBoard(int bnum) {
			System.out.println("Boardservice의 getBoard 왔음");
			BoardBean board = boardDAO.getBoard(bnum);
			return board;
		}
			
		@Override
		public List<BoardBean> getMyBoard(String uid) {
			List<BoardBean> boardList = boardDAO.getMyBoard(uid);
			return boardList;
		}
		
		@Override
		public void writeBoard(BoardBean boardBean) {
			boardDAO.writeBoard(boardBean);
			
		}

		@Override
		public void updatePost(BoardBean boardBean) {
			boardDAO.updatePost(boardBean);			
		}
		
		@Override
		public void deletePost(int bnum) {
			boardDAO.deletePost(bnum);
			
		}

		

		@Override
		public List<String> getMyboardCategory(String userId) {
			return boardDAO.getMyboardCategory(userId);
		}
		
		@Override
		public int createBoard(UserBean userBean) {
			return boardDAO.createBoard(userBean);
		}

		@Override
		public List<String> myCategoryBoard(String userId, String bcategory) {
			return boardDAO.myCategoryBoard(userId, bcategory);
		}

		@Override
		public int updateBoardOrder(String userId,List<String> order) {
			System.out.println("order :"+order);
			return boardDAO.updateBoardOrder(userId, order);
		}

		@Override
		public void deleteBoard(String userId, String boardName) {
			boardDAO.deleteBoard(userId, boardName);			
		}

		

		

		

		

		
}
