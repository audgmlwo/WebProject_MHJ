package board;

import java.sql.*;
import java.util.*;
import common.DBConnPool;

public class BoardDAO extends DBConnPool {

    // 게시물 총 갯수 조회
    public int selectCount(Map<String, Object> map) {
        int totalCount = 0;
        String query = "SELECT COUNT(*) FROM board";
        
        if (map.get("searchWord") != null) {
            query += " WHERE " + map.get("searchField") + " LIKE ?";
        }

        try (PreparedStatement psmt = conn.prepareStatement(query)) {
            if (map.get("searchWord") != null) {
                psmt.setString(1, "%" + map.get("searchWord") + "%");
            }

            try (ResultSet rs = psmt.executeQuery()) {
                if (rs.next()) {
                    totalCount = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            System.out.println("게시물 카운트 중 예외 발생");
            e.printStackTrace();
        }
        return totalCount;
    }

    // 게시물 목록 조회
    public List<BoardDTO> selectList(Map<String, Object> map) {
        List<BoardDTO> boardList = new ArrayList<>();
        String query =
            "SELECT * FROM (" +
            "  SELECT Tb.*, ROWNUM AS rNum" +
            "  FROM (" +
            "    SELECT * FROM board" +
            "    WHERE board_type = ?";

        // board_id 조건 추가
        if (map.get("board_id") != null) {
            query += " AND board_id = ?";
        }

        // 검색어 조건 추가
        if (map.get("searchWord") != null && !map.get("searchWord").toString().isEmpty()) {
            query += " AND " + map.get("searchField") + " LIKE ?";
        }

        query += " ORDER BY board_id DESC" +
                 "  ) Tb" +
                 "  WHERE ROWNUM <= ?" +
                 ") WHERE rNum >= ?";

        try (PreparedStatement psmt = conn.prepareStatement(query)) {
            int paramIndex = 1;
            psmt.setString(paramIndex++, map.get("boardType").toString()); // 게시판 타입

            if (map.get("board_id") != null) {
                psmt.setInt(paramIndex++, Integer.parseInt(map.get("board_id").toString())); // board_id
            }

            if (map.get("searchWord") != null && !map.get("searchWord").toString().isEmpty()) {
                psmt.setString(paramIndex++, "%" + map.get("searchWord") + "%"); // 검색어
            }

            psmt.setInt(paramIndex++, Integer.parseInt(map.get("end").toString())); // 끝 번호
            psmt.setInt(paramIndex, Integer.parseInt(map.get("start").toString())); // 시작 번호

            try (ResultSet rs = psmt.executeQuery()) {
                while (rs.next()) {
                    BoardDTO dto = new BoardDTO();
                    dto.setBoard_id(rs.getInt("board_id"));
                    dto.setBoard_type(rs.getString("board_type"));
                    dto.setUser_id(rs.getString("user_id"));
                    dto.setTitle(rs.getString("title"));
                    dto.setContent(rs.getString("content"));
                    dto.setCreated_date(rs.getDate("created_date"));
                    dto.setUpdated_date(rs.getDate("updated_date"));
                    dto.setO_file(rs.getString("o_file"));
                    dto.setS_file(rs.getString("s_file"));
                    dto.setDown_count(rs.getInt("down_count"));
                    dto.setVisit_count(rs.getInt("visit_count"));
                    boardList.add(dto);
                }
            }
        } catch (Exception e) {
            System.out.println("페이징 조회 중 예외 발생");
            e.printStackTrace();
        }
        return boardList;
    }


    // 게시물 작성
    public int insertWrite(BoardDTO dto) {
        int result = 0;
        String query = "INSERT INTO board (board_id, board_type, user_id, title, content, created_date, updated_date, o_file, s_file) "
                     + "VALUES (seq_board_num.NEXTVAL, ?, ?, ?, ?, SYSDATE, SYSDATE, ?, ?)";

        try (PreparedStatement psmt = conn.prepareStatement(query)) {
            psmt.setString(1, dto.getBoard_type());
            psmt.setString(2, dto.getUser_id());
            psmt.setString(3, dto.getTitle());
            psmt.setString(4, dto.getContent());
            psmt.setString(5, dto.getO_file());
            psmt.setString(6, dto.getS_file());

            result = psmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("게시물 입력 중 예외 발생");
            e.printStackTrace();
        }
        return result;
    }

    // 게시물 상세 보기
    public BoardDTO selectView(String board_id, String board_type) {
        BoardDTO dto = null;
        String query = "SELECT Bo.*, Me.name FROM board Bo "
                     + "INNER JOIN member Me ON Bo.user_id = Me.id "
                     + "WHERE Bo.board_id = ? AND Bo.board_type = ?";

        try (PreparedStatement psmt = conn.prepareStatement(query)) {
            psmt.setInt(1, Integer.parseInt(board_id));
            psmt.setString(2, board_type);

            try (ResultSet rs = psmt.executeQuery()) {
                if (rs.next()) {
                    dto = new BoardDTO();
                    dto.setBoard_id(rs.getInt("board_id"));
                    dto.setBoard_type(rs.getString("board_type"));
                    dto.setUser_id(rs.getString("user_id"));
                    dto.setTitle(rs.getString("title"));
                    dto.setContent(rs.getString("content"));
                    dto.setCreated_date(rs.getDate("created_date"));
                    dto.setUpdated_date(rs.getDate("updated_date"));
                    dto.setO_file(rs.getString("o_file"));
                    dto.setS_file(rs.getString("s_file"));
                    dto.setDown_count(rs.getInt("down_count"));
                    dto.setVisit_count(rs.getInt("visit_count"));
                    dto.setName(rs.getString("name"));
                }
            }
        } catch (Exception e) {
            System.out.println("게시물 상세보기 중 예외 발생");
            e.printStackTrace();
        }
        return dto;
    }

    // 조회수 증가
    public void updateVisitCount(String board_id, String board_type) {
        String query = "UPDATE board SET visit_count = visit_count + 1 WHERE board_id = ? AND board_type = ?";
        try (PreparedStatement psmt = conn.prepareStatement(query)) {
            psmt.setInt(1, Integer.parseInt(board_id));
            psmt.setString(2, board_type);
            psmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("게시물 조회수 증가 중 예외 발생");
            e.printStackTrace();
        }
    }

    // 다운로드 횟수 증가
    public void downCountPlus(String board_id, String board_type) {
        String query = "UPDATE board SET down_count = down_count + 1 WHERE board_id = ? AND board_type = ?";
        try (PreparedStatement psmt = conn.prepareStatement(query)) {
            psmt.setInt(1, Integer.parseInt(board_id));
            psmt.setString(2, board_type);
            psmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("게시물 다운로드 조회수 증가 중 예외 발생");
            e.printStackTrace();
        }
    }

    // 게시물 삭제
    public int deletePost(String board_id, String board_type) {
        int result = 0;
        String query = "DELETE FROM board WHERE board_id = ? AND board_type = ?";
        try (PreparedStatement psmt = conn.prepareStatement(query)) {
            psmt.setInt(1, Integer.parseInt(board_id));
            psmt.setString(2, board_type);
            result = psmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("게시물 삭제 중 예외 발생");
            e.printStackTrace();
        }
        return result;
    }

    // 게시물 수정
    public int updatePost(BoardDTO dto) {
        int result = 0;
        String query = "UPDATE board SET title = ?, content = ?, o_file = ?, s_file = ? WHERE board_id = ? AND board_type = ? AND user_id = ?";
        try (PreparedStatement psmt = conn.prepareStatement(query)) {
            psmt.setString(1, dto.getTitle());
            psmt.setString(2, dto.getContent());
            psmt.setString(3, dto.getO_file());
            psmt.setString(4, dto.getS_file());
            psmt.setInt(5, dto.getBoard_id());
            psmt.setString(6, dto.getBoard_type());
            psmt.setString(7, dto.getUser_id());

            result = psmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("게시물 수정 중 예외 발생");
            e.printStackTrace();
        }
        return result;
    }
}
