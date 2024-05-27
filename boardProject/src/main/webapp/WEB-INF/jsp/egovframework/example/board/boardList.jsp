<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
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
      th,
      td {
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
      	document.listForm.action = "<c:url value='/boardList.do'/>";
         	document.listForm.submit();
      }
    </script>
  </head>
  <body>
    <form:form commandName="searchVO" id="listForm" name="listForm" action="/boardList.do" method="post">
      <div class="container">
        <!-- 검색 창 -->
        <div class="bg-secondary-subtle p-3 mt-3">
          <div class="flex-grow-1 input-group bg-white p-4">
            <!-- 검색 조건 -->
            <label class="input-group-text fw-bold" for="searchType">검색조건</label>
            <form:select path="searchCondition" class="form-select" id="searchType" name="searchType">
				<form:options items="${SearchAndPagination.SearchCondition}" itemLabel="name" />
			</form:select>
            <!-- 검색어 -->
            <label for="keyword" class="input-group-text fw-bold">검색어</label>
			<form:input path="searchKeyword" type="text" id="keyword" class="form-control"/>
		  </div>
          <!-- 검색 버튼 -->
          <div class="d-flex justify-content-center mt-3">
            <button class="btn btn-secondary fw-bold" type="submit">검색</button>
          </div>
        </div>
        <!-- 본문 -->
        <div class="d-flex flex-column gap-3 mt-5">
          <div class="d-flex justify-content-between align-items-center">
            <!-- 총 게시글  개수 -->
            <span>전체 :
              <span class="fw-bold text-danger"><fmt:formatNumber value="${totalCnt}" pattern="#,##0" /></span>건
            </span>
            <a href="/boardRegister.do" class="btn btn-outline-secondary">글쓰기</a>
          </div>
          <!-- 게시글 목록 -->
          <table class="table table-bordered table-group-divider">
            <thead>
              <tr class="table-secondary">
                <th scope="col" class="col-md-1">순번</th>
                <th scope="col" class="col-md-5">제목</th>
                <th scope="col" class="col-md-2">작성자</th>
                <th scope="col" class="col-md-2">등록일</th>
                <th scope="col" class="col-md-1">조회수</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="board" items="${boardList}" varStatus="status">
                <!-- 답변 글의 경우 색상 변경 -->
                <tr class="${empty board.parentId ? '' : 'bg-body-secondary bg-opacity-75'}">
                  <!-- 게시글 ID -->
                  <td align="center">
                    <c:out value="${board.id}" />
                  </td>
                  <!-- 게시글 제목 -->
                  <td align="center" class="${empty board.parentId ? 'left-td' : 'p-2 left-td'}">
                    <!-- 답변글 -->
                    <c:if test="${not empty board.parentId}">
                      <span class="material-symbols-outlined">subdirectory_arrow_right</span>
                    </c:if>
                    <!-- 일반글 -->
                    <a href="/boardDetail.do?id=${board.id}" class="${empty board.parentId ? 'mw-25' : ''}">
                      <c:out value="${board.title}" />
                    </a>
                  </td>
                  <!-- 작성자 -->
                  <td align="left" class="left-td">
                    <c:out value="${board.member}" />&nbsp;
                  </td>
                  <!-- 등록일 -->
                  <fmt:parseDate value="${board.regDate}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
                  <td align="center">
                    <fmt:formatDate pattern="yyyy-MM-dd" value="${parsedDateTime}"/>
                  </td>
                  <!-- 조회수 -->
                  <td align="center">
                    <c:out value="${board.viewCount}" />&nbsp;
                  </td>
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
				<form:hidden path="pageIndex" />
            </ul>
          </div>
        </nav>
      </div>
    </form:form>
    <script
      src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
      integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
      crossorigin="anonymous"
    ></script>
  </body>
</html>
