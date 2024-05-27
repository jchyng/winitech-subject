package egovframework.example.mvc.service;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import egovframework.example.mvc.vo.FileVO;
import egovframework.example.mvc.vo.PostVO;
import egovframework.example.mvc.vo.SearchAndPaginationVo;

public interface PostService {
	/** 게시글 조회 */
	Map<String, Object> selectPost(Long id);
	
	/** 게시글 목록 조회 */
	List<PostVO> selectPostList(SearchAndPaginationVo searchVO);
	
	/** 게시글 개수 조회 */
	int selectPostTotCnt(SearchAndPaginationVo searchVO);
	
	/** 게시글에 속하는 파일 조회 */
	List<FileVO> selectFilesByPostId(Long id);
	
	
	/** 게시글 및 파일 저장 */
	Long savePostAndFiles(Map<String, Object> requetMap, MultipartFile[] files) throws SQLException;
	
	/** 파일 다운로드 */
	Map.Entry<String, File> download(Long id) throws SQLException;
	
	
	/** 게시글 수정 */
	void updatePost(Map<String, Object> map, MultipartFile[] files) 
			throws SQLException, JsonMappingException, JsonProcessingException;
	
	/** 게시글 삭제 */
	int deletePostById(Map<String, Long> ids);
}
