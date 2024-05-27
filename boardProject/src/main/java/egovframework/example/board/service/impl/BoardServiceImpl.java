package egovframework.example.board.service.impl;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import egovframework.example.board.service.BoardService;
import egovframework.example.board.service.mapper.BoardMapper;
import egovframework.example.board.vo.BoardVO;
import egovframework.example.board.vo.SearchAndPaginationVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static egovframework.example.board.util.ExceptionMessage.*;


@Service
public class BoardServiceImpl implements BoardService {
	@Autowired
	private BoardMapper boardMapper;
	
	/** --------------- 조회  --------------- */
	@Override
	public BoardVO selectBoard(Long id) throws SQLException {
		/* 게시글 조회 */
		BoardVO resultVO = boardMapper.selectBoard(id);
		
		/* 게시글이 존재하지 않으면 예외 발생 */
		if (resultVO == null) {
			throw new EntityNotFoundException(ENTITY_NOT_FOUND_MESSAGE.getMessage());
		}
		return resultVO;
	}

	@Override
	public List<BoardVO> selectBoardList(SearchAndPaginationVo searchVO) throws SQLException {
		return boardMapper.selectBoardList(searchVO);
	}

	@Override
	public int selectBoardListTotCnt(SearchAndPaginationVo searchVO) throws SQLException {
		return boardMapper.selectBoardListTotCnt(searchVO);
	}

	@Override
	public String selectPassword(Long id) throws SQLException {
		String password = boardMapper.selectPassword(id);
		if (password == null || password.isEmpty()) {
			throw new EntityNotFoundException(ENTITY_NOT_FOUND_MESSAGE.getMessage());
		}
		return password;
	}
 
	
	
	/** --------------- 생성 --------------- */
	
	/**
	 * 기존 샘플 코드에서는 IdGen을 사용해서 pk를 직접 set으로 넣어주는 방식을 사용했지만 과제 구현에서는 A.I를 사용해서 
	 * IdGen을 생략하고 더 간단하게 구현
	 * 
	 * 문제: 분산 DB 환경에서는 A.I가 따로 동기화 되어있지 않다면, 동일한 pk가 나타날 수 있다. 
	 * 		그렇기 때문에 시퀀스나 UUID와 같은 고유 식별자를 사용하거나, 
	 * 		시퀀스의 경우 미리 일정 범위의 시퀀스를 할당해서 각 분산 환경에서 사용하여 중복 문제를 해결할 수 있다.
	 * 		또한 데이터 삭제 시 A.I 번호에 구멍이 생기는 단점이 있다.
	 * 
	 * 결정한 이유: 현재 예제는 단일 DB 환경이고 MySQL을 사용하고 있기 때문에 A.I를 사용해서 
	 * 			  다음 id를 조회하는 추가 쿼리를 방지하고, String 보다 적은 용량을 차지하는 Long 타입으로 PK를 선택함. 
	 */
	@Override
	public Long insertBoard(BoardVO vo) throws SQLException {
		return boardMapper.insertBoard(vo);
	}

	
	
	/** --------------- 수정 --------------- */
	
	@Override
	public void updateBoard(BoardVO vo) throws SQLException {
		boardMapper.updateBoard(vo);
	}

	@Override
	public void increaseViewCount(Long id) throws SQLException {
		boardMapper.increaseViewCount(id);
	}

	
	
	/** --------------- 삭제 --------------- */
	
	@Override
	public void deleteBoard(BoardVO vo) throws SQLException {
		boardMapper.deleteBoard(vo);
	}

}
