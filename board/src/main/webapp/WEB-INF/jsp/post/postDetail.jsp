<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>게시글</title>
    <!-- CSRF 정보를 담은 META DATA -->
	<meta name="_csrf" content="${_csrf.token}"/>
	<meta name="_csrf_header" content="${_csrf.headerName}"/>
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
      rel="stylesheet"
      integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
      crossorigin="anonymous"
    />
    <link
      rel="stylesheet"
      href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@20..48,100..700,0..1,-50..200"
    />
	
	<script src="//code.jquery.com/jquery-3.5.1.min.js" ></script>
    <script type="text/javaScript" language="javascript" defer="defer">
	    /* CSRF info */
		const token = $("meta[name='_csrf']").attr("content");
		const header = $("meta[name='_csrf_header']").attr("content");
    
		/* 게시글 삭제 */
		function deletePost(){
			const id = ${postVO.id};
			$.ajax({
				url : "/user/postEliminator.do?id=" + id,
				type : "post",
				processData: false,
		        contentType: false,
		        /* 데이터 전송 전에 헤더에 csrf 값 설정 */
				beforeSend: function(xhr) {   
					xhr.setRequestHeader(header, token);
	            },
				success : (response, status, header) => {
				 	window.location.href = "/";
				},
				error: (request, status, error) => {
					alert("게시글을 삭제할 수 없습니다.");
				}
			});
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
	  <form>
	    <div class="container mt-3">
	      <!-- 제목 -->
	      <span class="fw-semibold fs-4 px-2 text-primary">${postVO.title}</span>
	      <div class="d-flex gap-3 pt-3 pb-3 border-2 border-bottom px-2 text-secondary">
	        <!-- 작성자 -->
	        <div class="d-flex gap-3">
	          <span class="fw-semibold">작성자</span> <span>${postVO.name}</span>
	        </div>
	        <div class="vr"></div>
	        <!-- 등록일 -->
	        <div class="d-flex gap-3">
	          <span class="fw-semibold">등록일</span>
	          <span>${postVO.reg_date}</span>
	        </div>
	        <div class="vr"></div>
	        <!-- 조회 수 -->
	        <div class="d-flex gap-3">
	          <span class="fw-semibold">조회수</span>
	          <span>${postVO.view_count}</span>
	        </div>
	      </div>
	    
	      <!-- 게시글 본문 -->
	      <div class="px-2 py-3" style="min-height: 500px">${postVO.content}</div>
	      
	      <!-- 첨부 파일 -->
	      <div class="border-top border-bottom d-flex align-items-center px-5 gap-5 py-3">
	        <span class="fw-semibold">첨부파일</span>
	        <div class="fileContainer row">
	          <c:forEach var="fileVO" items="${fileVOs}">
	            <a href="/download/file.do?id=${fileVO.id}"
	              class="link-underline-opacity-0 link-dark d-flex align-items-center mb-2" >
	              <!-- 다운로드 버튼 -->
	              <span class="material-symbols-outlined text-primary">download</span>
	              <!-- 파일 이름 -->
	              <span class="px-2">${fileVO.originalName}.${fileVO.extension}</span>
	              <!-- 파일 크기 -->
	              <span class="fileSize text-secondary">${fileVO.size}</span>
	            </a>
	          </c:forEach>
	        </div>
	      </div>
	      <!-- 하단 버튼 컨테이너  -->
	      <div class="py-3 d-flex justify-content-between align-items-center">
	        <a class="btn btn-secondary" href="/postList.do">목록</a>
	        <!-- 본인 게시글에만 수정, 삭제 버튼 활성화 -->
	        <c:if test="${isMy}">
		        <div class="d-flex gap-2">
		          <a href="/user/postModifier.do?id=${postVO.id}" class="btn btn-primary">수정</a>
	              <button type="button" onclick="deletePost()" class="btn btn-outline-secondary">삭제</button>
		        </div>
	        </c:if>
	      </div>
	    </div>
	    </form>
    
    <script
      src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
      integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
      crossorigin="anonymous"
    ></script>
  </body>
</html>