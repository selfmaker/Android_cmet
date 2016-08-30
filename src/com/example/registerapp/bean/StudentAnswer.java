package com.example.registerapp.bean;


public class StudentAnswer {

	private String question_id;//题目id
//	private String questionType;//题目类型
	private String answer;//学生所选的选项
	private String username; //学生ID
	
	
	
	@Override
	public String toString() {
		return "StudentAnswer [question_id=" + question_id + ", answer="
				+ answer + ", username=" + username + "]";
	}
	public String getQuestion_id() {
		return question_id;
	}
	public void setQuestion_id(String question_id) {
		this.question_id = question_id;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

}