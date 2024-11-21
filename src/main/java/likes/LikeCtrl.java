package likes;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/likes/LIKE")
public class LikeCtrl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        String boardType = req.getParameter("board_type");
        int postId = Integer.parseInt(req.getParameter("post_id"));
        String userId = (String) req.getSession().getAttribute("user_id"); // 세션에서 사용자 ID 가져오기

        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/Login/LoginForm.jsp");
            return;
        }

        LikeDAO dao = new LikeDAO();

        // 쿠키로 중복 확인
        String cookieName = "like_" + boardType + "_" + postId + "_" + userId;
        Cookie[] cookies = req.getCookies();
        boolean hasLiked = false;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    hasLiked = true;
                    break;
                }
            }
        }

        if (!hasLiked) {
            dao.addLike(boardType, postId, userId);

            // 좋아요 쿠키 생성
            Cookie likeCookie = new Cookie(cookieName, "true");
            likeCookie.setMaxAge(60 * 60 * 24 * 7); // 7일 유효
            resp.addCookie(likeCookie);
        } else {
            dao.removeLike(boardType, postId, userId);

            // 좋아요 쿠키 삭제
            Cookie unlikeCookie = new Cookie(cookieName, null);
            unlikeCookie.setMaxAge(0);
            resp.addCookie(unlikeCookie);
        }

        // 원래 페이지로 리다이렉트
        String referer = req.getHeader("Referer");
        resp.sendRedirect(referer != null ? referer : req.getContextPath());
    }
}
