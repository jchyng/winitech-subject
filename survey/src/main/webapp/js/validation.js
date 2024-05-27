/** 아이디 유효성 검증 */
function validateUsername(){
	var username = document.getElementById("username").value;

	/* 영어로 시작하고 영어+숫자 조합의 6~20자리 */	
	var reg = /^[a-zA-z][a-zA-Z0-9]{5,19}$/; //맨 앞에 a~Z 하나가 들어가서 1뺀 범위
	if(username && reg.test(username)){
		return true;
	}
	return false;
}

/** 아이디 중복 확인 여부 검증 */
function validateDuplication(){
	var usernameAvailability = document.getElementById("usernameAvailability").value;

	if(usernameAvailability === "available"){
		return true;
	} else if(usernameAvailability === "unavailable"){
		return false;
	}
}


/** 비밀번호 유효성 검증 */
function validatePassword(){
	var password = document.getElementById("password").value;

	/* 영어+숫자+특수문자 조합의 8~15자리 */
	var reg = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$/;
	if(password && reg.test(password)){
		return true;
	}
	return false;
}

/** 비밀번호 확인 검증 */
function validateConfirmPassword(){
	var isMatched = document.getElementById("isMatched").value;

	if(isMatched == 'matched'){
		return true;
	} else if(isMatched == 'not matched'){
		return false;
	}
}

/** 이름 유효성 검증 */
function validateName(){
	var name = document.getElementById("name").value;

	/* 한글 2~6자리 */
	var reg = /^[가-힣]{2,6}$/;
	if(name && reg.test(name)){
		return true;
	}
	return false;
}