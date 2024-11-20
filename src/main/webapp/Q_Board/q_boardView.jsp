<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page session="true"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 상세 보기</title>
<link
	href='http://fonts.googleapis.com/css?family=Raleway:400,100,200,300,500,600,700,800,900'
	rel='stylesheet' type='text/css'>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/skel-noscript.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style-desktop.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/board/view.css" />

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
										<col width="15%" />
										<col width="35%" />
										<col width="15%" />
										<col width="*" />
									</colgroup>
									<tbody>
										<tr>
											<td class="label">번호</td>
											<td>${dto.q_id}</td>
											<td class="label">질문게시판</td>
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
										</tr>
										<tr>
											<td class="label">제목</td>
											<td colspan="3">${dto.title}</td>
										</tr>
										<tr>
											<td class="label">내용</td>
											<td colspan="3" class="content-box">${dto.content}</td>
										</tr>
									</tbody>
								</table>

								<!-- 답변 영역 -->
								<div class="answer-section">
									<h3>답변 목록</h3>
									<c:choose>
										<c:when test="${not empty dto.answers}">
											<div class="answer-list">
												<c:forEach var="answer" items="${dto.answers}">
													<div class="answer-item">
														<div class="answer-meta">
															작성자: <strong>${answer.user_id}</strong> | 작성일: <small>${answer.created_date}</small>
														</div>
														<div class="answer-content">${answer.content}</div>
													</div>
												</c:forEach>
											</div>
										</c:when>
										<c:otherwise>
											<p class="no-answer">등록된 답변이 없습니다.</p>
										</c:otherwise>
									</c:choose>
									<div class="answer-btn">
										<button
											onclick="location.href='${pageContext.request.contextPath}/na_board/NA_BWC?q_id=${dto.q_id}'"
											class="btn btn-primary">답변 작성</button>
									</div>
								</div>

								<!-- 수정, 삭제, 목록 버튼 -->
								<div class="button-container">
									<c:if test="${not empty UserId}">
										<button class="button" onclick="location.href='../q_board/Q_BEC?q_id=${dto.q_id}'">수정하기</button>
										<button class="button" onclick="confirmDelete('${dto.q_id}')">삭제하기</button>
									</c:if>
									<button class="button green"
										onclick="location.href='../q_board/Q_BLPC'">목록으로</button>
								</div>
							</div>
						</section>
					</div>
				</div>
			</div>
		</div>
	</div>


	<script type="text/javascript">
		function confirmDelete(q_id) {
			if (confirm('정말로 삭제하시겠습니까?')) {
				location.href = '../q_board/Q_BDEC?q_id=${dto.q_id}';
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
