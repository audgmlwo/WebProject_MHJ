package board;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fileupload.FileUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.JSFunction;


@WebServlet("/board/BWC")
@MultipartConfig(
    maxFileSize = 1024 * 1024 * 50,  // 최대 파일 크기: 50MB
    maxRequestSize = 1024 * 1024 * 100  // 최대 요청 크기: 100MB
)
public class BoardWriteCtrl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, 
    		HttpServletResponse resp) 
    				throws ServletException, IOException {
    	
        HttpSession session = req.getSession();
        
        // 로그인 확인
        if (session.getAttribute("UserId") == null) {
            // 이전 요청 URL을 세션에 저장
            session.setAttribute("redirectAfterLogin", req.getRequestURI());
            JSFunction.alertLocation(resp, "로그인 후 이용해주세요.", "../Login/LoginForm.jsp");
            return;
        }

        // 쓰기 페이지로 이동
        req.getRequestDispatcher("/Board/boardWrite.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, 
            HttpServletResponse resp) 
            throws ServletException, IOException {
    	
        HttpSession session = req.getSession();

        // 로그인 확인
        String userId = (String) session.getAttribute("UserId");
        if (userId == null || userId.isEmpty()) {
            JSFunction.alertLocation(resp, "로그인 후 이용해주세요.", "../Login/LoginForm.jsp");
            return;
        }

        // 파일 업로드 처리
        String saveDirectory = req.getServletContext().getRealPath("/Uploads");
        String originalFileName = "";
        
        try {
            if (req.getContentType() != null && req.getContentType().startsWith("multipart/form-data")) {
                originalFileName = FileUtil.uploadFile(req, saveDirectory);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JSFunction.alertLocation(resp, "파일 업로드 중 오류가 발생했습니다.", "../board/BoardWriteCtrl");
            return;
        }

        // 게시판 유형 처리
        String boardType = req.getParameter("board_type");
        if (boardType == null || boardType.isEmpty()) {
            JSFunction.alertBack(resp, "게시판 유형을 선택해주세요.");
            return;
        }

        // DTO에 데이터 설정
        BoardDTO dto = new BoardDTO();
        dto.setUser_id(userId); // 작성자 아이디
        dto.setTitle(req.getParameter("title")); // 제목
        dto.setContent(req.getParameter("content")); // 내용
        dto.setBoard_type(boardType); // 검증된 board_type 설정
        

        // 파일명 처리
        if (!originalFileName.isEmpty()) {
            try {
                String savedFileName = FileUtil.renameFile(saveDirectory, originalFileName);
                dto.setO_file(originalFileName); // 원본 파일명
                dto.setS_file(savedFileName);    // 저장된 파일명
            } catch (Exception e) {
                e.printStackTrace();
                JSFunction.alertLocation(resp, "파일 저장 처리 중 오류가 발생했습니다.", "../board/boardWrite.jsp");
                return;
            }
        }

        // DAO 처리
        BoardDAO dao = null;
        try {
            dao = new BoardDAO();
            int result = dao.insertWrite(dto);
                       
            if (result == 1) {
                String contextPath = req.getContextPath(); // 동적 경로 가져오기

                // boardType에 따라 폴더 경로 매핑
                Map<String, String> folderMap = new HashMap<>();
                
                folderMap.put("fre", "board"); // 자유게시판
                folderMap.put("files", "files"); // 자료실
                folderMap.put("question", "q_board"); // 질문게시판
                // 필요한 다른 boardType도 추가

                // 기본 폴더 설정 (boardType이 매핑되지 않을 경우)
                String folder = folderMap.getOrDefault(boardType, "board");

                // 리다이렉트 경로 생성
                resp.sendRedirect(contextPath + "/" + folder + "/BLPC?board_type=" + boardType);
            } else {
                JSFunction.alertLocation(resp, "글쓰기에 실패했습니다.", "../board/boardWrite.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JSFunction.alertLocation(resp, "서버 오류로 글쓰기에 실패했습니다.", "../board/boardWrite.jsp");
        } finally {
            if (dao != null) {
                try {
                    dao.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
}

