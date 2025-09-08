package com.dh.goyeon.util;

public class Page {
		int displayPageNum = 5;
		int displayPostNum;
		private int totalData;
		private int currentPage;
		private int totalPages = 1;
		private int startPage = 1;
		private int endPage = 1;
		private int lastStartPage = 1;
		private int offset; // limit offset에서 offset담당
		
		public Page() {
			
		}
		
		public Page(int totalData, int currentPage, int displayPostNum) {
			this.totalData = totalData;
			this.currentPage = currentPage;
			if (totalData != 0) {
				totalPages = totalData / displayPostNum;
				if (totalData % displayPostNum > 0) {
					totalPages++;
				}

				lastStartPage = ((totalPages -1)/5)*5 +1;
				
				
				offset = (currentPage - 1)*displayPostNum; // limit offset에서 offset담당
				
				startPage = ((currentPage- 1) / 5) * 5 + 1;
				endPage = startPage + 4;
				if (endPage > totalPages) endPage = totalPages;
			}
		}

		
		public int getDisplayPostNum() {
			return displayPostNum;
		}

		public void setDisplayPostNum(int displayPostNum) {
			this.displayPostNum = displayPostNum;
		}

		public int getTotalData() {
			return totalData;
		}

		public boolean hasNoSpaces() {
			return totalData == 0;
		}

		public boolean hasSpaces() {
			return totalData > 0;
		}
		
		public int getCurrentPage() {
			return currentPage;
		}

		public int getTotalPages() {
			return totalPages;
		}

		public int getStartPage() {
			return startPage;
		}
		
		public int getEndPage() {
			return endPage;
		}
		public int getOffset() {
			return offset;
		}
		public void setOffset(int offset) {
			this.offset = offset;
		}
		public int getLastStartPage() {
			return lastStartPage;
		}
		public void setLastStartPage(int lastStartPage) {
			this.lastStartPage = lastStartPage;
		}
	

}
