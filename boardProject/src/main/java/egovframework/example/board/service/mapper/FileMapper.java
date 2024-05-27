package egovframework.example.board.service.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import egovframework.example.board.vo.FileVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper
public interface FileMapper {
	/** --------------- 조회  --------------- */
	List<FileVO> selectFileList(Long boardId) throws SQLException;
	
	FileVO selectFile(Long id) throws SQLException;
	
	
	/** --------------- 생성  --------------- */
	void insertAllFile(List<FileVO> file) throws SQLException;
	
	
	/** --------------- 삭제  --------------- */
	
	/**
	 * @param map  boardId와 retainedFileIds를 전달
	 * @throws SQLException
	 */
	void deleteByRetainedFiles(Map<String, Object> map) throws SQLException;
	
	
	void deleteFileByboardId(Long boardId) throws SQLException;
}
