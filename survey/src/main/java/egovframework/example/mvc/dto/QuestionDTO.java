package egovframework.example.mvc.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import egovframework.example.mvc.vo.FileVO;
import egovframework.example.mvc.vo.SelectOptionVO;
import egovframework.example.mvc.vo.enums.QuestionType;

public class QuestionDTO {
	private Long id;
	private Long surveyId;
	private String content;
	private QuestionType type;	
	private int position;
	private boolean isRequired;
	private List<SelectOptionVO> options;
	private FileVO fileVO;
	private MultipartFile file;

	
	/** 질문 객체 저장 후 반환 받은 id로 객관식 옵션의 질문 아이디 세팅 */
	public void setQuestionIdForOptions() {
		for (SelectOptionVO vo : options) {
			vo.setQuestionId(id);
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(Long surveyId) {
		this.surveyId = surveyId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public QuestionType getType() {
		return type;
	}

	public void setType(QuestionType type) {
		this.type = type;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}
	
	/* Controller에서 DTO 변환 시에는 getIs, setIs를 써야함 = boolean getter setter 규칙이 적용 안되는 듯*/
	public boolean getIsRequired() {
		return isRequired;
	}
	
	public void setIsRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public List<SelectOptionVO> getOptions() {
		return options;
	}

	public void setOptions(List<SelectOptionVO> options) {
		this.options = options;
	}

	public FileVO getFileVO() {
		return fileVO;
	}

	public void setFileVO(FileVO fileVO) {
		this.fileVO = fileVO;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

}
