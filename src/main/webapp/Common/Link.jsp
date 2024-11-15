<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<table border="1" width="90%"> 
    <tr>
    <tr>
        <td align="center">
        <!-- 로그인 여부에 따른 메뉴 변화 -->
        <% if (session.getAttribute("UserId") == null) { %>
            <a href="../Login/LoginForm.jsp">로그인</a>
        <% } else { %>
            <a href="../Login/Logout.jsp">로그아웃</a>
        <% } %>
        	
            
            &nbsp;&nbsp;&nbsp; 
            <a href="../mvcboard/list.do">자유게시판</a>
            &nbsp;&nbsp;&nbsp; 
            <a href="../mvcboard/listPage.do">Q&A 게시판</a>
            &nbsp;&nbsp;&nbsp; 
            <a href="../mvcboard/list.do">자료실 게시판</a>
        </td>
    </tr>
</table>
    