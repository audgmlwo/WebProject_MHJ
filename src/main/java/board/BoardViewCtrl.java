package board;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.CookieManager;
import board.BoardDAO;
import board.BoardDTO;


@WebServlet("/board/BVC")
public class BoardViewCtrl extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	/*
	 서블릿의 수명주기 메서드 중 요청을 받아 get/post 방식을 판단하는 service() 메서드를
	 통해 모든방식의 요청을 처리할 수 있다.*/
	@Override
	protected void service(HttpServletRequest req,
			HttpServletResponse resp) 
					throws ServletException, IOException {
		
			//게시물불러오기
			BoardDAO dao = new BoardDAO();
			
			//파라미터로 전달될 일련번호를 받기
			String board_id = req.getParameter("board_id");
			String board_type = req.getParameter("board_type");
			
			// 조회수 1 증가
			// 조회수 증가 여부 확인
	        String cookieName = "viewed_" + board_id; // 쿠키 이름
	        String isViewed = CookieManager.readCookie(req, cookieName);

	        if (isViewed == null || isViewed.isEmpty()) { // 새로운 조회라면
	            dao.updateVisitCount(board_id, board_type); // 조회수 증가
	            CookieManager.makeCookie(resp, cookieName, "true", 60 * 60 * 1); // 쿠키 생성 (1일 유지)
	        }
			
			//일련번호에 해당하는 게시물을 인출
			BoardDTO dto = dao.selectView(board_id,board_type);
			
			
			dao.close();
			
			
			//줄바꿈처리 : 웹브라우저에서 출력할때는 <br>태그로 변경해야 한다.
			dto.setContent(dto.getContent()
					.replaceAll("\r\n", "<br/>"));
			

			setFileExtension(dto, req);
			
			//게시물(dto) 저장 후 뷰로 포워드
			req.setAttribute("dto", dto);
			req.getRequestDispatcher("/Board/boardView.jsp")
			.forward(req, resp);
			
			String ext = null, fileName = dto.getS_file(), mimeType = null;
			if(fileName!=null) {
				ext =fileName.substring(fileName.lastIndexOf(".")+1);
			}
			
			String[] extArray1 = {"png","jpg","gif","pcx","bmp"};
			String[] extArray2 = {"mp3","wav"};
			String[] extArray3 = {"mp4","avi","wmv"};
			
			if(mimeContains(extArray1, ext)) {
				mimeType = "img";
				
			}
			else if(mimeContains(extArray2, ext)) {
				mimeType = "audio";
			}
			else if(mimeContains(extArray3, ext)) {
				mimeType = "video";
			}
			System.out.println("MIME타입="+mimeType);
			req.setAttribute("mimeType", mimeType);
			
	}
	
		public boolean mimeContains(String[] strArr, String ext) {
			  boolean retValue = false;
			  for(String s: strArr) {
				  if(s.equalsIgnoreCase(ext))
					  retValue = true;
			  }
			  return retValue;
			
		}
	
	  public void setFileExtension(BoardDTO dto, HttpServletRequest req) {
	        String o_file = dto.getO_file();

	        if (o_file != null && o_file.contains(".")) {
	            // 확장자 추출
	            String extension = o_file.substring(o_file.lastIndexOf(".") + 1).toLowerCase();
	            // 확장자를 request 속성에 저장
	            req.setAttribute("fileExtension", extension);
	        } else {
	            req.setAttribute("fileExtension", ""); // 확장자가 없을 때 빈 문자열 설정
	    }
	}	

}