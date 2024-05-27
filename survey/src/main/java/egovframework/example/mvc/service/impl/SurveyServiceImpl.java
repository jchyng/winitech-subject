package egovframework.example.mvc.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import egovframework.example.mvc.dto.AnswerDTO;
import egovframework.example.mvc.dto.PaginationDTO;
import egovframework.example.mvc.dto.QuestionDTO;
import egovframework.example.mvc.dto.SurveyDTO;
import egovframework.example.mvc.dto.statistics.StatisticsSurveyDTO;
import egovframework.example.mvc.service.SurveyService;
import egovframework.example.mvc.service.mapper.SurveyMapper;
import egovframework.example.mvc.utils.FileUtils;
import egovframework.example.mvc.vo.FileVO;
import egovframework.example.mvc.vo.SelectOptionVO;
import egovframework.example.mvc.vo.SurveyVO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Service
public class SurveyServiceImpl implements SurveyService {
	@Autowired SurveyMapper surveyMapper;
	@Autowired FileUtils fileUtils;
	
	
	@Override
	public List<EgovMap> searchInMySurveys(Map<String, Object> map) {
		return surveyMapper.selectMySurveys(map);
	}

	@Override
	public int getCountInMySurveys(Map<String, Object> map) {
		return surveyMapper.countMySurveys(map);
	}


	@Override
	public List<EgovMap> searchSurveys(Map<String, Object> map) {
		return surveyMapper.selectSurveys(map);
	}

	@Override
	public int getCountSurveys(PaginationDTO dto) {
		return surveyMapper.countSurveys(dto);
	}

	@Override
	public SurveyDTO findSurvey(Long id, Long memberId) {
		Map<String, Long> map = newIdsMap(id, memberId);
		SurveyDTO dto = surveyMapper.selectSurvey(map);
		
		//fileVO의 path를 img 태그의 src에 들어갈 주소로 변경
		for(QuestionDTO questionDTO : dto.getQuestions()) {
			FileVO fileVO = questionDTO.getFileVO(); 
			if(fileVO != null) {
				fileVO.setPath("/imgPreview.do?id=" + fileVO.getId());
			}
		}
		return dto;
	}
	
	@Override
	public StatisticsSurveyDTO findStatisticsForSurvey(Long id, Long memberId) {
		Map map = newIdsMap(id, memberId);
		StatisticsSurveyDTO dto = surveyMapper.selectStatistics(map);
		return dto;
	}
	
	@Override
	public byte[] getFileImg(Long id) throws IOException {
		FileVO fileVO = surveyMapper.selectFile(id);
		String path = fileVO.getSavedPath();
	    return Files.readAllBytes(Paths.get(path));
	}


	@Override
	public void createSurvey(SurveyDTO dto) throws IOException {
		/* 설문조사 저장 */
		surveyMapper.insertSurvey(dto);
		dto.setSurveyIdForQuestions();
		/* 질문 저장 */
		createQuestions(dto.getQuestions());
	}
	
	@Override
	public void createAnswer(AnswerDTO dto) {
		//todo: 필수 질문에 모두 답했는지 검증
		
		Map map = newIdsMap(dto.getSurveyId(), dto.getMemberId());
		int count = surveyMapper.countSumittedSurvey(map);
		
		if(count > 0) {
			throw new IllegalArgumentException("이미 응답을 완료한 설문입니다.");
		}
		surveyMapper.insertAnswers(dto);
	}


	@Override
	public void updateSurvey(SurveyDTO dto) throws IOException {
		/* 설문조사 내용 수정 */
		surveyMapper.updateSurvey(dto);
		/* 기존 질문 삭제 */
		Map<String, Long> map = newIdsMap(dto.getId(), dto.getMemberId());
		surveyMapper.deleteQuestionsBySurvey(map);
		/* 로컬에 저장된 파일 삭제 */
		//todo:
		
		/* 새로운 질문 저장 */
		dto.setSurveyIdForQuestions();
		createQuestions(dto.getQuestions());
	}


	@Override
	public void deleteSurvey(Long id, Long memberId) {
		Map<String, Long> map = newIdsMap(id, memberId);
		surveyMapper.deleteSurvey(map);
	}


	
	private void createQuestions(List<QuestionDTO> dtos) throws IOException {
		for(QuestionDTO dto : dtos) {
			/* 질문 저장 */
			surveyMapper.insertQuestions(dto);
						
			/* 질문 옵션 저장 */
			List<SelectOptionVO> optionVOs = dto.getOptions();
			if(optionVOs != null && !optionVOs.isEmpty()) {
				dto.setQuestionIdForOptions();
				surveyMapper.insertOptions(optionVOs);
			}
			
			/* 이미지 파일 저장 */
			MultipartFile file = dto.getFile();
			if(file != null && !file.isEmpty()) {
				FileVO fileVO = convertToFileVO(file);
				fileVO.setQuestionId(dto.getId());
				// DB 저장
				surveyMapper.insertFile(fileVO);
				// 로컬 저장
				String path = fileVO.getPath() + fileVO.getSavedName();
				fileUtils.saveToLocal(dto.getFile(), path);
			}
		}
	}

	/** 받은 파일의 유효성을 검사하고 FileVO를 생성 */
	private FileVO convertToFileVO(MultipartFile file) {
		/* 파일 유효성 검사 */
		fileUtils.validate(file);
		
		/* 필요한 파일 정보 추출 */
        String[] fullName = file.getOriginalFilename().split("\\.");
        String originalName = fullName[0];
        String extension = fullName[1].toLowerCase();
        String savedName = UUID.randomUUID().toString();
        String path = fileUtils.generateDirPath();
        Long size = file.getSize();
        /* FileVO 생성 */
        return new FileVO(originalName, extension, savedName, path, size);
	}
	
	
	//자주 사용되는 Map 생성 로직을 함수로 분리
	private Map<String, Long> newIdsMap(Long id, Long memberId) {
		Map<String, Long> map = new HashMap<>();
		map.put("id", id);
		map.put("memberId", memberId);
		
		return map;
	}
	
}
