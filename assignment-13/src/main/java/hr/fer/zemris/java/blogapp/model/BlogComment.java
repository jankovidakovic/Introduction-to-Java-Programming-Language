package hr.fer.zemris.java.blogapp.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.Gson;

/**
 * Model of a comment which is posted to some blog entry by some user. Any user
 * can post a comment to anyone's blog entry. Blog comments are stored in the
 * database, which is managed by hibernate.
 * 
 * @author jankovidakovic
 *
 */
@Entity
@Table(name = "blog_comments")
public class BlogComment {

	private Long id; // unique identifier of the blog comment
	private BlogEntry blogEntry; // blog entry which the comment belongs to
	private String usersNick; // nick of the user that posted the entry
	private String message; // message that the comment displays
	private Date postedOn; // date at which the comment was posted

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(nullable = false)
	public BlogEntry getBlogEntry() {
		return blogEntry;
	}

	public void setBlogEntry(BlogEntry blogEntry) {
		this.blogEntry = blogEntry;
	}

	@Column(length = 100, nullable = false)
	public String getUsersNick() {
		return usersNick;
	}

	public void setUsersNick(String usersNick) {
		this.usersNick = usersNick;
	}

	@Column(length = 4096, nullable = false)
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getPostedOn() {
		return postedOn;
	}

	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BlogComment)) {
			return false;
		}
		BlogComment other = (BlogComment) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
