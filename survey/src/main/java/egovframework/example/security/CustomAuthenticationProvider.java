package egovframework.example.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import egovframework.example.mvc.vo.MemberVO;

public class CustomAuthenticationProvider implements AuthenticationProvider {
	private Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.info("로그인 요청 발생");
		String loginUsername = String.valueOf(authentication.getPrincipal());
		String loginPassword = String.valueOf(authentication.getCredentials());
		log.info("loginUsername : " + loginUsername);
		log.info("loginPassword : " + loginPassword);

		MemberVO memberVO = (MemberVO) userDetailsService.loadUserByUsername(loginUsername);

		if(!matchPassword(loginPassword, memberVO.getPassword())) {
			log.info("로그인 실패: password 불일치");
			throw new BadCredentialsException(loginUsername);
		}

		if(!memberVO.isEnabled()) {
			log.info("로그인 실패: 휴먼 계정");
			throw new BadCredentialsException(loginUsername);
		}
		return new UsernamePasswordAuthenticationToken(memberVO, memberVO.getPassword(), memberVO.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}

	private boolean matchPassword(String loginPassword, String password) {
		return passwordEncoder.matches(loginPassword, password);
	}

}