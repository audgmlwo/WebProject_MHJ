<%@page import="q_board.Q_BoardDTO"%>
<%@page import="board.BoardDTO"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!--
	Citrusy by TEMPLATED
    templated.co @templatedco
    Released for free under the Creative Commons Attribution 3.0 license (templated.co/license)
-->
<html>
<head>
<title>Citrusy by TEMPLATED</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link
	href='http://fonts.googleapis.com/css?family=Raleway:400,100,200,300,500,600,700,800,900'
	rel='stylesheet' type='text/css'>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/skel.min.js"></script>
<script src="${pageContext.request.contextPath}/js/skel-panels.min.js"></script>
<script src="${pageContext.request.contextPath}/js/init.js"></script>
<script src="${pageContext.request.contextPath}/js/typingLimit.js"></script>



<link rel="stylesheet"href="${pageContext.request.contextPath}/css/main.css" />
<link rel="stylesheet"href="${pageContext.request.contextPath}/css/style.css" />
<link rel="stylesheet"href="${pageContext.request.contextPath}/css/style-desktop1.css" />

<%
    // 세션에서 사용자 정보 가져오기
    String userId = (String) session.getAttribute("UserId");
    String name = (String) session.getAttribute("Name");
    String email = (String) session.getAttribute("Email");
    
    // 디버깅 코드
    System.out.println("디버깅 - JSP에서 세션 값 확인:");
    System.out.println("UserId: " + userId);
    System.out.println("Name: " + name);
    System.out.println("Email: " + email);

    // 컨텍스트 경로 가져오기
    String contextPath = request.getContextPath();

    // 필수 데이터가 없는 경우 메인 컨트롤러로 리다이렉트
    if (request.getAttribute("freList") == null || 
        request.getAttribute("qnaList") == null || 
        request.getAttribute("filesList") == null) {
        response.sendRedirect(contextPath + "/main/MLC");
        return;
    }
%>

