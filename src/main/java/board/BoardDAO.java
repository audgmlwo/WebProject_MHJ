package board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import common.DBConnPool;

public class BoardDAO extends DBConnPool {
	
	
	public int selectCount(Map<String, Object>map) {
		int totalCount = 0;
		
		//오라클의 그룹함수 count()를 사용해서 쿼리문을 작성
		String query = "SELECT COUNT(*) FROM board";
		
		//매개변수로 전달된 검색어가 있는 경우에만 where 절을 동적으로 추가
		// "searchField" : 컬럼 // "searchWord" : 조건(검색어)
		if (map.get("searchWord") != null) {
		    query += " WHERE " + map.get("searchField") +" "+ " LIKE '%" + map.get("searchWord") + "%'";
		}
		try {
			//Statement 인스턴스 생성(정적쿼리문 실행)
			stmt = conn.createStatement();
			
			//쿼리문을 실행한 후 결과를 ResultSet으로 반환받는다.
			rs = stmt.executeQuery(query);
			
			/*
			 count()함수는 조건에 상관없이 항상 결과가 인출되므로
			 if 문과 같은 조건절 없이 바로 next()함수를 실행할 수 있다.
			 */ 
			rs.next(); 
			totalCount = rs.getInt(1);
			
		} 
		catch (Exception e) {
			System.out.println("게시물 카운트 중 예외 발생");
			e.printStackTrace();
		}
		
		return totalCount;
	}
	// 게시판 목록에 출력할 레코드를 인출하기 위한 메서드
	public List<BoardDTO> selectList(Map<String, Object>map) {
		//오라클에서 인출한 레코드를 저장하기 위한 List 생성
		List<BoardDTO> board = new Vector<BoardDTO>();
		
		//레코드 인출을 위한 쿼리문 작성
		String query = "SELECT * FROM board" ;
		
		//검색어 입력 여부에 따라 WHERE 절은 조건부로 추가됨
		if (map.get("searchWord") != null) {
		    query += " WHERE " + map.get("searchField") + " LIKE '%" + map.get("searchWord") + "%'";
		}
		//일련번호의 내림차순으로 정렬한 후 인출한다.(게시판은 항상 최근에 작성한 게시물이 상단에 노출되어야 한다.)
		query += " ORDER BY Board_id DESC";
		
		
		try {
			
			//prepareStatement 인스턴스 생성
			psmt = conn.prepareStatement(query);
			
			//쿼리문 실행 및 결과반환(ResultSet)
			rs = psmt.executeQuery();
			
			//ResultSet의 크기만큼 즉, 인출된 레코드의 갯수만큼 반복
			
			while (rs.next()) {
				
				 //하나의 레코드를 저장하기 위해 DTO인스턴스 생성
				 BoardDTO dto = new BoardDTO();
				 
				 /*
				  ResultSet 인스턴스에서 데이터를 추출할때 멤버변수의 타입에따라
				  getString, getInt(),getDate()로 구분하여 호출한다.
				  이 데이터를 DTO의 setter 를 이용해서 저장한다.
				  */
				 dto.setBoard_id(rs.getInt(1));
				 dto.setBoard_type(rs.getString(2));
				 dto.setUser_id(rs.getString(3));
				 dto.setTitle(rs.getString(4));
				 dto.setContent(rs.getString(5)); 
				 dto.setCreated_date(rs.getDate(6)); 
				 dto.setUpdated_date(rs.getDate(7)); 
				 dto.setO_file(rs.getString(8)); 
				 dto.setS_file(rs.getString(9)); 
				 dto.setDown_count(rs.getInt(10)); 
				 dto.setVisit_count(rs.getInt(11));
				 
				 //레코드가 저장된 DTO를 list 에 갯수만큼 추가한다.
				 board.add(dto);
				}
			}
			catch (Exception e) {
				System.out.println("게시물 조회 중 예외 발생");
				e.printStackTrace();
				
			}
			//마지막으로 인출할 레코드를 저장한 List 를 반환한다.
			return board;
	
	}
	
