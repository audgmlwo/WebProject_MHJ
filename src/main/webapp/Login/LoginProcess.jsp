<%@page import="login.MemberDTO"%>
<%@page import="login.MemberDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 

String userId = request.getParameter("user_id");
String userPw = request.getParameter("user_pw");


MemberDAO dao = new MemberDAO(); 

MemberDTO memberDTO = dao.getMemberDTO(userId, userPw);

dao.close();

// 만약 DTO 객체에 아이디가 저장되어 있다면 로그인에 성공한 것으로 판단.
if (memberDTO.getUser_id() != null){
	// 세션 영역에 아이디와 이름을 저장한다.
	session.setAttribute("UserId", memberDTO.getUser_id());
	session.setAttribute("UserName", memberDTO.getName());
	/*
	세선 영역에 저장된 속성값은 페이지를 이동하더라도 유지되므로 로그인 페이지로 이동한다.
	그리고 웹브라우저를 완전히 닫을때까지 저장된 정보는 유지된다.
	*/
	response.sendRedirect("LoginForm.jsp"); 
	
}
else {
	/*
	로그인에 실패한 경우에는 request 영역에 에러메세지를 저장한 후 
	로그인 페이지로 포워드한다. request 영역은 포워드 한 페이지까지 데이터를 공유한다.
	*/
	request.setAttribute("LoginErrMsg", "로그인 오류입니다.");
	request.getRequestDispatcher("LoginForm.jsp")
		.forward(request, response);
	
}
%>

<html>
<head>
<title>Insert title here</title>
</head>
<body>

</body>
</html>