package egovframework.example.mvc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import egovframework.example.mvc.vo.SearchAndPaginationVo;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import egovframework.example.mvc.service.PostService;
import egovframework.example.mvc.vo.FileVO;
import egovframework.example.mvc.vo.MemberVO;
import egovframework.example.mvc.vo.PostVO;

@Controller
public class PostController {
	@Autowired
	private PostService postService;
	
	
	/** 게시글 등록 페이지 */
	@GetMapping("/user/postRegister.do")
	public String postRegisterView(Model model) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication auth = securityContext.getAuthentication();
		MemberVO memberVO = (MemberVO) auth.getPrincipal();
		
		//작성자 표시를 위해 회원 이름 전달
		model.addAttribute("memberName", memberVO.getName());
		return "post/postRegister";
	}
	
	
	/** 게시글 등록  */
	@PostMapping("/user/postRegister.do")
	public ResponseEntity<?> postRegister(@RequestParam Map<String, Object> requestMap, MultipartFile[] files)
			throws SQLException {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication auth = securityContext.getAuthentication();
		MemberVO memberVO = (MemberVO) auth.getPrincipal();
		requestMap.put("memberId", memberVO.getId().toString());

		//게시글 저장 (DB, Local File)
		Long postId = postService.savePostAndFiles(requestMap, files);
		
		//Ajax에서 dataType을 text로 받기 때문에 String이 아닌 타입 전송 시 Type 에러가 발생함
		return ResponseEntity.status(HttpStatus.CREATED).body(postId.toString());
	}
	
	
	/**
	 * 게시글 목록 조회 
	 * @param searchVO	검색 키워드 유지 및 페이징을 위해 사용
	 */
	@RequestMapping("/postList.do")
	public String selectPostList( 
			@ModelAttribute("searchVO") SearchAndPaginationVo searchVO, 
			Model model) {
		/* 로그인 상태를 표시하기 위한 유저 인증 정보 */
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication auth = securityContext.getAuthentication();
		if (auth.getPrincipal() instanceof MemberVO) {
		    MemberVO memberVO = (MemberVO) auth.getPrincipal();
		    model.addAttribute("memberName", memberVO.getName());
		}
		
		/* 게시글 목록 및 총 게시글 개수 조회*/
		List<PostVO> postList = postService.selectPostList(searchVO);
		int totalCnt = postService.selectPostTotCnt(searchVO);
		
		/* 페이징 버튼 생성을 위한 정보 세팅 */
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());
		paginationInfo.setTotalRecordCount(totalCnt);
		
		/* 페이징 정보 */
		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		model.addAttribute("paginationInfo",paginationInfo);
		model.addAttribute("postList", postList);		
		model.addAttribute("totalCnt", totalCnt);
		return "post/postList";	
	}
	
	
	/** 게시글 상세 조회 */
	@GetMapping("/postDetail.do")
	public String selectPost(@RequestParam Long id, Model model) {
		Map<String, Object> map = postService.selectPost(id);
		List<FileVO> fileVOs = postService.selectFilesByPostId(id);
		
		//본인의 게시글에만 수정, 삭제 버튼을 생성하기 위해 flag 추가
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication auth = securityContext.getAuthentication();
		if (auth.getPrincipal() instanceof MemberVO) {
			MemberVO memberVO = (MemberVO) auth.getPrincipal();
		    
		    if(memberVO.getId() == map.get("member_id")) {
		    	model.addAttribute("isMy", true);
		    }
		}
		
		model.addAttribute("postVO", map);
		model.addAttribute("fileVOs", fileVOs);
		return "post/postDetail";
	}
	
	
	/** 파일 다운로드 */
	@RequestMapping("/download/file.do")
	public void fileDownload(@RequestParam Long id, HttpServletResponse response) 
			throws SQLException, IOException{
		/* 다운로드 받을 파일을 받아옴 */
		Entry<String, File> fileEntry = postService.download(id);
		File file = fileEntry.getValue();
		/* 파일 이름 전송 시 ASCII 문자 집합만을 허용하므로 UTF-8로 변환 */
		String originalFileName = URLEncoder.encode(fileEntry.getKey(), "UTF-8").replaceAll("\\+", "%20");
		
		/* response의 contentType을 다운로드 방식으로 설정 */
        response.setContentType("application/download");
        response.setContentLength((int)file.length());
        response.setHeader("Content-disposition", "attachment;filename=\"" + originalFileName + "\"");
        
        /* response 객체를 통해서 파일 전송을 위한 스트림 생성*/
        OutputStream os = response.getOutputStream();
        
        /* fileInputStream으로 파일을 읽은 후 그 값을 outputStream에 값을 넣어 전송 */
        FileInputStream fis = new FileInputStream(file);
        FileCopyUtils.copy(fis, os);
        
        /* 사용 후 리소스 누수 방지를 위한 close */
        fis.close();
        os.close();
	}
	
	/** 게시글 수정 페이지 */
	@GetMapping("/user/postModifier.do")
	public String postModifierView(@RequestParam Long id, Model model) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication auth = securityContext.getAuthentication();
		MemberVO memberVO = (MemberVO) auth.getPrincipal();
		
		Map<String, Object> map = postService.selectPost(id);
		List<FileVO> fileVOs = postService.selectFilesByPostId(id);
		
		model.addAttribute("memberName", memberVO.getName());
		model.addAttribute("postVO", map);
		model.addAttribute("fileVOs", fileVOs);
		return "post/postRegister";
	}
	
	/** 게시글 수정  */
	@PostMapping("/user/postModifier.do")
	public ResponseEntity<?> postModifier(@RequestParam Map<String, Object> requestMap
			, MultipartFile[] files) throws SQLException, 
											JsonMappingException, 
											JsonProcessingException {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication auth = securityContext.getAuthentication();
		MemberVO memberVO = (MemberVO) auth.getPrincipal();

		/* 게시글 아이디와 memberId가 일치하는 게시글이 있다면 진행, 없다면 badrequest 반환 */
		Long id = Long.valueOf((String) requestMap.get("id"));
		Map<String, Object> post = postService.selectPost(id);
		Long memberId = (Long) post.get("member_id");
		
		if(memberId != memberVO.getId()) {
			return ResponseEntity.badRequest().build();
		}
		
		//게시글 수정 호출
		postService.updatePost(requestMap, files);
		return ResponseEntity.ok().body(id.toString());
	}
	
	
	/** 게시글 삭제 */
	@PostMapping("/user/postEliminator.do")
	public ResponseEntity<?> postRemover(@RequestParam Long id) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication auth = securityContext.getAuthentication();
	    MemberVO memberVO = (MemberVO) auth.getPrincipal();
		
	    /* 아이디와 작성자 아이디가 일치하는 게시글만 삭제  */
	    Map<String, Long> ids = new HashMap<>();
	    ids.put("id", id);
	    ids.put("memberId", memberVO.getId());
	    
		int result = postService.deletePostById(ids);
		
		if(result > 0) {
			return ResponseEntity.status(HttpStatus.OK).build();
		} 
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
}
