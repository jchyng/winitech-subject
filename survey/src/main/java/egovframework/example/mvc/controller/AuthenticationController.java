package egovframework.example.mvc.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.example.mvc.service.MemberService;

@Controller
public class AuthenticationController {
	@Autowired 
	private MemberService memberService;
	
	
	/** 아이디 중복 검사 */
	@PostMapping("/username.do")
	public ResponseEntity<?> usernameDuplicationCheck(@RequestBody String username){
		boolean result = memberService.IsDuplicateUsername(username);
		String body = result ? "y" : "n";
		return ResponseEntity.status(HttpStatus.OK).body(body);
	}
	
	
	/** 회원가입 페이지  */
	@GetMapping("/signup.do")
	public String signUpView() {
		return "sign/signUp";
	}
	
	/** 회원가입 */
	@PostMapping("signup.do")
	public ResponseEntity<?> signUp(@RequestParam Map<String, String> requestMap){
		Boolean result = memberService.signUpMember(requestMap) > 0;
		String body = result ? "y" : "n";
		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}

	/** 로그인 페이지 */
	@GetMapping("/signin.do")
	public String signInView() {
		return "sign/signIn";
	}
}
