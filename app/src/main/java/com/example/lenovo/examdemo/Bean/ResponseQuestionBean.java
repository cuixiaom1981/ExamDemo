package com.example.lenovo.examdemo.Bean;

import java.util.List;

public class ResponseQuestionBean {
private List<String> queston;
private List<String> a;
private List<String> b;
private List<String> c;
private List<String> d;
private List<String> rightAnswer;
private List<String> questionId;
private List<String> department;
public List<String> getQuestion() {
	return queston;
}
public void setQuestion(List<String> queston) {
	this.queston = queston;
}
public List<String> getA() {
	return a;
}
public void setA(List<String> a) {
	a = a;
}
public List<String> getB() {
	return b;
}
public void setB(List<String> b) {
	b = b;
}
public List<String> getC() {
	return c;
}
public void setC(List<String> c) {
	c = c;
}
public List<String> getD() {
	return d;
}
public void setD(List<String> d) {
	d = d;
}
public List<String> getRightAnswer() {
	return rightAnswer;
}
public void setRightAnswer(List<String> rightAnswer) {
	this.rightAnswer = rightAnswer;
}
public List<String> getQuestionId() {
	return questionId;
}
public void setQuestionId(List<String> questionId) {
	this.questionId = questionId;
}
public List<String> getDepartment() {
	return department;
}
public void setDepartment(List<String> department) {
	this.department = department;
}
public ResponseQuestionBean(List<String> queston, List<String> a, List<String> b, List<String> c, List<String> d,
		List<String> rightAnswer, List<String> questionId, List<String> department) {
	super();
	this.queston = queston;
	this.a = a;
	this.b = b;
	this.c = c;
	this.d = d;
	this.rightAnswer = rightAnswer;
	this.questionId = questionId;
	this.department = department;
}
	public String toString() {
		return "ResponseQuestionBean{" +
				"question=" + queston +
				", A=" + a +
				", B=" + b +
				", C=" + c +
				", D=" + d +
				", rightAnswer=" + rightAnswer +
				", questionId=" + questionId +
				", department=" + department +
				'}';
	}

}
