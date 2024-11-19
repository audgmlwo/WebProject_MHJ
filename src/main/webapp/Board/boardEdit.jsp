<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시물 수정하기</title>
   	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/board/edit.css" />
</head>
<body>

<div id="wrapper">
    <h2>게시글 수정</h2>

    <form name="writeFrm" method="post" enctype="multipart/form-data" action="../board/BEC">
        <!-- 숨겨진 필드 -->
        
    
	<!-- 수정할 게시판의 일련번호 -->
	<div class="field-container">
		<label class="label">게시판</label>
		<input type="hid den" name="board_type" value="${ dto.board_type }"/>
	</div>
	<!-- 수정할 게시물의 일련번호 -->
    <div class="field-container">
	    <label class="label">게시물번호</label>
		<input type="hid den" name="board_id" value="${ dto.board_id }"/>
	</div>
	<!-- 작성자 아이디 -->
	<div class="field-container">
		<label class="label">아이디</label>
		<input type="hid den" name="user_id" value="${ dto.user_id }"/>
	</div>
	<!-- 수정할 게시물의 파일 -->
	<div class="field-container">
		<label class="label">파일</label>
		<input type="hid den" name="prevO_file" value="${ dto.o_file }" />
	</div>
	<!-- 수정된 게시물의 파일 -->
	<div class="field-container">
		<label class="label">파일</label>
		<input type="hid den" name="prevS_file" value="${ dto.s_file }" />
	</div>
	<div class="input-section">
         <table>
            <tr>
                <th>제목</th>
                <td>
                    <input type="text" name="title" style="width:95%;" value="${dto.title}" />
                </td>
            </tr>
            <tr>
                <th>내용</th>
                <td>
                    <textarea name="content" style="width:95%; height:100px;">${dto.content}</textarea>
                </td>
            </tr>
            <tr>
                <th>첨부파일</th>
                <td>
                    <input type="file" name="o_file" />
                </td>
            </tr>
        </table>
    </div>

        <div class="button-container">
            <button type="submit" class="button green">작성 완료</button>
            <button type="reset" class="button gray">RESET</button>
            <button type="button" class="button red" onclick="location.href='../board/BLPC';">목록으로 돌아가기</button>
        </div>
    </form>
</div>
</body>
</html>
