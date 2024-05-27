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
<script>
		//todo: Java와 JS가 같은 ENUM을 가져야한다는 문제가 있음 = 구현 끝나고 리팩토링
		var questionTypeMapping = {
		    "input": "SHORT",
		    "textarea": "LONG",
		    "radio": "MULTIPLE_ONE",
		    "checkbox": "MULTIPLE_MANY"
		};

		//============================HTTP 요청 함수============================//
		function submitRequest(){
				var formData = new FormData(document.getElementById('survey'));
				var questionEls = document.querySelectorAll(".question");
				setFormData(questionEls, formData);
				
				//요청 분류
				var isModify = ${survey.id != null};
		    var url = isModify ? '/admin/survey/update.do' : '/admin/survey/create.do';
		    
		    if(isModify){
		    	formData.append('id', ${survey.id});
		    }
		    
		    //question에 file을 저장했기 떄문에 form에서 file 삭제
		    formData.delete("files");
				
		    $.ajax({
		        url: url,
		        type: 'POST',
		        data: formData,
		        processData: false, // 데이터 처리 방식을 지정하지 않음
		        contentType: false, // 컨텐츠 타입을 지정하지 않음
		        success: function(data) {
		        	alert('저장 되었습니다.');
	            window.location.href = "/";
		        },
		        error: function(xhr, status, error) {
		            alert("시스템 에러가 발생하였습니다.");
		            console.error('Error:', error);
		        }
		    });
				
				return false;	//submit 방지
		}
		
		/* 설문지 삭제 */
		function deleteSurvey(){
			 $.ajax({
	        url: '/survey/delete.do?id=${survey.id}',
	        type: 'POST',
	        success: function(data) {
	            alert('삭제 되었습니다.');
	            window.location.href = "/";
	        },
	        error: function(xhr, status, error) {
	        		alert("시스템 에러가 발생하였습니다.");
	            console.error('Error:', error);
	        }
	    });
		}
	
		//============================데이터 관련 함수============================//
		/* 폼 데이터에 질문과 객관식 옵션 정보를 저장 */
		function setFormData(questionEls, formData){
				for(var i=0; i<questionEls.length; i++){
						var questionContent = questionEls[i].querySelector(".questionContent").value;
						var questionType = questionEls[i].querySelector(".questionType").value;
						var isRequired = questionEls[i].querySelector('.custom-switch > input').checked;
						var file = questionEls[i].querySelector(".imageFile").files[0];
						
						formData.append('questions[' + i + '].content', questionContent);
						formData.append('questions[' + i + '].type', questionTypeMapping[questionType]);
						formData.append('questions[' + i + '].isRequired', isRequired);
						formData.append('questions[' + i + '].position', i+1);
						
						//undefined나 null 등 File 객체가 들어가지 않으면 서버에서 DTO 변환 시 에러가 발생함
						if(file !== undefined && file.size > 0){
							formData.append('questions[' + i + '].file', file);
						}
						
						//옵션 정보 저장
						var selectOptionContainer = questionEls[i].querySelector(".selectOptionContainer");
					var options = setOptionData(selectOptionContainer, i, formData);
				}
		}
		
		/* 객관식 옵션 데이터를 가져온다 */
		function setOptionData(selectOptionContainer, questionIdx, formData){
				var options = selectOptionContainer.children;
				
				for(var i=0; i<options.length; i++){
						var selectOptionContent = options[i].querySelector(".selectOptionContent").value;
						formData.append('questions[' + questionIdx + '].options[' + i + '].content', selectOptionContent);
						formData.append('questions[' + questionIdx + '].options[' + i + '].position', i+1);
				}
		}
		
		/* 토글 스위치 랜덤 아이디 추가 */
		function setSwitchIds(switchEl){
		    var checkboxId = 'checkbox-' + Date.now();
		    var input = switchEl.querySelector("input");
		    var label = switchEl.querySelector("label");
		    
		    input.setAttribute('id', checkboxId);
		    label.setAttribute('for', checkboxId);
		}
		
		//============================이벤트 처리 함수============================//
		/* 질문 추가 */
		function addQuestion() {
				var questionContainerEl = document.getElementById("questions");
				var questionEl = createQuestion();
				questionContainerEl.appendChild(questionEl);
		}
		
		/* 질문 삭제 */
		function removeQuestion(button) {
				var questionEl = button.parentElement.parentElement.parentElement;
				questionEl.parentNode.removeChild(questionEl);
		}
	
		/* 질문 타입 선택 */
		function selectQuestionType(select) {
				var optionType = select.value;
				var questionEl = select.parentElement.parentElement;
				var selectOptionContainer = questionEl.querySelector(".selectOptionContainer");
				var mpcAddBtn = questionEl.querySelector(".mpcAddBtn");
		
				//초기화
				selectOptionContainer.innerText = '';
		
				//버튼이 존재한다면 삭제
				var removeBtn = mpcAddBtn.querySelector("button");
				if(removeBtn){					
					mpcAddBtn.removeChild(removeBtn);
				}
				
				//객관식인 경우 버튼 생성
				if (optionType && (optionType === 'checkbox' || optionType === 'radio')) {
						//버튼 추가
						var btn = createAddOptionButton(optionType)
						mpcAddBtn.appendChild(btn);
				}
		}
	
		/* 객관식 문항 추가 */
		function addselectOption(button) {
				var questionEl = button.parentElement.parentElement;
				var selectOptionContainer = questionEl.querySelector(".selectOptionContainer");
				//객관식 문항 생성
				var selectOption = createSelectOption(button.value);
				selectOptionContainer.appendChild(selectOption);
		}
	
		/* 객관식 문항 삭제 */
		function removeselectOption(button) {
				var parentElement = button.parentElement;
				parentElement.parentNode.removeChild(parentElement);
		}
	
		/* 파일 클릭 */
		function clickFile(button) {
				var fileEl = button.parentElement.querySelector(".imageFile");
				fileEl.click();
		}
	
		//============================태그 생성 함수============================//
		/* 질문 태그 추가 */
		function createQuestion(){
			// 새로운 질문을 생성합니다.
		    var questionEl = document.createElement('div');
		    questionEl.classList.add('question');
	
		    // 질문 내용을 추가합니다.
		    questionEl.innerHTML = `
		        <div class="border rounded-lg p-4 mb-3">
		            <img class="w-50 h-50 mb-4" src="" alt="질문 이미지" hidden />
		            <div class="d-flex mb-3">
		                <textarea class="questionContent form-control w-75" rows="3" required></textarea>
		                <input type="file" class="imageFile" name="files" accept="image/*" style="display: none" onchange="addImageSrc(this)" />
		                <button type="button" class="btn h-25 pt-0" onclick="clickFile(this)">
		                    <img src="/images/image-icon.png" alt="그림 추가 아이콘" width="30px" />
		                </button>
		                <select class="questionType form-control w-25" onchange="selectQuestionType(this)">
		                    <c:forEach var="questionType" items="${QuestionType.values()}">
		                        <option value="${questionType.getHtml()}">${questionType.getValue()}</option>
		                    </c:forEach>
		                </select>
		            </div>
		            <div class="selectOptionContainer"></div>
		            <div class="mpcAddBtn">
		            </div>
		            <div class="d-flex justify-content-between align-items-center mt-4">
		                <div class="custom-control custom-switch">
		                    <input type="checkbox" class="custom-control-input" />
		                    <label class="custom-control-label">필수</label>
		                </div>
		                <button class="btn btn-secondary ml-5" onclick="removeQuestion(this)">삭제</button>
		            </div>
		        </div>
		    `;
		    //토글을 위해 switch id 설정
		    var switchEl = questionEl.querySelector(".custom-switch");
		    setSwitchIds(switchEl);
		    
		    return questionEl;
		}
		
		/* 이미지 태그 src 추가 */
		function addImageSrc(input) {
				var questionEl = input.parentElement.parentElement.parentElement;
				var imgEl = questionEl.querySelector("img");
				var file = input.files[0];
		
				var reader = new FileReader();
		
				//파일이 존재하지 않을 때는 이미지 삭제
				if (!file) {
					imgEl.setAttribute('hidden', 'true');
					return;
				}
				//파일이 존재하면 이미지 렌더링
				reader.onload = function(e) {
					imgEl.src = e.target.result;
					imgEl.removeAttribute('hidden');
				};
				reader.readAsDataURL(file);
		}
		
		/* 객관식 문항 추가 버튼 생성 */
		function createAddOptionButton(optionType) {
		    // 버튼 요소 생성
		    var addButton = document.createElement('button');
		    addButton.setAttribute('type', 'button');
		    addButton.classList.add('btn', 'mt-2');
		    addButton.addEventListener('click', function() {
		        addselectOption(this);
		    });
		    addButton.value = optionType;

		    // 이미지 요소 생성
		    var img = document.createElement('img');
		    img.setAttribute('src', '/images/plus-icon.png');
		    img.setAttribute('alt', '객관식 문항 추가 아이콘');
		    img.setAttribute('width', '22px');

		    // 이미지를 버튼에 추가
		    addButton.appendChild(img);

		    return addButton;
		}

	
		/* 객관식 문항 태그 생성 */
		function createSelectOption(optionType) {
				// div 요소 생성
				var answerDiv = document.createElement('div');
				answerDiv.classList.add('d-flex', 'align-items-center');
		
				// 체크박스 생성
				var checkboxInput = document.createElement('input');
				checkboxInput.setAttribute('type', optionType);
				checkboxInput.setAttribute('disabled', true);
		
				// 답변 입력란 생성
				var answerInput = document.createElement('input');
				answerInput.setAttribute('type', 'text');
				answerInput.classList.add('selectOptionContent', 'form-control', 'w-50', 'border-top-0',
						'border-left-0', 'border-right-0', 'rounded-0');
				answerInput.setAttribute('placeholder', '답변');
				answerInput.setAttribute('required', true);
		
				// 삭제 버튼 생성
				var deleteButton = document.createElement('button');
				deleteButton.classList.add('btn', 'p-0');
				deleteButton.addEventListener("click", function() {
					removeselectOption(this);
				})
				// 버튼 이미지 생성
				var deleteImg = document.createElement('img');
				deleteImg.setAttribute('src', '/images/trash-can-icon.png');
				deleteImg.setAttribute('alt', '객관식 문항 삭제 버튼');
				deleteImg.setAttribute('width', '22px');
		
				deleteButton.appendChild(deleteImg);
		
				// 생성한 요소들을 답변 항목에 추가
				answerDiv.appendChild(checkboxInput);
				answerDiv.appendChild(answerInput);
				answerDiv.appendChild(deleteButton);
				return answerDiv;
		}
