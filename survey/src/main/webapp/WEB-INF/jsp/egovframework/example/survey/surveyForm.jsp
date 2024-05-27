<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import=" egovframework.example.mvc.vo.enums.QuestionType"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>설문 조사 작성</title>
<script type="text/javascript" src="/js/jquery-3.4.1.min.js"></script>
<script type="text/javascript" src="/js/bootstrap/bootstrap.min.js"></script>
<link type="text/css" rel="stylesheet" href="/css/bootstrap/bootstrap.min.css" />
<style>
	ul {
		padding: 0px;
	}
	
	li {
		list-style: none;
	}
</style>
<script>
	function submitRequest(){
			var formData = new FormData(document.getElementById('survey'));
			var answerEls = document.querySelectorAll(".answer");
			
			formData.append('surveyId', ${survey.id});
			var i=0; //for i로 할 경우 continue로 넘어간 다음 idx가 꼬임
			for (var answerEl of answerEls) {
				//객관식의 경우 선택되지 않은 답변은 제외
				if(answerEl.tagName === 'INPUT') {
				    if(!answerEl.checked){
								continue;			    
				    }
				}
				formData.append('vos[' + i + '].questionId', answerEl.name);
				formData.append('vos[' + i + '].content', answerEl.value);
				i++;
			}
			
			$.ajax({
			    url: '/survey/submit.do',
			    type: 'POST',
			    data: formData,
			    processData: false,
			    contentType: false,
			    success: function(data) {
		        alert("제출되었습니다.");
		        window.location.href = "/";
			    },
			    error: function(xhr, status, error) {
			    	alert("시스템 에러가 발생하였습니다.");
		        console.error('Error:', error);
			    }
			});
			return false;	//submit 방지
	}
</script>
</head>
<body>
	<!-- 설문지 작성 폼 (구조는 폼이지만 AJAX 사용) -->
	<form id="survey" onsubmit="return submitRequest()" class="container-sm mt-5">
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
			<!-- 설문 문항 -->
			<c:forEach var="question" items="${survey.questions}">
				<div class="border rounded-lg p-4 mt-3">
					<!-- 이미지 -->
					<c:if test="${question.fileVO ne null}">
						<img class="w-50 h-50 mb-4" src="${question.fileVO.path}"
							alt="질문 이미지" />
					</c:if>
					<!-- 내용 -->
					<p>${question.content}</p>
					<!-- 답변 (필수 여부에 따라 required 추가)-->
					<!-- 주관식 -->
					<c:if
						test="${question.type == QuestionType.SHORT || question.type == QuestionType.LONG}">
						<div class="d-flex mb-3">
							<textarea name="${question.id}" class="answer question form-control w-1--" rows="3"
								<c:if test="${question.isRequired}">required</c:if>></textarea>
						</div>
					</c:if>
					<!-- 객관식 -->
					<c:if test="${question.type == QuestionType.MULTIPLE_ONE || question.type == QuestionType.MULTIPLE_MANY}">
						<div class="d-flex align-items-center">
							<ul>
								<c:forEach var="option" items="${question.options}">
									<li>
										<input id="${option.id}" class="answer" type="${question.type.getHtml()}" 
											value="${option.content}" name="${question.id}"
											<c:if test="${question.isRequired && question.type.getHtml() != 'checkbox'}">required</c:if>
										/> 
										<label for="${option.id}">${option.content}</label>
									</li>
								</c:forEach>
							</ul>
						</div>
					</c:if>
				</div>
			</c:forEach>
			<!-- 하단 저장, 취소 버튼-->
			<div class="d-flex justify-content-end mt-5">
				<!-- 취소 -->
				<a href="/" class="btn btn-light mr-2">취소</a>
				<!-- 저장 -->
				<button class="btn btn btn-primary">저장</button>
			</div>
		</div>
	</form>
</body>
</html>
