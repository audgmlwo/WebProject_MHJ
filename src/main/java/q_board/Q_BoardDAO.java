package q_board;

import java.sql.*;
import java.util.*;
import common.DBConnPool;



public class Q_BoardDAO extends DBConnPool {

    // 게시물 총 갯수 조회 
    public int selectCountBoard(Map<String, Object> map) {
    	
        int totalCount = 0;
        String query = "SELECT COUNT(*) FROM q_board ";
        
        if (map.get("searchWord") != null) {
            query += " WHERE " + map.get("searchField") + " LIKE ?";
        }

        try {
            psmt = conn.prepareStatement(query);
            
            if (map.get("searchWord") != null) {
                psmt.setString(1, "%" + map.get("searchWord") + "%");
            }

            rs = psmt.executeQuery();
            if (rs.next()) {
                totalCount = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("게시물 카운트 중 예외 발생");
            e.printStackTrace();
        }
        return totalCount;
    }
    
    
	
    // 게시물 목록 조회
    public List<Q_BoardDTO> selectListPageBoard(
    		Map<String,Object> map) {
    	
        List<Q_BoardDTO> q_board = new Vector<Q_BoardDTO>();
        
        String query = 
                 " SELECT * FROM ( "
               + "  SELECT Tb.*, ROWNUM rNum FROM ( "
               + "    SELECT * FROM q_board ";
        
        if (map.get("searchWord") != null) {
            query += " WHERE " + map.get("searchField") + " LIKE ? ";
        }
        query += "     ORDER BY Q_ID DESC "
               + "   ) Tb "
               + " ) "
               + " WHERE rNum BETWEEN ? AND ?";

        try {
            psmt = conn.prepareStatement(query);
            
            int paramIndex = 1;
            if (map.get("searchWord") != null) {
                psmt.setString(paramIndex++, "%" + map.get("searchWord") + "%");
            }
            psmt.setInt(paramIndex++, Integer.parseInt(map.get("start").toString()));
            psmt.setInt(paramIndex, Integer.parseInt(map.get("end").toString()));
            rs = psmt.executeQuery();

            while (rs.next()) {
            	
                Q_BoardDTO dto = new Q_BoardDTO();

                dto.setQ_id(rs.getInt("q_id"));
                dto.setUser_id(rs.getString("user_id"));
                dto.setTitle(rs.getString("title"));
                dto.setContent(rs.getString("content"));
                dto.setCreated_date(rs.getDate("created_date"));
                dto.setUpdated_date(rs.getDate("updated_date"));             
                dto.setIs_accepted(rs.getInt("is_accepted"));
                dto.setVisit_count(rs.getInt("visit_count"));
                
                dto.setBoard_type("question");
                
                q_board.add(dto);
            }
        
    } catch (Exception e) {
        System.out.println("게시물 조회 중 예외 발생");
        e.printStackTrace();
        
    } finally {
    	
        // 리소스 정리: ResultSet -> PreparedStatement -> Connection 순으로 닫음
    	
        try {
            if (rs != null) rs.close();
            if (psmt != null) psmt.close();
            // conn은 DAO 전체에서 관리되고 있다면 여기서 닫지 않음
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    return q_board;
}
    
   
    // 게시물 작성
    public int insertWrite(Q_BoardDTO dto) {
    	
        int result = 0;
        String query = " INSERT INTO q_board (q_id, user_id, title, content, created_date, updated_date) "
                     + " VALUES (seq_board_num.NEXTVAL, ?, ?, ?, SYSDATE, SYSDATE) ";

        try {
        	
        	psmt = conn.prepareStatement(query);
        	
            psmt.setString(1, dto.getUser_id());
            psmt.setString(2, dto.getTitle());
            psmt.setString(3, dto.getContent());
      
            result = psmt.executeUpdate();
            
        } catch (Exception e) {
            System.out.println("게시물 입력 중 예외 발생");
            e.printStackTrace();
        }
        return result;
    }

    // 게시물 상세 보기
    public Q_BoardDTO selectView(String q_id) {
    	
        Q_BoardDTO dto = null;
        
        
        String query = " SELECT Bo.*, Me.name FROM q_board Bo "
                     + " INNER JOIN member Me ON Bo.user_id = Me.user_id "
                     + " WHERE Bo.q_id = ? ";

        try {
        	
        	psmt = conn.prepareStatement(query);
        	
            psmt.setInt(1, Integer.parseInt(q_id));
            
            try (ResultSet rs = psmt.executeQuery()) {
            	
                if (rs.next()) {
                    dto = new Q_BoardDTO();
                    
                    dto.setQ_id(rs.getInt("q_id"));
                    dto.setUser_id(rs.getString("user_id"));
                    dto.setTitle(rs.getString("title"));
                    dto.setContent(rs.getString("content"));
                    dto.setCreated_date(rs.getDate("created_date"));
                    dto.setUpdated_date(rs.getDate("updated_date"));                  
                    dto.setVisit_count(rs.getInt("visit_count"));
                    dto.setIs_accepted(rs.getInt("is_accepted"));
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
    public void updateVisitCount(String q_id) {
        String query = "UPDATE q_board SET visit_count = visit_count + 1 WHERE q_id =?";
        
        try {
        	
        	psmt = conn.prepareStatement(query);
        	
            psmt.setInt(1, Integer.parseInt(q_id));
           
            psmt.executeUpdate();
            
        } catch (Exception e) {
        	
            System.out.println("게시물 조회수 증가 중 예외 발생");
            e.printStackTrace();
        }
    }

    
    // 게시물 삭제
    public int deletePost(String q_id) {
    	
        int result = 0;
        
        String query = " DELETE FROM q_board WHERE q_id = ? ";
        
        try {
        	
        	psmt = conn.prepareStatement(query);
        	
            psmt.setInt(1, Integer.parseInt(q_id));
          
            result = psmt.executeUpdate();
            
        } catch (Exception e) {
        	
            System.out.println("게시물 삭제 중 예외 발생");
            e.printStackTrace();
        }
        return result;
    }

    // 게시물 수정
    public int updatePost(Q_BoardDTO dto) {
        int result = 0;
        
        String query = "UPDATE q_board SET title = ?, content = ? WHERE q_id = ? AND user_id = ?";
        
        
        try {
        	
        	psmt = conn.prepareStatement(query);
        	
            psmt.setString(1, dto.getTitle());
            psmt.setString(2, dto.getContent());
            psmt.setInt(3, dto.getQ_id());            
            psmt.setString(4, dto.getUser_id());

            result = psmt.executeUpdate();
            
        } catch (Exception e) {
        	
            System.out.println("게시물 수정 중 예외 발생");
            e.printStackTrace();
        }
        
        return result;
    }
}
