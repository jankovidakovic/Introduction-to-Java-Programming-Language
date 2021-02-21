package hr.fer.zemris.java.blogapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.google.gson.Gson;

/**
 * Model of a blog entry which is created by some existing user of the blog
 * application, and can be viewed by anyone that requests to see it. Blog
 * entries are stored in the database, which is managed by hibernate.
 * 
 * @author jankovidakovic
 *
 */
@Entity
@Table(name = "blog_entries")
public class BlogEntry {

	private Long id; // unique identifier of blog entry
	private List<BlogComment> comments = new ArrayList<>(); // comments made on
															// the blog
	private Date createdAt; // date at which the blog entry was created
	private Date lastModifiedAt; // date at which the blog entry was last
									// modified
	private String title; // title of the blog entry
	private String text; // text content of the blog entry
	private BlogUser creator; // user that created the entry

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToMany(mappedBy = "blogEntry", fetch = FetchType.LAZY,
			cascade = CascadeType.PERSIST, orphanRemoval = true)
	public List<BlogComment> getComments() {
		return comments;
	}

	public void setComments(List<BlogComment> comments) {
		this.comments = comments;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	@Column(length = 200, nullable = false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(length = 4096, nullable = false)
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	// TODO - check annotations
	@ManyToOne
	public BlogUser getCreator() {
		return creator;
	}

	public void setCreator(BlogUser creator) {
		this.creator = creator;
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
		if (!(obj instanceof BlogEntry)) {
			return false;
		}
		BlogEntry other = (BlogEntry) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
