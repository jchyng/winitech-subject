package egovframework.mvc.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.mvc.domain.Toilet;
import egovframework.mvc.service.ToiletService;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Controller
public class MapController {
	/**
	 * controller = list, get, post, update, delete
	 * service = fetchItemList, fecth, add, update, remove
	 * mapper = select, insert, update, delete
	 * repo = find, save, update, delete 
	 * */
	@Autowired private ToiletService toiletService;
	
	

	@GetMapping("/map.do")
	public String getMap() {
		return "main";
	}
	
	@PostMapping("/list/toilet.do")
	public @ResponseBody ResponseEntity<?> listToilet(@RequestBody Map<String, Double> location) 
			throws SQLException{
		List<EgovMap> toilets =  toiletService.fetchToiletListByLocation(location);
		return ResponseEntity.ok(toilets);
	}
}
