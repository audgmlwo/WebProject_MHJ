package board;

import java.io.IOException;

import fileupload.FileUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.JSFunction;

@WebServlet("/board/BEC")

@MultipartConfig(
		maxFileSize = 1024*1024*50,
		maxRequestSize = 1024*1024*100
	)

public class BoardEditCtrl extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req,
			HttpServletResponse resp) 
					throws ServletException, IOException {
	//로그인 확인
	HttpSession session = req.getSession();
	
	// 로그인 확인
    if (session.getAttribute("UserId") == null) {
        // 이전 요청 URL을 세션에 저장
        session.setAttribute("redirectAfterLogin", req.getRequestURI());
        JSFunction.alertLocation(resp, "로그인 후 이용해주세요.", "../Login/LoginForm.jsp");
        return;
    }
	
	//게시물 얻어오기 : '열람' 에서 사용했던 메서드를 그대로 호출
    
	String board_id = req.getParameter("board_id");
	String board_type = req.getParameter("board_type");
	
	BoardDAO dao = new BoardDAO();
	BoardDTO dto = dao.selectView(board_id, board_type);
	
	//작성자 본인 확인 : DTO에 저장된 id와 로그인 아이디를 비교
	if(!dto.getUser_id().equals(session.getAttribute("UserId").toString())) {
		
		//작성자 본인이 아니라면 경고창을 띄운 후 뒤로 이동한다.
		JSFunction.alertBack(resp, "작성자 본인만 수정할 수 있습니다.");
		return;
	}
	
	//작성자 본인이라면 request 영역에 DTO를 저장한 후 포워드한다.
	req.setAttribute("dto", dto);
	req.getRequestDispatcher("/Board/boardEdit.jsp")
	.forward(req, resp);
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
		
		//파일업로드 
		//업로드 디렉토리의 물리적 경로 확인.
		String saveDirectory = req.getServletContext()
				.getRealPath("/Uploads");
		
		//1.파일 업로드  ============================================================
		String originalFileName = "";
		try {
			//작성폼에서 전송한 파일을 업로드 처리
			originalFileName = FileUtil.uploadFile(req, saveDirectory);
			
		} 
		catch (Exception e) {
			//문제가 있는 경우 예외처리
			JSFunction.alertBack(resp, "파일 업로드 오류입니다.");
			
			return;
		}
		//2. 파일 업로드 외 처리
		//수정 내용을 매개변수에 얻어옴.
		
		//수정할 게시물의 일련번호
		String board_id = req.getParameter("board_id"); 
		int board_id_int = Integer.parseInt(board_id);
		String board_type = req.getParameter("board_type"); 
		
		//기존에 입력된 파일정보(원본명, 저장된파일명)
		String prevO_file = req.getParameter("prevO_file"); 
		String prevS_file = req.getParameter("prevS_file"); 
		
		//사용자가 수정한 제목
		String title = req.getParameter("title");
		
		//사용자가 수정한 내용
		String content = req.getParameter("content");
		
		//수정시간
		java.sql.Date updated_date_date = new java.sql.Date(System.currentTimeMillis());
		
		// DTO에 저장
		BoardDTO dto = new BoardDTO();
		
		//파일을 제외한 나머지 폼값을 먼저 저장
		dto.setBoard_id(board_id_int);
		dto.setBoard_type(board_type);
		
		//특히 아이디는 session에 저장된 내용으로 추가
		dto.setUser_id(session.getAttribute("UserId").toString());
		dto.setTitle(title);
		dto.setContent(content);
		dto.setUpdated_date(updated_date_date);
		
		//원본 파일명과 저장된 파일 이름 설정
		if (originalFileName !="") {
			
			//새로운 파일을 업로드하는 경우에는 서버에 저장된 파일명을 변경한다.
			String savedFileName = FileUtil.renameFile(saveDirectory, originalFileName);
			
			//파일정보를 DTO에 저장
			dto.setO_file(originalFileName); //원래 파일 이름
			dto.setS_file(savedFileName); // 서버에 저장된 파일 이름
			
			//기존 파일 삭제
			FileUtil.deleteFile(req, "/Uploads", prevS_file);
			
		}
		else {
			//첨부 파일이 없으면 기존 이름 유지(hidden 입력상자에 설정한 내용)
			dto.setO_file(prevO_file);
			dto.setS_file(prevS_file);
		}
		//DB에 수정 내용 반영
		BoardDAO dao = new BoardDAO();
		int result = dao.updatePost(dto);
		dao.close();
		
		//성공 or 실패?
		if (result==1) { //수정성공
			
			//수정에 성공하면 '열람'페이지로 이동해서 수정된 내용을 확인한다.
			resp.sendRedirect("../board/BVC?board_id=" + board_id + "&board_type=" + board_type);
		}
		else { //수정실패
			
			//수정에 실패하면 경고창을 띄운다.
			JSFunction.alertLocation(resp, "수정에 실패했습니다. 다시 시도해주세요.", 
					"../board/BVC?board_id=" + board_id + "&board_type=" + board_type);
		}
	}
}















