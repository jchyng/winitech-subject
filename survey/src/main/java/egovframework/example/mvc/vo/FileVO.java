package egovframework.example.mvc.vo;

import egovframework.example.mvc.utils.FileUtils;

public class FileVO {
	private Long id;
	private Long questionId;
	private String originalName;
	private String extension;
	private String savedName;
	private String path;
	private Long size;

	
	public FileVO(){
		
	}
	
	public FileVO(String originalName, String extension, String savedName, String path, Long size) {
		super();
		this.originalName = originalName;
		this.extension = extension;
		this.savedName = savedName;
		this.path = path;
		this.size = size;
	}

	
	public String getSavedPath() {
		return FileUtils.FILE_PATH + path + savedName;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
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

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
