package egovframework.example.board.util;

import java.util.UUID;

import static egovframework.example.board.util.ExceptionMessage.ILLEGAL_FILE_EXTENSION;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import egovframework.example.board.vo.FileVO;
import egovframework.rte.fdl.property.EgovPropertyService;



/** 파일과 관련된 처리를 담당하는 유틸 클래스 */
@Component
public class FileUtils {
	private Log log = LogFactory.getLog(this.getClass());

	@Autowired
	protected EgovPropertyService propertiesService;

	private String FILE_PATH;
	

	
	/**
	 * 노출되면 위함할 수 있는 파일 경로를  properties 파일을 통해 외부에서 받아온다.
	 */
	@PostConstruct
	public void init() {
		FILE_PATH = propertiesService.getString("filePath");
	} 
	
	
	
	/**
	 * @param files
	 * @return List<FileVO>
	 * @throws IOException
	 * 
	 * 받은 파일을 기반으로 유효성을 검사하고 FileVO를 생성한다.
	 * 더미 파일이 들어올 경우 빈 리스트 반환
	 */
	public List<FileVO> convertToFileVO(MultipartFile[] files, Long boardId) {
		List<FileVO> fileVOs = new ArrayList<FileVO>();
		
		for(MultipartFile file : files) {
			/* 파일 유효성 검사 */
			if(!validate(file)) {
				continue;
			}
			
			/* 필요한 파일 정보 추출 */
            String[] fullName = file.getOriginalFilename().split("\\.");
            
            String realName = fullName[0];
            String extension = fullName[1];
            String savedName = UUID.randomUUID().toString();
            String path = generateDirPath();
            Long size = file.getSize();
                        
            /* FileVO 생성 */
            FileVO fileVO = new FileVO(boardId, realName, extension, savedName, path, size, file);
            fileVOs.add(fileVO);
        }		
		
		return fileVOs;
	}
	
	
	/**
	 * @param savedLocalFiles
	 * @throws IOException
	 * 
	 * FileVO에서 File과 경로를 얻어 로컬에 저장한다.
	 */
	public void saveToLocal(List<FileVO> fileVOs) throws IOException {
		/* 폴더 생성은 최초 한번만 하면 되기 때문에  0번 인덱스에 대해서만 진행 */
		makeDirectory(fileVOs.get(0));
		
		/* 로컬에 파일 저장 */
		for(FileVO fileVO : fileVOs) {
			MultipartFile multipartFile = fileVO.getFile();
			File file = makeFileByVO(fileVO);
			
			multipartFile.transferTo(file);
			log.info("파일이 정상적으로 생성되었습니다 - PATH: " + file.getPath());
		}
	}
	
	
	
	/**
	 * @param fileVO
	 * @return FileVO의 경로를 통해 생성된 File
	 */
	public File makeFileByVO(FileVO fileVO) {
		String path = fileVO.getPath() + File.separator + fileVO.getSavedName();
		return new File(path);
	}
	
	
	
	/**
	 * @return 파일을 2024-04와 같이 년도와 월로 구분하기 위한 폴더 경로 생성
	 * */
	private String generateDirPath() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String folderName = sdf.format(date);
		
        return FILE_PATH + File.separator + folderName;
	}
	
	
	
	/**
	 * @param fileVO
	 * 
	 * FileVO의 경로를 통해 해당 디렉터리가 존재하는지 확인한 후 없으면 폴더를 생성한다.
	 */
	private void makeDirectory(FileVO fileVO) {
		File dir = new File(fileVO.getPath());
		if(!dir.exists()) {
			dir.mkdir();
			log.info("디렉터리가 생성되었습니다 - PATH: " + dir.getPath());
		}
	}
	
	
	/**
	 * @param file
	 * @throws IllegalArgumentException 유효하지 않은 파일 확장자인 경우
	 * @return 유효한 파일인 경우 true, 그렇지 않은 경우 false
	 * 
	 * 파일의 유효성을 검사한다.
	 */
	private boolean validate(MultipartFile file) throws IllegalArgumentException {
		/** 
		 * 더미 파일이 넘어왔을 경우 스킵 
		 * HTML File 전송 시 악성 사이트에서 사용자의 파일 경로를 확인하는 것을 방지하기 위해 
		 * C:\fakepath\가 prefix로 붙게 된다. => 빈 파일이 들어온다.
		 */		
		if(file.isEmpty()) {
			return false;
		}
		
		/** 
		 * 확장자 검증 
		 * 허가되지 않은 확장자 업로드 시 문제가 될 수 있기 때문에 
		 * 다음 작업으로 넘어가지 않기 위해 예외 발생
		 */
		String[] fullName = file.getOriginalFilename().split("\\.");
	    String extension = fullName[1];
	    PreventExtension.validate(extension);
	    
	    return true;
	}
	
	
	
	
	
	
	/**
	 * 파일 확장자를 관리하기 위한 enum
	 */
	private enum PreventExtension {
		/* 실행 가능 파일 */
		EXE("exe"), BAT("bat"), SH("sh"),
		
		/* script & sql */
		JS("js"), JSP("jsp"), PHP("php"), SQL("sql");
		
		
		private final String extension;

		private PreventExtension(String extension) {
			this.extension = extension;
		}

		public String getExtension() {
			return extension;
		}
		
		private static void validate(String extension) {
			for(PreventExtension ex: PreventExtension.values()) {
				if(ex.getExtension().equals(extension.toLowerCase())) {
					/* 허가되지 않은 확장자 사용 시 예외 발생 */
					throw new IllegalArgumentException(ILLEGAL_FILE_EXTENSION.getMessage());
				}
			}
		}
	}
}
