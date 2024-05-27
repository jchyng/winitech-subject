package egovframework.example.mvc.service;

import java.util.Map;

public interface MemberService {
	/** 유저 아이디 중복 검사 */
	boolean IsDuplicateUsername(String username);
	
	/** 회원가입  */
	int signUpMember(Map<String, String> requestMap);
}
