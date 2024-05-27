<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<!-- CSRF 정보를 담은 META DATA -->
	<meta name="_csrf" content="${_csrf.token}"/>
	<meta name="_csrf_header" content="${_csrf.headerName}"/>
	<title>로그인</title>
	<script src="//code.jquery.com/jquery-3.5.1.min.js" ></script>
	<script src="js/validation.js"></script>
	<script type="text/javaScript" defer="defer">
		/* CSRF info */
		const token = $("meta[name='_csrf']").attr("content");
		const header = $("meta[name='_csrf_header']").attr("content");
		
		
		/** 유저 아이디 중복 체크 */
		function checkDuplicateUsername(){
			const username = $("#username").val();
			
			//아이디를 입력하지 않으면 경고를 띄운 후 요청 중단
			if(!username || username.length < 1){
				alert("아이디를 입력해주세요.");
				return;
			}
			
			$.ajax({
				url: "/username.do",
				method: "POST",
				dataType : 'text',
			    contentType: 'text/plain; charset=utf-8',
				data: username,
				/* 데이터 전송 전에 헤더에 csrf 값 설정 */
				beforeSend: function(xhr) {   
					xhr.setRequestHeader(header, token);
	            },
				success: (response) => {
					alert("사용할 수 있는 아이디입니다.");
		            document.getElementById("usernameAvailability").value = "available";
		        },
		        error:(error) => {
		            alert("중복된 아이디 입니다.");
		            console.log(error);
		        }
			})
		}
		
		
		/** 회원 가입 요청 */
		function signUp(form){
			/* 폼 데이터 검증 후 alert 메세지를 받았다면 경고를 띄우고 요청 중지 */
			const alertMSG = validateForm();
			if(alertMSG){
				alert(alertMSG);
				return;
			}
			
			/* 가입 요청 데이터 */
			const signUpData = {
				username: $("#username").val(),
				password: $("#password").val(),
				name: $("#name").val(),
			}
			
			$.ajax({
				url: "/signup.do",
				method: "POST",
				data: signUpData,
				/* 데이터 전송 전에 헤더에 csrf 값 설정 */
				beforeSend: function(xhr) {   
					xhr.setRequestHeader(header, token);
	            },
				success: (response) => {
					alert("회원가입에 성공하셨습니다.");
					window.location.href = "/signin.do";		
		        },
		        error:(error) => {
		            alert("회원가입에 실패했습니다.");
		            console.log(error);
		        }
			})
			
		}
		
		/** 회원 가입 검증 */
		function validateForm(){
			if(!validateUsername()){
				return "아이디를 확인해주세요.";
			}
			if(!validateDuplication()){
				return "아이디 중복 검사를 진행해주세요.";
			}
			if(!validatePassword()){
				return "비밀번호를 확인해주세요.";
			}
			if(validateConfirmPassword()){
				return "비밀번호가 일치하지 않습니다.";
			}
			if(!validateName()){
				return "이름을 확인해주세요.";
			}
			return ;
		}
		
		/** 아이디 입력 시 검사 후 상태 메세지 변경 */
		function validateUsernameByInput(){
			//중복 검사 상태를 non으로 변경 => ajax의 .val()은 값 복사라 바로 변경 불가
			let availableUsername = $("#usernameAvailability"); 
			availableUsername.value = "unavailable";
		
			const usernameMSG = $("#usernameMSG");
			if(validateUsername()){
				usernameMSG.hide(); 	
			} else{
				usernameMSG.show();
			}
			//입력창에 아무것도 없을 때 메세지 display none
			const username = $("#username").val();
			if(username.length < 1){
				usernameMSG.hide(); 
			}
		}
		
		/** 비밀번호 입력 시 검사 후 상태 메세지 변경 */
		function validatePasswordByInput(){
			const passwordMSG = $("#passwordMSG");
			if(validatePassword()){
				passwordMSG.hide(); 	
			} else{
				passwordMSG.show();
			}
			//비밀번호 확인 상태도 변경 
			validateConfirmPasswordByInput();
			//입력창에 아무것도 없을 때 메세지 display none
			const password = $("#password").val();
			if(password.length < 1){
				passwordMSG.hide(); 
			}
		}
		
		/* 확인 비밀번호 입력 시 검사 후 match 상태와 상태 메세지 변경 */
		function validateConfirmPasswordByInput(){
			const password = $("#password").val();
			const confirmPassword = $("#confirmPassword").val();
			
			let isMatched = $("#isMatched");
			const validConfirmPasswordMSG = $("#validConfirmPasswordMSG");
			const invalidConfirmPasswordMSG = $("#invalidConfirmPasswordMSG");
			
			if(password == confirmPassword){
				isMatched.value = "matched";
				validConfirmPasswordMSG.show();
				invalidConfirmPasswordMSG.hide();
			} else{
				isMatched.value = "not matched";
				validConfirmPasswordMSG.hide();
				invalidConfirmPasswordMSG.show();
			}
			//입력창에 아무것도 없을 때 메세지 display none
			if(confirmPassword.length < 1){
				validConfirmPasswordMSG.hide(); 
				invalidConfirmPasswordMSG.hide();
			}
		}
		
		/** 이름 입력 시 검사 후 상태 메세지 변경 */
		function validateNameByInput(){
			const nameMSG = $("#nameMSG");
			if(validateName()){
				nameMSG.hide(); 	
			} else{
				nameMSG.show();
			}
			//입력창에 아무것도 없을 때 메세지 display none
			const name = $("#name").val();
			if(name.length < 1){
				nameMSG.hide(); 
			}
		}
	</script>
	<link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
      rel="stylesheet"
      integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
      crossorigin="anonymous"
    />
    <style>
      a {
        text-decoration: none;
        color: black;
      }
    </style>
