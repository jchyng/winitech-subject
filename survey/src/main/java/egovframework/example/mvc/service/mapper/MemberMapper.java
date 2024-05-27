package egovframework.example.mvc.service.mapper;

import java.util.Map;

import egovframework.example.mvc.vo.MemberVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper
public interface MemberMapper {
	/*INSERT QUERY*/
	int insertMember(Map<String, String> map);
	
	/*SELECT QUERY*/
	Boolean checkUsernameExists(String username);
	MemberVO selectMemberByUsername(String username);
	
	/*UPDATE QUERY*/
	
	/*DELETE QUERY*/
}