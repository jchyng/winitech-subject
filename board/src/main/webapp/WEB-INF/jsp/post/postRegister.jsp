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
	<!-- CSRF 정보를 담은 META DATA -->
	<meta name="_csrf" content="${_csrf.token}"/>
	<meta name="_csrf_header" content="${_csrf.headerName}"/>
	<title>게시글 등록</title>
	<link
		href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
		rel="stylesheet"
		integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
		crossorigin="anonymous" />
	<link rel="stylesheet"
		href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200" />
	<style>
		img {
			object-fit: cover;
		}
		#content > img {
			width: 200px;
			height: 200px;
		}
	</style>

	<!-- 게시글 등록인지 수정인지 구분하기 위한 flag -->
	<c:set var="flag" value="${empty postVO.id ? 'create' : 'modify'}"/>
	<!-- script -->
	<script src="//code.jquery.com/jquery-3.5.1.min.js" ></script>
	<script type="text/javaScript" language="javascript" defer="defer">
		/* CSRF info */
		const token = $("meta[name='_csrf']").attr("content");
		const header = $("meta[name='_csrf_header']").attr("content");
		
		/* File 저장 */
		const dataTransfer = new DataTransfer();
		/* 썸네일로 사용할 파일 이름 저장 */
		let thumbnail = '${postVO.thumbnail}';
		/* 삭제된 파일 아이디 저장 */
		let removedFileIds = [];
		
		/* 등록 & 수정 요청 */
		function request(){
			/* src가 너무 길기 때문에 초기화
			     현재 업로드한 파일에 대해서만 초기화 해야하기 때문에 
			   src가 data로  시작하는 src만 초기화
			*/
			$("#content img[src^='data']").attr("src", "");
			
			/* validate */
			if(!validateVO()){
				return;
			}
			/* target URL 생성 */
			var requestType = "<c:out value='${flag}'/>";
			var requestURL = requestType == 'create'? '/user/postRegister.do' : '/user/postModifier.do';
			
			/* 게시글 수정 시 수정할 게시글 id 추가 */
			var form = new FormData();
			if(requestType == 'modify'){
				form.append('id', ${postVO.id});
				form.append('removedFileIds', removedFileIds);
			}	
			form.append('title', $("#title").val());
			form.append('content', $("#content").html());
			form.append('thumbnail', thumbnail);
			
			/* 파일 추가 */
		   	for (var i = 0; i < dataTransfer.files.length; i++) {
	   			form.append("files", dataTransfer.files[i]);
		   	}
		   	
			$.ajax({
				url : requestURL,
				type : "post",
				processData: false,
		        contentType: false,
				data : form,
				beforeSend: function(xhr) {   
					xhr.setRequestHeader(header, token);
	            },
				dataType: 'text', 	
				success : (response, status, header) => {
				 	window.location.href = "/postDetail.do?id=" + response;
				},
				error: (request, status, error) => {
					alert(error)
				}
			});
		}
		
		/* VO 검증 */
	    function validateVO(){
	        var title = $("#title").val();
	        var content = $("#content").html();
	    	
	      	//제목 검증
	        if(!title || title.length < 1) {
	        	alert("제목은 필수입니다.");
	        	return false;
	        }
	        if(title.length > 100) {	        	
	        	alert("제목은 100자 이내로 작성해주세요.");
	        	return false;
	        }
	        //내용 검증
	        if(!content || content.length < 1) {
	        	alert("내용은 필수입니다.");
	        	return false;
	        }
	        if(content.length > 1000){	        	
	        	alert("내용은 1000자 이내로 작성해주세요.");
	        	return false;
	        }
			//썸네일 선택
			if(!thumbnail || thumbnail.lengh < 1){
				alert("썸네일 이미지를 선택해주세요.");
				return false;
			}
	        return true;
	    }		
		
	    /* 파일 업로드 */
		function uploadFiles() {
	      // 입력 받은 파일 정보
		  var fileInput = document.getElementById("files");
		  var files = fileInput.files;
		  
		  /* 파일의 크기가 0인 더미 파일을 제외 */
		  for (var i = 0; i < files.length; i++) {
		    var file = files[i];
		    if(file.size > 0){	
		    	dataTransfer.items.add(file);
		    	//게시글 내용에 이미지 추가
		    	addImage(file);	
				//업로드된 파일 태그 생성
		    	createUploadedFileTag(file);
		  	 }
		  }
		  /* JS 클로저로 인해 파일 삭제 이벤트를  태그 생성이 끝난 후 일괄적으로 등록 */
		  addOnClickEventForFileRemover();
		  
		  // 다음 입력을 받기 위해 비워준다 
		  fileInput.value = "";
		}
	    
	    /* 이미지 컨텐츠에 추가 */
	    function addImage(file){
	    	let content = $("#content");
	    	
	    	var reader = new FileReader();
	    	
	        reader.onload = function(e) {
	            var imgElement = document.createElement('img');
	            imgElement.src = e.target.result;
	            imgElement.alt = "image";
	            imgElement.setAttribute('data-filename', file.name);

	            content.append(imgElement);
	        };
	        reader.readAsDataURL(file);
	    }
	    
	    /* 업로드된 파일 태그 생성 */
	    function createUploadedFileTag(file){
	    	// 파일 정보를 추가할 부모 태그
		    var fileList = document.getElementById("fileList");
	    	
	    	/* 업로드된 파일 태그 생성 */
		    var fileEntry = document.createElement("div");
		    fileEntry.className = "px-2 pt-3 d-block d-flex gap-1 align-items-center";
		
		    /* 썸네일 라디오 버튼 */
		    var radioButton = document.createElement("input");
		    radioButton.type = "radio";
		    radioButton.name = "thumbnail";
		    radioButton.value = file.name;
		    radioButton.onclick = function(e){
		    	thumbnail = e.target.value;
		    };
		    
		    // 파일 이름
		    var fileNameSpan = document.createElement("span");
		    fileNameSpan.textContent = file.name;
		
		    // 파일 크기
		    var fileSize = formatBytes(file.size); //파일 사이즈 포맷팅
		    var fileSizeEm = document.createElement("em");
		    fileSizeEm.textContent = "[" + fileSize + "]";
			
		    // 삭제 버튼
		    var deleteButton = document.createElement("button");
		    deleteButton.type = "button";
		    deleteButton.className = "fileRemover btn d-flex align-items-center";
		    deleteButton.innerHTML = '<span class="material-symbols-outlined text-danger">cancel</span>';
		    
		    fileEntry.appendChild(radioButton);
		    fileEntry.appendChild(fileNameSpan);
		    fileEntry.appendChild(fileSizeEm);
		    fileEntry.appendChild(deleteButton);
		    
		    /* FileList 태그에 업로드된 파일 태그 추가 */
		    fileList.appendChild(fileEntry);
	    }
	    
	    
	    /* 파일 삭제 버튼에 삭제 이벤트 추가
		   JS 클로저에 의해 태그 생성할 때 onClick 할당이 안되기 때문에
		     태그를 전부 생성한 후 한번에 이벤트 추가
		*/
		function addOnClickEventForFileRemover(){
			var deleteButtons = document.querySelectorAll('.fileRemover');
			  deleteButtons.forEach((button) => {
			    button.onclick = () => {
			        var fileEntry = button.parentElement;
			        var fileName = fileEntry.querySelector('div > span').textContent;
			        
			        // 파일 이름을 removeFile 함수에 전달하여 호출
			        removeFile(fileName);
			     	// 파일 엔트리를 찾아 삭제
			        fileEntry.remove();
			    };
			  });
		}
	    
		/* 업로드된 파일 삭제 */
		function removeFile(fileName){
		 	for (var i = 0; i < dataTransfer.files.length; i++) {
		        if (dataTransfer.files[i].name == fileName) {
		            dataTransfer.items.remove(i);
		         	/* 파일 삭제 후 해당 파일 이름을 데이터 속성으로 가진 이미지 태그 삭제 
		         	   수정 시에는 data-filename attr이 없기 때문에 입력창의 이미지가 지워지지않음 
		         	   직접 입력 창에서 백스페이스로 지워야함 */
		         	var query = 'img[data-filename="' + fileName + '"]'; 
		            let elements = document.querySelectorAll(query);
		            elements.forEach(function(element) {
		                element.parentNode.removeChild(element);
		            });
		        }
		    }
		}
		
		/* 글 수정 시 FileVO를 담는 태그만 삭제 */
		function removeFileEntry(button) {
		    var fileEntry = button.parentElement;
		    fileEntry.remove();
		  	// 삭제된 파일을 기록
		    removedFileIds.push(fileEntry.id);
		}
	    
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
		/* 파일 크기 포맷팅 */
		function formatBytes(bytes) {
			if (bytes === 0) return '0 Bytes';
			var k = 1024, 
				sizes = [ 'Bytes', 'KB', 'MB', 'GB', 'TB' ], 
				i = Math.floor(Math.log(bytes) / Math.log(k));
			return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
		}
	</script>
