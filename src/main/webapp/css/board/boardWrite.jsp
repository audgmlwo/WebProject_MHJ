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
        <form action="../board/BoardWriteCtrl" method="post" enctype="multipart/form-data">
		    <div class="form-group">
		        <label for="board_type">게시판을 선택해주세요:</label>
		        <select id="board_type" name="board_type" required>
		            <option value="" disabled selected>게시판 선택</option>
		            <option value="fre">자유 게시판</option>
		            <option value="files">자료실</option>
		            <option value="qna">Q&A 게시판</option>
		            <option value="ntc">공지사항</option>
		        </select>
		    </div>
			<script>
			    document.querySelector('form').addEventListener('submit', function(e) {
			        const boardType = document.getElementById('board_type').value;
			        if (!boardType) {
			            alert('게시판을 선택해주세요!');
			            e.preventDefault(); // 제출 중단
			        }
			    });
			</script>

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

            <!-- 첨부파일 -->
            <div class="form-group">
                <label for="file">첨부파일:</label>
                <input type="file" id="file" name="o_file" />
            </div>

            <!-- 버튼 -->
            <div class="button-container">
                <button type="button" class="btn-cancel" onclick="location.href='../Board/boardList.jsp'">취소</button>
                <button type="submit" class="btn-submit">작성 완료</button>
            </div>
        </form>
    </div>
</body>
</html>
