package na_board;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.JSFunction;

@WebServlet("/na_board/NA_BWC")
public class NA_BoardWriteCtrl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 답변 작성 페이지로 이동 (GET 요청)
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();

        // 요청에서 q_id 가져오기
        String q_id = req.getParameter("q_id");

        // q_id 유효성 검증
        if (q_id == null || q_id.isEmpty()) {
            JSFunction.alertBack(resp, "유효한 q_id가 필요합니다.");
            return;
        }
        
        // q_id 세션에 저장
        session.setAttribute("q_id", q_id);

        // 로그인 확인
        if (session.getAttribute("UserId") == null) {
            // 로그인 후 리디렉션할 URL을 세션에 저장
            String originalUrl = req.getRequestURI() + (req.getQueryString() != null ? "?" + req.getQueryString() : "");
            session.setAttribute("redirectAfterLogin", originalUrl);

            // 로그인 페이지로 이동
            JSFunction.alertLocation(resp, "로그인 후 이용해주세요.", "../Login/LoginForm.jsp");
            return;
        }

        // 세션에서 q_id 다시 확인 (로그인 후 작업에 필요)
        String q_id_2 = (String) session.getAttribute("q_id");

        if (q_id_2 == null || q_id_2.isEmpty()) {
            JSFunction.alertBack(resp, "유효한 q_id가 필요합니다.");
            return;
        }

        // 정상적인 요청 처리
        req.getRequestDispatcher("/NA_Board/na_boardWrite.jsp").forward(req, resp);
    }

    // 답변 저장 처리 (POST 요청)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();

        // 로그인 확인
        String userId = (String) session.getAttribute("UserId");
        if (userId == null || userId.isEmpty()) {
            JSFunction.alertLocation(resp, "로그인 후 이용해주세요.", "../Login/LoginForm.jsp");
            return;
        }

        // 요청 데이터 가져오기
        String q_id = req.getParameter("q_id");
        String content = req.getParameter("content");

        if (q_id == null || q_id.isEmpty()) {
            JSFunction.alertBack(resp, "유효한 q_id가 필요합니다.");
            return;
        }
        if (content == null || content.isEmpty()) {
            JSFunction.alertBack(resp, "내용을 입력하세요.");
            return;
        }

        // DTO 설정
        NA_BoardDTO dto = new NA_BoardDTO();
        dto.setQ_id(Integer.parseInt(q_id));
        dto.setUser_id(userId);
        dto.setContent(content);

        // DAO를 통해 데이터 저장
        NA_BoardDAO dao = new NA_BoardDAO();
        int result = 0;
        try {
            result = dao.insertWrite(dto);
        } catch (Exception e) {
            e.printStackTrace();
            JSFunction.alertBack(resp, "서버 처리 중 오류가 발생했습니다.");
            return;
        } finally {
            dao.close();
        }

        // 결과 처리
        if (result > 0) {
            resp.sendRedirect(req.getContextPath() + "/q_board/Q_BVC?q_id=" + q_id);
        } else {
            JSFunction.alertBack(resp, "답변 저장에 실패했습니다.");
        }
    }

}