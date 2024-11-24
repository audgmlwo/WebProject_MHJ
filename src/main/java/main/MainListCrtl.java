package main;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import board.BoardDAO;
import board.BoardDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import login.MemberDAO;
import login.MemberDTO;
import q_board.Q_BoardDAO;
import q_board.Q_BoardDTO;
import utils.JSFunction;


@WebServlet("/main/MLC")
public class MainListCrtl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();

        // 로그인하지 않아도 목록 출력 가능
    	String userId = (String) session.getAttribute("UserId");
    	if (userId == null) {
    	    System.out.println("비로그인 사용자 접근 허용: 게시판 목록만 출력합니다.");
    	    
    	}

        // DAO를 사용해 데이터 조회
        BoardDAO freDao = null;
        Q_BoardDAO qnaDao = null;
        BoardDAO filesDao = null;

        try {
            freDao = new BoardDAO();
            qnaDao = new Q_BoardDAO();
            filesDao = new BoardDAO();

            // 데이터 가져오기
            List<BoardDTO> freList = freDao.selectListPageBoard(Map.of("start", "1", "end", "10"));
            List<Q_BoardDTO> qnaList = qnaDao.selectListPageBoard(Map.of("start", "1", "end", "10"));
            List<BoardDTO> filesList = filesDao.selectListPageFiles(Map.of("start", "1", "end", "10"));
           
            
            // 데이터 전달
            req.setAttribute("freList", freList);
            req.setAttribute("qnaList", qnaList);
            req.setAttribute("filesList", filesList);
                    

            // 메인 페이지로 이동
            req.getRequestDispatcher("/Main/main.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            // DAO 정리
            if (freDao != null) freDao.close();
            if (qnaDao != null) qnaDao.close();
            if (filesDao != null) filesDao.close();
            
           
        }
    }
    
   
    @Override
    protected void doPost(HttpServletRequest req, 
            HttpServletResponse resp) 
            throws ServletException, IOException {
    	
    	HttpSession session = req.getSession();
    	
    	// 세션에서 사용자 정보 가져오기
    	String userId = (String) session.getAttribute("UserId");
        String userName = (String) session.getAttribute("UserName");
        String userEmail = (String) session.getAttribute("Email");;   
         
        
        if (session.getAttribute("UserId") == null) {
            // 이전 요청 URL을 세션에 저장
            session.setAttribute("redirectAfterLogin", req.getRequestURI());
            JSFunction.alertLocation(resp, "로그인 후 이용해주세요.", "../Login/LoginForm.jsp");
            return;
        }
        
        
        try {
            // MemberDAO를 사용해 데이터베이스에서 사용자 정보 조회
                       	
        	// 사용자 정보 디버깅
            System.out.println("UserId: " + userId);
            System.out.println("UserName: " + userName);
            System.out.println("UserEmail: " + userEmail);
            	
            // 쓰기 페이지로 이동
            req.getRequestDispatcher("/Main/main.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}