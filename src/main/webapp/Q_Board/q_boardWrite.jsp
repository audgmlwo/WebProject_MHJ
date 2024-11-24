<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>글쓰기 - 게시판</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/board/write.css" />
</head>

<body>
    <div class="overlay"></div>
    <div class="form-container">
        <form action="${pageContext.request.contextPath}/q_board/BWC" method="post" enctype="multipart/form-data">	 	

            <!-- 제목 -->
            <div class="form-group">
                <label for="title">제목을 입력해주세요:</label>
                <input type="text" id="title" name="title" placeholder="제목을 입력해주세요." required />
            </div>

            <!-- 본문 -->
            <div class="form-group">
                <label for="content">내용:</label>
                <textarea id="content" name="content" rows="10" placeholder="내용을 입력해주세요." required></textarea>
            </div>
         

            <!-- 버튼 -->
            <div class="button-container">
                <button type="button" class="btn-cancel" onclick="location.href='../Q_Board/q_boardList.jsp'">취소</button>
                <button type="submit" class="btn-submit">작성 완료</button>
            </div>
        </form>
    </div>
</body>
</html>