</head>
<body>
	<div class="d-flex justify-content-center mt-5">
		<form class="d-flex flex-column gap-3 w-50">
			<div class="d-flex justify-content-between align-items-end">
				<!-- 폼 제목 -->
				<span class="fs-4 fw-bold">회원정보 입력</span> 
				<!-- 필수 입력 메세지 -->
				<span class="text-body-tertiary">
					<span class="text-danger fw-bold">*</span>표시는 반드시 입력하셔야 합니다.
				</span>
			</div>
			
			<!-- 아이디 입력 -->
			<div>
				<label for="username" class="form-label">아이디<span
					class="fw-bolder text-danger">*</span></label>
				<div class="input-group">
					<input id="username" type="text"
						oninput="validateUsernameByInput()" class="form-control" />
					<button type="button" onclick="checkDuplicateUsername()"
						class="btn btn-outline-secondary">중복 체크</button>
				</div>
			</div>
			<!-- 입력한 아이디의 유효성 상태 메세지 -->
			<span class="invalid-feedback" id="usernameMSG">
				아이디는 6~20자리로 영어로 시작하고 영어 + 숫자로만 사용할 수 있습니다.
			</span>
			<!-- 아이디 중복 여부 -->
			<input id="usernameAvailability" type="hidden" value="unavaliable"/>


			<!-- 비밀번호 입력 -->
			<div>
				<label for="password">비밀번호
					<span class="fw-bolder text-danger">*</span>
				</label> 
				<input id="password" type="password" class="form-control" 
					oninput="validatePasswordByInput()"/>
			</div>
			<!-- 입력한 비밀번호의 유효성 상태 메세지 -->
			<span class="invalid-feedback" id="passwordMSG">
				비밀번호는 8~15자리로 영문 + 숫자 + 특수문자를 모두 포함합니다.
			</span>


			<!-- 비밀번호 확인 -->
			<div>
				<label>비밀번호 확인
					<span class="fw-bolder text-danger">*</span>
				</label> 
				<input id="confirmPassword" type="password" class="form-control" 
					oninput="validateConfirmPasswordByInput()" />
			</div>
			<!-- 비밀번호 일치 여부 -->
			<input id="isMatched" type="hidden" value="not matched" />
			<span class="valid-feedback" id="validConfirmPasswordMSG">
				비밀번호가 일치합니다.
			</span>
			<span class="invalid-feedback" id="invalidConfirmPasswordMSG">
				비밀번호가 일치하지 않습니다.
			</span>
			

			<!-- 이름 입력 -->
			<div>
				<label for="name">이름
					<span class="fw-bolder text-danger">*</span>
				</label> 
				<input class="form-control" id="name" type="text" 
					oninput="validateNameByInput()"/>
			</div>
			<!-- 입력한 비밀번호의 유효성 상태 메세지 -->
			<span class="invalid-feedback" id="nameMSG">
				이름은 한글 2~6자리 입니다.
			</span>
			

			<!-- Buttons -->
			<div class="d-flex justify-content-center gap-3 mt-5">
				<a class="btn btn-outline-secondary" href="/signin.do">취소</a>
				<button type="button" class="btn btn-outline-danger"
					onclick="signUp(this.form)">가입</button>
			</div>
		</form>
	</div>
</body>
</html>