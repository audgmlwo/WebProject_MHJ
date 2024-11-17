package login;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Login/UpdateAccountProcess")
public class UpdateAccountProcess extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 입력 데이터 수집
        String userId = req.getParameter("user_id");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirm_password");
        String name = req.getParameter("name");
        String email = req.getParameter("email");

        // 데이터베이스 작업 객체 생성
        MemberDAO dao = new MemberDAO();

        // 사용자 정보 가져오기
        MemberDTO currentUser = dao.getMember(userId);

        // 사용자가 존재하지 않을 경우 로그인 페이지로 리다이렉트
        if (currentUser == null) {
            resp.sendRedirect("/WebProject_MHJ/Login/LoginForm.jsp");
            return;
        }

        // 에러 메시지를 저장할 컬렉션
        Map<String, String> errors = new HashMap<>();

        // 유효성 검사 로직
        if (name == null || name.length() < 2 || name.length() > 15) {
            errors.put("name", "이름은 2~15자 사이로 입력해 주세요.");
        } else if (dao.UserCheck(name) == 1 && !name.equals(currentUser.getName())) {
            errors.put("name", "이미 사용 중인 이름(닉네임)입니다.");
        }

        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            errors.put("email", "올바른 이메일 형식을 입력해 주세요.");
        } else if (dao.UserCheck(email) == 1 && !email.equals(currentUser.getEmail())) {
            errors.put("email", "이미 사용 중인 이메일입니다.");
        }

        if (password != null && !password.isEmpty()) {
            if (!password.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,16}$")) {
                errors.put("password", "비밀번호는 6~16자의 영문, 숫자, 특수문자를 포함해야 합니다.");
            }
            if (!password.equals(confirmPassword)) {
                errors.put("confirm_password", "비밀번호가 일치하지 않습니다.");
            }
        }

        // 에러가 있는 경우 다시 폼으로 전달
        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.setAttribute("currentUser", currentUser);
            req.getRequestDispatcher("/Login/UpdateAccount.jsp").forward(req, resp);
            return; // 추가 처리 중단
        }

        // 성공 처리 로직
        try {
            MemberDTO updatedUser = new MemberDTO();
            updatedUser.setUser_id(userId);
            updatedUser.setName(name);
            updatedUser.setEmail(email);

            // 비밀번호는 선택적으로 업데이트
            if (password != null && !password.isEmpty()) {
                updatedUser.setPass(password);
            }

            // 데이터베이스 업데이트
            dao.updateMember(updatedUser);

            // 성공 메시지와 함께 업데이트 페이지로 리다이렉트
            String encodedMessage = URLEncoder.encode("회원 정보가 성공적으로 수정되었습니다.", StandardCharsets.UTF_8.toString());
            resp.sendRedirect("/WebProject_MHJ/Login/LoginForm.jsp?successMessage=" + encodedMessage);
            

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "회원 정보 수정 중 오류가 발생했습니다.");
            req.getRequestDispatcher("/Login/UpdateAccount.jsp").forward(req, resp);
        }
    }
}
