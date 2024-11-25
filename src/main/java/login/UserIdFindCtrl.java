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

@WebServlet("/login/UIFC")
public class UserIdFindCtrl extends HttpServlet {
    private static final long serialVersionUID = 1L;
      
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {

        String email = req.getParameter("email");

        // DAO 호출하여 사용자 확인
        MemberDAO dao = new MemberDAO();
        MemberDTO dto = dao.findUserIdByEmail(email); // MemberDTO로 받기

        if (dto != null) {
            String userId = dto.getUser_id(); // 사용자 ID 가져오기
            try {
                MailUtil.sendEmail(email, "아이디 확인", "아이디: " + userId);
                req.setAttribute("message", "아이디가 이메일로 발송되었습니다.");
            } catch (Exception e) {
                req.setAttribute("message", "아이디를 발송하는 중 오류가 발생했습니다.");
                e.printStackTrace();
            }
        } else {
            req.setAttribute("message", "등록된 이메일이 없습니다.");
        }

        RequestDispatcher dispatcher = req.getRequestDispatcher("/Main/main.jsp");
        dispatcher.forward(req, resp);
    }
}