package egovframework.example.board.service;

import java.sql.SQLException;
import java.util.List;

import egovframework.example.board.vo.BoardVO;
import egovframework.example.board.vo.SearchAndPaginationVo;

public interface BoardService {
	/** --------------- 조회  --------------- */
	List<BoardVO> selectBoardList(SearchAndPaginationVo searchVO) throws SQLException;
	
	int selectBoardListTotCnt(SearchAndPaginationVo searchVO) throws SQLException;
	
	BoardVO selectBoard(Long id) throws SQLException;
	
	String selectPassword(Long id) throws SQLException;
	
	
	/** --------------- 생성  --------------- */
	Long insertBoard(BoardVO vo) throws SQLException;
	

	/** --------------- 수정  --------------- */
	void updateBoard(BoardVO vo) throws SQLException;
	
	void increaseViewCount(Long id) throws SQLException;
	
	
	/** --------------- 삭제 --------------- */
	void deleteBoard(BoardVO vo) throws SQLException;
}
