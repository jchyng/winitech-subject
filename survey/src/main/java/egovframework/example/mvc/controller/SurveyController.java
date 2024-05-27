package egovframework.example.mvc.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.example.mvc.dto.AnswerDTO;
import egovframework.example.mvc.dto.PaginationDTO;
import egovframework.example.mvc.dto.SurveyDTO;
import egovframework.example.mvc.dto.statistics.StatisticsSurveyDTO;
import egovframework.example.mvc.service.SurveyService;
import egovframework.example.mvc.vo.MemberVO;
import egovframework.example.mvc.vo.enums.Role;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class SurveyController {
	@Autowired
	private SurveyService surveyService;
	
	
	//===================================== 페이지 조회 컨트롤러 =======================================//
	//============ROLE_ADMIN=============//
	@RequestMapping("/survey/list.do")
	public String getSurveyListView(@ModelAttribute("paginationDTO") PaginationDTO dto, Model model, 
			HttpServletResponse response) throws IOException{
		/* 페이지 정보 세팅 */
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(dto.getPageIndex());
		paginationInfo.setRecordCountPerPage(dto.getPageUnit());
		paginationInfo.setPageSize(dto.getPageSize());
		 
		/* 검색을 위한 페이지 정보 */
		dto.setFirstIndex(paginationInfo.getFirstRecordIndex());
		dto.setLastIndex(paginationInfo.getLastRecordIndex());
		dto.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		/* 설문 목록 및 총 설문 개수 조회*/
		List<EgovMap> surveys = new ArrayList<>();
		int totalCnt = 0;
		
		/* 권한 체크 */
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication auth = securityContext.getAuthentication();
		if (auth == null || auth instanceof AnonymousAuthenticationToken) {
	        response.sendRedirect("/signin.do");
	        return null; 
	    }
		MemberVO memberVO = (MemberVO) auth.getPrincipal();
		Map<String, Object> map = new HashMap<>();
		map.put("dto", dto);
		map.put("memberId", memberVO.getId());
		//관리자 일 때
		if(memberVO.getRole() == Role.ROLE_ADMIN) {
			surveys = surveyService.searchInMySurveys(map);
			totalCnt = surveyService.getCountInMySurveys(map);
		} //관리자가 아닐 때
		else {				
			surveys = surveyService.searchSurveys(map);
			totalCnt = surveyService.getCountSurveys(dto);
		}
		model.addAttribute("member", memberVO);
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("surveys", surveys);
		return "survey/surveyList";
	}
	
	/** 설문지 생성 페이지 */
	@GetMapping("/admin/survey/register.do")
	public String getSurveyRegisterView() {
		return "survey/surveyRegister";
	}
	
	/** 설문지 수정 페이지 */
	@GetMapping("/admin/survey/modifier.do")
	public String getSurveyModifierView(@RequestParam Long id, Model model){
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication auth = securityContext.getAuthentication();
		MemberVO memberVO = (MemberVO) auth.getPrincipal();
		
		SurveyDTO dto = surveyService.findSurvey(id, memberVO.getId());
		model.addAttribute("survey", dto);
		return "survey/surveyRegister";
	}
	
	/** 통계 조회 */
	@GetMapping("/admin/survey/statistics.do")
	public String getSurveyStatisticsView(@RequestParam Long id, Model model) 
			throws JsonProcessingException{
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication auth = securityContext.getAuthentication();
		MemberVO memberVO = (MemberVO) auth.getPrincipal();
		
		StatisticsSurveyDTO dto = surveyService.findStatisticsForSurvey(id, memberVO.getId());
		
		//js에서 Chart 객체로 사용하기 위해 JSON으로 변환
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonDTO = objectMapper.writeValueAsString(dto);
		
		model.addAttribute("survey", dto);
		model.addAttribute("jsonDTO", jsonDTO);
		return "survey/surveyStatistics";
	}
	
	
	//============ROLE_USER=============//
	/** 설문지 응답 제출 페이지 */
	@GetMapping("/survey/form.do")
	public String getSurveyFormView(@RequestParam Long id, Model model) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication auth = securityContext.getAuthentication();
		MemberVO memberVO = (MemberVO) auth.getPrincipal();
		
		SurveyDTO dto = surveyService.findSurvey(id, memberVO.getId());  
		model.addAttribute("survey", dto);
		return "survey/surveyForm";
	}
	
	
	//============ROLE_ANONYMOUS=============//
	/** 파일 조회 */
	@GetMapping(value = "/imgPreview.do",produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<?> getImage(@RequestParam Long id) throws IOException{
		byte[] fileStream = surveyService.getFileImg(id);
		return ResponseEntity.ok(fileStream);
	}
	
	
	
	//===================================== AJAX 요청 처리 컨트롤러 =======================================//
	//============ROLE_ADMIN=============//
	/** 설문지 생성 요청  */
	@PostMapping("/admin/survey/create.do")
	public ResponseEntity<?> createSurvey(SurveyDTO dto) throws IOException{
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication auth = securityContext.getAuthentication();
		MemberVO memberVO = (MemberVO) auth.getPrincipal();
		
		dto.setMemberId(memberVO.getId());
		surveyService.createSurvey(dto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	/** 설문지 업데이트 요청 */
	@PostMapping("/admin/survey/update.do")
	public ResponseEntity<?> updateSurvey(SurveyDTO dto) throws IOException{
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication auth = securityContext.getAuthentication();
		MemberVO memberVO = (MemberVO) auth.getPrincipal();
		
		dto.setMemberId(memberVO.getId());
		surveyService.updateSurvey(dto);
		return ResponseEntity.ok().build();
	}
	
	/** 설문지 삭제 요청 */
	@PostMapping("/survey/delete.do")
	public ResponseEntity<?> deleteSurvey(@RequestParam Long id) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication auth = securityContext.getAuthentication();
		MemberVO memberVO = (MemberVO) auth.getPrincipal();
		
		surveyService.deleteSurvey(id, memberVO.getId());
		return ResponseEntity.ok().build();
	}
	
	
	//============ROLE_USER=============//
	/** 설문지 응답 제출 요청 */
	@PostMapping("/survey/submit.do")
	public ResponseEntity<?> submitSurvey(AnswerDTO dto) throws IOException{
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication auth = securityContext.getAuthentication();
		MemberVO memberVO = (MemberVO) auth.getPrincipal();
		
		dto.setMemberId(memberVO.getId());
		surveyService.createAnswer(dto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	
	//============ROLE_ANONYMOUS=============//
	
	
}