	//글쓰기 처리를 위한 쿼리문 실행
	public int insertWrite(BoardDTO dto) {
		int result = 0;
		try {
			/* default값이 있는 3개의 컬럼을 제외한 나머지 컬럼에 대해서만
			 * insert 쿼리문을 작성. 일련번호 board_id의 경우에는 시퀀스를 사용. */
			
			String query = "INSERT INTO board ( "
		             + "board_id, board_type, user_id, title, content, updated_date, o_file, s_file) "
		             + "VALUES (seq_board_num.NEXTVAL, ?, ?, ?, ?, SYSDATE, ?, ?)";
			
			
			//쿼리문을 인수로 preparedStatement 인스턴스 생성
			psmt = conn.prepareStatement(query);
			
			String boardType = dto.getBoard_type();
	        if (boardType == null || boardType.isEmpty()) {
	        	boardType = "fre"; 
	        }
			
	        System.out.println("BOARD_TYPE: " + boardType);
	        
			//인스턴스를 통해 인파라미터 설정
	        psmt.setString(1, boardType);
			psmt.setString(2, dto.getUser_id());
			psmt.setString(3, dto.getTitle());
			psmt.setString(4, dto.getContent());
			psmt.setString(5, dto.getO_file());
			psmt.setString(6, dto.getS_file());
			
			//쿼리문 실행. insert 쿼리의 경우 입력된 행의 갯수가 반환됨. ==> (insert delete update) 3개의 쿼리문은 정수타입의 반환값을 가짐.
			result = psmt.executeUpdate();
		} 
		catch (Exception e) {
			System.out.println("게시물 입력 중 예외 발생");
			e.printStackTrace();
		}
		return result;
	}
	public BoardDTO selectView(String board_id, String board_type) {
		
		//인출한 레코드(board_id)를 저장하기 위해 DTO 생성
		BoardDTO dto = new BoardDTO();
		
		//내부조인(Inner join)을 이용해서 쿼리문 작성, member 테이블의 name 컬럼까지 포함. 
		//-> 테이블명에 별칭(Alise) 부여(표준 SQL 방식)
		String query1 = "SELECT Bo.*, Me.name FROM board Bo "
		+" INNER JOIN member Me ON Bo.user_id=Me.id "
		+" WHERE board_id = ? and WHERE board_type = ?";
		
		//오라클 방식의 join 쿼리문
		String query = "SELECT * FROM board Bo , "
		+" member Me WHERE Bo.id=Me.user_id and board_id=? and board_type = ? ";
		
		
		try {
			psmt = conn.prepareStatement(query);
			
			int board_id_int = Integer.parseInt(board_id);
			
			psmt.setInt(1, board_id_int);
			psmt.setString(2, board_type);
			
			rs = psmt.executeQuery();
			
			//하나의 게시물을 인출하므로 if 문으로 조건에 맞는 게시물이 있는지 확인
			if(rs.next()) {
				
				//결과를 DTO 객체에 저장.
				
				dto.setBoard_id(rs.getInt(1));
				dto.setBoard_type(rs.getString(2));
				dto.setUser_id(rs.getString(3));
				dto.setTitle(rs.getString(4));
				dto.setContent(rs.getString(5)); 
				dto.setCreated_date(rs.getDate(6)); 
				dto.setUpdated_date(rs.getDate(7)); 
				dto.setO_file(rs.getString(8)); 
				dto.setS_file(rs.getString(9)); 
				dto.setDown_count(rs.getInt(10)); 
				dto.setVisit_count(rs.getInt(11));
				dto.setName(rs.getString(12));
				
			}
		} 
		catch (Exception e) {
			System.out.println("게시물 상세보기 중 예외 발생");
			e.printStackTrace();
		}
		return dto;
	}
	
	//주어진 일련번호에 해당하는 게시물의 조회수를 1 증가시킵니다.
	public void updateVisitCount(String board_id, String board_type) {
		
		//visitcount 컬럼은 number 타입이므로 산술연산이 가능함.
		//1을 더한 결과를 컬럼에 재반영하는 형식으로 update 쿼리문 작성.
		String query = "UPDATE board SET "
					 +" visitcount=visitcount+1 "
					 +" board_id=? and board_type = ? ";
		try {
			psmt = conn.prepareStatement(query);
			
			int board_id_int = Integer.parseInt(board_id);
			psmt.setInt(1, board_id_int);
			psmt.setString(2, board_type);
			
			int result = psmt.executeUpdate();
			
			/*
			 쿼리실행시 주로 아래의 두가지 메서드를 사용한다.
			 excuteQuery(): select 계열의 쿼리문을 실행한다. 반환 타입은 resultSet.
			 executeUpdate() : insert, update, delete 계열의 쿼리문을 실행한다. 반환 타입은 int
			 
			 만약 쿼리 실행 후 별도의 반환값이 필요하지 않다면 위 2개의 메서드중 어떤것을 사용해도 무방하다.*/
			//psmt.excuteQuery
			
		} catch (Exception e) {
			System.out.println("게시물 조회수 증가 중 예외 발생");
			e.printStackTrace();
		
		}
	}
	public void downCountPlus(String board_id, String board_type) {
		String sql = "UPDATE board SET "
				 +" downcount=downcount+1 "
				 +" board_id=? and board_type = ? ";
		try {
			psmt = conn.prepareStatement(sql);
			
			int board_id_int = Integer.parseInt(board_id);
			psmt.setInt(1, board_id_int);
			psmt.setString(2, board_type);
			
			psmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("게시물 다운로드 조회수 증가 중 예외 발생");
			e.printStackTrace();
		
		}
	}
	
