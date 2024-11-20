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
        String q_id = req.getParameter("q_id");

        Q_BoardDAO questionDao = new Q_BoardDAO();
        NA_BoardDAO answerDao = new NA_BoardDAO();

        Q_BoardDTO question = questionDao.selectView(q_id);
        List<NA_BoardDTO> answers = answerDao.getAnswersByQuestionId(q_id);

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