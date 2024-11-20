package na_board;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/na_board/NA_BWC")
public class NA_BoardWriteCtrl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
    protected void doPost(HttpServletRequest req,
    		HttpServletResponse resp) 
    				throws ServletException, IOException {
    	
        String q_id = req.getParameter("q_id");
        String content = req.getParameter("content");
        String user_id = (String) req.getSession().getAttribute("user_id");

        if (q_id == null || content == null || user_id == null) {
            resp.sendRedirect(req.getContextPath() + "/q_board/view.jsp?q_id=" + q_id);
            return;
        }

        NA_BoardDTO dto = new NA_BoardDTO();
        dto.setQ_id(Integer.parseInt(q_id)); // 질문 ID를 설정
        dto.setUser_id(user_id);
        dto.setContent(content);

        NA_BoardDAO dao = new NA_BoardDAO();
        dao.insertWrite(dto);

        resp.sendRedirect(req.getContextPath() + "/q_board/view.jsp?q_id=" + q_id);
    }
}