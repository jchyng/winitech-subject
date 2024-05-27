package egovframework.example.board.controller;


import egovframework.example.board.service.BoardService;
import egovframework.example.board.service.FileService;
import egovframework.example.board.service.MemberService;
import egovframework.example.board.vo.BoardVO;
import egovframework.example.board.vo.FileVO;
import egovframework.example.board.vo.SearchAndPaginationVo;
import egovframework.example.board.vo.validator.BoardValidator;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;



@Controller
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private BoardValidator boardValidator;
	
	/**
	 * @param searchVO
	 * @return
	 * @throws Exception
	 * 
	 * 게시글과 답변 글 목록을 불러오는 컨트롤러
	 */
	@RequestMapping("/boardList.do")
	public String selectBoardList(
			@ModelAttribute("searchVO") SearchAndPaginationVo searchVO, 
			Model model) throws SQLException {
		/* 페이지 정보 세팅 */
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(searchVO.getPageIndex());
		paginationInfo.setRecordCountPerPage(searchVO.getPageUnit());
		paginationInfo.setPageSize(searchVO.getPageSize());
		
		/* 검색을 위한 페이지 정보 */
		searchVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
		searchVO.setLastIndex(paginationInfo.getLastRecordIndex());
		searchVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
		/* 게시글 목록 및 총 게시글 개수 조회*/
		List<BoardVO> boardList = boardService.selectBoardList(searchVO);
		int totalCnt = boardService.selectBoardListTotCnt(searchVO);
		
		paginationInfo.setTotalRecordCount(totalCnt);
		
		
		model.addAttribute("paginationInfo",paginationInfo);
		model.addAttribute("boardList", boardList);		
		model.addAttribute("totalCnt", totalCnt);
		return "board/boardList";	
	}

	
	
	
	/**
	 * @param id (게시글 아이디)
	 * @return
	 * @throws Exception
	 * 
	 * 게시글 상세 정보를 불러오는 컨트롤러
	 */
	@RequestMapping("/boardDetail.do")
	public String selectBoardDetail(@RequestParam Long id, Model model) throws SQLException {
		/* 게시글 조회 수 증가 */
		boardService.increaseViewCount(id);
		
		/* 게시글 조회 */
		BoardVO boardVO = boardService.selectBoard(id);
		
		/* 파일 조회 */
		List<FileVO> fileVOs = fileService.selectFileList(id);
		
		model.addAttribute("boardVO", boardVO);
		model.addAttribute("fileVOs",fileVOs);
		return "board/boardDetail";
	}
	
	
	
	@RequestMapping("/download/file.do")
	public void fileDownload(@RequestParam Long id, HttpServletResponse response) 
			throws SQLException, IOException{
		/* 다운로드 받을 파일을 받아옴 */
		Entry<String, File> fileEntry = fileService.download(id);
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
	
	
	
	
	/**
	 * @param parentId  (선택 요소) 게시글과 답변 글 구분에 사용 
	 * @param boardVO	게시글 등록 실패 시 입력 정보를 그대로 유지시키기 위한 VO
	 * @return
	 * 
	 * 게시글을 등록하는 컨트롤러
	 * 부모 아이디가 있으면 답변 글 등록 VIEW를 반환하고, 부모 아이디가 없다면 일반 게시글로 등록 VIEW를 반환한다.
	 */
	@RequestMapping(value = "/boardRegister.do", method = RequestMethod.GET)
	public String insertBoardView(
			@RequestParam(required = false) Long parentId,
			@ModelAttribute("boardVO") BoardVO boardVO,
			Model model) {
		/*부모 아이디 설정*/
		boardVO.setParentId(parentId);
		
		model.addAttribute("boardVO", boardVO);
		return "board/boardRegister";
	}
	
	
	
	/**
	 * @param boardVO
	 * @param files
	 * @param model
	 * @return
	 * @throws Exception
	 */
	//controller transaction -> service
	@Transactional(rollbackFor = {Exception.class})	//DB 접근 뿐만 아닌 단순 예외 발생 시에도 롤백
	@RequestMapping(value = "/boardRegister.do", method = RequestMethod.POST)
	public String insertBoard(
			@ModelAttribute BoardVO boardVO,
            @RequestParam MultipartFile[] files,
			Model model) throws SQLException, IOException {
		/* boardVO 필드 유효성 검사 */
		boardValidator.validate(boardVO);
		
		/* 게시글 정보 세팅 */
		boardVO.setRegDate();	//등록 시간 = 서버 현재 시간
		boardVO.setModDate();	//수정 시간 = 서버 현재 시간
		
		/* 비밀번호 암호화 */
		String encryptedPassword = memberService.encryptPassword(boardVO.getPassword());
		boardVO.setPassword(encryptedPassword);
		
		/** 
		 * 게시글 저장  
		 * ID를 메서드가 반환하는 것이 아닌 VO에 반환하기 때문에 VO에서 꺼내야한다.
		 * 메서드는 저장 성공 여부로 0과 1을 리턴한다. 
		 */
		boardService.insertBoard(boardVO);
		
		/* 파일 저장을 위해 유효성 검사 후 VO 객체로 변환  (빈 더미 파일 업로드 시 null)*/
		List<FileVO> fileVOs =  fileService.convertToFileVO(files, boardVO.getId());
		
		if(fileVOs.size() > 0) {			
			/* File Local 저장 */
			fileService.saveToLocal(fileVOs);
			/* FileVO DB 저장 */
			fileService.insertAllFile(fileVOs);
		}
		
		return "redirect:/boardList.do";	
	}
	
	
	
	
	
	/**
	 * @param id		게시글 아이디
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/boardModifier.do", method = RequestMethod.GET)
	public String updateBoardView(@RequestParam Long id, Model model) throws SQLException {
		/* 수정할 게시글 조회*/
		BoardVO boardVO = boardService.selectBoard(id);
		List<FileVO> fileVOs = fileService.selectFileList(id);
		
		model.addAttribute("boardVO", boardVO);
		model.addAttribute("fileVOs", fileVOs);
		return "board/boardRegister";
	}


	
	
	
	/**
	 * @param boardVO
	 * @param retainedFileIds 삭제되지 않고 유지된 파일 아이디
	 * @param MultipartFile[]
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/boardModifier.do", method = RequestMethod.POST)
	public String updateBoard(
			BoardVO boardVO,
			@RequestParam(value = "retainedFileIds", required = false) Long[] retainedFileIds,
			@RequestParam MultipartFile[] files,
			Model model) throws SQLException, IOException {
		Long id = boardVO.getId();
		
		/* 게시글 수정 시간 현재 시간으로 수정 */
		boardVO.setModDate();
		
		/* 게시글 비밀번호 조회 */
		String storedPassword = boardService.selectPassword(id);
		
		/* 비밀번호 검사 */
		if(!memberService.isMatchPassword(boardVO.getPassword(), storedPassword)){
			model.addAttribute("boardVO", boardVO);	//실패 시 입력 데이터를 그대로 가지고 다시 게시글 수정 페이지로 이동
			return "board/boardRegister";
		}
		
		/* 게시글 수정 */
		boardService.updateBoard(boardVO);	
		
		/* 파일 변경 사항 반영 (삭제)*/
		fileService.deleteByRetainedFiles(id, retainedFileIds);
		/* 파일 변경 사항 반영 (추가)*/
		List<FileVO> fileVOs =  fileService.convertToFileVO(files, boardVO.getId());
		
		if(fileVOs.size() > 0) {			
			/* File Local 저장 */
			fileService.saveToLocal(fileVOs);
			/* FileVO DB 저장 */
			fileService.insertAllFile(fileVOs);
		}
		
		/* 수정 성공 시 해당 게시글의 상세 페이지로 이동 */
		return "redirect:/boardDetail.do?id=" + id;
	}
		
		
	
	
	
	/**
	 * @param boardVO
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/boardEliminator.do")
	public String deleteBoard(BoardVO boardVO) throws SQLException {
		Long id = boardVO.getId();
		/* 게시글 비밀번호 조회 */
		String storedPassword = boardService.selectPassword(id);
		
		/* 게시글 비밀번호 검증 */
		if(!memberService.isMatchPassword(boardVO.getPassword(), storedPassword)) {
			return "redirect:/boardDetail.do?id=" + id; //실패 시 다시 상세 페이지로 이동
		} 
		
		/* 게시글 삭제 */
		boardService.deleteBoard(boardVO);
		/* 게시글에 속한 파일 삭제 */
		fileService.deleteFileByboardId(id);
		
		/* 삭제 성공 시 목록 페이지로 이동 */		
		return "redirect:/boardList.do";
	}

}
