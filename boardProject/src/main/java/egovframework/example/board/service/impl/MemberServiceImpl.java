package egovframework.example.board.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import egovframework.example.board.service.MemberService;


@Service
public class MemberServiceImpl implements MemberService{
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	
	@Override
	public String encryptPassword(String password) {
		return encoder.encode(password);
	}
	
	@Override
	public boolean isMatchPassword(String newPassword, String storedPassword) {
		/* 상위에서 유효성 검사를 진행할 것이기 때문에 여기서는 할 필요 없음*/
		return encoder.matches(newPassword, storedPassword);
	}
}