</script>
</head>
<body>
	<form id="survey" onsubmit="return submitRequest()"
		class="container-sm mt-5">
		<!-- 제목 및 개요 -->
		<div class="d-flex flex-column bd-highlight mb-3">
			<input type="text" name="title" placeholder="제목" required
				value="${survey.title}" class="form-control form-control-lg" />

			<textarea form="survey" name="description" placeholder="개요" required
				class="mt-1 form-control" rows="3" maxlength="240">${survey.description}</textarea>
		</div>
		<!-- 설문 기간 -->
		<div class="form-row">
			<div class="input-group mb-3 col-md-6">
				<div class="input-group-prepend">
					<label for="stDate" class="input-group-text">시작일</label>
				</div>
				<input type="date" name="stDate" class="form-control" required
					value="<fmt:formatDate pattern="yyyy-MM-dd" value="${survey.stDate}"/>" />
			</div>
			<div class="input-group mb-3 col-md-6">
				<div class="input-group-prepend">
					<label for="edDate" class="input-group-text">종료일</label>
				</div>
				<!-- formatDate: Date to Date, parseDate: String to Date -->
				<input type="date" name="edDate" class="form-control" required
					value="<fmt:formatDate pattern="yyyy-MM-dd" value="${survey.edDate}"/>" />
			</div>
		</div>
		<!-- 질문 -->
		<div id="questions">
			<!-- 수정 페이지에서 보여줄 질문 -->
			<c:forEach var="question" items="${survey.questions}">
				<div class="question">
					<div class="border rounded-lg p-4 mb-3">
						<!-- todo: 이미지가 있을 때만 -->
						<c:if test="${question.fileVO ne null}">
							<img class="w-50 h-50 mb-4" src="${question.fileVO.path}" alt="질문 이미지"/>
						</c:if>
						<div class="d-flex mb-3">
							<!-- 질문 내용 -->
							<textarea class="questionContent form-control w-75" rows="3"
								required>${question.content}</textarea>
							<!-- 파일 추가 -->
							<input type="file" class="imageFile" name="files"
								accept="image/*" style="display: none"
								onchange="addImageSrc(this)" />
							<button type="button" class="btn h-25 pt-0"
								onclick="clickFile(this)">
								<img src="/images/image-icon.png" alt="그림 추가 아이콘" width="30px" />
							</button>
							<!-- 질문 타입 -->
							<select class="questionType form-control w-25"
								onchange="selectQuestionType(this)">
								<c:forEach var="questionType" items="${QuestionType.values()}">
									<c:if test="${questionType == question.type}">
										<option value="${questionType.getHtml()}" selected>${questionType.getValue()}</option>
									</c:if>
									<c:if test="${questionType != question.type}">
										<option value="${questionType.getHtml()}">${questionType.getValue()}</option>
									</c:if>
								</c:forEach>
							</select>
						</div>
						<!-- 객관식 옵션  -->
						<div class="selectOptionContainer">
							<c:forEach var="option" items="${question.options}">
								<div class="d-flex align-items-center">
									<input type="${question.type.getHtml()}" disabled="true">
										<input type="text" placeholder="답변" required="true"
										value="${option.content}"
										class="selectOptionContent form-control w-50 
														border-top-0 border-left-0 border-right-0 rounded-0">
									<button class="btn p-0" onclick="removeselectOption(this)">
										<img src="/images/trash-can-icon.png" alt="객관식 문항 삭제 버튼"
											width="22px">
									</button>
								</div>
							</c:forEach>
						</div>
						<!-- 객관식 옵션 추가 버튼  -->
						<div class="mpcAddBtn">
							<c:if
								test="${question.type == QuestionType.MULTIPLE_ONE || question.type == QuestionType.MULTIPLE_MANY}">
								<button type="button" class="btn mt-2"
									value="${question.type.getHtml()}"
									onclick="addselectOption(this)">
									<img src="/images/plus-icon.png" alt="객관식 문항 추가 아이콘"
										width="22px" />
								</button>
							</c:if>
						</div>
						<div
							class="d-flex justify-content-between align-items-center mt-4">
							<!-- 필수 선택 버튼 -->
							<div class="custom-control custom-switch">
								<c:if test="${question.isRequired}">
									<input type="checkbox" class="custom-control-input" id="${question.id}" checked/>
									<label class="custom-control-label" for="${question.id}">필수</label>
								</c:if>
								<c:if test="${!question.isRequired}">
									<input type="checkbox" class="custom-control-input" id="${question.id}"/>
									<label class="custom-control-label" for="${question.id}">필수</label>
								</c:if>
							</div>
							<button class="btn btn-secondary ml-5"
								onclick="removeQuestion(this)">삭제</button>
						</div>
					</div>
				</div>
			</c:forEach>
		</div>
		<!-- 설문 추가 버튼 -->
		<button type="button" class="btn mt-2 w-100 border py-5"
			onclick="addQuestion()">
			<img src="/images/plus-icon3.png" alt="객관식 문항 추가 아이콘" width="44px" />
		</button>
		<!-- 저장 및 취소 버튼-->
		<div class="d-flex justify-content-end mt-5">
			<a href="/" class="btn btn-light mr-2">취소</a>
			<c:if test="${survey.id ne null}">
				<button type="button" class="btn btn-danger mr-2" onclick="deleteSurvey()">삭제</button>
			</c:if>
			<button class="btn btn-primary">저장</button>
		</div>
	</form>
</body>
</html>
<!-- 고려했던 내용들
	부트스트랩, JQuery 다운로드 해서 임포트 (폐쇄망 고려)
  Chart.js는 방법 찾는 것에 시간이 소요되어 구현 완료 후 변경 예정
  ES6 지양 (IE 플랫폼 고려) 
  contentEditable은 mobile 환경에서 각 환경별 추가 설정이 필요한 단점으로 인해 textarea로 대체
-->
