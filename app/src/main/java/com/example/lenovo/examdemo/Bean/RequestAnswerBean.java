package com.example.lenovo.examdemo.Bean;

import java.util.List;

public class RequestAnswerBean {

	private List<String> answer;
	private List<Integer> questionId;
	private List<Integer> isRight;
	private String stuId;
	private String examName;
	public RequestAnswerBean(List<String> answer, List<Integer> questionId, List<Integer> isRight, String stuId,
			String examName, int score) {
		super();
		this.answer = answer;
		this.questionId = questionId;
		this.isRight = isRight;
		this.stuId = stuId;
		this.examName = examName;
		this.score = score;
	}
	public List<String> getAnswer() {
		return answer;
	}
	public void setAnswer(List<String> answer) {
		this.answer = answer;
	}
	public List<Integer> getQuestionId() {
		return questionId;
	}
	public void setQuestionId(List<Integer> questionId) {
		this.questionId = questionId;
	}
	public List<Integer> getIsRight() {
		return isRight;
	}
	public void setIsRight(List<Integer> isRight) {
		this.isRight = isRight;
	}
	public String getStuId() {
		return stuId;
	}
	public void setStuId(String stuId) {
		this.stuId = stuId;
	}
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
	private int score;

	public static RequestAnswerBean toUpload(List answer,List questionId, List<Integer> isRight,String stuId,
											 String examName, int score) {
		return new RequestAnswerBean(answer, questionId, isRight,stuId, examName,score);
	}
}