</head>
<body class="homepage">
	<div id="header-wrapper">
		<div id="header">
			<div class="container">
				<div id="logo">
					<!-- Logo -->
					<h1>
						<a href="#">Citrusy</a>
					</h1>
					<p>by TEMPLATED</p>
				</div>
			</div>
			<div class="container">
				<nav id="nav">
					<ul>
					<li class="current_page_item"><a href="${pageContext.request.contextPath}/Main/main.jsp">메인화면</a></li>
                    <li><a href="threecolumn.jsp">공지사항</a></li>
                    <li class="current_page_item"><a href="${pageContext.request.contextPath}/Q_Board/q_boardList.jsp">QnA 게시판</a></li>
                    <li class="current_page_item"><a href="${pageContext.request.contextPath}/Files/filesList.jsp">자료실 게시판</a></li>
                    <li class="current_page_item"><a href="${pageContext.request.contextPath}/Board/boardList.jsp">자유게시판</a></li>  
					</ul>
				</nav>
			</div>
		</div>
	</div>
	
	<div id="wrapper">
    <div class="container">
        <!-- 배너 -->
        <div class="row">
            <div id="banner" class="12u">
                <div class="container">
                    <a href="#"><img src="<%= request.getContextPath() %>/images/pics09.jpg" alt="배너 이미지"></a>
                </div>
            </div>
        </div>

        <!-- 마케팅 박스 -->
       <div id="marketing">
		    <div class="container">
		        <div class="row divider">
		            <!-- 마케팅 박스 1번: 로그인/사용자 정보 -->
		            <div class="3u">
		                <section>
		                    <div class="auth-box">
		                        <!-- 로그인 상태에 따른 렌더링 -->
		                        <% if (userId != null) { %>
		                        <!-- 로그인 상태 -->
		                        <div class="user-info">
		                            <p>안녕하세요, <strong><%= name %></strong>님!</p>
		                            <p class="email-info">이메일: <strong><%= email %></strong></p>
		                            <a href="<%= request.getContextPath() %>/Login/Logout.jsp" class="logout-button">[로그아웃]</a>
		                        </div>
		                        <% } else { %>
		                        <!-- 비로그인 상태 -->
		                        <div class="login-box">
		                           <form action="../main/MLC" method="post" class="login-form">
									    <button type="submit" class="login-button">로그인</button>
									</form>
		                            <div class="auth-links">
		                                <a href="../Board/boardList.jsp">아이디</a>
		                                <span>/</span>
		                                <a href="../Login/PwdFind.jsp">비밀번호 찾기</a>
		                                <span>|</span>
		                                <a href="../Login/SignUp.jsp">회원가입</a>
		                            </div>
		                        </div>
		                        <% } %>
		                    </div>
		                </section>
		            </div>

                    <!-- 자유게시판 미리보기 -->
                    <div class="3u">
                        <section>
                            <div class="mbox-style">
                                <h2 class="title">자유게시판</h2>
                                <div class="content">
                                    <ul>
                                        <% 
										    List<BoardDTO> freList = (List<BoardDTO>) request.getAttribute("freList");
										    if (freList != null && !freList.isEmpty()) {
										        for (BoardDTO freItem : freList) { 
										%>
										        <li>
										            <a href="<%= request.getContextPath() %>/board/BVC?board_id=<%= freItem.getBoard_id() %>&board_type=fre">
										                <%= freItem.getTitle().replace("\n", "<br>") %>
										            </a>
										        </li>
										<% 
										        }
										    } else { 
										%>
										    <li>게시물이 없습니다.</li>
										<% 
										    } 
										%>
                                    </ul>
                                </div>
                                <p class="button-style2"><a href="../Board/boardList.jsp">자유게시판으로 이동</a></p>
                            </div>
                        </section>
                    </div>

                    <!-- QnA 게시판 미리보기 -->
                    <div class="3u">
                        <section>
                            <div class="mbox-style">
                                <h2 class="title">QnA 게시판</h2>
                                 <div class="content">
                                    <ul>
                                        <% 
										    List<Q_BoardDTO> qnaList = (List<Q_BoardDTO>) request.getAttribute("qnaList");
										    if (qnaList != null && !qnaList.isEmpty()) {
										        for (Q_BoardDTO qnaItem : qnaList) { 
										%>
										        <li>
										            <a href="<%= request.getContextPath() %>/q_board/Q_BVC?q_id=<%= qnaItem.getQ_id() %>&board_type=question">
										                <%= qnaItem.getTitle().replace("\n", "<br>") %>
										            </a>
										        </li>
										<% 
										        }
										    } else { 
										%>
										    <li>게시물이 없습니다.</li>
										<% 
										    } 
										%>
                                    </ul>
                                </div>
                                <p class="button-style2"><a href="../Q_Board/q_boardList.jsp">QnA 게시판으로 이동</a></p>
                            </div>
                        </section>
                    </div>

                    <!-- 자료실 미리보기 -->
                    <div class="3u">
                        <section>
                            <div class="mbox-style">
                                <h2 class="title">자료실</h2>
                                <div class="content">
                                    <ul>
                                        <% 
										    List<BoardDTO> filesList = (List<BoardDTO>) request.getAttribute("filesList");
										    if (freList != null && !freList.isEmpty()) {
										        for (BoardDTO filesItem : filesList) { 
										%>
										        <li>
										            <a href="<%= request.getContextPath() %>/files/BVC?board_id=<%= filesItem.getBoard_id() %>&board_type=files">
										                <%= filesItem.getTitle().replace("\n", "<br>") %>
										            </a>
										        </li>
										<% 
										        }
										    } else { 
										%>
										    <li>게시물이 없습니다.</li>
										<% 
										    } 
										%>
                                    </ul>
                                </div>
                                <p class="button-style2"><a href="../Files/filesList.jsp">자료실로 이동</a></p>
                            </div>
                        </section>
                    </div>
                </div>
            </div>
        </div>

        <!-- 페이지 섹션 (기존 유지) -->
        <div id="page">
            <div class="container">
                <div class="row">
                    <div class="3u">
                        <div id="sidebar2">
                            <!-- Sidebar 2 내용 유지 -->
                        </div>
                    </div>
                    <div class="6u">
                        <div class="skel-cell-important" id="content">
                            <!-- 메인 콘텐츠 내용 유지 -->
                        </div>
                    </div>
                    <div class="3u" id="sidebar1">
                        <!-- Sidebar 1 내용 유지 -->
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

	<div id="footer-wrapper">
		<div class="container">
			<div class="row" id="footer-content">
				<div class="3u" id="box1">
					<section>
						<h2>Maecenas luctus lectus</h2>
						<p>
							<a href="#"><img src="<%= request.getContextPath() %>/images/pics06.jpg" alt=""></a>												</p>
						<p>&nbsp;</p>
						<p>Donec placerat odio vel elit. Nullam ante orci,
							pellentesque eget, tempus quis, ultrices in, est. Curabitur sit
							amet nulla. Nam in massa. Sed vel tellus. Curabitur sem urna,
							consequat.</p>
					</section>
				</div>
				<div class="3u" id="box2">
					<section>
						<h2>Donec dictum metus</h2>
						<ul class="style6">
							<li class="first">
								<h3>Mauris vulputate dolor sit amet</h3>
								<p>
									<a href="#">Donec leo, vivamus fermentum nibh in augue
										praesent a lacus at urna congue rutrum.</a>
								</p>
							</li>
							<li>
								<h3>Fusce ultrices fringilla metus</h3>
								<p>
									<a href="#">Donec leo, vivamus fermentum nibh in augue
										praesent a lacus at urna congue rutrum.</a>
								</p>
							</li>
							<li>
								<h3>Donec dictum metus in sapien</h3>
								<p>
									<a href="#">Donec leo, vivamus fermentum nibh in augue
										praesent a lacus at urna congue rutrum.</a>
								</p>
							</li>
						</ul>
					</section>
				</div>
				<div class="3u" id="box4">
					<section>
						<h2>Nulla luctus eleifend</h2>
						<p>
							<strong>Aliquam erat volutpat. Pellentesque tristique
								ante ut risus. </strong>
						</p>
						<p>&nbsp;</p>
						<p>Quisque dictum. Integer nisl risus, sagittis convallis,
							rutrum id, elementum congue, nibh. Suspendisse dictum porta
							lectus.</p>
						<p>&nbsp;</p>
						<p>Donec placerat odio vel elit. Nullam ante orci,
							pellentesque eget, tempus quis, ultrices in, est. Curabitur sit
							amet nulla. Nam in massa. Sed vel tellus. Curabitur sem urna,
							consequat.</p>
					</section>
				</div>
				<div class="3u" id="box3">
					<section>
						<h2>Contact Information</h2>
						<p>
							<strong>Phone</strong>: +1 234 567 8900
						</p>
						<p>
							<strong>Email</strong>: myemail@myemail.com
						</p>
						<p>&nbsp;</p>
						<p>
							<strong>My CompanyName</strong>
						</p>
						<p>1234 Pellentesque tristique ante risus</p>
						<p>State, Plus Country 1234</p>
					</section>
				</div>
			</div>
		</div>
	</div>
	<div>
		<div class="container" id="copyright">
			Design: <a href="http://templated.co">TEMPLATED</a> Images: <a
				href="http://unsplash.com">Unsplash</a> (<a
				href="http://unsplash.com/cc0">CC0</a>)
		</div>
	</div>
</body>
</html>