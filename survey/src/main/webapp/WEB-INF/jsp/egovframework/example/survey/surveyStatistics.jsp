<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import=" egovframework.example.mvc.vo.enums.QuestionType"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>설문 조사 통계</title>
    <script type="text/javascript" src="/js/jquery-3.4.1.min.js"></script>
    <script type="text/javascript" src="/js/bootstrap/bootstrap.min.js"></script>
    <link type="text/css" rel="stylesheet" href="/css/bootstrap/bootstrap.min.css" />
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
      ul {
        padding: 0px;
      }
      li {
        list-style: none;
      }

      .response-container {
        padding-top: 20px;
        margin-bottom: 0px;
      }
      .response-count {
        font-size: 12px;
      }
      .reponse-item {
        border-radius: 4px;
        padding: 6px;
        margin-bottom: 4px;
        background-color: rgba(0, 0, 0, 0.1);
      }
    </style>
  </head>
  <body>
    <div id="statistics" class="container-sm mt-5">
      <div class="d-flex flex-column bd-highlight mb-3">
        <!-- 제목 -->
        <div class="d-flex justify-content-between align-items-end mb-2">
          <h1 id="title">${survey.title}</h1>
          <!-- 설문 기간 -->
					<div class="text-secondary">
						<span class="mr-2">설문 기간: </span> <span><fmt:formatDate
								pattern="yyyy-MM-dd" value="${survey.stDate}" /></span> ~ <span><fmt:formatDate
								pattern="yyyy-MM-dd" value="${survey.edDate}" /></span>
					</div>
        </div>
        <!-- 개요 -->
				<div class="mt-2 mb-4">
					<p id="description">${survey.description}</p>
				</div>
				<!-- 응답 -->
        <c:forEach var="question" items="${survey.questions}">
	        <!-- 주관식 -->
        	<c:if test="${question.type == QuestionType.SHORT || question.type == QuestionType.LONG}">
	        	<div class="card mt-3 p-4">
	          <div class="font-weight-bold">${question.content}</div>
	          <span class="response-count">응답 ${question.totalAnswerCount}개</span>
	          <ul class="response-container">
	          	<c:forEach var="answer" items="${question.answers}">
		            <li class="reponse-item">
		              <span>${answer.content}</span>
		            </li>
	          	</c:forEach>
	          </ul>
	        	</div>
        	</c:if>
        	<!-- 객관식 -->
        	<c:if test="${question.type == QuestionType.MULTIPLE_ONE || question.type == QuestionType.MULTIPLE_MANY}">
	        	<div class="card mt-3 p-4">
		          <div class="font-weight-bold">${question.content}</div>
		          <span class="response-count">응답 ${question.totalAnswerCount}개</span>
		          <!-- 그래프 -->
		          <canvas id="${question.id}" class="mx-auto" style="height: 30vh; width: 50vw"></canvas>
		        </div>
        	</c:if>
        </c:forEach>
      </div>
      <div class="d-flex justify-content-end mt-2 mb-5">
	      <a href="/" class="btn btn-secondary">목록</a>
			</div>
    </div>

    <script>
    	var charts = [];

    	var jsonDTO = JSON.stringify(${jsonDTO});
    	var data = JSON.parse(jsonDTO);
    
    	for(var question of data.questions){
    			if(question.type !== "MULTIPLE_ONE" && question.type !== "MULTIPLE_MANY") {
    					continue;
    			}
    			
 		      var ctx = document.getElementById(question.id);
    			var label = [];
    			var count = [];
    			
    			for(answer of question.answers){
    				label.push(answer.content);
    				count.push(answer.countPerAnswer);
    			}
					
 		      var chart = new Chart(ctx, {
 		        type: "pie",
 		        data: {
 		          labels: label,
 		          datasets: [
 		            {
 		              label: "# of Votes",
 		              data: count,
 		              borderWidth: 1,
 		            },
 		          ],
 		        },
 		        options: {
 		          responsive: false,
 		        },
 		      });
 		      
 		      charts.push(chart);
    	}
    </script>
  </body>
</html>
