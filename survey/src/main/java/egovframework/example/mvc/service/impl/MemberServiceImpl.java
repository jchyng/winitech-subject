package egovframework.example.mvc.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import egovframework.example.mvc.service.MemberService;
import egovframework.example.mvc.service.mapper.MemberMapper;
import egovframework.example.mvc.vo.enums.Role;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberMapper mapper;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Override
    public Boolean IsDuplicateUsername(String username) {
        return mapper.checkUsernameExists(username);
    }
    
    @Override
    public int signUpMember(Map<String, String> requestMap) {
    	if(Boolean.parseBoolean(requestMap.get("role"))) {
    		requestMap.put("role", Role.ROLE_ADMIN.name());
    	} else {
    		requestMap.put("role", Role.ROLE_USER.name());
    	}
        
        // 비밀번호 암호화
        String password = requestMap.get("password");
        password = passwordEncoder.encode(password);
        requestMap.put("password", password);
        
        // 저장
        return mapper.insertMember(requestMap);
    }
}
