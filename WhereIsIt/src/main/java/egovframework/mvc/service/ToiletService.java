package egovframework.mvc.service;

import java.util.List;
import java.util.Map;

import egovframework.mvc.domain.Toilet;
import egovframework.rte.psl.dataaccess.util.EgovMap;

public interface ToiletService {
	/** 지도에서 보여줄 범위  */
	List<EgovMap> fetchToiletListByLocation(Map<String, Double> location);
}
