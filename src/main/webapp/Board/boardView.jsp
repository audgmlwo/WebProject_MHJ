<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page session="true" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시판 상세 보기</title>
    <link href='http://fonts.googleapis.com/css?family=Raleway:400,100,200,300,500,600,700,800,900' rel='stylesheet' type='text/css'>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/skel-noscript.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style-desktop.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/board/view.css" />

    <style>
        /* 좋아요 버튼 컨테이너 스타일 */
        .like-container {
            display: flex;
            justify-content: center;
            align-items: center;
            margin: 20px 0; /* 상하 여백 */
            padding: 10px;
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            background-color: #f9f9f9;
        }

        /* 좋아요 버튼 스타일 */
        .like-button {
            background-color: #4CAF50;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 5px;
            font-size: 14px;
            font-weight: bold;
            cursor: pointer;
        }

        .like-button:hover {
            background-color: #45a049;
        }

        /* 좋아요 수 스타일 */
        .like-count {
            margin-left: 15px;
            font-size: 16px;
            font-weight: bold;
            color: #333;
        }

        .button-container {
            margin-top: 20px;
        }

    </style>
</head>
<body>
<div id="header-wrapper">
    <div id="header">
        <div class="container">
        </div>
    </div>
</div>

<div id="wrapper">
    <div id="page" class="container">
        <div class="row">
            <div class="12u">
                <div class="mobileUI-main-content" id="content">
                    <section>
                        <div class="post">
                            <h2 class="custom-heading">게시물 상세보기</h2>

                            <table class="board-detail" border="1">
                                <colgroup>
                                    <col width="15%"/> <col width="35%"/>
                                    <col width="15%"/> <col width="*"/>
                                </colgroup>
                                <tbody>
                                <tr>
                                    <td class="label">번호</td>
                                    <td>${dto.board_id}</td>
                                    <td class="label">게시판</td>
                                    <td>${dto.board_type}</td>
                                </tr>
                                <tr>
                                    <td class="label">작성자</td>
                                    <td>${dto.name}</td>
                                    <td class="label">작성일</td>
                                    <td>${dto.created_date}</td>
                                </tr>
                                <tr>
                                    <td class="label">조회수</td>
                                    <td>${dto.visit_count}</td>
                                    <td class="label">다운로드수</td>
                                    <td>${dto.down_count}</td>
                                </tr>
                                <tr>
                                    <td class="label">제목</td>
                                    <td colspan="3">${dto.title}</td>
                                </tr>
                                <tr>
                                    <td class="label">내용</td>
                                    <td colspan="3" class="content-box">
                                        ${dto.content}
                                        <!-- 첨부파일 출력 -->
                                        <c:if test="${not empty dto.o_file}">
                                            <div class="attachment">
                                                <c:choose>
                                                    <c:when test="${fn:endsWith(dto.o_file, 'png') || fn:endsWith(dto.o_file, 'jpg') || fn:endsWith(dto.o_file, 'gif')}">
                                                        <img src="../Uploads/${dto.s_file}" alt="첨부 이미지"/>
                                                    </c:when>
                                                    <c:when test="${fn:endsWith(dto.o_file, 'mp3') || fn:endsWith(dto.o_file, 'wav')}">
                                                        <audio src="../Uploads/${dto.s_file}" controls></audio>
                                                    </c:when>
                                                    <c:when test="${fn:endsWith(dto.o_file, 'mp4') || fn:endsWith(dto.o_file, 'avi') || fn:endsWith(dto.o_file, 'wmv')}">
                                                        <video src="../Uploads/${dto.s_file}" controls></video>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a href="../Uploads/${dto.s_file}">${dto.o_file}</a>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </c:if>
                                    </td>
                                </tr>
                                </tbody>
                            </table>

                            <!-- 좋아요 버튼과 좋아요 수 -->
                            <div class="like-container">
                                <c:if test="${not empty sessionScope.UserId}">
                                    <form action="${pageContext.request.contextPath}/likes/LIKE" method="post">
                                        <input type="hidden" name="board_type" value="${dto.board_type}" />
                                        <input type="hidden" name="post_id" value="${dto.board_id}" />
                                        <button type="submit" class="like-button">
                                            ${likeDAO.hasLiked(dto.board_type, dto.board_id, sessionScope.UserId) ? '좋아요 취소' : '좋아요'}
                                        </button>
                                    </form>
                                    <span class="like-count">
                                        좋아요 수: ${likeCount != null ? likeCount : 0}
                                    </span>
                                </c:if>
                                <c:if test="${empty sessionScope.UserId}">
                                    <p>로그인 후 좋아요를 사용할 수 있습니다.</p>
                                </c:if>
                            </div>

                            <!-- 수정/삭제/목록 버튼 -->
                            <div class="button-container">
                                <c:if test="${not empty sessionScope.UserId}">
                                    <button class="button" onclick="location.href='../board/BEC?board_id=${dto.board_id}&board_type=${dto.board_type}'">수정하기</button>
                                    <button class="button" onclick="confirmDelete('${dto.board_id}&${dto.board_type}')">삭제하기</button>
                                </c:if>
                                <button class="button green" onclick="location.href='../board/BLPC'">목록으로 돌아가기</button>
                            </div>
                        </div>
                    </section>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    function confirmDelete(board_id, board_type) {
        if (confirm('정말로 삭제하시겠습니까?')) {
            location.href = '../board/BDEC?board_id=' + board_id + '&board_type=' + board_type;
        }
    }
</script>

<div id="footer-wrapper">
    <div class="container">
        <div id="copyright">
            <p>게시판 시스템 © 2024. All rights reserved.</p>
        </div>
    </div>
</div>
</body>
</html>
