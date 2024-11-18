<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Citrusy by TEMPLATED</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <link href='http://fonts.googleapis.com/css?family=Raleway:400,100,200,300,500,600,700,800,900' rel='stylesheet' type='text/css'>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/skel.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/skel-panels.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/init.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/skel-noscript.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style-desktop.css" />
</head>
<body>
<div id="header-wrapper">
    <div id="header">
        <div class="container">
            <div id="logo"> <!-- Logo -->
                <h1><a href="#">Citrusy</a></h1>
                <p>by TEMPLATED</p>
            </div>
        </div>
        <div class="container">
            <nav id="nav">
                <ul>
                    <li><a href="index.jsp">메인화면</a></li>
                    <li><a href="threecolumn.jsp">공지사항</a></li>
                    <li><a href="twocolumn1.jsp">Q&A 게시판</a></li>
                    <li><a href="twocolumn2.jsp">자료실</a></li>
                    <li class="current_page_item"><a href="${pageContext.request.contextPath}/board/BoardListCtrl">자유게시판</a></li>
                </ul>
            </nav>
        </div>
    </div>
</div>

<div id="wrapper">
    <div id="page">
        <div class="container">
            <div class="row">
                <div class="12u">
                    <div class="mobileUI-main-content" id="content">
                        <section>
                            <div class="post">
                                <h2 class="custom-heading">자유게시판 목록</h2>
                                
                                 <form method="get">  
							     <table border="1" width="90%">
							     <tr>
							        <td align="center">
							            <select name="searchField">
							                <option value="title">제목</option>
							                <option value="content">내용</option>
							                <option value="name">작성자</option>
							            </select>
							            <input type="text" name="searchWord" />
							            <input type="submit" value="검색하기" />
							        </td>
							     </tr>
							     </table>
							     </form>
                                
							    <table border="1" width="90%">
							        <tr>
							            <th width="10%">번호</th>
							            <th width="*">제목</th>
							            <th width="15%">작성자</th>
							            <th width="10%">조회수</th>
							            <th width="15%">작성일</th>							           
							            <th width="8%">첨부</th>
							        </tr>
                                   
                                        <c:choose>
                                            <c:when test="${empty boardLists}">
                                                <tr>
                                                    <td colspan="6" align="center">등록된 게시물이 없습니다</td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach items="${boardLists }" var="row"
													varStatus="loop">
										        <tr align="center">
										        
										        	<!-- 게시물의 갯수를 저장한 map.totalCount 에서 인출되는 인스턴스의 인덱스를 차감해서 순차적인 번호를 출력 -->
										        	<!-- (map.totalCount : 5) -  (loop.index : 0,1,2,3,4) ==> 5,4,3,2,1  -->
										        	<!-- pageNum 현재 진입해있는 페이지 번호
										        	     pageSize 한 페이지에 출력할 게시물의 수 -->
										            <td>
										            	${ map.totalCount - (((map.pageNum-1) * map.pageSize)
										            	+ loop.index)}	
										            </td> 
										            <td align="left">
										            	<!-- 제목 클릭시 '열람'페이지로 이동해야 하므로
										            		 게시물의 일련번호를 파라미터로 전달한다. -->
														<a href="../board/view.do?idx=${row.idx }">
															${row.title }</a>        
										            </td> 
										            <!-- 현재 루프에서 row는 BoardDTO를 의미하므로, 각 멤버변수의
										            	 getter()를 통해 저장된 값을 출력한다. -->
										            <td>${ row.id}</td>
										            <td>${ row.visitcount}</td>
										            <td>${ row.postdate}</td>
										            <td>
										            
										            <!-- 첨부파일이 있는 경우에만 다운로드 링크를 출력한다. -->
										            <c:if test="${not empty row.ofile }">
										          	  <a href="../board/download.do?ofile=${row.ofile }&sfile=${row.sfile}&idx=${row.idx}">[Down]</a>
										            </c:if>    
										            </td>
										        </tr>
										        </c:forEach>	
                                            </c:otherwise>
                                        </c:choose>
                                    </tbody>
                                </table>
                                		 
							    
  								<!-- 페이징 이미지 표시 -->
								    <table border="1" width="90%">
								        <tr align="center">
								            <td>
								            	${ map.pagingImg }
											</td>
											
								        </tr>
								   </table>
								   <div class="button-container">
									 <button type="button"onclick="location.href='../Board/boardWrite.jsp';">글쓰기</button>
								   </div>
                                <!-- 자유게시판 목록 코드 끝 -->
                            </div>
                        </section>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Footer Section -->
<div id="footer-wrapper">
    <div class="container">
        <div class="row" id="footer-content">
            <div class="3u" id="box1">
                <section>
                    <h2>Maecenas luctus lectus</h2>
                    <p><a href="#"><img src="${pageContext.request.contextPath}/images/pics02.jpg" alt=""></a></p>
                    <p>Donec placerat odio vel elit. Nullam ante orci, pellentesque eget, tempus quis, ultrices in, est. Curabitur sit amet nulla. Nam in massa. Sed vel tellus. Curabitur sem urna, consequat.</p>
                </section>
            </div>
            <div class="3u" id="box2">
                <section>
                    <h2>Donec dictum metus</h2>
                    <ul class="style6">
                        <li>
                            <h3>Mauris vulputate dolor sit amet</h3>
                            <p><a href="#">Donec leo, vivamus fermentum nibh in augue praesent a lacus at urna congue rutrum.</a></p>
                        </li>
                        <li>
                            <h3>Fusce ultrices fringilla metus</h3>
                            <p><a href="#">Donec leo, vivamus fermentum nibh in augue praesent a lacus at urna congue rutrum.</a></p>
                        </li>
                    </ul>
                </section>
            </div>
            <div class="3u" id="box3">
                <section>
                    <h2>Contact Information</h2>
                    <p><strong>Phone</strong>: +1 234 567 8900</p>
                    <p><strong>Email</strong>: myemail@myemail.com</p>
                    <p><strong>My CompanyName</strong><br>1234 Pellentesque tristique ante risus<br>State, Plus Country 1234</p>
                </section>
            </div>
        </div>
        <div class="container" id="copyright">
            Design: <a href="http://templated.co">TEMPLATED</a> Images: <a href="http://unsplash.com">Unsplash</a> (<a href="http://unsplash.com/cc0">CC0</a>)
        </div>
    </div>
</div>
</body>
</html>