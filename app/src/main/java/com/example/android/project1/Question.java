package com.example.android.project1;

import java.util.HashMap;
import java.util.Map;

public class Question {

    private String question;
    private String answer;
    private String questionid;


    public Question(){


    }

    public Question(String question, String answer, String questionid) {
        this.question = question;
        this.answer = answer;
        this.questionid = questionid;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("questionid", questionid);
        result.put("question", question);
        result.put("answer", answer);

        return result;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestionid() {
        return questionid;
    }

    public void setQuestionid(String questionid) {
        this.questionid = questionid;
    }
}
