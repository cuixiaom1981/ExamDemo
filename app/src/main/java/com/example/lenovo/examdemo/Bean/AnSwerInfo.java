package com.example.lenovo.examdemo.Bean;


public class AnSwerInfo {

	public int questionId; // 试题主键
	public String questionName; // 试题题目
	public String QuestionFor; // （0模拟试题，1竞赛试题）
	public String QuestionType; // 试题类型
	public String analysis; // 试题分析
	public String rightAnswer; // 正确答案
	public String optionA; // 正确答案A
	public String optionB; // 正确答案B
	public String optionC; // 正确答案C
	public String optionD; // 正确答案D
	public String optionE; // 正确答案E
	public String score; // 分值
	public String option_type; // 是否是图片题0是1否
	public String isSelect; // 是否选择0是1否
	public String answer;//点击答案
	public String content; //题型描述
	public String caseQuestion; //案例题案例
	public String questionType; //区分案例题与视频题
	public int perScore; //题型分值
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public String getQuestionName() {
		return questionName;
	}
	public void setQuestionName(String questionName) {
		this.questionName = questionName;
	}
	public String getQuestionFor() {
		return QuestionFor;
	}
	public void setQuestionFor(String questionFor) {
		QuestionFor = questionFor;
	}
	public String getQuestionType() {
		return QuestionType;
	}
	public void setQuestionType(String questionType) {
		QuestionType = questionType;
	}
	public String getAnalysis() {
		return analysis;
	}
	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}

	public String getRightAnswer() {
		return rightAnswer;
	}

	public void setRightAnswer(String rightAnswer) {
		this.rightAnswer = rightAnswer;
	}

	public String getOptionA() {
		return optionA;
	}
	public void setOptionA(String optionA) {
		this.optionA = optionA;
	}
	public String getOptionB() {
		return optionB;
	}
	public void setOptionB(String optionB) {
		this.optionB = optionB;
	}
	public String getOptionC() {
		return optionC;
	}
	public void setOptionC(String optionC) {
		this.optionC = optionC;
	}
	public String getOptionD() {
		return optionD;
	}
	public void setOptionD(String optionD) {
		this.optionD = optionD;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getIsSelect() {
		return isSelect;
	}
	public void setIsSelect(String isSelect) {
		this.isSelect = isSelect;
	}
//	public String getOptionE() {
//		return optionE;
//	}
//	public void setOptionE(String optionE) {
//		this.optionE = optionE;
//	}
	public String getOption_type() {
		return option_type;
	}
	public void setOption_type(String option_type) {
		this.option_type = option_type;
	}

	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public String getCaseQuestion() {
		return caseQuestion;
	}
	public void setCaseQuestion(String caseQuestion) {
		this.caseQuestion = caseQuestion;
	}

	public int getPerScore() {
		return perScore;
	}
	public void setPerScore(int perScore) {
		this.perScore = perScore;
	}

	public String getquestionType() {
		return questionType;
	}
	public void setquestionType(String questionType) {
		this.questionType = questionType;
	}

}
