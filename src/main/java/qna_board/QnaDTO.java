package qna_board;

import java.sql.Date;

public class QnaDTO {

	private int qna_id;
	private int parent_id;
	private String post_type;
	private String user_id;
	private String title;
	private String content; 
	private Date created_date;
	private Date upeated_date;
	private int visit_count; 
	private int is_accepted;
	
	public int getQna_id() {
		return qna_id;
	}
	public void setQna_id(int qna_id) {
		this.qna_id = qna_id;
	}
	public int getParent_id() {
		return parent_id;
	}
	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}
	public String getPost_type() {
		return post_type;
	}
	public void setPost_type(String post_type) {
		this.post_type = post_type;
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
	
	
	
}
