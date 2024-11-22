<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>비밀번호 찾기 결과</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
    <link href="https://fonts.googleapis.com/css?family=Raleway:400,100,200,300,500,600,700,800,900" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login/login.css" />
</head>
<body>
   <div class="login-wrapper">
        <div class="login-container">
            <h2>비밀번호 찾기 결과</h2>
            <p>${message}</p>
            <p>임시 비밀번호로 로그인하셨습니다. 새 비밀번호를 설정해주세요.</p>
			<form action="<%= request.getContextPath() %>/Login/ChangePwdFind.jsp" method="get">
			    <button type="submit">비밀번호 변경</button>
			</form>
        </div>
    </div>
</body>
</html>