</head>
<body>	
	<form id="postForm" class="container mt-3">
		<!-- 작성자 입력 -->
		<div class="row py-3 border-bottom">
			<label for="member" class="d-flex align-items-center col-md-2 col-form-label fw-semibold">작성자
				<span class="material-symbols-outlined text-danger">check</span>
			</label>
			<div class="col-md-10">
				<input class="form-control w-25" type="text" disabled value="${memberName}"/>
			</div>
		</div>
		<!-- 제목 입력 -->
		<div class="row border-bottom py-3">
			<label for="title"
				class="d-flex align-items-center col-md-2 col-form-label fw-semibold">제목
				<span class="material-symbols-outlined text-danger">check</span>
			</label>
			<div class="col-md-10">
				<input id="title" type="text" class="form-control" value="${postVO.title}"/>
			</div>
		</div>
		<!-- 내용 입력 -->
		<div class="row border-bottom py-3">
			<label for="contents" class="col-md-2 col-form-label fw-semibold">내용</label>
			<div class="col-md-10">
				<div contenteditable="true" id="content" class="form-control"
					style="min-height: 360px">${postVO.content}</div>
			</div>
		</div>
		
		<!-- 파일 첨부  -->
		<div class="row border-bottom py-3" >
			<label for="files" class="col-md-2 col-form-label fw-semibold">첨부파일</label>
			<div class="col-md-10" id="fileList">
				<div class="input-group w-75 mb-3">
					<input id="files" class="form-control" type="file" multiple  accept="image/*"/>
					<button class="btn btn-outline-secondary" type="button" onclick="uploadFiles()">업로드</button>
				</div>
				
				<!-- 수정 시 받아온 파일 렌더링(이때는 각 버튼에 삭제 시 삭제된 id를 보관하는 메서드를 넣어줘야함) -->
			 	<c:forEach var="fileVO" items="${fileVOs}" varStatus="status">
					<div id="${fileVO.id}" class="px-2 pt-3 d-block d-flex gap-1 align-items-center">
					    <input type="radio" name="thumbnail" value="${fileVO.originalName}.${fileVO.extension}"/>
					    <span>${fileVO.originalName}.${fileVO.extension}</span>
					    <em class="fileSize">${fileVO.size}</em>
					    <button type="button" class="fileRemover btn d-flex align-items-center"
					    	onclick="removeFileEntry(this)">
					        <span class="material-symbols-outlined text-danger">cancel</span>
					    </button>
					</div>
				</c:forEach>
			</div>
		</div>
		
		<!-- buttons   -->
		<div class="d-flex justify-content-between gap-2 pt-3"> 
			<span class="text-body-secondary">* 체크된 이미지 파일은 썸네일 이미지로 지정됩니다.</span>
			<!-- 저장 버튼 -->
			<div class="b-flex gap-2">
			    <button type="button" onclick="request()" class="btn btn-primary">
			    	<c:if test="${flag == 'create'}">
					    <c:out value="등록"/>
					</c:if>
					<c:if test="${flag == 'modify'}">
					    <c:out value="저장"/>
					</c:if>
			    </button>	
				<!-- 취소 버튼 -->
				<a href="/" class="btn btn-outline-secondary">취소</a>
			</div>
		</div>
	</form>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
		integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
		crossorigin="anonymous"></script>
</body>
</html>