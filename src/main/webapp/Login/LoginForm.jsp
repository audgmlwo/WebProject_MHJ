<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
    <title>Login</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
    <link href="https://fonts.googleapis.com/css?family=Raleway:400,100,200,300,500,600,700,800,900" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="/WebProject_MHJ/css/login.css" /> <!-- login.css 파일 -->
</head>

<body>
	<jsp:include page="../Common/Link.jsp" />
	
    <div class="login-wrapper">
        <div class="login-container">
        
            <h2>Login to Citrusy</h2>
            
            <span style="color: red; font-size: 1.2em;">
			
				<%= request.getAttribute("LoginErrMsg") == null ? 
						"" : request.getAttribute("LoginErrMsg")%>
			</span>
			
			<%
			if (session.getAttribute("UserId") == null){// 로그인 상태 확인)	
			%>
			
            <form action="LoginProcess.jsp" method="post" name="loginFrm" onsubmit="return validateForm(this);">
		        <div>
		            <input type="text" name="id" id="id" placeholder="아이디" required 
		                   oninvalid="this.setCustomValidity('아이디를 입력해 주세요.')" 
		                   oninput="this.setCustomValidity('')"> <!-- input 시 오류 메시지 초기화 -->
		            <div id="id-error" class="error-message"></div>
		        </div>
		        <div>
		            <input type="password" name="password" id="password" placeholder="비밀번호" required 
		                   oninvalid="this.setCustomValidity('비밀번호를 입력해 주세요.')" 
		                   oninput="this.setCustomValidity('')"> <!-- input 시 오류 메시지 초기화 -->
		            <div id="password-error" class="error-message"></div>
		        </div>
		        <button type="submit">로그인</button>
		    </form>
		
		    <script>
		        function validateForm(form) {
		            // 추가 JavaScript 유효성 검사 필요 시 여기서 처리 가능
		            return true; // 모든 검사가 완료되면 true 반환
		        }
		    </script>
            <p class="signup-text">계정이 필요하신가요? <a href="signup.html">가입하기</a></p>
        </div>
    </div>
		    <%
			} else { //로그인된 상태
			%>
				<%= session.getAttribute("UserName") %> 회원님, 로그인성공.<br />
				<a href="Logout.jsp">[로그아웃]</a>
			<%
			}
			%>
</body>
</html>