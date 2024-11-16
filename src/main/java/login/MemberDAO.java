package login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import jakarta.servlet.ServletContext;
import login.MemberDTO;

public class MemberDAO  {
	
	public Connection conn;
	public Statement stmt;
	public PreparedStatement psmt;
	public ResultSet rs;
	
	public MemberDAO() {
		try {
		
			Context initCtx = new InitialContext();
			Context ctx = (Context)initCtx.lookup("java:comp/env");
			DataSource source = (DataSource)ctx.lookup("dbcp_myoracle");
			conn = source.getConnection();
			System.out.println("DB 커넥션 풀 연결 성공");
				}
			catch (Exception e) {
				System.out.println("DB 커넥션 풀 연결 실패");
				e.printStackTrace();
				}
			}
	public void close() {
		
		try{
			
			if (rs != null) rs.close();
			if (stmt != null) stmt.close();
			if (psmt != null) psmt.close();
			if (conn != null) conn.close();
			
			System.out.println("DB 커넥션 풀 자원 반납");
		   }
		catch(Exception e) {
			e.printStackTrace();
		  }
	   }
	
	
	public int insertMember(MemberDTO dto) {
		
		String sql = "INSERT INTO member (user_id, pass, name, email,regi_date) VALUES (?, ?, ?, ?,sysdate)";



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
	
	public MemberDTO getMemberDTO(String user_id, String passward) {
		
		MemberDTO dto = new MemberDTO();
		
		String query = "SELECT * FROM member WHERE user_id=? AND pass=?";

		try {
			
			psmt = conn.prepareStatement(query);
		
			psmt.setString(1, user_id);
			psmt.setString(2, passward);
			
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
}
	