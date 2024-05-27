package egovframework.mvc.service.mapper;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Mapper
public interface ToiletMapper {
	/* 
	 * RuntimeException은 JVM에서 알아서 throws를 해주기 때문에 throws를 생략할 수 있다.
	 * => SQL Exception 생략 
	 * */
	
	/** 위도 경도 범위에 속하는 화장실 목록을 조회한다. */
	List<EgovMap> selectToiletListByLocation(Map<String, Double> location);
}
