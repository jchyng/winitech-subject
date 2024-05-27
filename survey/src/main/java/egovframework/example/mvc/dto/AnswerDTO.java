package egovframework.example.mvc.dto;

import java.util.List;

import egovframework.example.mvc.vo.AnswerVO;

public class AnswerDTO {
	private Long surveyId;
	private Long memberId;
	private List<AnswerVO> vos;

	
	public Long getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(Long surveyId) {
		this.surveyId = surveyId;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public List<AnswerVO> getVos() {
		return vos;
	}

	public void setVos(List<AnswerVO> vos) {
		this.vos = vos;
	}

}
