<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="validator"
	uri="http://www.springmodules.org/tags/commons-validator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<title>게시글 등록</title>
	<link
		href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
		rel="stylesheet"
		integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
		crossorigin="anonymous" />
	<link rel="stylesheet"
		href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200" />
	
	<!-- 게시글 등록인지 수정인지 구분하기 위한 flag -->
	<c:set var="registerFlag" value="${empty boardVO.id ? 'create' : 'modify'}"/>
	<c:set var="action" value="${empty boardVO.id ? '/boardRegister.do' : '/boardModifier.do'}"/>
	
	<script type="text/javaScript" language="javascript" defer="defer">
		const dataTransfer = new DataTransfer();
		/* 데이러 렌더링 시 파일 크기 포맷팅 JS 적용 */
		document.addEventListener("DOMContentLoaded", function() {
			var fileSizeElements = document.querySelectorAll('.fileSize');
			fileSizeElements.forEach(
					function(element) {
						var fileSize = parseInt(element.textContent.trim());
						element.textContent = formatBytes(fileSize);
					});
			}
		);
		
		
		/* onSubmit */
		function request(){
			/* validate */
			if(!validateVO()){
				return false;
			}
			// 파일을 업로드 버튼을 누르지 않은  파일은 제외
			var fileInput = document.getElementById("file");
			fileInput.value = '';
			// form이 전송되기 전에 dataTransfer에 담아둔 파일을 input에 넣어서 같이 전송
			fileInput.files = dataTransfer.files;
			
			return true;
		}
		
		
		/* 글 수정 시 FileVO를 담는 태그만 삭제 */
		function removeFileEntry(button) {
		    var fileEntry = button.parentElement;
		    fileEntry.remove();
		}
		
		/* 업로드된 파일 삭제 */
		function removeFile(fileName){
		 	for (var i = 0; i < dataTransfer.files.length; i++) {
		        if (dataTransfer.files[i].name == fileName) {
		            dataTransfer.items.remove(i);
		            return true; // 파일을 삭제했으므로 true 반환
		        }
		    }
		}
		
		/* 파일 삭제 버튼에 삭제 이벤트 추가
		   JS 클로저에 의해 태그 생성할 때 onClick 할당이 안되기 때문에
		     태그를 전부 생성한 후 한번에 이벤트 추가
		*/
		function addOnClickEventForFileRemover(){
			var deleteButtons = document.querySelectorAll('.fileRemover');
			  deleteButtons.forEach((button) => {
			    button.onclick = () => {
			        // 클릭된 버튼의 부모 요소인 파일 엔트리를 가져옴
			        var fileEntry = button.parentElement;
			        // 파일 이름을 가져옴
			        var fileName = fileEntry.querySelector('div > span').textContent;
			        
			        // 파일 이름을 removeFile 함수에 전달하여 호출
			        removeFile(fileName);
			     	// 파일 엔트리를 찾아 삭제
			        fileEntry.remove();
			    };
			  });
		}
		
	    /* 파일 업로드 */
		function uploadFiles() {
	      // 입력 받은 파일 정보
		  var fileInput = document.getElementById("file");
		  var files = fileInput.files;
		  
		  // 파일 정보를 추가할 부모 태그
		  var fileList = document.getElementById("fileList");
		
		  /* 파일 다중 업로드의 경우 반복이 필요함 */
		  for (var i = 0; i < files.length; i++) {
		    var file = files[i];
		    dataTransfer.items.add(file);
		
		    /* 업로드된 파일 태그 생성 */
		    var fileEntry = document.createElement("div");
		    fileEntry.className = "px-2 pt-3 d-block d-flex gap-1 align-items-center";
		
		    var deleteButton = document.createElement("button");
		    deleteButton.type = "button";
		    deleteButton.className = "fileRemover btn d-flex align-items-center";
		    deleteButton.innerHTML = '<span class="material-symbols-outlined text-danger">cancel</span>';
		
		    var fileNameSpan = document.createElement("span");
		    fileNameSpan.textContent = file.name;
		
		    var fileSize = formatBytes(file.size); //파일 사이즈 포맷팅
		    var fileSizeEm = document.createElement("em");
		    fileSizeEm.textContent = "[" + fileSize + "]";
				    
		    fileEntry.appendChild(deleteButton);
		    fileEntry.appendChild(fileNameSpan);
		    fileEntry.appendChild(fileSizeEm);
		    
		    /* FileList 태그에 업로드된 파일 태그 추가 */
		    fileList.appendChild(fileEntry);
		  }
		  
		  /* 업로드된 파일 삭제 함수 추가 */
	      addOnClickEventForFileRemover();
		  
		  /* 다음 입력을 받기 위해 비워준다 */
		  fileInput.value = "";
		}
	    
	    /* VO 검증 */
	    function validateVO(){
	    	var member = document.forms["boardDetailForm"]["member"].value;
	        var password = document.forms["boardDetailForm"]["password"].value;
	        var title = document.forms["boardDetailForm"]["title"].value;
	        var content = document.forms["boardDetailForm"]["content"].value;
	    	
	      	//작성자 검증
	        if(!member){
	        	alert("작성자 이름은 필수입니다.");
	        	return false;
	        }
	        if(member.length > 6){	        	
	        	alert("작성자 이름은 6자 이내로 작성해주세요.");
	        	return false;
	        }
	      	//비밀번호 검증
	        if(!password){	        	
	        	alert("비밀번호는 필수입니다.");
	        	return false;
	        }
	        if(!password.match("^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$")){
	        	alert("비밀번호는 영문,숫자,특수 문자를 포함한 8~15자 입니다.");
	        	return false;
	        }
	      	//제목 검증
	        if(!title) {
	        	alert("제목은 필수입니다.");
	        	return false;
	        }
	        if(title.length > 100) {	        	
	        	alert("제목은 100자 이내로 작성해주세요.");
	        	return false;
	        }
	        //내용 검증
	        if(!content) {
	        	alert("내용은 필수입니다.");
	        	return false;
	        }
	        if(content.length > 1000){	        	
	        	alert("작성자 이름은 1000자 이내로 작성해주세요.");
	        	return false;
	        }
	        return true;
	    }
	      
      	/* 파일 업로드 검증 */
	    function validateFile() {
	        var fileInput = document.getElementById('file');
	        var file = fileInput.files[0];
	        var preventExtensions = ['exe', 'bat', 'sh', 'jsp', 'js', 'php', 'sql'];

	        var fileName = file.name;
	        var fileExtension = fileName.split('.').pop();
	        if (preventExtensions.includes(fileExtension.toLowerCase())) {
	        	alert('업로드가 허용되지 않는 파일입니다.');
	            fileInput.value = ''; // 업로드된 파일을 초기화하여 다시 선택
	        }
	    }  
		
		/* 파일 크기 포맷팅 */
		function formatBytes(bytes) {
		    if (bytes === 0) return '0 Bytes';
		    var k = 1024,
		        sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'],
		        i = Math.floor(Math.log(bytes) / Math.log(k));
		    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
		}


	</script>
