package q_board;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likes.LikeDAO;
import na_board.NA_BoardDAO;
import na_board.NA_BoardDTO;
import utils.CookieManager;


@WebServlet("/q_board/Q_BVC")
public class Q_BoardViewCtrl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
    	
        String q_id = req.getParameter("q_id");

        Q_BoardDAO questionDao = new Q_BoardDAO();
        NA_BoardDAO answerDao = new NA_BoardDAO();
        LikeDAO likeDAO = new LikeDAO(); // 좋아요 DAO 추가
        
        // 조회수 1 증가
		// 조회수 증가 여부 확인
        String cookieName = "viewed_" + q_id; // 쿠키 이름
        String isViewed = CookieManager.readCookie(req, cookieName);

        if (isViewed == null || isViewed.isEmpty()) { // 새로운 조회라면
        	questionDao.updateVisitCount(q_id); // 조회수 증가
            CookieManager.makeCookie(resp, cookieName, "true", 60 * 60 * 1); // 쿠키 생성 (1일 유지)
        }
        
        Q_BoardDTO question = questionDao.selectView(q_id);
        List<NA_BoardDTO> answers = answerDao.getAnswersByQuestionId(q_id);
        
        String board_type = "question";
        
        
        // 좋아요 수 조회
        int likeCount = likeDAO.getLikeCount(board_type, Integer.parseInt(q_id));
        req.setAttribute("likeCount", likeCount);
        
        questionDao.close();
        answerDao.close();

        System.out.println("Question: " + question);
        System.out.println("Answers: " + answers);

        if (question != null) {
            question.setAnswers(answers);
        }

        req.setAttribute("dto", question);
        req.getRequestDispatcher("/Q_Board/q_boardView.jsp").forward(req, resp);
    }
}