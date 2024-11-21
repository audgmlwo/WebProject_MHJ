package na_board;

import java.sql.*;
import java.util.*;
import common.DBConnPool;
import q_board.Q_BoardDTO;



public class NA_BoardDAO extends DBConnPool {

	

	
    // 게시물 작성
	public int insertWrite(NA_BoardDTO dto) {
	    int result = 0;
	    String query = "INSERT INTO na_board (a_id, q_id, user_id, content, created_date, updated_date) "
	                 + "VALUES (seq_board_num.NEXTVAL, ?, ?, ?, SYSDATE, SYSDATE)";

	    try {
	        psmt = conn.prepareStatement(query);
	        psmt.setInt(1, dto.getQ_id());
	        psmt.setString(2, dto.getUser_id());
	        psmt.setString(3, dto.getContent());
	        result = psmt.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return result;
	}



    // 게시물 상세 보기
    public NA_BoardDTO selectView(String a_id) {
    	
        NA_BoardDTO dto = null;
        
        
        String query = " SELECT Bo.*, Me.name FROM q_board Bo "
                     + " INNER JOIN member Me ON Bo.user_id = Me.user_id "
                     + " WHERE Bo.q_id = ? ";

        try {
        	
        	psmt = conn.prepareStatement(query);
        	
            psmt.setInt(1, Integer.parseInt(a_id));
            
            try (ResultSet rs = psmt.executeQuery()) {
            	
                if (rs.next()) {
                    dto = new NA_BoardDTO();
                    
                    dto.setQ_id(rs.getInt("a_id"));
                    dto.setQ_id(rs.getInt("q_id"));
                    dto.setUser_id(rs.getString("user_id"));
                    dto.setContent(rs.getString("content"));
                    dto.setCreated_date(rs.getDate("created_date"));
                    dto.setUpdated_date(rs.getDate("updated_date"));                    
                    dto.setName(rs.getString("name"));
                    
                }
            }
        } catch (Exception e) {
        	
            System.out.println("게시물 상세보기 중 예외 발생");
            e.printStackTrace();
        }
        return dto;
    }

        
    //답변만 조회
    
    public List<NA_BoardDTO> getAnswersByQuestionId(String q_id) {
        List<NA_BoardDTO> answers = new ArrayList<>();
        String query = "SELECT * FROM na_board WHERE q_id = ? ORDER BY created_date ASC";

        try {
            psmt = conn.prepareStatement(query);

            // 디버깅: 전달받은 q_id 값 확인
            System.out.println("q_id 값: " + q_id);

            // 디버깅: 쿼리와 매개변수 출력
            System.out.println("Executing Query: " + query);

            psmt.setInt(1, Integer.parseInt(q_id)); // String을 Integer로 변환
            rs = psmt.executeQuery();

            while (rs.next()) {
                NA_BoardDTO answer = new NA_BoardDTO();
                answer.setA_id(rs.getInt("a_id"));
                answer.setQ_id(rs.getInt("q_id"));
                answer.setUser_id(rs.getString("user_id"));
                answer.setContent(rs.getString("content"));
                answer.setCreated_date(rs.getDate("created_date"));
                answer.setUpdated_date(rs.getDate("updated_date"));
                answers.add(answer);
            }

            // 디버깅: 결과 확인
            System.out.println("Fetched Answers: " + answers);
        } catch (Exception e) {
            System.out.println("답변 조회 중 예외 발생");
            e.printStackTrace();
        }
        return answers;
    }
}