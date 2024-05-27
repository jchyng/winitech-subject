package egovframework.example.mvc.dto;

import java.util.Date;
import java.util.List;


/**
 * 설문 데이터 전송 오브젝트
 */
public class SurveyDTO {
	private Long id;
	private Long memberId;
	private String memberName;
	private String title;
	private String description;
	private Date stDate;
	private Date edDate;
	private List<QuestionDTO> questions;

	/** 설문조사 객체 저장 후 반환 받은 id로 질문의 설문 아이디 세팅 */
	public void setSurveyIdForQuestions() {
		for (QuestionDTO dto : questions) {
			dto.setSurveyId(this.id);
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public Date getStDate() {
		return stDate;
	}

	public void setStDate(Date stDate) {
		this.stDate = stDate;
	}

	public Date getEdDate() {
		return edDate;
	}

	public void setEdDate(Date edDate) {
		this.edDate = edDate;
	}

	public List<QuestionDTO> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionDTO> questions) {
		this.questions = questions;
	}
}
