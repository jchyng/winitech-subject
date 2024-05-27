package egovframework.example.mvc.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import egovframework.example.mvc.service.MemberService;
import egovframework.example.mvc.service.mapper.MemberMapper;
import egovframework.example.mvc.vo.MemberVO;
import egovframework.example.mvc.vo.Role;


@Service
public class MemberServiceImpl implements MemberService {
	@Autowired
	private MemberMapper mapper;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	@Override
	public boolean IsDuplicateUsername(String username) {
		MemberVO memberVO = mapper.selectMemberByUsername(username);
		return memberVO != null ? true : false; 
	}
	
	@Override
	public int signUpMember(Map<String, String> requestMap) {
		//todo: 검증 기능 (프론트 검증을 먼저 만들어두었으니 서버 검증은 다른 기능 구현 끝내고 진행)
		requestMap.put("role", Role.ROLE_USER.name());
		
		//비밀번호 암호화
		String password = requestMap.get("password");
		password = passwordEncoder.encode(password);
		requestMap.put("password", password);
		
		//저장
		return mapper.insertMember(requestMap);
	}
}
