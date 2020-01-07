package com.example.lenovo.examdemo.Bean;

import java.util.List;

public class CaseQuestion {
private String introduce;
private String questionType;
private String question;
private int perScore;
private int questionId;
private List<ResponseQuestionBean> questions;
public CaseQuestion(String introduce, String questionType, String question, int perScore, int questionId,
		List<ResponseQuestionBean> questions) {
	super();
	this.introduce = introduce;
	this.questionType = questionType;
	this.question = question;
	this.perScore = perScore;
	this.questionId = questionId;
	this.questions = questions;
}
public String getIntroduce() {
	return introduce;
}
public void setIntroduce(String introduce) {
	this.introduce = introduce;
}
public String getQuestionType() {
	return questionType;
}
public void setQuestionType(String questionType) {
	this.questionType = questionType;
}
public String getQuestion() {
	return question;
}
public void setQuestion(String question) {
	this.question = question;
}
public int getPerScore() {
	return perScore;
}
public void setPerScore(int perScore) {
	this.perScore = perScore;
}
public int getQuestionId() {
	return questionId;
}
public void setQuestionId(int questionId) {
	this.questionId = questionId;
}
public List<ResponseQuestionBean> getQuestions() {
	return questions;
}
public void setQuestions(List<ResponseQuestionBean> questions) {
	this.questions = questions;
}

}
