package com.vnrit.mykidsdrawing.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "quiz_questions")
public class QuizQuestions extends AuditModel{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String grade;
	
	@Column
	private String question;
	
	@Column
    private String filename;
	
	@Column
    private String tumbnailName;
	
	@Column
    private Boolean status ;
	
	@Column
    private Boolean isquestionComplete;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "subcategory_id",nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private category subcategory;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getTumbnailName() {
		return tumbnailName;
	}

	public void setTumbnailName(String tumbnailName) {
		this.tumbnailName = tumbnailName;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Boolean getIsquestionComplete() {
		return isquestionComplete;
	}

	public void setIsquestionComplete(Boolean isTopicComplete) {
		this.isquestionComplete = isTopicComplete;
	}

	public category getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(category subcategory) {
		this.subcategory = subcategory;
	}


}