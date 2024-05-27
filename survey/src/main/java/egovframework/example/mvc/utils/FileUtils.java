package egovframework.example.mvc.utils;

import javax.annotation.PostConstruct;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import egovframework.example.mvc.vo.enums.PreventFileExtension;
import egovframework.rte.fdl.property.EgovPropertyService;



@Component
public class FileUtils {
	private Log log = LogFactory.getLog(this.getClass());

	@Autowired
	protected EgovPropertyService propertiesService;

	public static String FILE_PATH;	
	
	/** 
	 * 노출되면 위함할 수 있는 파일 경로를  properties 파일을 통해 외부에서 받아옴
	 * 이때 propertiesService가 빈이기 때문에 FileUtils 생성 후 FILE_PATH 주입
	 * */
	@PostConstruct
	public void init() {
		FILE_PATH = propertiesService.getString("filePath");
	} 
	
	
	
	/** File을 로컬의 원하는 경로에 저장한다. */
	public void saveToLocal(MultipartFile file, String path) throws IOException {
		/* 로컬에 파일 저장 */
		File savedfile = new File(FILE_PATH + path);
		file.transferTo(savedfile);
		log.info("파일이 정상적으로 생성되었습니다 - PATH: " + savedfile.getPath() + savedfile.getName());
	}
	
	
	/** 파일의 유효성을 검사 */
	public void validate(MultipartFile file) throws IllegalArgumentException {
		/** 
		 * 더미 파일이 넘어왔을 경우 스킵 
		 * HTML File 전송 시 악성 사이트에서 사용자의 파일 경로를 확인하는 것을 방지하기 위해 
		 * C:\fakepath\가 prefix로 붙게 된다. => 빈 파일이 들어온다.
		 */		
		if(file.isEmpty()) {
			return;
		}
		/** 허가되지 않은 확장자 업로드 시 문제가 될 수 있기 때문에 예외 발생 */
		String[] fullName = file.getOriginalFilename().split("\\.");
		String extension = fullName[1];

		if(!PreventFileExtension.validate(extension)) {			
			throw new IllegalArgumentException("허가되지 않은 확장자를 가진 파일 입니다.");
		}
	}
	
	
	/** 파일을 날짜 별(yyyy-MM)로 구분하기 위한 폴더 경로 생성 */
	public String generateDirPath() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String dirPath = sdf.format(date) + "/";
		
		//존재하지 않는 경로일 경우 폴더 생성
		makeDirectory(dirPath);
		
        return dirPath;
	}
	
	/** FileVO의 경로를 통해 해당 디렉터리가 존재하는지 확인한 후 없으면 폴더를 생성 */
	private void makeDirectory(String dirPath) {
		File dir = new File(FILE_PATH + dirPath);
		if(!dir.exists()) {
			dir.mkdir();
			log.info("디렉터리가 생성되었습니다 - PATH: " + dir.getPath());
		}
	}
	
}