	public int deletePost(String board_id, String board_type) {
		int result = 0;
		try {
			String query = "DELETE FROM board board_id=? and board_type = ?";
			
			psmt = conn.prepareStatement(query);
			
			int board_id_int = Integer.parseInt(board_id);
			psmt.setInt(1, board_id_int);
			psmt.setString(2, board_type);
			
			result = psmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("게시물 삭제 중 예외 발생");
			e.printStackTrace();
		}
		return result;
	}
	
	//게시글 데이터를 받아 DB에 저장되어 있던 내용을 갱신합니다.(파일 업로드 지원)
	public int updatePost(BoardDTO dto) {
	    int result = 0;
	    try {
	        // UPDATE 쿼리: 특정 board_id의 게시물의 제목, 내용, 첨부파일을 수정
	        String query = "UPDATE board SET title = ?, content = ?, o_file = ?, s_file = ? WHERE board_id=? and board_type=? and user_id=? ";
	        
	        // PreparedStatement 인스턴스 생성 및 인파라미터 설정
	        psmt = conn.prepareStatement(query);
	        psmt.setString(1, dto.getTitle());   // 수정할 제목
	        psmt.setString(2, dto.getContent());  // 수정할 내용
	        psmt.setString(3, dto.getO_file());    // 수정할 원본 파일명
	        psmt.setString(4, dto.getS_file());    // 수정할 저장된 파일명
	        psmt.setInt(5, dto.getBoard_id());      // 게시물 번호
	        psmt.setString(6, dto.getBoard_type()); // 게시물 타입
	        psmt.setString(7, dto.getUser_id());  

	        // 쿼리 실행
	        result = psmt.executeUpdate();
	        
	    } catch (Exception e) {
	    	
	        System.out.println("게시물 수정 중 예외 발생");
	        e.printStackTrace();
	    }
	    return result;
	}
	
	public List<BoardDTO> selectListPage(Map<String, Object> map) {
		
	    List<BoardDTO> board = new Vector<>();
	    String query = 
	        "SELECT * FROM (" +
	        "  SELECT Tb.*, ROWNUM rNum FROM (" +
	        "    SELECT * FROM board";

	    // 검색 조건 추가
	    if (map.get("searchWord") != null && !map.get("searchWord").toString().isEmpty()) {
	        query += " WHERE " + map.get("searchField") + " LIKE ?";
	    }
	    query += " ORDER BY board_id DESC" +
	             "  ) Tb" +
	             " ) WHERE rNum BETWEEN ? AND ?";

	    try {
	        psmt = conn.prepareStatement(query);

	        // 파라미터 바인딩
	        int paramIndex = 1;
	        if (map.get("searchWord") != null && !map.get("searchWord").toString().isEmpty()) {
	            psmt.setString(paramIndex++, "%" + map.get("searchWord") + "%");
	        }
	        psmt.setInt(paramIndex++, Integer.parseInt(map.get("start").toString()));
	        psmt.setInt(paramIndex, Integer.parseInt(map.get("end").toString()));
			
			while (rs.next()) {
				BoardDTO dto = new BoardDTO();
				
				dto.setBoard_id(rs.getInt("board_id"));
				dto.setBoard_type(rs.getString(2));
				dto.setUser_id(rs.getString(3));
				dto.setTitle(rs.getString(4));
				dto.setContent(rs.getString(5)); 
				dto.setCreated_date(rs.getDate(6)); 
				dto.setUpdated_date(rs.getDate(7)); 
				dto.setO_file(rs.getString(8)); 
				dto.setS_file(rs.getString(9)); 
				dto.setDown_count(rs.getInt(10)); 
				dto.setVisit_count(rs.getInt(11));
				 
				board.add(dto);
			}

		} catch (Exception e) {
	        System.out.println("게시물 조회 중 예외 발생: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return board;
	}
}





