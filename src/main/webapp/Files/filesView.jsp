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
									                    <img src="../Uploads/${dto.s_file}" alt="첨부 이미지" style="max-width: 200px; height: auto;" />
									                </c:when>
																		    
									                <c:when test="${fn:endsWith(dto.o_file, 'mp3') || fn:endsWith(dto.o_file, 'wav')}">
									                    <audio src="../Uploads/${dto.s_file}" controls></audio>
									                </c:when>
									
									                <c:when test="${fn:endsWith(dto.o_file, 'mp4') || fn:endsWith(dto.o_file, 'avi') || fn:endsWith(dto.o_file, 'wmv')}">
									                    <video src="../Uploads/${dto.s_file}" controls style="max-width: 300px; height: auto;"></video>
									                </c:when>
									
									                <c:otherwise>
									                    <!-- 로그인된 사용자일 경우 -->
									                    <c:if test="${not empty UserId}">
									                        <a href="../files/BDC?o_file=${dto.o_file}&s_file=${dto.s_file}&board_id=${dto.board_id}&board_type=${dto.board_type}" onclick="this.style.pointerEvents='none';">
									                            ${dto.o_file} [다운로드]
									                        </a>
									                    </c:if>
									                    <!-- 로그인되지 않은 사용자일 경우 -->
									                    <c:if test="${empty UserId}">
									                        <span style="color: #ff0000;">로그인 후 다운로드 가능합니다.</span>
									                    </c:if>
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
