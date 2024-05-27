package egovframework.mvc.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.mvc.domain.Toilet;
import egovframework.mvc.service.ToiletService;
import egovframework.mvc.service.mapper.ToiletMapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;


@Service
public class ToiletServiceImpl implements ToiletService {
	@Autowired private ToiletMapper toiletMapper;

	@Override
	public List<EgovMap> fetchToiletListByLocation(Map<String, Double> location) {
		return toiletMapper.selectToiletListByLocation(location);
	}
	
}
