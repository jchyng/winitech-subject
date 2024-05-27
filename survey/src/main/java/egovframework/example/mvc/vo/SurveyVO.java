package egovframework.example.mvc.vo;

import java.time.LocalDateTime;
import java.util.Date;

public class SurveyVO {
	private Long id;
	private Long memberId;
	private String title;
	private String description;
	private Date stDate;
	private Date edDate;
	private LocalDateTime regDate;
	private LocalDateTime modDate;
	private boolean useYn;
	
	

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

	public LocalDateTime getRegDate() {
		return regDate;
	}

	public void setRegDate(LocalDateTime regDate) {
		this.regDate = regDate;
	}

	public LocalDateTime getModDate() {
		return modDate;
	}

	public void setModDate(LocalDateTime modDate) {
		this.modDate = modDate;
	}

	public boolean isUseYn() {
		return useYn;
	}

	public void setUseYn(boolean useYn) {
		this.useYn = useYn;
	}

}
