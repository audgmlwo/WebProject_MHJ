<%@page import="utils.CookieManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
/*
로그아웃 처리 1 : session 영역의 속성명을 지정해서 삭제한다.
*/
session.removeAttribute("UserId");
session.removeAttribute("UserName");

//쿠키 삭제
CookieManager.deleteCookie(response, "UserId");

/*
로그아웃 처리2 : session 영역의 전체의 속성을 한꺼번에 삭제한다.
			  만약 회원인증 이외의 데이터가 있다면 조심해야한다.
*/
session.invalidate();

String contextPath = request.getContextPath();
response.sendRedirect(contextPath + "/Main/main.jsp");



%>



<html>
<head><title>Session</title></head>
<body>
	
</body>
</html>