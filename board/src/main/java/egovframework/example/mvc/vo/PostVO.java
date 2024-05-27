package egovframework.example.mvc.vo;

import java.time.LocalDateTime;



public class PostVO {
	private Long id;
	private Long memberId;
	private String title;
	private String content;
	private String thumbnail;
	private Long viewCount;
	private LocalDateTime regDate;
	private LocalDateTime modDate;
	private boolean useYn;
	
	
	public PostVO() {
	}

	public PostVO(Long memberId, String title, String content, String thumbnail) {
		this.memberId = memberId;
		this.title = title;
		this.content = content;
		this.thumbnail = thumbnail;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Long getViewCount() {
		return viewCount;
	}

	public void setViewCount(Long viewCount) {
		this.viewCount = viewCount;
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
