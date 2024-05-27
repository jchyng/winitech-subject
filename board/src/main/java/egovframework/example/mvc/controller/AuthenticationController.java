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
	public ResponseEntity<Void> usernameDuplicationCheck(@RequestBody String username){
		if(memberService.IsDuplicateUsername(username)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); 
		}
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	
	/** 회원가입 페이지  */
	@GetMapping("/signup.do")
	public String signUpView() {
		return "sign/signUp";
	}
	
	/** 회원가입 */
	@PostMapping("signup.do")
	public ResponseEntity<Void> signUp(@RequestParam Map<String, String> requestMap){
		//todo: exception 처리는  controllerAdvice에서 처리
		if(memberService.signUpMember(requestMap) == 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/** 로그인 페이지 */
	@GetMapping("/signin.do")
	public String signInView() {
		return "sign/signIn";
	}
}
