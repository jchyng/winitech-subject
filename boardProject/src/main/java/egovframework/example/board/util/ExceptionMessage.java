package egovframework.example.board.util;



/** 예외 발생 메세지를 한 곳에서 관리하기 위한 ENUM */
public enum ExceptionMessage {
    ENTITY_NOT_FOUND_MESSAGE("엔티티가 존재하지 않습니다."),
	ILLEGAL_FILE_EXTENSION("사용할 수 없는 파일 확장자 입니다."),
	
	NOT_EMPTY_TITLE("제목은 필수 입력 요소입니다."),
	LENGTH_OVER_TITLE("제목은 100자 이내로 작성 가능합니다."),
	
	NOT_EMPTY_CONTENT("내용은 필수 입력 요소입니다."),
	LENGTH_OVER_CONTENT("내용은 1000자 이내로 작성 가능합니다."),
	
	NOT_EMPTY_MEMBER("작성자 이름은 필수 입력 요소입니다."),
	LENGTH_OVER_MEMBER("작성자 이름은 6자 이내로 작성 가능합니다."),
	
	INVALID_PASSWORD("비밀번호는 8~16자 이내로 영문, 숫자, 특수문자를 1개 이상 포함해야합니다."),
	;

	
	
    final String message;

    private ExceptionMessage(String message) {
        this.message = message;
    }

	public String getMessage() {
		return message;
	}

}
