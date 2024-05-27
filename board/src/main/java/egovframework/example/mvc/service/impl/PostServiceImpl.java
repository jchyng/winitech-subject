package egovframework.example.mvc.service.impl;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import egovframework.example.mvc.service.PostService;
import egovframework.example.mvc.service.mapper.FileMapper;
import egovframework.example.mvc.service.mapper.PostMapper;
import egovframework.example.mvc.utils.FileUtils;
import egovframework.example.mvc.vo.FileVO;
import egovframework.example.mvc.vo.PostVO;
import egovframework.example.mvc.vo.SearchAndPaginationVo;
import egovframework.rte.psl.dataaccess.util.EgovMap;


@Service
public class PostServiceImpl implements PostService{
	@Autowired
	private PostMapper postMapper;
	
	@Autowired
	private FileMapper fileMapper;
	
	@Autowired
	private FileUtils fileUtils;
	
	private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

	
	
	@Override
	public Map<String, Object> selectPost(Long id) {
		Map<String, Object> map = postMapper.selectPost(id);
		postMapper.increaseViewCount(id); //조회 수 증가 => todo: 세션으로 하루에 하나만 증가되도록 변경
		
		/* 썸네일 이미지 조회 시 이미지 파일이 저장되는 폴더 경로 추가 
		 * (저장 위치를 바꿀 때 DB 데이터를 변경하지 않기 위해) 
		 * */
		String key = "thumbnail";
		String thumbnail = (String) map.get(key);
		map.put(key, FileUtils.RELATIVE_PATH + thumbnail);	
		return map;
	}

	@Override
	public List<PostVO> selectPostList(SearchAndPaginationVo searchVO) {
		List<EgovMap> results = postMapper.selectPostList(searchVO);
		List<PostVO> postVOs = new ArrayList<>();
		
		/* egovMap으로 받을 경우 각각이 egovMap 타입이기 때문에 타입 캐스팅 */
		ObjectMapper objectMapper = new ObjectMapper();
		// JDK8에서 DateTime 변환을 지원하지 않기 때문에 모듈 추가
		objectMapper.registerModule(new JavaTimeModule());	
		
		for(EgovMap map : results) {
			String key = "thumbnail";
			String thumbnail = (String) map.get(key);
			map.put(key, FileUtils.RELATIVE_PATH + thumbnail);
			
			PostVO postVO = objectMapper.convertValue(map, PostVO.class);
			postVOs.add(postVO);
		}
		
		return postVOs;
	}

	@Override
	public int selectPostTotCnt(SearchAndPaginationVo searchVO) {
		return postMapper.selectPostTotCnt(searchVO);
	}

	
	@Override
	public List<FileVO> selectFilesByPostId(Long id){
		return fileMapper.selectFilesByPostId(id);
	}
	
	
	@Override
	public Long savePostAndFiles(Map<String, Object> requestMap, MultipartFile[] files) 
			throws SQLException{
		/* Map에서 필요한 데이터 추출 */
		Long memberId = Long.valueOf((String) requestMap.get("memberId"));
		String title = (String) requestMap.get("title");
		String content = (String) requestMap.get("content");
		String thumbnail = (String) requestMap.get("thumbnail");
		
		/* 파일 VO 변환 */
		List<FileVO> fileVOs = fileUtils.convertToFileVO(files);
		
		/* FileVO의 경로로 Content img src 치환 */
		content = replacementContentImgSrc(content, fileVOs);
		
		/* 썸네일 주소 세팅 */
		thumbnail = generateSavedThumbnailURL(fileVOs, thumbnail);
		
		/* 게시글 객체 생성 */
		PostVO postVO = new PostVO(memberId, title, content, thumbnail);
		
		/* 게시글 저장 */
		postMapper.insertPost(postVO);
		
		/* FileVO에 게시글 아이디 주입 */
		for(FileVO fileVO : fileVOs) {
			fileVO.setPostId(postVO.getId());
		}
		
		/* DB에 파일 저장 */
		fileMapper.insertFiles(fileVOs);
		
		try {
			if(!fileVOs.isEmpty()) {
				/* 로컬에 파일 저장 */
				fileUtils.saveToLocal(fileVOs);
			}
		} catch (IOException e) {
			/* 로컬 파일 저장 실패 시 로컬에 저장된 파일 삭제 */
			fileUtils.deleteToLocal(fileVOs);
		}
		return postVO.getId();
	}
	
