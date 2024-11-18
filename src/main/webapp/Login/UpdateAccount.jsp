<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@page import="login.MemberDTO"%>
<%@page import="login.MemberDAO"%>


<% 
// 세션에서 로그인된 사용자 ID 가져오기
String userId = (String) session.getAttribute("UserId");
if (userId == null) {
    // 로그인이 되어 있지 않은 경우
    response.sendRedirect("LoginForm.jsp");
    return;
}

//데이터베이스에서 사용자 정보 가져오기
MemberDAO dao = new MemberDAO();
MemberDTO memberDTO = dao.getMember(userId);
dao.close();

//JSP에 사용자 정보 전달
request.setAttribute("currentUser", memberDTO);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>내 정보 관리</title>
    <link href="https://fonts.googleapis.com/css?family=Raleway:400,100,200,300,500,600,700,800,900" rel="stylesheet" type="text/css">
   	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/login/updateaccount.css" />
    <script>
        function toggleEditForm() {
            const formContainer = document.querySelector('.update-form-container');
            formContainer.style.display = formContainer.style.display === 'none' || formContainer.style.display === '' ? 'block' : 'none';
        }
    </script>
</head>


<body>
    <!-- 어두운 배경 -->
    <div class="overlay"></div>
	
    <!-- 컨테이너 -->
    <div class="container">
        <!-- 기본 정보 테이블 -->
        <table class="info-table">
            <tr>
                <th colspan="2">기본 정보</th>
            </tr>
            <tr>
                <td>아이디</td>
                <td>${currentUser.user_id}</td>
            </tr>
            <tr>
                <td>닉네임</td>
                <td>${currentUser.name}</td>
            </tr>
            <tr>
                <td>이메일</td>
                <td>${currentUser.email}</td>
            </tr>
            <tr>
                <td colspan="2" style="text-align: center;">
                    <button class="edit-button" onclick="toggleEditForm()">회원 정보 수정</button>
                </td>
            </tr>
        </table>

        <!-- 회원 정보 수정 폼 -->
        <div class="update-form-container">
            <h3>회원 정보 수정</h3>
            
            <form action="${pageContext.request.contextPath}/login/UpdateAccountProcess" method="post">
            
                <input type="hidden" name="user_id" value="${currentUser.user_id}">

                <!-- 이름 -->
                <label for="name">닉네임</label>
                <input type="text" name="name" id="name" value="${currentUser.name}" placeholder="닉네임" required>

                <!-- 이메일 -->
                <label for="email">이메일</label>
                <input type="email" name="email" id="email" value="${currentUser.email}" placeholder="이메일" required>

                <!-- 비밀번호 -->
                <label for="password">새 비밀번호</label>
                <input type="password" name="password" id="password" placeholder="새 비밀번호를 입력하세요.">

                <!-- 비밀번호 확인 -->
                <label for="confirm_password">비밀번호 확인</label>
                <input type="password" name="confirm_password" id="confirm_password" placeholder="비밀번호를 다시 입력하세요.">

                <!-- 제출 -->
                <input type="submit" value="정보 수정">
                
                <c:if test="${not empty errors}">
  				  <div class="error-messages">
       				 <c:forEach var="error" items="${errors}">
           				  <p>${error.value}</p>
     		  		 </c:forEach>
   				  </div>
				</c:if>
				
            </form>
        </div>
    </div>
</body>
</html>
