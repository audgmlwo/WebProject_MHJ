package na_board;

import java.sql.*;
import java.util.*;
import common.DBConnPool;
import q_board.Q_BoardDTO;



public class NA_BoardDAO extends DBConnPool {

	//질문&답변 조회
	public Q_BoardDTO getQuestionWithAnswers(String q_id) {
		
	Q_BoardDTO q_dto = null;
	List<NA_BoardDTO> answers = new ArrayList<>();
		
		
	// 질문 조회
	String Q_Query = "SELECT * FROM q_board WHERE q_id = ?";
   
    	 try {
    	        psmt = conn.prepareStatement(Q_Query);
    	        psmt.setInt(1, Integer.parseInt(q_id));
    	        rs = psmt.executeQuery();

    	        if (rs.next()) {
    	        	q_dto = new Q_BoardDTO();
    	        	q_dto.setQ_id(rs.getInt("q_id"));
    	        	q_dto.setUser_id(rs.getString("user_id"));
    	        	q_dto.setTitle(rs.getString("title"));
    	        	q_dto.setContent(rs.getString("content"));
    	        	q_dto.setCreated_date(rs.getDate("created_date"));
    	        	q_dto.setUpdated_date(rs.getDate("updated_date"));
    	        	q_dto.setVisit_count(rs.getInt("visit_count"));
    	        }
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    }	
    	
       
    	// 답변 조회
    	String NA_Query = "SELECT * FROM q_answer WHERE q_id = ? ORDER BY created_date ASC";
        
    	try {
            psmt = conn.prepareStatement(NA_Query);
            psmt.setInt(1, Integer.parseInt(q_id));
            rs = psmt.executeQuery();

            while (rs.next()) {
            	
                NA_BoardDTO answer = new NA_BoardDTO();
                
                answer.setA_id(rs.getInt("a_id"));
                answer.setQ_id(rs.getInt("q_id"));
                answer.setUser_id(rs.getString("user_id"));
                answer.setContent(rs.getString("content"));
                answer.setCreated_date(rs.getDate("created_date"));
                answers.add(answer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (q_dto != null) {
        	q_dto.setAnswers(answers);
        }
        return q_dto;
    }

	
    // 게시물 작성
    public int insertWrite(NA_BoardDTO dto) {
    	
        int result = 0;
        String query = " INSERT INTO q_board (a_id, q_id, user_id, content, created_date, updated_date) "
                     + " VALUES (seq_board_num.NEXTVAL, ?, ?, ?, SYSDATE, SYSDATE) ";

        try {
        	
        	psmt = conn.prepareStatement(query);
        	
        	psmt.setInt(1, dto.getQ_id());
	        psmt.setString(2, dto.getUser_id());
	        psmt.setString(3, dto.getContent());
      
            result = psmt.executeUpdate();
            
        } catch (Exception e) {
            System.out.println("게시물 입력 중 예외 발생");
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

    // 조회수 증가
    public void updateVisitCount(String a_id) {
        String query = "UPDATE q_board SET visit_count = visit_count + 1 WHERE a_id =?";
        
        try {
        	
        	psmt = conn.prepareStatement(query);
        	
            psmt.setInt(1, Integer.parseInt(a_id));
           
            psmt.executeUpdate();
            
        } catch (Exception e) {
        	
            System.out.println("게시물 조회수 증가 중 예외 발생");
            e.printStackTrace();
        }
    }

    
    // 게시물 삭제
    public int deletePost(String a_id) {
    	
        int result = 0;
        
        String query = " DELETE FROM na_board WHERE a_id = ? ";
        
        try {
        	
        	psmt = conn.prepareStatement(query);
        	
            psmt.setInt(1, Integer.parseInt(a_id));
          
            result = psmt.executeUpdate();
            
        } catch (Exception e) {
        	
            System.out.println("게시물 삭제 중 예외 발생");
            e.printStackTrace();
        }
        return result;
    }

    // 게시물 수정
    public int updatePost(NA_BoardDTO dto) {
        int result = 0;
        
        String query = "UPDATE q_board SET content = ? WHERE q_id = ? AND user_id = ?";
        
        
        try {
        	
        	psmt = conn.prepareStatement(query);
        	
            
            psmt.setString(1, dto.getContent());
            psmt.setInt(2, dto.getQ_id());            
            psmt.setString(3, dto.getUser_id());

            result = psmt.executeUpdate();
            
        } catch (Exception e) {
        	
            System.out.println("게시물 수정 중 예외 발생");
            e.printStackTrace();
        }
        
        return result;
    }
    
    //답변만 조회
    
    public List<NA_BoardDTO> getAnswersByQuestionId(String q_id) {
        List<NA_BoardDTO> answers = new ArrayList<>();
        String query = "SELECT * FROM na_board WHERE q_id = ? ORDER BY created_date ASC";

        try {
            psmt = conn.prepareStatement(query);
            psmt.setInt(1, Integer.parseInt(q_id));
            rs = psmt.executeQuery();

            while (rs.next()) {
                NA_BoardDTO dto = new NA_BoardDTO();
                dto.setA_id(rs.getInt("a_id"));
                dto.setQ_id(rs.getInt("q_id"));
                dto.setUser_id(rs.getString("user_id"));
                dto.setContent(rs.getString("content"));
                dto.setCreated_date(rs.getDate("created_date"));
                dto.setUpdated_date(rs.getDate("updated_date"));
                answers.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return answers;
    }    
    
    
}
