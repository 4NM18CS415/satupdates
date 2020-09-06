package com.vnrit.mykidsdrawing.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Category")
public class category extends AuditModel{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String Topic;
	
	@Column
	private String subcategory;
	
	@Column
    private String filename;
	
	@Column
    private String tumbnailName;
	
	@Column
    private Boolean status ;
	
	@Column
    private Boolean isTopicComplete;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



	public String getTopic() {
		return Topic;
	}

	public void setTopic(String topic) {
		Topic = topic;
	}

	public String getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
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

	public Boolean getIsTopicComplete() {
		return isTopicComplete;
	}

	public void setIsTopicComplete(Boolean isTopicComplete) {
		this.isTopicComplete = isTopicComplete;
	}
	
	
}
	
