package board;

import java.io.IOException;

import fileupload.FileUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/board/BDC")
public class BoardDownloadCtrl extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req,
			HttpServletResponse resp) 
					throws ServletException, IOException {
					String o_file = req.getParameter("o_file");
		 			String s_file = req.getParameter("s_file");
		 			String board_id = req.getParameter("board_id");
		 			String board_type = req.getParameter("board_type");
		 			
		 			
		 			FileUtil.download(req, resp, "/Uploads", s_file, o_file, board_id, board_type);
		 			
		 			BoardDAO dao = new BoardDAO();
		 			dao.downCountPlus(board_id, board_type);
		 			dao.close();
	}
}
