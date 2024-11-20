package q_board;

import java.sql.Date;
import java.util.List;

import na_board.NA_BoardDTO;

public class Q_BoardDTO {
		
	
	private int q_id;  
	private String user_id;
	private String title;
	private String content; 
	private Date created_date;
	private Date updated_date;
	private int visit_count;
	private int is_accepted;
	private String name;
	

	private List<NA_BoardDTO> answers;
	
	
	public int getQ_id() {
		return q_id;
	}
	public void setQ_id(int q_id) {
		this.q_id = q_id;
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
	public Date getUpdated_date() {
		return updated_date;
	}
	public void setUpdated_date(Date updated_date) {
		this.updated_date = updated_date;
	}
	public int getVisit_count() {
		return visit_count;
	}
	public void setVisit_count(int visit_count) {
		this.visit_count = visit_count;
	}
	public int getIs_accepted() {
		return is_accepted;
	}
	public void setIs_accepted(int is_accepted) {
		this.is_accepted = is_accepted;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<NA_BoardDTO> getAnswers() {
		return answers;
	}
	public void setAnswers(List<NA_BoardDTO> answers) {
		this.answers = answers;
	}
	
	
	
	
	
}
	
	
	
	
	