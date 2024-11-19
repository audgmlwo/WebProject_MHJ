package board;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.BoardPage;
import utils.JSFunction;

@WebServlet("/board/BLPC")
public class BoardListPageCtrl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	HttpSession session = req.getSession();
    	
    	
    	String userId = (String) session.getAttribute("UserId");
    	if (userId == null) {
    	    System.out.println("비로그인 사용자 접근 허용: 게시판 목록만 출력합니다.");
    	    // 로그인하지 않아도 목록 출력 가능
    	}

        // DAO 생성
        BoardDAO dao = new BoardDAO();

        // 뷰에 전달할 매개변수 저장용 맵 생성
        Map<String, Object> map = new HashMap<>();

        String searchField = req.getParameter("searchField");
        String searchWord = req.getParameter("searchWord");
        if (searchWord != null) {
            map.put("searchField", searchField);
            map.put("searchWord", searchWord);
        }
        int totalCount = dao.selectCount(map);

        /* 페이지 처리 start */
        ServletContext application = getServletContext();
        int pageSize = Integer.parseInt(application.getInitParameter("POSTS_PER_PAGE"));
        int blockPage = Integer.parseInt(application.getInitParameter("PAGES_PER_BLOCK"));

        int pageNum = 1;
        String pageTemp = req.getParameter("pageNum");
        if (pageTemp != null && !pageTemp.equals("")) {
            pageNum = Integer.parseInt(pageTemp);
        }

        int start = (pageNum - 1) * pageSize + 1;
        int end = pageNum * pageSize;
        map.put("start", start);
        map.put("end", end);
        /* 페이지 처리 end */

        List<BoardDTO> boardLists = dao.selectListPage(map);
        dao.close();

        // 뷰에 전달할 매개변수 추가
        String pagingImg = BoardPage.pagingStr(totalCount, pageSize, blockPage, pageNum, "../board/BLPC");
        map.put("pagingImg", pagingImg);
        map.put("totalCount", totalCount);
        map.put("pageSize", pageSize);
        map.put("pageNum", pageNum);

        // 전달할 데이터를 request 영역에 저장 후 List.jsp로 포워드
        req.setAttribute("boardLists", boardLists);
        req.setAttribute("map", map);

        req.getRequestDispatcher("/Board/boardList.jsp").forward(req, resp);
    }
}
