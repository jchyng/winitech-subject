
package egovframework.example.mvc.service.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import egovframework.example.mvc.vo.PostVO;
import egovframework.example.mvc.vo.SearchAndPaginationVo;
import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Mapper("postMapper")
public interface PostMapper {
	/** 게시글 조회 */
	Map<String, Object> selectPost(Long id);
	
	/** 게시글 목록 조회 */
	List<EgovMap> selectPostList(SearchAndPaginationVo searchVO);
	
	/** 총 게시글 수 조회 */
	int selectPostTotCnt(SearchAndPaginationVo searchVO);
	
	/** 게시글 등록  */
	void insertPost(PostVO postVO) throws SQLException;
	
	/** 조회 수 증가 */
	void increaseViewCount(Long id);
	
	/** 수정 */
	int updatePost(Map<String, Object> map);

	/** 삭제 */
	int deletePostById(Map<String, Long> ids);
}
