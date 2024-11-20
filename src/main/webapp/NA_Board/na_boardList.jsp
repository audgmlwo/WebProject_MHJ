<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>답변 작성</title>
</head>
<body>
<h2>답변 작성</h2>
<form method="post" action="../na_board/NA_BWC">

    <input type="hidden" name="q_id" value="${param.q_id}" />
    <label for="content">답변 내용</label><br>
    <textarea name="content" rows="5" cols="50"></textarea><br>
    <button type="submit">답변 등록</button>
    
</form>
</body>
</html>