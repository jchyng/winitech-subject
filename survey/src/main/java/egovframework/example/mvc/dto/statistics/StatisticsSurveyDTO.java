package egovframework.example.mvc.dto.statistics;

import java.util.Date;
import java.util.List;

/**
 * 통계 데이터 전송 오브젝트
 * */
public class StatisticsSurveyDTO {
	private Long id;
	private String title;
	private String description;
	private Date stDate;
	private Date edDate;
	private List<StatisticsQuestionDTO> questions;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<StatisticsQuestionDTO> getQuestions() {
		return questions;
	}

	public void setQuestions(List<StatisticsQuestionDTO> questions) {
		this.questions = questions;
	}

}
