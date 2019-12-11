package com.example.lenovo.examdemo.Bean;

import java.util.List;

/**
 * Created by lenovo on 2019/10/18.
 * 获取试题接口
 */

public class QuestionBean {
    private List<String> question;
    private List<String> A;
    private List<String> B;
    private List<String> C;
    private List<String> D;
    private List<String> rightAnswer;
    private List<String> questionId;
    private String department;

    public List<String> getQuestion() {
        return question;
    }

    public void setQuestion(List<String> question) {
        this.question = question;
    }

    public List<String> getA() {
        return A;
    }

    public void setA(List<String> a) {
        A = a;
    }

    public List<String> getB() {
        return B;
    }

    public void setB(List<String> b) {
        B = b;
    }

    public List<String> getC() {
        return C;
    }

    public void setC(List<String> c) {
        C = c;
    }

    public List<String> getD() {
        return D;
    }

    public void setD(List<String> d) {
        D = d;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public QuestionBean(List question,List A,List B,List C,List D,List rightAnswer,List questionId,String department) {
        this.question = question;
        this.A=A;
        this.B=B;
        this.C=C;
        this.D=D;
        this.rightAnswer=rightAnswer;
        this.questionId=questionId;
    }
}
