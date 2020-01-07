package com.example.lenovo.examdemo.Utils;


import java.util.ArrayList;
import java.util.List;

public class ConstantData {

	public static List<String> answerName = new ArrayList<String>(40);
	public static List<String> answerOptionA = new ArrayList<String>(40);
	public static List<String> answerOptionB = new ArrayList<String>(40);
	public static List<String> answerOptionC = new ArrayList<String>(40);
	public static List<String> answerOptionD = new ArrayList<String>(40);
	public static List<String> rightAnswer = new ArrayList<String>(40);
	public static List<Integer> answerId = new ArrayList<Integer>(40);
	public static List<String> Content = new ArrayList<String>(40); //题目描述 --- 每个题型一个、每个案例一个
	public static List<String> caseQuestion = new ArrayList<String>(40); //案例题 案例
	public static List<Integer> perScore = new ArrayList<Integer>(40);  //每道题分值
	public static List<String> questionType = new ArrayList<String>(40); //案例题类型

	public static String token = "";   //统一存储token
	public static String stuid = "";   //统一存储考生id


}
