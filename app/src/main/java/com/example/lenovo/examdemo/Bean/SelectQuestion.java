package com.example.lenovo.examdemo.Bean;

import java.util.List;

public class SelectQuestion {
private String introduce;
private int perScoer;
private List<ResponseQuestionBean> questions;
public SelectQuestion(String introduce, int perScoer, List<ResponseQuestionBean> questions) {
	super();
	this.introduce = introduce;
	this.perScoer = perScoer;
	this.questions = questions;
}
public String getIntroduce() {
	return introduce;
}
public void setIntroduce(String introduce) {
	this.introduce = introduce;
}
public int getPerScoer() {
	return perScoer;
}
public void setPerScoer(int perScoer) {
	this.perScoer = perScoer;
}
public List<ResponseQuestionBean> getQuestions() {
	return questions;
}
public void setQuestions(List<ResponseQuestionBean> questions) {
	this.questions = questions;
}
}