	@Override
	public Map.Entry<String, File> download(Long id) throws SQLException{
		FileVO fileVO = fileMapper.selectFileById(id);
		File file = fileUtils.makeFileByVO(fileVO);
		String originalFileName = fileVO.getOriginalName() + "." + fileVO.getExtension();
		
		return new AbstractMap.SimpleEntry<>(originalFileName, file);
	}
	
	
	@Override
	public void updatePost(Map<String, Object> requestMap
			, MultipartFile[] files) throws SQLException, 
											JsonMappingException, 
											JsonProcessingException {
		Long id = Long.valueOf((String) requestMap.get("id"));
		String content = (String) requestMap.get("content");
		String thumbnail = (String) requestMap.get("thumbnail");
		String removedFileIds = (String) requestMap.get("removedFileIds");
		/* 문자열을 List<Long>으로 변환 */
		List<Long> removedFileIdList = new ArrayList<>();
		String[] fileIds = removedFileIds.split(",");
		for(String fileId: fileIds) {
			removedFileIdList.add(Long.valueOf(fileId));
		}
		
		/* 파일 VO 변환 */
		List<FileVO> fileVOs = fileUtils.convertToFileVO(files);
		/* FileVO에 게시글 아이디 주입 */
		for(FileVO fileVO : fileVOs) {
			fileVO.setPostId(id);
		}
		
		/* FileVO의 경로로 Content img src 치환 */
		content = replacementContentImgSrc(content, fileVOs);
		
		/* 썸네일 주소 세팅 */
		thumbnail = generateSavedThumbnailURL(fileVOs, thumbnail);
		
		/* 게시글 수정 */
		postMapper.updatePost(requestMap);
		/* DB에 추가된 파일 저장 */
		if(!fileVOs.isEmpty() || fileVOs.size() > 0) {
			fileMapper.insertFiles(fileVOs);
		}
		/* 삭제한 파일 DB에서 삭제 처리 */
		fileMapper.deleteFileByIds(removedFileIdList);

		try {
			if(!fileVOs.isEmpty()) {
				/* 로컬에 파일 저장 */
				fileUtils.saveToLocal(fileVOs);
			}
		} catch (IOException e) {
			/* 로컬 파일 저장 실패 시 로컬에 저장된 파일 삭제 */
			fileUtils.deleteToLocal(fileVOs);
		}
	}
	
	@Override
	public int deletePostById(Map<String, Long> ids){
		return postMapper.deletePostById(ids);
	}
	
	
	/** 업로드된 이미지에 해당하는 이미지 태그를 찾아서 새로운  파일 경로를 가진 이미지 태그로 치환 */
	private String replacementContentImgSrc(String content, List<FileVO> fileVOs) {
		for(FileVO fileVO : fileVOs) {
			String before = generateTargetImgTag(fileVO);
			String after = generateNewImgTag(fileVO);
			content = content.replaceAll(before, after);
		}
		return content;
	}
	
	/** 미리 정해진 규칙에 해당하는 이미지 태그를 생성 */
	private String generateTargetImgTag(FileVO fileVO) {
		String prefix = "<img src=\"\" alt=\"image\" data-filename=\"";
		String postfix = "\">";
		return prefix + escapeRegex(fileVO.getOriginalName()) + "\\." + fileVO.getExtension() + postfix;
	}
	
	/** 저장된 파일의 경로를 담은 새로운 이미지 태그  생성 */
	private String generateNewImgTag(FileVO fileVO) {
		//상대경로 사용 시 baseURL이 달라지기 때문에 슬래시 추가
		String prefix = "<img src=\"/";	
		String postfix = "\" alt=\"image\">";
		return prefix + FileUtils.RELATIVE_PATH + fileVO.getPath() + fileVO.getSavedName() + postfix;
	}
	
	/** 파일 이름에 정규식에 사용되는 패턴 문자가 들어가지 않도록 필터링 */
	private String escapeRegex(String input) {
        // 이스케이프 처리할 특수 문자들
        String[] specialChars = {"\\", ".", "*", "+", "?", "^", "$", "(", ")", "[", "]", "{", "}", "|", "<", ">"};

        // 특수 문자들을 이스케이프 처리하여 정규식 패턴 생성
        StringBuilder patternBuilder = new StringBuilder();
        for (String specialChar : specialChars) {
            patternBuilder.append("\\").append(specialChar);
        }
        String pattern = patternBuilder.toString();

        // 입력된 문자열에서 특수 문자들을 이스케이프 처리
        return input.replaceAll("[" + pattern + "]", "\\\\$0");
    }
	
	/** 저장될 썸네일 이미지 주소 생성 */
	private String generateSavedThumbnailURL(List<FileVO> fileVOs, String thumbnail) {
		for(FileVO fileVO: fileVOs) {
			String fileName = fileVO.getOriginalName() + "." + fileVO.getExtension();
			if(fileName.equals(thumbnail)) {
				return fileVO.getPath() + fileVO.getSavedName();
			}
		}
		return thumbnail;	//수정 시 썸네일 지정을 안했다면 기존 값이 들어가도록 
	}
	
}
