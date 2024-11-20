package q_board;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.JSFunction;

//게시물 삭제 처리
@WebServlet("/q_board/Q_BDEC")
public class Q_BoardDeleteCtrl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
	        throws ServletException, IOException {
		
	    HttpSession session = req.getSession();

	    // 로그인 확인
	    if (session.getAttribute("UserId") == null) {
	        session.setAttribute("redirectAfterLogin", req.getRequestURI());
	        resp.sendRedirect(req.getContextPath() + "/Login/LoginForm.jsp");
	        return;
	    }

	    // board_id와 board_type 얻기
	    String q_id = req.getParameter("q_id");
	   

	    // DAO 호출
	    Q_BoardDAO dao = new Q_BoardDAO();
	    Q_BoardDTO dto = dao.selectView(q_id);

	    // 게시물이 존재하지 않을 경우 처리
	    if (dto == null) {
	        resp.sendRedirect(req.getContextPath() + "/Q_Board/q_boardView.jsp");
	        return;
	    }

	    // 작성자 본인 확인
	    if (!dto.getUser_id().equals(session.getAttribute("UserId").toString())) {
	        JSFunction.alertBack(resp, "작성자 본인만 수정할 수 있습니다.");
	        return;
	    }
	    
		//게시물 삭제
		
		int result = dao.deletePost(q_id);
		
		dao.close();
		
		
		//삭제가 완료되면 목록으로 이동한다.
	
		JSFunction.alertLocation(resp, "삭제되었습니다.", "../q_board/Q_BLPC");
	}
}














