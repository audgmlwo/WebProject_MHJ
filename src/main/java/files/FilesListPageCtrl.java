package files;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import board.BoardDAO;
import board.BoardDTO;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.BoardPage;
import utils.JSFunction;

@WebServlet("/files/BLPC")
public class FilesListPageCtrl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	HttpSession session = req.getSession();
    	
    	String boardType = req.getParameter("board_type");
        if (boardType == null || boardType.isEmpty()) {
            boardType = "files"; // 기본값: 자료실
        }
        
    	String userId = (String) session.getAttribute("UserId");
    	if (userId == null) {
    	    System.out.println("비로그인 사용자 접근 허용: 게시판 목록만 출력합니다.");
    	    // 로그인하지 않아도 목록 출력 가능
    	}
        
        // 뷰에 전달할 매개변수 저장용 맵 생성
        Map<String, Object> map = new HashMap<>();
        map.put("board_type", boardType);
        
        String searchField = req.getParameter("searchField");
        String searchWord = req.getParameter("searchWord");
        
        if (searchWord != null) {
            map.put("searchField", searchField);
            map.put("searchWord", searchWord);
        }
        
        // DAO 생성
    	BoardDAO dao = new BoardDAO();
        int totalCount = dao.selectCountFiles(map);

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

        List<BoardDTO> boardLists = dao.selectListPageFiles(map);
        dao.close();

        // 뷰에 전달할 매개변수 추가
        String baseUrl = "../files/BLPC?board_type=" + boardType +"&";
        
        String pagingImg = BoardPage.pagingStr(totalCount,
        		pageSize, blockPage, pageNum, baseUrl);
        
        // 최종적으로 ?& 제거
        pagingImg = pagingImg
        		.replace("href='../files/BLPC?board_type=" 
        			+ boardType + "&?", "href='../files/BLPC?board_type=" + boardType + "&");
        
        map.put("pagingImg", pagingImg);
        map.put("totalCount", totalCount);
        map.put("pageSize", pageSize);
        map.put("pageNum", pageNum);
        map.put("board_type", boardType);

        // 전달할 데이터를 request 영역에 저장 후 List.jsp로 포워드
        req.setAttribute("boardLists", boardLists);
        req.setAttribute("map", map);

        req.getRequestDispatcher("/Files/filesList.jsp").forward(req, resp);
    }
}
