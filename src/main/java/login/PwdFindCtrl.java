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

@WebServlet("/login/PFC")
public class PwdFindCtrl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest request,
    		HttpServletResponse response) 
            throws ServletException, IOException {
        
        String email = request.getParameter("email");

        // DAO 호출하여 사용자 확인
        MemberDAO dao = new MemberDAO();
        MemberDTO user = dao.findByEmail(email); // MemberDTO를 반환하도록 수정

        if (user != null) {
            // 임시 비밀번호 생성
            String tempPassword = generateTemporaryPassword();

            // 비밀번호 업데이트
            dao.updatePassword(user.getUser_id(), tempPassword);

            // 이메일 발송
            try {
                MailUtil.sendEmail(email, "비밀번호 재설정", "임시 비밀번호: " + tempPassword);
                request.setAttribute("message", "임시 비밀번호가 이메일로 발송되었습니다.");
            } catch (Exception e) {
                request.setAttribute("message", "임시 비밀번호를 발송하는 중 오류가 발생했습니다.");
                e.printStackTrace();
            }
        } else {
            // 사용자 없음 메시지 전달
            request.setAttribute("message", "등록된 이메일이 없습니다.");
        }

        // 결과 페이지로 포워딩
        RequestDispatcher dispatcher = request.getRequestDispatcher("/Login/PwdResult.jsp");
        dispatcher.forward(request, response);
    }

    private String generateTemporaryPassword() {
        // UUID를 사용해 임시 비밀번호 생성
        return UUID.randomUUID().toString().substring(0, 8); // 8자리 임시 비밀번호
    }
}