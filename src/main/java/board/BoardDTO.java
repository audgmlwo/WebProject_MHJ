package board;

import java.sql.Date;

public class BoardDTO {
		
	
	private int board_id;  
	private String board_typ;
	private String user_id;
	private String title;
	private String content; 
	private Date created_date;
	private Date upeated_date;
	private String o_file;
	private String s_file;
	private int down_count;
	private int visit_count;
	
	public int getBoard_id() {
		return board_id;
	}
	public void setBoard_id(int board_id) {
		this.board_id = board_id;
	}
	public String getBoard_typ() {
		return board_typ;
	}
	public void setBoard_typ(String board_typ) {
		this.board_typ = board_typ;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreated_date() {
		return created_date;
	}
	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}
	public Date getUpeated_date() {
		return upeated_date;
	}
	public void setUpeated_date(Date upeated_date) {
		this.upeated_date = upeated_date;
	}
	public String getO_file() {
		return o_file;
	}
	public void setO_file(String o_file) {
		this.o_file = o_file;
	}
	public String getS_file() {
		return s_file;
	}
	public void setS_file(String s_file) {
		this.s_file = s_file;
	}
	public int getDown_count() {
		return down_count;
	}
	public void setDown_count(int down_count) {
		this.down_count = down_count;
	}
	public int getVisit_count() {
		return visit_count;
	}
	public void setVisit_count(int visit_count) {
		this.visit_count = visit_count;
	}
}

