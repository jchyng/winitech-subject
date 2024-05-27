<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>게시글 목록</title>
<!-- CSRF 정보를 담은 META DATA -->
<meta name="_csrf" content="${_csrf.token}" />
<meta name="_csrf_header" content="${_csrf.headerName}" />

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
	crossorigin="anonymous" />
<link rel="stylesheet"
	href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0" />

<style>
a {
	text-decoration: none;
	color: black;
}

li {
	list-style: none;
}

.left-td {
	max-width: 480px;
	text-align: left;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
}

.post-text {
	font-size: 14px;
	color: darkgray;
}

.icon {
	font-size: 16px;
}

.thumbnail {
	height: 250px;
	width: 250px;
}
</style>
<script src="//code.jquery.com/jquery-3.5.1.min.js"></script>
<script type="text/javaScript" language="javascript" defer="defer">
	  /* CSRF info */
	  const token = $("meta[name='_csrf']").attr("content");
      const header = $("meta[name='_csrf_header']").attr("content");
      
      /* 페이지 이동 */
      function linkPage(pageNo){
      	document.listForm.pageIndex.value = pageNo;
      	document.listForm.action = "<c:url value='/postList.do'/>";
       	document.listForm.submit();
      }
      
      /* 로그아웃 */
      function logout(){
    	  $.ajax({
				url : "/signout.do",
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
					console.log(e);
				}
		  });
      }
    </script>
</head>
<body>
	<form:form commandName="searchVO" id="listForm" name="listForm"
		action="/postList.do" method="post">
		<input name="${_csrf.parameterName}" type="hidden"
			value="${_csrf.token}" />
		<div class="container">
			<!-- 로그인 상태 관리  -->
			<c:if test="${empty memberName}">
				<div class="d-flex justify-content-end mt-3">
					<a class="text-body-tertiary" href="/signin.do">로그인</a>
				</div>
			</c:if>
			<c:if test="${not empty memberName}">
				<div class="d-flex justify-content-end align-items-center mt-3">
					<span class="fw-semibold text-body-secondary">${memberName}</span>
					<span class="ps-2">|</span>
					<button class="btn text-bodyA-tertiary" onclick="logout()">로그아웃</button>
				</div>
			</c:if>
			<!-- 검색 창 -->
			<div class="bg-secondary-subtle p-3 mt-3">
				<div class="flex-grow-1 input-group bg-white p-4">
					<!-- 검색 조건 -->
					<label class="input-group-text fw-bold" for="searchType">검색조건</label>
					<form:select path="searchCondition" class="form-select"
						id="searchType">
						<option value="title">제목</option>
						<option value="member">작성자</option>
					</form:select>
					<!-- 검색어 -->
					<label for="keyword" class="input-group-text fw-bold">검색어</label>
					<form:input path="searchKeyword" type="text" id="keyword"
						class="form-control" />
				</div>
				<!-- 검색 버튼 -->
				<div class="d-flex justify-content-center mt-3">
					<button class="btn btn-secondary fw-bold" type="submit">검색</button>
				</div>
			</div>
			<!-- 본문 -->
			<div class="d-flex flex-column gap-3 mt-5">
				<!-- 총 게시글  개수 -->
				<div class="d-flex justify-content-between align-items-center">
					<span>전체 : <span class="fw-bold text-danger"><fmt:formatNumber
								value="${totalCnt}" pattern="#,##0" /></span>건
					</span> <a href="/user/postRegister.do" class="btn btn-outline-secondary">글쓰기</a>
				</div>
				<!-- 게시글 목록 -->
				<ul class="border-top border-2 p-3">
					<!-- 게시글 -->
					<div class="row mb-3">
						<c:forEach var="post" items="${postList}" varStatus="status">
							<li class="w-25"><a href="/postDetail.do?id=${post.id}"
								class="d-flex flex-column"> <!-- 게시글 이미지  todo: 사이즈 조절 --> <img
									class="thumbnail mb-2" src="${post.thumbnail}" alt="게시글 이미지" />
									<!-- 게시글 제목 --> <span class="left-td fw-bold" align="center">
										<c:out value="${post.title}" />
								</span> <!-- 등록일 | 조회수 -->
									<div class="d-flex gap-2 fw-semibold">
										<!-- 등록일 -->
										<fmt:parseDate value="${post.regDate}"
											pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
										<span align="center" class="post-text"> <fmt:formatDate
												pattern="yyyy-MM-dd" value="${parsedDateTime}" />
										</span> <span class="post-text">|</span>
										<!-- 조회수 -->
										<div class="d-flex align-items-center gap-1">
											<span class="material-symbols-outlined post-text icon">
												visibility </span> <span align="center" class="post-text"> <c:out
													value="${post.viewCount}" />
												&nbsp;
											</span>
										</div>
									</div>
							</a></li>
						</c:forEach>
					</div>
				</ul>
			</div>

			<!-- 페이지네이션 -->
			<nav class="d-flex justify-content-center">
			<div id="paging">
				<ul class="pagination">
					<ui:pagination paginationInfo="${paginationInfo}" type="image"
						jsFunction="linkPage" />
					<form:hidden path="pageIndex" />
				</ul>
			</div>
			</nav>
		</div>
	</form:form>
</body>
</html>
