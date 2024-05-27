
package egovframework.example.mvc.service.mapper;

import java.sql.SQLException;
import java.util.List;

import egovframework.example.mvc.vo.FileVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("fileMapper")
public interface FileMapper {
	/** 파일 저장 */
	void insertFiles(List<FileVO> fileVOs) throws SQLException;
	
	/** 게시글에 해당하는 파일 조회 */
	List<FileVO> selectFilesByPostId(Long postId);
	
	/** 아이디에 해당하는 파일 조회 */
	FileVO selectFileById(Long id);
	
	/** 아이디  리스트에 해당하는 파일들 삭제 */
	void deleteFileByIds(List<Long> ids);
}
