<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import=" egovframework.example.mvc.dto.enums.SearchCondition"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>
	<meta charset="UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<title>설문조사 검색</title>
	<script type="text/javascript" src="/js/jquery-3.4.1.min.js"></script>
 	<link
     href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
     rel="stylesheet"
     integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
     crossorigin="anonymous"
   />
   <link
     rel="stylesheet"
     href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@24,400,0,0"
   />
	<style>
	a {
		text-decoration: none;
		color: black;
	}
	
	th, td {
		text-align: center;
		background-color: transparent !important;
	}
	
	td.left-td {
		max-width: 480px;
		text-align: left;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}
	
	.comment-left-td {
		max-width: 480px;
		text-align: left;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}
	</style>

	<script type="text/javaScript" language="javascript" defer="defer">
	  /* 페이지 이동 */
	  function linkPage(pageNo){
	  	document.listForm.pageIndex.value = pageNo;
	  	document.listForm.action = "<c:url value='/survey/list.do'/>";
     	document.listForm.submit();
	  }
	  
	  /* 로그아웃 */
    function logout(){
  	  $.ajax({
				url : "/signout.do",
				type : "post",
				processData: false,
        contentType: false,
				success : function(response, status, header) {
				 	window.location.href = "/";
				},
				error: function(request, status, error) {
					console.log(error);
				}
		  });
    }
	  
    function gotoForm(surveyId, startDate, endDate, response) {
    	if(response == 1){
    		alert("이미 참여한 설문입니다.");
    		return;
    	}
    	
    	var currentDate = new Date();
    	if(currentDate < new Date(startDate) || currentDate > new Date(endDate)){
    		alert("설문 기간이 아닙니다.");
    		return;
    	}
   		
    	var url = '/survey/form.do?id=' + surveyId;
      window.location.href = url;
    }
	</script>
</head>
<body>
	<form id="listForm" name="paginationDTO" action="/survey/list.do" method="post">
		<input type="hidden" name="pageIndex" value="${paginationInfo.currentPageNo}"/>
		<div class="container">
			<!-- 로그인 상태 관리  -->
			<c:if test="${empty member.name}">
				<div class="d-flex justify-content-end mt-3">
					<a class="text-body-tertiary" href="/signin.do">로그인</a>
				</div>
			</c:if>
			<c:if test="${not empty member.name}">
				<div class="d-flex justify-content-end align-items-center mt-3">
					<span class="fw-semibold text-body-secondary">${member.name}</span>
					<span class="ps-2">|</span>
					<button type="button" class="btn text-bodyA-tertiary" onclick="logout()">로그아웃</button>
				</div>
			</c:if>
			<!-- 검색 창 -->
			<div class="bg-secondary-subtle p-3 mt-3">
				<div class="flex-grow-1 input-group bg-white p-4">
					<!-- 검색 조건 -->
					<label class="input-group-text fw-bold" for="searchType">검색조건</label>
					<select name="searchCondition" class="form-select" id="searchType">
						<c:forEach var="searchCondition" items="${SearchCondition.values()}">
							<option value="${searchCondition.name()}">${searchCondition.getValue()}</option>
						</c:forEach>
					</select>
					<!-- 검색어 -->
					<label for="keyword" class="input-group-text fw-bold">검색어</label>
					<input type="text" id="keyword" class="form-control" name="searchKeyword"/>
				</div>
				<!-- 검색 버튼 -->
				<div class="d-flex justify-content-center mt-3">
					<button class="btn btn-secondary fw-bold" type="submit">검색</button>
				</div>
			</div>
			<!-- 본문 -->
			<div class="d-flex flex-column gap-3 mt-5">
				<div class="d-flex justify-content-between align-items-center">
					<!-- 전체 설문  개수 -->
					<span>전체 : 
						<span class="fw-bold text-danger">
							<fmt:formatNumber value="${totalCnt}" pattern="#,##0"/>
						</span>건
					</span> 
					<!-- 관리자 일 때만 생성 -->
					<c:if test = "${member.role.name() == 'ROLE_ADMIN'}">
						<a href="/admin/survey/register.do" class="btn btn-outline-secondary">설문 생성</a>
					</c:if>
				</div>
				<!-- 설문 목록 -->
				<table class="table table-bordered table-group-divider">
					<thead>
						<tr class="table-secondary">
							<th scope="col" class="col-md-5">제목</th>
							<th scope="col" class="col-md-2">작성자</th>
							<th scope="col" class="col-md-2">설문 기간</th>
							<!-- 관리자 -->
							<c:if test = "${member.role.name() == 'ROLE_ADMIN'}">
								<th scope="col" class="col-md-1">응답 통계</th>
							</c:if>
							<!-- 유저 -->
							<c:if test = "${member.role.name() == 'ROLE_USER'}">
								<th scope="col" class="col-md-1">응답 여부</th>
							</c:if>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="survey" items="${surveys}">
							<tr>
								<!-- 게시글 제목 -->
								<td align="center">
									<c:if test = "${member.role == 'ROLE_ADMIN'}">
										<a href="${'/admin/survey/modifier.do?id='}${survey.id}">
											<c:out value="${survey.title}" />
										</a>
									</c:if>
									<c:if test = "${member.role != 'ROLE_ADMIN'}">
<%-- 										<a href="${'/survey/form.do?id='}${survey.id}"> --%>
<%-- 											<c:out value="${survey.title}" /> --%>
<!-- 										</a> -->
										<button type="button" class="btn"
											onclick="gotoForm('${survey.id}', '${survey.stDate}', '${survey.edDate}', '${survey.response}')">
											<c:out value="${survey.title}" />
										</button>
									</c:if>
								</td>
								<!-- 작성자 -->
								<td align="center">
									<c:out value="${survey.name}"/>&nbsp;
								</td>
								<!-- 설문 기간 -->
								<td align="center">
									<fmt:formatDate pattern="yyyy-MM-dd" value="${survey.stDate}"/> ~
									<fmt:formatDate pattern="yyyy-MM-dd" value="${survey.edDate}"/>
								</td>
								<!-- 응답 통계 -->
								<c:if test = "${member.role == 'ROLE_ADMIN'}">
									<td align="center">
										<a class="btn btn-secondary" href="${'/admin/survey/statistics.do?id='}${survey.id}">통계 보기</a>
									</td>
								</c:if>
								<!-- 응답 여부 -->
								<c:if test = "${member.role == 'ROLE_USER'}">
									<td align="center">
										<span class="btn ${survey.response == 1 ? "btn-success" : "btn-secondary"}">
											${survey.response == 1 ? "참여 완료" : "참여 가능"}
										</span>
									</td>
								</c:if>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>

			<!-- 페이지네이션 -->
			<nav class="d-flex justify-content-center">
			<div id="paging">
				<ul class="pagination">
					<ui:pagination paginationInfo="${paginationInfo}" type="image" jsFunction="linkPage" />
				</ul>
			</div>
			</nav>
		</div>
	</form>
	<script
      src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
      integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
      crossorigin="anonymous"
    ></script>
</body>
</html>
