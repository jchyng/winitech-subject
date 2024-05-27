package egovframework.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import egovframework.example.mvc.service.mapper.MemberMapper;
import egovframework.example.mvc.vo.MemberVO;


public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private MemberMapper memberMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MemberVO memberVO = memberMapper.selectMemberByUsername(username);

		if(memberVO == null) {
			throw new UsernameNotFoundException(username);
		}
		return memberVO;
	}

}