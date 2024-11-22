<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>비밀번호 변경</title>
</head>
<body>
    <h1>비밀번호 변경</h1>
    <form action="<%= request.getContextPath() %>/login/CPC" method="post">
        <!-- 현재 비밀번호 -->
        <label for="currentPassword">현재 비밀번호:</label>
        <input type="password" id="currentPassword" name="tempPassword" required>
        <c:if test="${not empty error}">
            <p style="color: red;">${error}</p>
        </c:if>

        <!-- 새 비밀번호 -->
        <label for="newPassword">새 비밀번호:</label>
        <input type="password" id="newPassword" name="newPassword" required>

        <!-- 새 비밀번호 확인 -->
        <label for="confirmPassword">새 비밀번호 확인:</label>
        <input type="password" id="confirmPassword" name="confirm_password" required>

        <button type="submit">비밀번호 변경</button>
    </form>
</body>
</html>
