package com.example.lenovo.examdemo.Bean;

import java.util.List;

public class RequestAnswerBean {
	private List<String> answer;
	private List<String> questionId;
	private String stuId;
	private int score;
	public RequestAnswerBean(List<String> answer, List<String> questionId, String stuId, int score) {
		super();
		this.answer = answer;
		this.questionId = questionId;
		this.stuId = stuId;
		this.score = score;
	}
	public List<String> getAnswer() {
		return answer;
	}
	public void setAnswer(List<String> answer) {
		this.answer = answer;
	}
	public List<String> getQuestionId() {
		return questionId;
	}
	public void setQuestionId(List<String> questionId) {
		this.questionId = questionId;
	}
	public String getStuId() {
		return stuId;
	}
	public void setStuId(String stuId) {
		this.stuId = stuId;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}

	public static RequestAnswerBean toUpload(List answer,List questionId, String stuId,int score) {
		return new RequestAnswerBean(answer, questionId, stuId, score);
	}
}
