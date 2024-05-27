package egovframework.example.board.service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import egovframework.example.board.vo.FileVO;

public interface FileService {	
	
	/** --------------- 조회  --------------- */
	List<FileVO> selectFileList(Long boardId) throws SQLException;
	
	FileVO selectFile(Long id) throws SQLException;

	
	/** --------------- 생성  --------------- */
	/** 파일 저장*/
	void insertAllFile(List<FileVO> file) throws SQLException;
	
	
	
	/** --------------- 삭제 --------------- */
	void deleteByRetainedFiles(Long boardId, Long[] retainedFileIds) throws SQLException;
	
	void deleteFileByboardId(Long boardId) throws SQLException;
	
	
	
	/** id에 해당하는 file과 원래 파일 이름을 반환 */
	Map.Entry<String, File> download(Long id) throws SQLException;
	
	/** DB에 File 정보를 저장하기 위해 VO객체로 변환 */
	List<FileVO> convertToFileVO(MultipartFile[] files, Long boardId);
	
	/** 
     * 파일을 하나씩 저장할 경우 예외 발생 시 이전에 저장한 파일을 전부 지워야하는 
     * 번거로움이 발생하기 때문에 모든 검증이 끝난 후 한번에 로컬에 저장 
     */
	void saveToLocal(List<FileVO> files) throws IOException;
}
