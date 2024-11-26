package login;

/*import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import jakarta.servlet.ServletContext;
import login.MemberDTO;*/

import java.sql.SQLException;
import common.DBConnPool;

public class MemberDAO extends DBConnPool {
		
	// 회원정보 받음
	public int insertMember(MemberDTO dto) {
		
		String sql = "INSERT INTO member (user_id, pass, name, email, regi_date) VALUES (?, ?, ?, ?,sysdate)";



       try {
    	   
    	   System.out.println("Executing SQL: " + sql);
           psmt = conn.prepareStatement(sql);
           
           psmt.setString(1, dto.getUser_id());     
           psmt.setString(2, dto.getPass());     
           psmt.setString(3, dto.getName());      
           psmt.setString(4, dto.getEmail());     
           
           
           return psmt.executeUpdate();
           
       } catch (Exception e) {
           e.printStackTrace();
           
       }
       
	return 0;
    }
	
	//중복 확인
	public int UserCheck(String userId) {
	    String query = "SELECT COUNT(*) FROM member WHERE user_id = ?";
	    try  {
	    	psmt = conn.prepareStatement(query);
	    	
	        psmt.setString(1, userId);
	        rs = psmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getInt(1); // 중복된 경우 true 반환
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return -1;
	}
	
	public boolean NameCheck(String name) {
	    String query = "SELECT COUNT(*) FROM member WHERE name = ?";
	    try  {
	    	psmt = conn.prepareStatement(query);
	    	
	        psmt.setString(1, name);
	        rs = psmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getInt(1) > 0; // 중복된 경우 true 반환
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	public boolean EmailCheck(String email) {
	    String query = "SELECT COUNT(*) FROM member WHERE email = ?";
	    try  {
	    	psmt = conn.prepareStatement(query);
	    	
	        psmt.setString(1, email);
	        rs = psmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getInt(1) > 0; // 중복된 경우 true 반환
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	// 회원정보 확인
	public MemberDTO getMemberDTO(String user_id, String password) {
		
		MemberDTO dto = new MemberDTO();
		
		String query = "SELECT * FROM member WHERE user_id=? AND pass=?";

		try {
			
			psmt = conn.prepareStatement(query);
		
			psmt.setString(1, user_id);
			psmt.setString(2, password);
			
			rs = psmt.executeQuery();
			
			
			if (rs.next()) {
			
				dto.setUser_id(rs.getString("user_id"));
				dto.setPass(rs.getString("pass"));
				dto.setName(rs.getString("name"));
				dto.setEmail(rs.getString("email"));
				dto.setRegi_date(rs.getDate("regi_date"));
			}	
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
		//회원정보를 저장한 DTO객체를 반환한다.
		return dto;
	}
	
	public MemberDTO getMember(String userId) {
	    MemberDTO member = null;
	    String query = "SELECT user_id, name, email FROM member WHERE user_id = ?";

	    try {
	        psmt = conn.prepareStatement(query);
	        psmt.setString(1, userId);

	        rs = psmt.executeQuery();

	        if (rs.next()) {
	            member = new MemberDTO();
	            member.setUser_id(rs.getString("user_id"));
	            member.setName(rs.getString("name"));
	            member.setEmail(rs.getString("email"));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (psmt != null) psmt.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return member;
	}
	
	public void updateMember(MemberDTO dto) throws SQLException {
		
	    String query = "UPDATE member SET name = ?, email = ?, pass = NVL(?, pass) WHERE user_id = ?";
	    
	    try  {
	    	
	    	psmt = conn.prepareStatement(query);
	    	
	        psmt.setString(1, dto.getName());
	        psmt.setString(2, dto.getEmail());
	        psmt.setString(3, dto.getPass()); 
	        psmt.setString(4, dto.getUser_id());
	        
	        psmt.executeUpdate();
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw e;
	    }
	}
	
	// 이메일로 사용자 찾기
	public MemberDTO findByEandI(String email, String user_id) {
	    String query = "SELECT * FROM member WHERE email = ? and user_id = ?";
	    MemberDTO member = null;

	    try {
	        psmt = conn.prepareStatement(query);
	        psmt.setString(1, email);
	        psmt.setString(2, user_id);
	        rs = psmt.executeQuery();

	        if (rs.next()) {
	            member = new MemberDTO();
	            member.setUser_id(rs.getString("user_id"));
	            member.setName(rs.getString("name"));
	            member.setEmail(rs.getString("email"));
	            member.setPass(rs.getString("pass")); // 비밀번호도 필요할 경우 추가
	            member.setRegi_date(rs.getDate("regi_date"));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (psmt != null) psmt.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return member;
	}

	// 비밀번호 업데이트
	public void updatePassword1(String userId, String newPassword) {
	    String query = "UPDATE member SET pass = ? WHERE user_id = ?";

	    try {
	        psmt = conn.prepareStatement(query);
	        
	        psmt.setString(1, newPassword);
	        psmt.setString(2, userId);
	        
	        System.out.println("Executing query: " + query);
	        int rowsAffected = psmt.executeUpdate(); 
	        System.out.println("Rows affected2: " + rowsAffected);
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	    	
	        try {
	        	
	            if (psmt != null) psmt.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
	// 비밀번호 업데이트
		public void updatePassword(String userId, String newPassword) {
		    String query = "UPDATE member SET pass = ? WHERE user_id = ?";

		    try {
		        psmt = conn.prepareStatement(query);
		        
		        psmt.setString(1, newPassword);
		        psmt.setString(2, userId);
		        
		        int rowsAffected = psmt.executeUpdate(); 
		        System.out.println("Rows affected1: " + rowsAffected);
		        
		    } catch (SQLException e) {
		        e.printStackTrace();
		    } finally {
		    	
		        try {
		        	
		            if (psmt != null) psmt.close();
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		}
	
	// 사용자 ID로 사용자 정보 조회
	public MemberDTO findById(String userId) {
	    String query = "SELECT user_id, pass, email FROM member WHERE user_id = ?";
	    MemberDTO dto = null;

	    try {
	        psmt = conn.prepareStatement(query);
	        psmt.setString(1, userId);

	        rs = psmt.executeQuery();
	        if (rs.next()) {
	            dto = new MemberDTO();
	            dto.setUser_id(rs.getString("user_id"));
	            dto.setPass(rs.getString("pass"));
	            dto.setEmail(rs.getString("email"));
	            
	         // 디버깅 코드
	            System.out.println("디버깅 - 데이터베이스 조회 결과:");
	            System.out.println("UserId: " + dto.getUser_id());
	            System.out.println("Name: " + dto.getName());
	            System.out.println("Email: " + dto.getEmail());
	            
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (psmt != null) psmt.close();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	    }

	    return dto; // 결과가 없으면 null 반환
	}
		
	// 이메일로 사용자 ID 조회
	public MemberDTO findUserIdByEmail(String email) {
	    String query = "SELECT user_id, name, email FROM member WHERE email = ?";
	    MemberDTO member = null;

	    try {
	        psmt = conn.prepareStatement(query);
	        psmt.setString(1, email);
	        rs = psmt.executeQuery();

	        if (rs.next()) {
	            member = new MemberDTO();
	            member.setUser_id(rs.getString("user_id"));
	            member.setName(rs.getString("name"));
	            member.setEmail(rs.getString("email"));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (psmt != null) psmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return member;
	}
}
