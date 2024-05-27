package egovframework.example.mvc.service.mapper;

import java.util.List;
import java.util.Map;

import egovframework.example.mvc.dto.AnswerDTO;
import egovframework.example.mvc.dto.PaginationDTO;
import egovframework.example.mvc.dto.QuestionDTO;
import egovframework.example.mvc.dto.SurveyDTO;
import egovframework.example.mvc.dto.statistics.StatisticsSurveyDTO;
import egovframework.example.mvc.vo.FileVO;
import egovframework.example.mvc.vo.SelectOptionVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Mapper
public interface SurveyMapper {
	/* INSERT QUERY */
	int insertSurvey(SurveyDTO dto);
	int insertQuestions(QuestionDTO dto);
	int insertOptions(List<SelectOptionVO> vos);
	int insertFile(FileVO vo);
	int insertAnswers(AnswerDTO dto);
	
	/* SELECT QUERY */
	List<EgovMap> selectMySurveys(Map<String, Object> map);
	List<EgovMap> selectSurveys(Map<String, Object> map);
	SurveyDTO selectSurvey(Map<String, Long> map);
	StatisticsSurveyDTO selectStatistics(Map<String, Long> map);
	FileVO selectFile(Long id);
	int countMySurveys(Map<String, Object> map);
	int countSurveys(PaginationDTO dto);
	int countSumittedSurvey(Map<String, Long> map);
	
	/* UPDATE QUERY */
	int updateSurvey(SurveyDTO dto);
	int deleteSurvey(Map<String, Long> map);
	
	/* DELETE QUERY */
	int deleteQuestionsBySurvey(Map<String, Long> map);
}
