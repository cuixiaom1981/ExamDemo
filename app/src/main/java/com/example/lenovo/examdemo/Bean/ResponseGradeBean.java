package com.example.lenovo.examdemo.Bean;


public class ResponseGradeBean {
	
	private String examName;	
	private int score;
	private String examTime;
	private int isUpload;
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getExamTime() {
		return examTime;
	}
	public void setExamTime(String examTime) {
		this.examTime = examTime;
	}
	public int getIsUpload() {
		return isUpload;
	}
	public void setIsUpload(int isUpload) {
		this.isUpload = isUpload;
	}
	@Override
	public String toString() {
		return "ResponseGradeBean [examName=" + examName + ", score=" + score + ", examTime=" + examTime + ", isUpload="
				+ isUpload + "]";
	}
	public ResponseGradeBean(String examName, int score, String examTime, int isUpload) {
		super();
		this.examName = examName;
		this.score = score;
		this.examTime = examTime;
		this.isUpload = isUpload;
	}
	
	
}
