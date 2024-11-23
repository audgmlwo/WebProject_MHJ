package main;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import board.BoardDAO;
import board.BoardDTO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import q_board.Q_BoardDAO;
import q_board.Q_BoardDTO;

@WebServlet("/main/MLC")
public class MainListCrtl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        BoardDAO freDao = null;
        Q_BoardDAO qnaDao = null;
        BoardDAO filesDao = null;

        try {
            // DAO 객체 생성
            freDao = new BoardDAO();
            qnaDao = new Q_BoardDAO();
            filesDao = new BoardDAO();

            // 자유게시판 데이터 가져오기
            List<BoardDTO> freList = freDao.selectListPageBoard(Map.of(
                "start", "1",
                "end", "10"
            ));

            // QnA 데이터 가져오기
            List<Q_BoardDTO> qnaList = qnaDao.selectListPageBoard(Map.of(
                "start", "1",
                "end", "10"
            ));

            // 자료실 데이터 가져오기
            List<BoardDTO> filesList = filesDao.selectListPageFiles(Map.of(
                "start", "1",
                "end", "10"
            ));

            // JSP로 데이터 전달
            req.setAttribute("freList", freList);
            req.setAttribute("qnaList", qnaList);
            req.setAttribute("filesList", filesList);

            // JSP로 포워딩
            req.getRequestDispatcher("/Main/main.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            // DAO 자원 정리
            if (freDao != null) freDao.close();
            if (qnaDao != null) qnaDao.close();
            if (filesDao != null) filesDao.close();
        }
    }
}
