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
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/board/view.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/likes.css" />
  
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
                            <h2 class="custom-heading">자료실</h2>

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
										<td colspan="3" class="content-box"><textarea
												id="content" name="content" rows="20"
												style="width: 100%; height: 400px; resize: none;"readonly>${dto.content}</textarea>
										</td>
									</tr>
                           	        <!-- 첨부파일 출력 -->
									<tr>
									    <td class="label">첨부파일</td>
									    <td colspan="3">
									        <!-- 첨부파일이 있을 경우 -->
									        <c:if test="${not empty dto.o_file}">
									            <c:choose>
									                
									                <c:when test="${fn:endsWith(dto.o_file, 'png') || fn:endsWith(dto.o_file, 'jpg') || fn:endsWith(dto.o_file, 'gif')}">
									                <!-- 이미지 파일 -->
									                    <img src="../Uploads/${dto.s_file}" alt="첨부 이미지" style="max-width: 200px; height: auto;" />								                    
									                    <a href="../board/BDC?o_file=${dto.o_file}&s_file=${dto.s_file}" onclick="this.style.pointerEvents='none';">
									                        [다운로드]
									                    </a>
									                </c:when>
																	                
									                <c:when test="${fn:endsWith(dto.o_file, 'mp3') || fn:endsWith(dto.o_file, 'wav')}">
									                <!-- 오디오 파일 -->		                 
									                    <audio src="../Uploads/${dto.s_file}" controls></audio>
									                    <a href="../board/BDC?o_file=${dto.o_file}&s_file=${dto.s_file}" onclick="this.style.pointerEvents='none';">
									                        [다운로드]
									                    </a>
									                </c:when>
																	                
									                <c:when test="${fn:endsWith(dto.o_file, 'mp4') || fn:endsWith(dto.o_file, 'avi') || fn:endsWith(dto.o_file, 'wmv')}">
									                <!-- 비디오 파일 -->
									                    <video src="../Uploads/${dto.s_file}" controls style="max-width: 300px; height: auto;"></video>
									                    <a href="../board/BDC?o_file=${dto.o_file}&s_file=${dto.s_file}" onclick="this.style.pointerEvents='none';">
									                        [다운로드]
									                    </a>
									                </c:when>
																		                
									                <c:otherwise>
									                <!-- 기타 파일 -->
									                    <span>${dto.o_file}</span>
									                    <a href="../board/BDC?o_file=${dto.o_file}&s_file=${dto.s_file}" onclick="this.style.pointerEvents='none';">
									                        [다운로드]
									                    </a>
									                </c:otherwise>
									            </c:choose>
									        </c:if>
									
									        <!-- 첨부파일이 없는 경우 -->
									        <c:if test="${empty dto.o_file}">
									            <span style="color: #999;">첨부파일이 없습니다.</span>
									        </c:if>
									    </td>
									</tr>
                                </tbody>
                            </table>
							
							<div class="like-container">
								<!-- 로그인된 사용자일 때 -->
								<c:if test="${not empty sessionScope.UserId}">
									<form action="${pageContext.request.contextPath}/likes/LIKE"
										method="post">
										<!-- 숨겨진 데이터 전달 -->
										<input type="hidden" name="board_type"
											value="${dto.board_type}" /> <input type="hidden"
											name="post_id" value="${dto.board_id}" />

										<!-- 좋아요 버튼 -->
										<button type="submit" class="like-button">
											${likeDAO.hasLiked(dto.board_type, dto.q_id, sessionScope.UserId) ? '좋아요 취소' : '좋아요'}
										</button>
									</form>

									<!-- 좋아요 수 표시 -->
									<span class="like-count"> 좋아요 수: ${likeCount != null ? likeCount : 0}
									</span>
								</c:if>

								<!-- 로그인되지 않은 사용자일 때 -->
								<c:if test="${empty sessionScope.UserId}">
									<p>로그인 후 좋아요를 사용할 수 있습니다.</p>
								</c:if>
							</div>
							
                            <div class="button-container">
                                <c:if test="${not empty UserId}">
                                    <button class="button" onclick="location.href='../files/BEC?board_id=${dto.board_id}&board_type=${dto.board_type}'">수정하기</button>
                                    <button class="button" onclick="confirmDelete('${dto.board_id}&${dto.board_type}')">삭제하기</button>
                                </c:if>
                                <button class="button green" onclick="location.href='../files/BLPC'">목록으로</button>
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
		        location.href = '../files/BDEC?board_id=${dto.board_id}&board_type=${dto.board_type}';
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
