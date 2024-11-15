package notice_board;

import java.sql.Date;

public class NoticeDTO {

	private int Notice_id;
	private String user_id;
	private String title;
	private String content; 
	private Date created_date;
	private Date upeated_date;
	private int visit_count;
	private String attachment_link; 
	private int is_pinned;
	
	public int getNotice_id() {
		return Notice_id;
	}
	public void setNotice_id(int notice_id) {
		Notice_id = notice_id;
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
	public String getAttachment_link() {
		return attachment_link;
	}
	public void setAttachment_link(String attachment_link) {
		this.attachment_link = attachment_link;
	}
	public int getIs_pinned() {
		return is_pinned;
	}
	public void setIs_pinned(int is_pinned) {
		this.is_pinned = is_pinned;
	}
	
	
}
