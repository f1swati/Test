package org.gradle;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;

public class Poll {

	@JsonView({ Views.User.class })
	private String id;
	@NotBlank(message="{question.missing}")
	@JsonView({ Views.User.class })
	private String question;
	//@Future(message="{startdate.invalid}")
	@JsonView({ Views.User.class })
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
	private Date started_at;
	//@Future(message="{enddate.invalid}")
	@JsonView({ Views.User.class })
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
	private Date expired_at;@DateTimeFormat
	@NotNull(message="{options.missing}")
	@JsonView({ Views.User.class })
	private String choice[];
	@JsonView(Views.Moderator.class)
	private List<Integer> results;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		SecureRandom random=new SecureRandom();
		this.id=new BigInteger(30, random).toString(36).toUpperCase();
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Date getStarted_at() {
		return started_at;
	}

	public void setStarted_at(Date started_at) {
		this.started_at = started_at;
	}

	public Date getExpired_at() {
		return expired_at;
	}

	public void setExpired_at(Date expired_at) {
		this.expired_at = expired_at;
	}

	public String[] getChoice() {
		return choice;
	}

	public void setChoice(String[] choice) {
		this.choice = choice;
	}

	public List<Integer> getResults() {
		if(null==results){
			results=new ArrayList<Integer>();
			for (int i = 0; i < choice.length; i++) {
				results.add(0);	
			}
		}
		return results;
	}

	public void setResults(List<Integer> results) {
		this.results = results;
	}

}
