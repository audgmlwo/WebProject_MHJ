package files;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import board.BoardDAO;
import board.BoardDTO;
import fileupload.FileUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.JSFunction;


@WebServlet("/files/FilesWriteCtrl")
@MultipartConfig(
		
	maxFileSize = 5L * 1024 * 1024 * 1024,   // 최대 파일 크기: 5GB
	maxRequestSize = 10L * 1024 * 1024 * 1024  // 최대 요청 크기: 10GB
)
public class FilesWriteCtrl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, 
    		HttpServletResponse resp) throws ServletException, 
    
    IOException {
        HttpSession session = req.getSession();
        
        // 로그인 확인
        if (session.getAttribute("UserId") == null) {
            // 이전 요청 URL을 세션에 저장
            session.setAttribute("redirectAfterLogin", req.getRequestURI());
            JSFunction.alertLocation(resp, "로그인 후 이용해주세요.", "../Login/LoginForm.jsp");
            return;
        }

        // 쓰기 페이지로 이동
        req.getRequestDispatcher("/Files/filesWrite.jsp").forward(req, resp);
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
            JSFunction.alertLocation(resp, "파일 업로드 중 오류가 발생했습니다.", "../files/FilesWriteCtrl");
            return;
        }

        // board_type 처리 및 검증
        String boardType = req.getParameter("board_type");
        List<String> validBoardTypes = Arrays.asList("files");

        // 잘못된 게시판 타입 처리
        if (boardType == null || !validBoardTypes.contains(boardType)) {
            JSFunction.alertBack(resp, "유효하지 않은 게시판입니다."); // 클라이언트에 알림
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
                JSFunction.alertLocation(resp, "파일 저장 처리 중 오류가 발생했습니다.", "../Files/filesWrite.jsp");
                return;
            }
        }

        // DAO 처리
        BoardDAO dao = null;
        try {
            dao = new BoardDAO();
            int result = dao.insertWrite(dto);

            if (result == 1) {
                resp.sendRedirect("../files/BLPC?board_type=" + boardType); // 성공 시 해당 게시판 목록으로 이동
            } else {
                JSFunction.alertLocation(resp, "글쓰기에 실패했습니다.", "../Files/filesWrite.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JSFunction.alertLocation(resp, "서버 오류로 글쓰기에 실패했습니다.", "../Files/filesWrite.jsp");
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

