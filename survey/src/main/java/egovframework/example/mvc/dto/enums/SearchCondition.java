package egovframework.example.mvc.dto.enums;

/** 검색 조건 */
public enum SearchCondition {
	TITLE("제목"), MEMBER("작성자");
	
	private String value;

	
	private SearchCondition(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
}