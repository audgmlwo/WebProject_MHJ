package login;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.CookieManager;

@WebServlet("/login/CPC")
public class ChangePwdCtrl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        
        HttpSession session = req.getSession();

        // 1. 세션에서 UserId 가져오기
        String userId = (String) session.getAttribute("UserId");

        // 2. 세션에서 UserId가 없을 경우, 쿠키에서 UserId 가져오기
        if (userId == null) {
            userId = CookieManager.readCookie(req, "UserId");
            if (userId != null) {
                session.setAttribute("UserId", userId); // 세션에 다시 저장
            }
        }

        // 3. UserId가 여전히 없으면 로그인 화면으로 리다이렉트
        if (userId == null) {
            System.out.println("세션과 쿠키 모두에서 사용자 ID를 찾을 수 없습니다.");
            resp.sendRedirect(req.getContextPath() + "/Login/LoginForm.jsp");
            return;
        }

        System.out.println("ChangePwdCtrl - UserId: " + userId);

        // 이후 비밀번호 변경 로직
        String currentPassword = req.getParameter("tempPassword");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirm_password");

        // 에러 메시지를 저장할 변수
        String error = null;

        // 현재 비밀번호 확인
        MemberDAO dao = new MemberDAO();
        MemberDTO dto = dao.findById(userId); // DB에서 사용자 정보 가져오기

        if (!currentPassword.equals(dto.getPass())) {
            error = "현재 비밀번호가 올바르지 않습니다.";
        }

        // 새 비밀번호와 확인 비밀번호가 일치하는지 확인
        if (error == null && !newPassword.equals(confirmPassword)) {
            error = "새 비밀번호가 서로 일치하지 않습니다.";
        }

        // 비밀번호 정책 검증
        if (error == null && (newPassword == null || 
                !newPassword.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,16}$"))) {
            error = "비밀번호는 6~16자의 영문, 숫자, 특수문자를 포함해야 합니다.";
        }

        // 에러가 있으면 다시 폼으로 이동
        if (error != null) {
            req.setAttribute("error", error);
            req.getRequestDispatcher("Login/ChangePwdFind.jsp").forward(req, resp);
            return;
        }

        // 비밀번호 업데이트
        dao.updatePassword(userId, newPassword);
        dao.close();

     // 세션 무효화 및 리다이렉트
        session.invalidate();

        try {
            String message = "비밀번호가 성공적으로 변경되었습니다.";
            String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8.toString());
            resp.sendRedirect(req.getContextPath() + "/Main/main.jsp?message=" + encodedMessage);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/Login/LoginForm.jsp");
        }
    }
}
