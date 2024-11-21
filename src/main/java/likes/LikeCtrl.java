package likes;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utils.CookieManager;


@WebServlet("/likes/LIKE")
public class LikeCtrl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        HttpSession session = req.getSession();

        // 로그인을 확인하고 리다이렉트 처리
        if (session.getAttribute("UserId") == null) { 
            resp.sendRedirect(req.getContextPath() + "/Login/LoginForm.jsp");
            return;
        }

        // 세션에서 user_id 가져오기
        String userId = (String) session.getAttribute("UserId");
        System.out.println("UserId in LikeCtrl: " + userId);
        if (userId == null || userId.isEmpty()) {
            System.out.println("UserId is null or empty in LikeCtrl!");
            resp.sendRedirect(req.getContextPath() + "/Login/LoginForm.jsp");
            return;
        }

        // 요청 파라미터 처리
        String boardType = req.getParameter("board_type");
        int postId = Integer.parseInt(req.getParameter("post_id"));

        

        // 쿠키로 중복 확인
        String cookieName = "like_" + boardType + "_" + postId + "_" + userId;

        // 쿠키 확인
        String cookieValue = CookieManager.readCookie(req, cookieName);
        boolean hasLiked = cookieValue != null && cookieValue.equals("true");

        LikeDAO dao = new LikeDAO();

        if (!hasLiked) {
            // 좋아요 추가
            dao.addLike(boardType, postId, userId);
            CookieManager.makeCookie(resp, cookieName, "true", 60 * 60 * 24 * 7); // 7일 유지
            System.out.println("좋아요 추가 및 쿠키 생성: " + cookieName);
        } else {
            // 좋아요 취소
            dao.removeLike(boardType, postId, userId);
            CookieManager.deleteCookie(resp, cookieName); // 쿠키 삭제
            System.out.println("좋아요 취소 및 쿠키 삭제: " + cookieName);
        }

        // 갱신된 좋아요 수 조회
        int likeCount = dao.getLikeCount(boardType, postId);
        System.out.println("좋아요 수: " + likeCount);

        // 좋아요 수를 전달하고 리다이렉트
        req.setAttribute("likeCount", likeCount);
        req.setAttribute("boardType", boardType);
        req.setAttribute("postId", postId);

        // 원래 페이지로 리다이렉트
        String referer = req.getHeader("Referer");
        resp.sendRedirect(referer != null ? referer : req.getContextPath());
    }
}
