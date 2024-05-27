package egovframework.example.mvc.vo;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

public class FileVO {
	private Long id; // PK
	private Long postId; // FK
	private String originalName; // 파일을 다운 받을 때 사용할 실제 이름
	private String extension; // 파일 확장자
	private String savedName; // 파일 저장 시 중복 방지를 위한 UUID
	private String path; // 파일이 저장된 경로
	private Long size; // 파일의 크기
	private LocalDateTime regDate; // 등록 날짜
	private boolean useYn = true;

	/**
	 * 파일 저장 로직에서 DB 저장 로직과 Local 저장 로직을 분리해야함. 
	 * Local 저장에 사용되는 UUID를 VO가 가지기 때문에 VO에 
	 * File을 같이 저장해서 Local 저장에 활용
	 */
	private MultipartFile file;

	public FileVO() {
	}

	public FileVO(String originalName, String extension, String savedName, String path, Long size,
			MultipartFile file) {
		this.originalName = originalName;
		this.extension = extension;
		this.savedName = savedName;
		this.path = path;
		this.size = size;
		this.regDate = LocalDateTime.now();
		this.file = file;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPostId() {
		return postId;
	}

	public void setPostId(Long postId) {
		this.postId = postId;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getSavedName() {
		return savedName;
	}

	public void setSavedName(String savedName) {
		this.savedName = savedName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public LocalDateTime getRegDate() {
		return regDate;
	}

	public void setRegDate(LocalDateTime regDate) {
		this.regDate = regDate;
	}

	public MultipartFile getFile() {
		return file;
	}

	public boolean isUseYn() {
		return useYn;
	}

	public void setUseYn(boolean useYn) {
		this.useYn = useYn;
	}

}
