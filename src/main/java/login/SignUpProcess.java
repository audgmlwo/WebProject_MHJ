package login;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/Login/SignUpProcess")

public class SignUpProcess extends HttpServlet {
    protected void doPost(HttpServletRequest request,
    		HttpServletResponse response) 
    				throws ServletException, IOException {
    	
        // 입력 데이터 수집
        String userId = request.getParameter("user_id");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        MemberDAO dao = new MemberDAO();

        // 데이터베이스 중복 확인
        int checkUserResult = dao.UserCheck(userId);
        int checkNameResult = dao.UserCheck(name);
        int checkEmailResult = dao.UserCheck(email);

        // 에러 메시지를 저장할 컬렉션
        Map<String, String> errors = new HashMap<>();

        // 유효성 검사 로직
        if (userId == null || userId.length() < 4 || userId.length() > 12 || !userId.matches("^[a-zA-Z0-9]+$")) {
            errors.put("user_id", "아이디는 4~12자의 영문 소문자와 숫자만 가능합니다.");
        } else if (checkUserResult == 1) {
            errors.put("user_id", "이미 사용 중인 아이디입니다.");
        }

        if (password == null || !password.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,16}$")) {
            errors.put("password", "비밀번호는 6~16자의 영문, 숫자, 특수문자를 포함해야 합니다.");
        }
        if (!password.equals(confirmPassword)) {
            errors.put("confirm_password", "비밀번호가 일치하지 않습니다.");
        }

        if (name == null || name.length() < 2 || name.length() > 15) {
            errors.put("name", "이름은 2~15자 사이로 입력해 주세요.");
        } else if (checkNameResult == 1) {
            errors.put("name", "이미 사용 중인 이름(닉네임)입니다.");
        }

        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            errors.put("email", "올바른 이메일 형식을 입력해 주세요.");
        } else if (checkEmailResult == 1) {
            errors.put("email", "이미 사용 중인 이메일입니다.");
        }

        // 에러가 있는 경우 다시 폼으로 전달
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("SignUp.jsp").forward(request, response);
            return;
        }
        
        // 성공 처리 로직
        MemberDTO newMember = new MemberDTO();
        newMember.setUser_id(userId);
        newMember.setPass(password);
        newMember.setName(name);
        newMember.setEmail(email);

        dao.insertMember(newMember);

        // 성공 페이지로 리다이렉트
        response.sendRedirect("LoginForm.jsp");
    }
}