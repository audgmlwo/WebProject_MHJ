<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Sign Up</title>
	<link
		href="https://fonts.googleapis.com/css?family=Raleway:400,100,200,300,500,600,700,800,900"
		rel="stylesheet" type="text/css">
	<link rel="stylesheet" href="/WebProject_MHJ/css/signup.css">
</head>	
<!-- 별도 CSS 파일 -->
<body>
	<div class="SignUp-wrapper">
		<div class="SignUp-container">
			<h2>회원가입</h2>
			 <form action="SignUpProcess" method="post">
				<!-- 아이디 -->
				<div>
					<input type="text" name="user_id" id="user_id" placeholder="아이디"
						value="${param.user_id}" required
						oninvalid="this.setCustomValidity('아이디를 입력해 주세요.')"
						oninput="this.setCustomValidity('')">
					<c:if test="${not empty errors.user_id}">
						<div class="error-message">${errors.user_id}</div>
					</c:if>
				</div>

				<!-- 비밀번호 -->
				<div>
					<input type="password" name="password" id="password"
						placeholder="비밀번호" required
						oninvalid="this.setCustomValidity('비밀번호를 입력해 주세요.')"
						oninput="this.setCustomValidity('')">
					<c:if test="${not empty errors.password}">
						<div class="error-message">${errors.password}</div>
					</c:if>
				</div>

				<!-- 비밀번호 확인 -->
				<div>
					<input type="password" name="confirm_password"
						id="confirm_password" placeholder="비밀번호 확인" required
						oninvalid="this.setCustomValidity('비밀번호를 확인해 주세요.')"
						oninput="this.setCustomValidity('')">
					<c:if test="${not empty errors.confirm_password}">
						<div class="error-message">${errors.confirm_password}</div>
					</c:if>
				</div>

				<!-- 이름 -->
				<div>
					<input type="text" name="name" id="name" placeholder="이름"
						value="${param.name}" required
						oninvalid="this.setCustomValidity('이름을 입력해 주세요.')"
						oninput="this.setCustomValidity('')">
					<c:if test="${not empty errors.name}">
						<div class="error-message">${errors.name}</div>
					</c:if>
				</div>

				<!-- 이메일 -->
				<div>
					<input type="email" name="email" id="email" placeholder="이메일"
						value="${param.email}" required
						oninvalid="this.setCustomValidity('유효한 이메일 주소를 입력해 주세요.')"
						oninput="this.setCustomValidity('')">
					<c:if test="${not empty errors.email}">
						<div class="error-message">${errors.email}</div>
					</c:if>
				</div>

				<!-- 제출 -->
				<div>
					<input type="submit" value="회원가입">
				</div>
			</form>
			<div class="login-text">
				이미 계정이 있으신가요? <a href="LoginForm.jsp">로그인</a>
			</div>
		</div>
	</div>
</body>
</html>
