package egovframework.example.mvc.vo.enums;


/**
 * 설문조사 질문 타입을 관리하기 위한 ENUM
 */
public enum QuestionType {
	//주관식
	SHORT("단답형", "input"), LONG("장문형", "textarea"),
	//객관식
	MULTIPLE_ONE("객관식(1)", "radio"), MULTIPLE_MANY("객관식(N)", "checkbox");
	
	private final String value;
	private final String html;
	
	private QuestionType(String value, String html) {
		this.value = value;
		this.html = html;
	}

	public String getValue() {
		return value;
	}

	public String getHtml() {
		return html;
	}

}