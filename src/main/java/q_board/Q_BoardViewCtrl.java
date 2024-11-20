package q_board;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import na_board.NA_BoardDAO;
import na_board.NA_BoardDTO;



@WebServlet("/q_board/Q_BVC")
public class Q_BoardViewCtrl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {

        // 게시물 불러오기
        Q_BoardDAO dao = new Q_BoardDAO();
        NA_BoardDAO answerDao = new NA_BoardDAO(); // 답변 DAO 추가

        // 파라미터로 전달될 일련번호를 받기
        String q_id = req.getParameter("q_id");

        // 조회수 1 증가
        dao.updateVisitCount(q_id);

        // 일련번호에 해당하는 게시물을 인출
        Q_BoardDTO dto = dao.selectView(q_id);

        // 해당 질문의 답변 리스트를 조회
        List<NA_BoardDTO> answers = answerDao.getAnswersByQuestionId(q_id);

        // DAO 연결 종료
        dao.close();
        answerDao.close();

        // 줄바꿈 처리: 웹브라우저에서 출력할 때 <br> 태그로 변경
        if (dto != null) {
            dto.setContent(dto.getContent().replaceAll("\r\n", "<br/>"));
            dto.setAnswers(answers); // 답변 리스트를 설정
        }

        // 게시물(dto) 저장 후 뷰로 포워드
        req.setAttribute("dto", dto);
        req.getRequestDispatcher("/Q_Board/q_boardView.jsp").forward(req, resp);
    }
}



