package egovframework.example.mvc.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

import egovframework.example.mvc.dto.enums.SearchCondition;

/** 
 * 검색과 페이징에 사용될 VO 객체 = 전자정부 샘플 코드 기반
 */
public class PaginationDTO implements Serializable {
	private static final long serialVersionUID = -858838578081269359L;

	/* 검색 */
	private String searchKeyword = ""; 	 	 //검색 키워드
	private SearchCondition searchCondition = SearchCondition.TITLE; //검색 조건
	
	/* 페이지네이션 */
	private int pageIndex = 1;		//현재 페이지
	private int pageUnit = 10;		//페이지 개수
	private int pageSize = 10;		//페이지 사이즈
	private int recordCountPerPage = 10;	//페이지 당 컨텐츠 수

	private int firstIndex = 1;		//첫 번째 인덱스
	private int lastIndex = 1;		//마지막 인덱스
	

	
	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
	
	public SearchCondition getSearchCondition() {
		return searchCondition;
	}
	

	public void setSearchCondition(SearchCondition searchCondition) {
		this.searchCondition = searchCondition;
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
