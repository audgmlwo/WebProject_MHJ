package q_board;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.JSFunction;


@WebServlet("/q_board/BWC")
@MultipartConfig(
    maxFileSize = 1024 * 1024 * 50,  // 최대 파일 크기: 50MB
    maxRequestSize = 1024 * 1024 * 100  // 최대 요청 크기: 100MB
)
public class Q_BoardWriteCtrl extends HttpServlet {
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

        // 쓰기 페이지로 이동
        req.getRequestDispatcher("/Q_Board/q_boardWrite.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, 
            HttpServletResponse resp) 
            throws ServletException, IOException {
        HttpSession session = req.getSession();

        // 로그인 확인
        String userId = (String) session.getAttribute("UserId");
        if (userId == null || userId.isEmpty()) {
            JSFunction.alertLocation(resp, "로그인 후 이용해주세요.", "../Login/LoginForm.jsp");
            return;
        }


        // DTO에 데이터 설정
        Q_BoardDTO dto = new Q_BoardDTO();
        dto.setUser_id(userId); // 작성자 아이디
        dto.setTitle(req.getParameter("title")); // 제목
        dto.setContent(req.getParameter("content")); // 내용
         

        // DAO 처리
        Q_BoardDAO dao = null;
        try {
        	
            dao = new Q_BoardDAO();
            
            int result = dao.insertWrite(dto);

            if (result == 1) {
                resp.sendRedirect("../q_board/BLPC"); // 성공 시 해당 게시판 목록으로 이동
            } else {
                JSFunction.alertLocation(resp, "글쓰기에 실패했습니다.", "../Q_Board/q_boardWrite.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JSFunction.alertLocation(resp, "서버 오류로 글쓰기에 실패했습니다.", "../Q_Board/q_boardWrite.jsp");
        } finally {
            if (dao != null) {
                try {
                    dao.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
}

