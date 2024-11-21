package board;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likes.LikeDAO;
import utils.CookieManager;
import board.BoardDAO;
import board.BoardDTO;


@WebServlet("/board/BVC")
public class BoardViewCtrl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 게시물 불러오기
        BoardDAO boardDAO = new BoardDAO();
        LikeDAO likeDAO = new LikeDAO(); // 좋아요 DAO 추가

        // 파라미터로 전달될 일련번호를 받기
        String board_id = req.getParameter("board_id");
        String board_type = req.getParameter("board_type");

        // 조회수 1 증가 (쿠키로 중복 조회 방지)
        String cookieName = "viewed_" + board_id; // 쿠키 이름
        String isViewed = CookieManager.readCookie(req, cookieName);

        if (isViewed == null || isViewed.isEmpty()) { // 새로운 조회라면
            boardDAO.updateVisitCount(board_id, board_type); // 조회수 증가
            CookieManager.makeCookie(resp, cookieName, "true", 60 * 60 * 1); // 쿠키 생성 (1일 유지)
        }

        // 게시물 데이터 인출
        BoardDTO dto = boardDAO.selectView(board_id, board_type);

        // 좋아요 수 조회
        int likeCount = likeDAO.getLikeCount(board_type, Integer.parseInt(board_id));
        req.setAttribute("likeCount", likeCount);

        // 줄바꿈 처리
        dto.setContent(dto.getContent().replaceAll("\r\n", "<br/>"));
        setFileExtension(dto, req);

        // 게시물(dto) 저장 후 뷰로 포워드
        req.setAttribute("dto", dto);
        req.getRequestDispatcher("/Board/boardView.jsp").forward(req, resp);

        // MIME 타입 처리
        String ext = null, fileName = dto.getS_file(), mimeType = null;
        if (fileName != null) {
            ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        }

        String[] extArray1 = {"png", "jpg", "gif", "pcx", "bmp"};
        String[] extArray2 = {"mp3", "wav"};
        String[] extArray3 = {"mp4", "avi", "wmv"};

        if (mimeContains(extArray1, ext)) {
            mimeType = "img";
        } else if (mimeContains(extArray2, ext)) {
            mimeType = "audio";
        } else if (mimeContains(extArray3, ext)) {
            mimeType = "video";
        }
        System.out.println("MIME타입=" + mimeType);
        req.setAttribute("mimeType", mimeType);

        // DAO 자원 반납
        boardDAO.close();
        likeDAO.close();
    }

    public boolean mimeContains(String[] strArr, String ext) {
        for (String s : strArr) {
            if (s.equalsIgnoreCase(ext)) return true;
        }
        return false;
    }

    public void setFileExtension(BoardDTO dto, HttpServletRequest req) {
        String o_file = dto.getO_file();

        if (o_file != null && o_file.contains(".")) {
            String extension = o_file.substring(o_file.lastIndexOf(".") + 1).toLowerCase();
            req.setAttribute("fileExtension", extension);
        } else {
            req.setAttribute("fileExtension", "");
        }
    }
}
