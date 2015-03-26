package org.gradle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


public class Moderator {
	
	private Integer id;
	@NotNull( message="{name.missing}")
	private String name;
	@NotNull(groups={Views.Moderator.class,Default.class},message="{email.missing}")
	private String email;
	@NotNull(groups={Views.Moderator.class,Default.class},message="{password.missing}")
	private String password;
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS")
	@JsonSerialize(contentAs=Date.class)
	private Calendar created_at;
	@JsonIgnore
	private List<String> pollIds;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Calendar getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Calendar created_at) {
		this.created_at = created_at;
	}
	public List<String> getPollIds() {
		return pollIds=null==pollIds?new ArrayList<String>():pollIds;
	}
	public void setPollIds(List<String> pollIds) {
		this.pollIds = pollIds;
	}
	
	
	
	

}
