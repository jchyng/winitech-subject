package egovframework.example.board.vo;

import java.time.LocalDateTime;



public class BoardVO {
	private Long id;			//PK
	private Long parentId;		//부모 아이디
	private String title;		//제목	[INDEX]
	private String content;		//내용
	private String member;		//회원(현재는 이름)		[INDEX]
	private String password;	//비밀번호 (로그인이 없기 때문에 수정, 삭제를 위해 사용) 
	private LocalDateTime regDate;		//게시글 등록 날짜
	private LocalDateTime modDate;		//게시글 수정 날짜
	private int viewCount = 1;			//조회 수
	private boolean useYn = true;


	public Long getId() {
		return id;
	}
	
	public Long getParentId() {
		return parentId;
	}

	public String getTitle() {
		return title;
	}
	
	public String getContent() {
		return content;
	}
	
	public String getMember() {
		return member;
	}
	
	public String getPassword() {
		return password;
	}
	
	public LocalDateTime getRegDate() {
		return regDate;
	}
	
	public LocalDateTime getModDate() {
		return modDate;
	}
	
	public int getViewCount() {
		return viewCount;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public void setRegDate(LocalDateTime regDate) {
		this.regDate = regDate;
	}

	public void setModDate(LocalDateTime modDate) {
		this.modDate = modDate;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public void setMember(String member) {
		this.member = member;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public void setRegDate() {
		this.regDate = LocalDateTime.now();	
	}


	public void setModDate() {
		this.modDate = LocalDateTime.now();	
	}


	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public boolean isUseYn() {
		return useYn;
	}

	public void setuseYn(boolean userYn) {
		this.useYn = userYn;
	}
	
	
	
}
