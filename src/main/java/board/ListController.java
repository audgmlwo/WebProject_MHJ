package board;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//이 컨트롤러(서블릿)의 매핑은 web.xml에서 정의한다. 
public class ListController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//게시판에서 목록은 특정 메뉴를 클릭해서 진입하므로 get방식의 요청임 
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
    	
    	//DAO 인스턴스 생성. 생성과 동시에 DBCP를 통해 오라클에 연결된다.
    	BoardDAO dao = new BoardDAO();
    	
    	//검색어 관련 파라미터 저장을 위해 Map 생성. 
        Map<String, Object> map = new HashMap<String, Object>();

        //검색을 위해 검색어를 입력했다면 파라미터로 전달된 값을 Map에 저장. 
        String searchField = req.getParameter("searchField");
        String searchWord = req.getParameter("searchWord");
        if (searchWord != null) {
        	//검색어가 있는 경우 Map에 파라미터를 저장한다. 
            map.put("searchField", searchField);
            map.put("searchWord", searchWord);
        }
        
        //게시물의 갯수 카운트를 위한 메서드 호출
        int totalCount = dao.selectCount(map);
        //결과를 Map에 저장 
        map.put("totalCount", totalCount);

        //목록에 출력할 레코드를 인출하기 위한 메서드 호출 
        List<BoardDTO> boardLists = dao.selectList(map); 
        //DB 연결 해제 
        dao.close();  
        
        //View로 전달할 데이터는 request영역에 저장한다. 
        req.setAttribute("boardLists", boardLists);
        req.setAttribute("map", map);
        //View로 포워드한다. 
        req.getRequestDispatcher("/14MVCBoard/List.jsp")
        	.forward(req, resp);
        /*
        request영역은 포워드 된 페이지까지 데이터를 공유할 수 있으므로 
        서블릿에서 처리한 내용은 request영역을 통해 JSP쪽으로 공유된다. 
        */
    }
}



