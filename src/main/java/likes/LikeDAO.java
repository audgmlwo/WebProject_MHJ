package likes;

import java.sql.SQLException;
import common.DBConnPool;

public class LikeDAO extends DBConnPool {

    // 특정 게시글에 좋아요가 있는지 확인
    public boolean hasLiked(String boardType, int postId, String userId) {
        String query = "SELECT COUNT(*) FROM likes WHERE board_type = ? AND post_id = ? AND user_id = ?";
        boolean hasLiked = false;

        try {
            psmt = conn.prepareStatement(query);
            psmt.setString(1, boardType);
            psmt.setInt(2, postId);
            psmt.setString(3, userId);
            rs = psmt.executeQuery();

            if (rs.next()) {
                hasLiked = rs.getInt(1) > 0; // 좋아요가 존재하면 true 반환
            }
        } catch (SQLException e) {
            System.out.println("좋아요 확인 중 오류 발생");
            e.printStackTrace();
        } finally {
            close(); // 자원 반납
        }
        return hasLiked;
    }

    // 좋아요 추가
    public void addLike(String boardType, int postId, String userId) {
        String query = "INSERT INTO likes (board_type, post_id, user_id) VALUES (?, ?, ?)";
        try {
            psmt = conn.prepareStatement(query);
            psmt.setString(1, boardType);
            psmt.setInt(2, postId);
            psmt.setString(3, userId);
            psmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("좋아요 추가 중 오류 발생");
            e.printStackTrace();
        } finally {
            close(); // 자원 반납
        }
    }

    // 좋아요 삭제
    public void removeLike(String boardType, int postId, String userId) {
        String query = "DELETE FROM likes WHERE board_type = ? AND post_id = ? AND user_id = ?";
        try {
            psmt = conn.prepareStatement(query);
            psmt.setString(1, boardType);
            psmt.setInt(2, postId);
            psmt.setString(3, userId);
            psmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("좋아요 삭제 중 오류 발생");
            e.printStackTrace();
        } finally {
            close(); // 자원 반납
        }
    }

    // 특정 게시글의 좋아요 수 조회
    public int getLikeCount(String boardType, int postId) {
        String query = "SELECT COUNT(*) FROM likes WHERE board_type = ? AND post_id = ?";
        int likeCount = 0;

        try {
            psmt = conn.prepareStatement(query);
            psmt.setString(1, boardType);
            psmt.setInt(2, postId);
            rs = psmt.executeQuery();

            if (rs.next()) {
                likeCount = rs.getInt(1); // 좋아요 수 반환
            }
        } catch (SQLException e) {
            System.out.println("좋아요 수 조회 중 오류 발생");
            e.printStackTrace();
        } finally {
            close(); // 자원 반납
        }
        return likeCount;
    }
}
