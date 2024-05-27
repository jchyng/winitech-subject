package egovframework.example.board.service.mapper;

import java.sql.SQLException;
import java.util.List;

import egovframework.example.board.vo.BoardVO;
import egovframework.example.board.vo.SearchAndPaginationVo;
import egovframework.rte.psl.dataaccess.mapper.Mapper;


@Mapper
public interface BoardMapper {
	/** --------------- 조회  --------------- */
	
	/* 게시글 상세 조회 */
	BoardVO selectBoard(Long id) throws SQLException;
	
	/* 게시글 목록 조회 */
	List<BoardVO> selectBoardList(SearchAndPaginationVo searchVO) throws SQLException;

	/* 게시글 목록 개수 조회 */
	int selectBoardListTotCnt(SearchAndPaginationVo searchVO) throws SQLException;
	
	/* 게시글 비밀번호 조회 */
	String selectPassword(Long id) throws SQLException;

	
	
	/** --------------- 생성  --------------- */
	
	/* 게시글 생성 */
	Long insertBoard(BoardVO vo) throws SQLException;

	
	
	/** --------------- 수정  --------------- */
	
	/* 게시글 수정*/
	void updateBoard(BoardVO vo) throws SQLException;
	
	/* 게시글 조회 수 증가 */
	void increaseViewCount(Long id) throws SQLException;
	
	
	
	/** --------------- 삭제  --------------- */
	
	/* 게시글 삭제 */
	void deleteBoard(BoardVO vo) throws SQLException;
	
}
