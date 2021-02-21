package hr.fer.zemris.java.blogapp.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.gson.Gson;

/**
 * Model of a blog user. Users are stored in the database, which is managed by
 * hibernate.
 * 
 * @author jankovidakovic
 *
 */
@Entity
@Table(name = "blog_users")
public class BlogUser {

	private Long id; // unique identifier of the user
	private String firstName; // user's first name
	private String lastName; // user's last name
	private String nick; // user's nickname used to log into the application
	private String email; // user's email
	private String passwordHash; // user's password hash(PASSWORDS ARE NOT STORE
									// IN PLAIN TEXT!)

	// collection of blog entries that the user has created, which are available
	// for general public to view and comment on.
	private List<BlogEntry> blogEntries = new ArrayList<BlogEntry>();

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(nullable = false)
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(nullable = false)
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Column(nullable=false, unique=true)
	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	@Column(nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(nullable = false)
	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	@OneToMany(mappedBy = "creator")
	public List<BlogEntry> getBlogEntries() {
		return blogEntries;
	}

	public void setBlogEntries(List<BlogEntry> blogEntries) {
		this.blogEntries = blogEntries;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
