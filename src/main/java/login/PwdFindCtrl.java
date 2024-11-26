package login;

import java.io.IOException;
import java.util.UUID;

import common.MailUtil;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.JSFunction;

@WebServlet("/login/PFC")
public class PwdFindCtrl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    
    @Override
    protected void doGet(HttpServletRequest req, 
    		HttpServletResponse resp) throws ServletException, 
    IOException {
        HttpSession session = req.getSession();
        
        // 로그인 확인
        if (session.getAttribute("UserId") == null) {
            // 이전 요청 URL을 세션에 저장
            session.setAttribute("redirectAfterLogin", req.getRequestURI());
            JSFunction.alertLocation(resp, "로그인 후 이용해주세요.", "../Login/LoginForm.jsp");
            return;
        }
        
        MemberDTO dto = new MemberDTO();
        
     // 세션에 사용자 정보 저장
        session.setAttribute("UserId", dto.getUser_id());

     // 결과 페이지로 포워딩
        RequestDispatcher dispatcher = req.getRequestDispatcher("/Login/PwdResult.jsp");
        dispatcher.forward(req, resp);
        
    }

    private String generateTemporaryPassword() {
        // UUID를 사용해 임시 비밀번호 생성
        return UUID.randomUUID().toString().substring(0, 8); // 8자리 임시 비밀번호
    }
    
         
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
    	
    	
        HttpSession session = req.getSession();
        String email = req.getParameter("email");
        String UserId = req.getParameter("user_id");

        // DAO 호출하여 사용자 확인
        MemberDAO dao = new MemberDAO();
        MemberDTO dto = dao.findByEandI(email,UserId);

        if (dto != null) {
            // 임시 비밀번호 생성
            String tempPassword = generateTemporaryPassword();

            // 비밀번호 업데이트
            dao.updatePassword(dto.getUser_id(), tempPassword);
            
        	

            // 이메일 발송
            try {
                MailUtil.sendEmail(email, "비밀번호 재설정", "임시 비밀번호: " + tempPassword);
                req.setAttribute("message", "임시 비밀번호가 이메일로 발송되었습니다.");
            } catch (Exception e) {
                req.setAttribute("message", "임시 비밀번호를 발송하는 중 오류가 발생했습니다.");
                e.printStackTrace();
            }
        } else {
            // 사용자 없음 메시지 전달
            req.setAttribute("message", "등록된 이메일이 없습니다.");
        }

        // 로그인 확인: 결과 페이지 포워딩 전에 로그인 여부 확인
        if (session.getAttribute("userId") == null) {
            // 이전 요청 URL을 세션에 저장
            session.setAttribute("redirectAfterLogin", req.getRequestURI());
            JSFunction.alertLocation(resp, "로그인 후 이용해주세요.", "../Login/LoginForm.jsp");
            return;
        }

    }
}
