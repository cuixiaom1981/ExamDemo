package com.example.lenovo.examdemo.Bean;


public class ResponseQuestionBean {
	private String question;
	private String a;
	private String b;
	private String c;
	private String d;
	private String rightAnswer;
	private int questionId;
	public ResponseQuestionBean(String question, String a, String b, String c, String d, String rightAnswer, int questionId) {
		super();
		this.question = question;
		a = a;
		b = b;
		c = c;
		d = d;
		this.rightAnswer = rightAnswer;
		this.questionId = questionId;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getA() {
		return a;
	}
	public void setA(String a) {
		a = a;
	}
	public String getB() {
		return b;
	}
	public void setB(String b) {
		b = b;
	}
	public String getC() {
		return c;
	}
	public void setC(String c) {
		c = c;
	}
	public String getD() {
		return d;
	}
	public void setD(String d) {
		d = d;
	}
	public String getRightAnswer() {
		return rightAnswer;
	}
	public void setRightAnswer(String rightAnswer) {
		this.rightAnswer = rightAnswer;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

}
