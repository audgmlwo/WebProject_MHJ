package board;

import java.io.IOException;

import fileupload.FileUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.JSFunction;


@WebServlet("/board/BoardWriteCtrl")
@MultipartConfig(
    maxFileSize = 1024 * 1024 * 50,  // 최대 파일 크기: 50MB
    maxRequestSize = 1024 * 1024 * 100  // 최대 요청 크기: 100MB
)
public class BoardWriteCtrl extends HttpServlet {
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
            // 요청의 Content-Type 확인 및 업로드 여부 판단
            if (req.getContentType() != null && req.getContentType().startsWith("multipart/form-data")) {
                originalFileName = FileUtil.uploadFile(req, saveDirectory);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JSFunction.alertLocation(resp, "파일 업로드 중 오류가 발생했습니다.", "../board/BoardWriteCtrl");
            return;
        }

        // DTO에 데이터 설정
        BoardDTO dto = new BoardDTO();
        dto.setUser_id(userId); // 작성자 아이디
        dto.setTitle(req.getParameter("title")); // 제목
        dto.setContent(req.getParameter("content")); // 내용

        // board_type 처리
        String boardType = req.getParameter("board_type");
        if (boardType == null || boardType.isEmpty()) {
            boardType = "fre"; // 기본값 설정
        }
        dto.setBoard_type(boardType);

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

        BoardDAO dao = null; // dao 변수를 초기화
        try {
            dao = new BoardDAO(); // DAO 객체 생성
            int result = dao.insertWrite(dto); // 글쓰기 실행

            if (result == 1) {
                resp.sendRedirect("../board/BLPC"); // 성공 시 목록으로 이동
            } else {
                JSFunction.alertLocation(resp, "글쓰기에 실패했습니다.", "../board/boardWrite.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JSFunction.alertLocation(resp, "서버 오류로 글쓰기에 실패했습니다.", "../board/boardWrite.jsp");
        } finally {
            if (dao != null) { // dao가 null이 아닌 경우에만 자원 해제
                try {
                    dao.close(); // 명시적으로 DAO 자원 해제
                } catch (Exception e) {
                    e.printStackTrace(); // 자원 해제 중 예외 처리
                }
            }
        }
    }
}
