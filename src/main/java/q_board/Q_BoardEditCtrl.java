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

@WebServlet("/q_board/Q_BEC")

@MultipartConfig(
		maxFileSize = 1024*1024*50,
		maxRequestSize = 1024*1024*100
	)

public class Q_BoardEditCtrl extends HttpServlet{
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

	  
	    // 작성자 본인 확인
	    if (!dto.getUser_id().equals(session.getAttribute("UserId").toString())) {
	        JSFunction.alertBack(resp, "작성자 본인만 수정할 수 있습니다.");
	        return;
	    }

	    // 데이터 전달
	    req.setAttribute("dto", dto);
	    req.getRequestDispatcher("/Q_Board/q_boardEdit.jsp").forward(req, resp);
	}
	
	
	//수정처리
	@Override
	protected void doPost(HttpServletRequest req,
			HttpServletResponse resp) 
					throws ServletException, IOException {
		//로그인 확인
		HttpSession session = req.getSession();
		
		//로그인 전이라면 로그인 페이지로 이동한다.
		if(session.getAttribute("UserId")==null) {
			
			JSFunction.alertLocation(resp, "로그인 후 이용해주세요",
					"../Login/LoginForm.jsp");
			return;
		}
		
		//작성자 본인 확인 : 수정폼의 hidden 속성으로 추가한 내용으로 비교
		if(!req.getParameter("user_id").equals(session.getAttribute("UserId").toString())) {
			JSFunction.alertBack(resp, "작성자 본인만 수정할 수 있습니다.");
			return;
		}	
				
		// 파일 업로드 외 처리
		//수정 내용을 매개변수에 얻어옴.
		
		//수정할 게시물의 일련번호
		String q_id = req.getParameter("q_id"); 
		int q_id_int = Integer.parseInt(q_id);
		
		//사용자가 수정한 제목
		String title = req.getParameter("title");
		
		//사용자가 수정한 내용
		String content = req.getParameter("content");
		
		//수정시간
		java.sql.Date updated_date_date = new java.sql.Date(System.currentTimeMillis());
		
		// DTO에 저장
		Q_BoardDTO dto = new Q_BoardDTO();
		
		//파일을 제외한 나머지 폼값을 먼저 저장
		dto.setQ_id(q_id_int);
		
		//특히 아이디는 session에 저장된 내용으로 추가
		dto.setUser_id(session.getAttribute("UserId").toString());
		dto.setTitle(title);
		dto.setContent(content);
		dto.setUpdated_date(updated_date_date);
		
		
		//DB에 수정 내용 반영
		Q_BoardDAO dao = new Q_BoardDAO();
		int result = dao.updatePost(dto);
		dao.close();
		
		//성공 or 실패?
		if (result==1) { //수정성공
			
			//수정에 성공하면 '열람'페이지로 이동해서 수정된 내용을 확인한다.
			resp.sendRedirect("../q_board/Q_BVC?q_id=" + q_id );
		}
		else { //수정실패
			
			//수정에 실패하면 경고창을 띄운다.
			JSFunction.alertLocation(resp, "수정에 실패했습니다. 다시 시도해주세요.", 
					"../q_board/Q_BVC?q_id=" + q_id);
		}
	}
}















