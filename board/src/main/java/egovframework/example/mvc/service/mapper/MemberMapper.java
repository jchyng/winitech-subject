package egovframework.example.mvc.service.mapper;

import java.util.Map;

import egovframework.example.mvc.vo.MemberVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("memberMapper")
public interface MemberMapper {
	/** 아이디에 해당하는  유저 조회 */
	public MemberVO selectMemberByUsername(String username);
	
	/** 유저 저장 */
	public int insertMember(Map<String, String> map);
}
