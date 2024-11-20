<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>답변 작성</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h1>답변 작성</h1>
    <form method="post" action="../na_board/NA_BWC">
        <input type="hidden" name="q_id" value="${param.q_id}" />
        <div class="mb-3">
            <label for="content" class="form-label">답변 내용</label>
            <textarea class="form-control" id="content" name="content" rows="5" required></textarea>
        </div>
        <button type="submit" class="btn btn-primary">등록</button>
        <button type="button" class="btn btn-secondary" onclick="history.back()">취소</button>
    </form>
</div>
</body>
</html>
