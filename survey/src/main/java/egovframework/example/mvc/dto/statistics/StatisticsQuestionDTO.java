package egovframework.example.mvc.dto.statistics;

import java.util.List;

import egovframework.example.mvc.vo.enums.QuestionType;



public class StatisticsQuestionDTO {
	private Long id;
	private QuestionType type;
	private int totalAnswerCount;
	private String content;
	private List<StatisticsAnswerDTO> answers;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public QuestionType getType() {
		return type;
	}

	public void setType(QuestionType type) {
		this.type = type;
	}

	public int getTotalAnswerCount() {
		return totalAnswerCount;
	}

	public void setTotalAnswerCount(int totalAnswerCount) {
		this.totalAnswerCount = totalAnswerCount;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<StatisticsAnswerDTO> getAnswers() {
		return answers;
	}

	public void setAnswers(List<StatisticsAnswerDTO> answers) {
		this.answers = answers;
	}

}
