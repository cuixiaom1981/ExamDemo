package com.example.lenovo.examdemo.Bean;

import java.util.List;

public class ResponsePaperBean {
private SelectQuestion select;
private List<CaseQuestion> caseQuestions;
public ResponsePaperBean(SelectQuestion select, List<CaseQuestion> caseQuestions) {
	super();
	this.select = select;
	this.caseQuestions = caseQuestions;
}
public SelectQuestion getSelect() {
	return select;
}
public void setSelect(SelectQuestion select) {
	this.select = select;
}
public List<CaseQuestion> getCaseQuestions() {
	return caseQuestions;
}
public void setCaseQuestions(List<CaseQuestion> caseQuestions) {
	this.caseQuestions = caseQuestions;
}
}
