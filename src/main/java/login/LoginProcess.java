package login;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.CookieManager;

@WebServlet("/login/loginprocess")
public class LoginProcess extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
        // 클라이언트로부터 전달받은 파라미터
        String userId = request.getParameter("user_id");
        String userPw = request.getParameter("user_pw");

        // DAO를 사용하여 사용자 인증
        MemberDAO dao = new MemberDAO();
        MemberDTO memberDTO = dao.getMemberDTO(userId, userPw);
        dao.close();

        // 로그인 결과 처리
        if (memberDTO != null && memberDTO.getUser_id() != null) { 
        	
            HttpSession session = request.getSession();
            
            session.setAttribute("UserId", memberDTO.getUser_id());
            session.setAttribute("UserName", memberDTO.getName());
            
            CookieManager.makeCookie(response, "UserId", memberDTO.getUser_id(), 60 * 60 * 1); // 7일 동안 유지
            
            // 이전 페이지로 리다이렉트
            String redirectURL = (String) session.getAttribute("redirectAfterLogin");
            if (redirectURL != null) {
                session.removeAttribute("redirectAfterLogin");
                response.sendRedirect(redirectURL); // 이전 페이지로 이동
            } else {
                response.sendRedirect("/WebProject_MHJ/Login/LoginForm.jsp"); // 기본 로그인 성공 페이지
            }
        } else {
            // 로그인 실패 처리
            request.setAttribute("LoginErrMsg", "로그인 오류입니다.");
            request.getRequestDispatcher("LoginForm.jsp").forward(request, response);
        }
    }
}