</head>
<body>	
	<form action="${action}" name="boardVO" id="boardDetailForm" enctype="multipart/form-data" 
			onsubmit="return request();" method="post" class="container mt-3" >
		<!-- 게시글 수정 및 답변글에 사용될 데이터 = Controller에서 ID에 해당하는 PW를 검증하기 때문에 유저가 변경해도 문제 없다 -->
		<input type="hidden" name="id" value="${boardVO.id}" />	
		<input type="hidden" name="parentId" value="${boardVO.parentId}" />
		
		<div class="row py-3 border-bottom">
			<!-- 작성자 입력 -->
			<div class="row col">
				<label for="member" class="d-flex align-items-center col-md-4 col-form-label fw-semibold">작성자
					<span class="material-symbols-outlined text-danger">check</span>
				</label>
				<div class="col-md-7">
					<input name="member" type="text" class="form-control" value="${boardVO.member}"/>
				</div>
			</div>
			<!-- 비밀번호 입력 -->
			<div class="row col">
				<label for="password" class="d-flex align-items-center col-md-4 col-form-label fw-semibold">비밀번호
					<span class="material-symbols-outlined text-danger">check</span>
				</label>
				<!-- 게시글 생성 때만 입력 창 생성 -->
				<c:if test="${registerFlag == 'create'}">
					<div class="col-md-7" >
						<input name="password" type="password" class="form-control" id="password" />
					</div>
				</c:if>
			</div>
		</div>
		<!-- 제목 입력 -->
		<div class="row border-bottom py-3">
			<label for="title"
				class="d-flex align-items-center col-md-2 col-form-label fw-semibold">제목
				<span class="material-symbols-outlined text-danger">check</span>
			</label>
			<div class="col-md-10">
				<input name="title" type="text" class="form-control" value="${boardVO.title}"/>
			</div>
		</div>
		<!-- 내용 입력 -->
		<div class="row border-bottom py-3">
			<label for="contents" class="col-md-2 col-form-label fw-semibold">내용</label>
			<div class="col-md-10">
				<textarea name="content" form="boardDetailForm" class="form-control"
					style="min-height: 360px">${boardVO.content}</textarea>
			</div>
		</div>
		<!-- 파일 첨부  -->
		<div class="row border-bottom py-3" >
			<label for="files" class="col-md-2 col-form-label fw-semibold">첨부파일</label>
			<div class="col-md-10" id="fileList">
				<div class="input-group w-75 mb-3">
					<input multiple type="file" name="files" id="file" onchange="validateFile()" class="form-control" />
					<button class="btn btn-outline-secondary" type="button" onclick="uploadFiles()">업로드</button>
				</div>
				
				<!-- 수정 시 받아온 파일 렌더링(이때는 각 버튼에 삭제 시 삭제된 id를 보관하는 메서드를 넣어줘야함) -->
			 	<c:forEach var="fileVO" items="${fileVOs}" varStatus="status">
					<div class="px-2 pt-3 d-block d-flex gap-1 align-items-center">
					    <button type="button" class="fileRemover btn d-flex align-items-center"
					    	onclick="removeFileEntry(this)">
					        <span class="material-symbols-outlined text-danger">cancel</span>
					    </button>
					    <span>${fileVO.originalName}.${fileVO.extension}</span>
					    <em class="fileSize">${fileVO.size}</em>
					   	<input type="hidden" name="retainedFileIds" value="${fileVO.id}" />
					</div>
				</c:forEach>
				
			</div>
		</div>
		
		<div class="d-flex justify-content-end gap-2 pt-3"> 
			<!-- 수정일 때는 비밀 번호 입력 창 하단에 생성 -->
			<c:if test="${registerFlag == 'modify'}">
				<input name="password" id="password" type="password" class="form-control w-auto" placeholder="비밀번호" />
			</c:if>
		    <button type="submit" class="btn btn-primary">저장</button>	
			<!-- 취소 버튼 -->
			<a href="/boardList.do" class="btn btn-outline-secondary">취소</a>
		</div>
	</form>
	
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
		crossorigin="anonymous"></script>
</body>
</html>