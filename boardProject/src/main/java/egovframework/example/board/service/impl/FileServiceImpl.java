package egovframework.example.board.service.impl;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import egovframework.example.board.service.FileService;
import egovframework.example.board.service.mapper.FileMapper;
import egovframework.example.board.util.FileUtils;
import egovframework.example.board.vo.FileVO;


@Service
public class FileServiceImpl implements FileService{
	@Autowired
	private FileMapper fileMapper;
	
	@Autowired
	private FileUtils fileUtils;
	
	
	/** --------------- 조회  --------------- */
	@Override
	public List<FileVO> selectFileList(Long boardId) throws SQLException {
		return fileMapper.selectFileList(boardId);
	}
	
	@Override
	public FileVO selectFile(Long id) throws SQLException {
		return fileMapper.selectFile(id);
	}



	/** --------------- 생성  --------------- */
	@Override
	public void insertAllFile(List<FileVO> fileVOs) throws SQLException {
		fileMapper.insertAllFile(fileVOs);
	}
	
	/** --------------- 삭제  --------------- */
	@Override
	public void deleteByRetainedFiles(Long boardId, Long[] retainedFileIds) throws SQLException{
		Map<String, Object> map = new HashMap<>();
		
		map.put("boardId", boardId);
		map.put("retainedFileIds", retainedFileIds);
		
		fileMapper.deleteByRetainedFiles(map);
	}
	
	@Override
	public void deleteFileByboardId(Long boardId) throws SQLException{
		fileMapper.deleteFileByboardId(boardId);
	}
	
	
	
	/** --------------- 유틸 --------------- */
	@Override
	public List<FileVO> convertToFileVO(MultipartFile[] files, Long boardId) {
		return fileUtils.convertToFileVO(files, boardId);
	}

	@Override
	public void saveToLocal(List<FileVO> fileVOs) throws IOException{
		fileUtils.saveToLocal(fileVOs);
	}
	
	@Override
	public Map.Entry<String, File> download(Long id) throws SQLException{
		FileVO fileVO = selectFile(id);
		File file = fileUtils.makeFileByVO(fileVO);
		String originalFileName = fileVO.getOriginalName() + "." + fileVO.getExtension();
		
		return new AbstractMap.SimpleEntry<>(originalFileName, file);
	}

	
}
