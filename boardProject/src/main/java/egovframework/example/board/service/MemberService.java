package egovframework.example.board.service;

public interface MemberService {
	/* 비밀번호 검증 로직 */
	
	/**
	 * @param password
	 * @return
	 * 
	 * 비밀번호 암호화
	 */
	String encryptPassword(String password);
	
	
	/**
	 * @param newPassword		비교할 비밀번호
	 * @param storedPassword	비교될 비밀번호
	 * @return
	 * 
	 * 비밀번호 일치 여부 확인
	 */
	boolean isMatchPassword(String newPassword, String storedPassword);
}
