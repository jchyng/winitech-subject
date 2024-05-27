<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"   uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ui"     uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>로그인</title>
	<script src="//code.jquery.com/jquery-3.5.1.min.js" ></script>
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
      <form action="${pageContext.request.contextPath}/signin.do" method="post"
        class="d-flex flex-column align-items-center gap-3 w-25">
        <!-- REST 방식이 아닌 FORM의 경우 CSRF 방지를 위해 토큰을 같이 전송 -->
        <input name="${_csrf.parameterName}" type="hidden" value="${_csrf.token}"/>
        
        <!-- 폼 제목 -->
        <span class="fs-3 fw-bold">로그인</span>
        
		<!-- 아이디 -->
        <div class="w-100">
          <label for="username" class="form-label fw-bold">아이디</label>
          <input type="text" name="username" class="form-control"/>
        </div>
        <!-- 비밀번호 -->
        <div class="w-100">
          <label for="password" class="form-label fw-bold">비밀번호</label>
          <input type="password" name="password" class="form-control"/>
        </div>
        
		<!-- 로그인 버튼 -->
        <button type="submit" onclick="login(this.form)" class="btn btn-danger w-100">로그인</button>
        <!-- 자동 로그인 & 회원가입 버튼 -->
        <div class="d-flex gap-3 fs-6 text-body-secondary">
          <div class="d-flex gap-2">
            <input name="remember-me" type="checkbox" />자동 로그인
          </div>
          <span>|</span>
          <a href="/signup.do">회원가입</a>
        </div>
      </form>
    </div>
  </body>
</html>