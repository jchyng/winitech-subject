package egovframework.example.mvc.service;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import egovframework.example.mvc.dto.AnswerDTO;
import egovframework.example.mvc.dto.PaginationDTO;
import egovframework.example.mvc.dto.SurveyDTO;
import egovframework.example.mvc.dto.statistics.StatisticsSurveyDTO;
import egovframework.example.mvc.vo.SurveyVO;
import egovframework.rte.psl.dataaccess.util.EgovMap;


public interface SurveyService {
	/** 내 설문조사 검색 */
	List<EgovMap> searchInMySurveys(Map<String, Object> map);
	/** 내 설문조사 검색 결과 개수*/
	int getCountInMySurveys(Map<String, Object> map);
	/** 전체 설문조사 검색 */
	List<EgovMap> searchSurveys(Map<String, Object> map);
	/** 전체 설문조사 검색 결과 개수 */
	int getCountSurveys(PaginationDTO dto);
	
	/** 설문조사 조회 */
	SurveyDTO findSurvey(Long id, Long memberId);
	/** 설문조사 통계 조회 */
	StatisticsSurveyDTO findStatisticsForSurvey(Long id, Long memberId);
	/** 이미지 조회 */
	byte[] getFileImg(Long id) throws IOException;

	
	/** 설문조사 생성 [설문조사, 질문, 객관식 옵션, 이미지 파일] 모두 포함 */
	void createSurvey(SurveyDTO dto) throws IOException;
	/** 답변 생성 */
	void createAnswer(AnswerDTO dto);

	
	/** 설문조사 수정 */
	void updateSurvey(SurveyDTO dto) throws IOException;
	
	
	/** 설문조사 삭제 */
	void deleteSurvey(Long id, Long memberId);
}
