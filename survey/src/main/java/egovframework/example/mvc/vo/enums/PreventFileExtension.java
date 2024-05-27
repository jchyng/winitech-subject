package egovframework.example.mvc.vo.enums;


/** 
 * 파일 확장자를 관리하기 위한 enum 
 */
public enum PreventFileExtension {
	/* 실행 가능 파일 */
	EXE("exe"), BAT("bat"), SH("sh"),
	
	/* script & sql */
	JS("js"), JSP("jsp"), PHP("php"), SQL("sql");
	
	
	private final String extension;

	private PreventFileExtension(String extension) {
		this.extension = extension;
	}

	public String getExtension() {
		return extension;
	}
	
	
	public static boolean validate(String extension) {
		for(PreventFileExtension ex: PreventFileExtension.values()) {
			if(ex.getExtension().equals(extension.toLowerCase())) {
				return false;
			}
		}
		return true;
	}
}
