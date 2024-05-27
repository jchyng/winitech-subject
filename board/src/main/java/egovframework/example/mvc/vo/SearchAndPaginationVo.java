package egovframework.example.mvc.vo;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/** 검색과 페이징에 사용될 VO 객체 = 전자정부 샘플 코드 기반 */
public class SearchAndPaginationVo implements Serializable {
	private static final long serialVersionUID = -858838578081269359L;

	/* 검색 */
	private String searchCondition = "member"; //검색 조건
	private String searchKeyword = ""; 	 	//검색 키워드
	
	/* 페이지네이션 */
	private int pageIndex = 1;		//현재 페이지
	private int pageUnit = 8;		//페이지 개수
	private int pageSize = 10;		//페이지 사이즈
	private int recordCountPerPage = 8;	//페이지 당 컨텐츠 수

	private int firstIndex = 1;		//첫 번째 인덱스
	private int lastIndex = 1;		//마지막 인덱스
	
	
	public String getSearchCondition() {
		return searchCondition;
	}

	public void setSearchCondition(String searchCondition) {
		this.searchCondition = searchCondition;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
	
	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageUnit() {
		return pageUnit;
	}

	public void setPageUnit(int pageUnit) {
		this.pageUnit = pageUnit;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getRecordCountPerPage() {
		return recordCountPerPage;
	}

	public void setRecordCountPerPage(int recordCountPerPage) {
		this.recordCountPerPage = recordCountPerPage;
	}

	public int getFirstIndex() {
		return firstIndex;
	}

	public void setFirstIndex(int firstIndex) {
		this.firstIndex = firstIndex;
	}


	public int getLastIndex() {
		return lastIndex;
	}

	public void setLastIndex(int lastIndex) {
		this.lastIndex = lastIndex;
	}

	

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
