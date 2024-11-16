<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="login.MemberDAO" %>
<%@ page import="login.MemberDTO" %>
<%
    // 클라이언트에서 전송된 데이터 가져오기
    String userId = request.getParameter("user_id");
    String password = request.getParameter("password");
    String confirmPassword = request.getParameter("confirm_password");
    String name = request.getParameter("name");
    String email = request.getParameter("email");

    // 유효성 검사
    if (userId == null || password == null || confirmPassword == null || name == null || email == null ||
        userId.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty() || email.isEmpty()) {
        request.setAttribute("errorMessage", "모든 필드를 입력해 주세요.");
        request.getRequestDispatcher("Sign_up.jsp").forward(request, response);
        return;
    }

    if (!password.equals(confirmPassword)) {
        request.setAttribute("errorMessage", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        request.getRequestDispatcher("Sign_up.jsp").forward(request, response);
        return;
    }

    // DAO 객체를 이용한 회원가입 처리
    MemberDAO dao = new MemberDAO();
    MemberDTO dto = new MemberDTO();
    
    dto.setUser_id(userId);
    dto.setPass(password);
    dto.setName(name);
    dto.setEmail(email);

    int result = dao.insertMember(dto);
    dao.close();

    if (result > 0) {
        // 회원가입 성공
        response.sendRedirect("LoginForm.jsp");
    } else {
        // 회원가입 실패
        request.setAttribute("errorMessage", "회원가입에 실패했습니다. 다시 시도해 주세요.");
        request.getRequestDispatcher("Sign_up.jsp").forward(request, response);
    }
%>