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
        String q_id = req.getParameter("q_id");

        // q_id 유효성 검사
        if (q_id == null || q_id.isEmpty()) {
            JSFunction.alertBack(resp, "유효한 q_id가 필요합니다.");
            return;
        }

        req.setAttribute("q_id", q_id); // JSP로 전달
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

        // 클라이언트에서 데이터 가져오기
        String q_id = req.getParameter("q_id");
        String content = req.getParameter("content");

        String errorMessage = null;

        if (q_id == null || q_id.isEmpty()) {
            errorMessage = "ID 값이 유효하지 않습니다. (값이 비어 있거나 없습니다)";
        } else if (content == null || content.isEmpty()) {
            errorMessage = "내용(content)이 유효하지 않습니다. (값이 비어 있거나 없습니다)";
        } else if (userId == null || userId.isEmpty()) {
            errorMessage = "유저(userId)이 유효하지 않습니다. (값이 비어 있거나 없습니다)";
        
        }

        if (errorMessage != null) {
            JSFunction.alertBack(resp, errorMessage);
            return;
        }

        // DTO 설정
        NA_BoardDTO dto = new NA_BoardDTO();
        dto.setQ_id(Integer.parseInt(q_id));
        dto.setUser_id(userId);
        dto.setContent(content);

        // DAO를 통한 데이터 저장
        NA_BoardDAO dao = new NA_BoardDAO();
        int result = 0;

        try {
            result = dao.insertWrite(dto);
        } catch (Exception e) {
            e.printStackTrace();
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