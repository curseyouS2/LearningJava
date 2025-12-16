package com.team.model;

import java.io.Serializable;

public class Question implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;                 // 고유 번호 (PK) - 오답노트의 핵심
    private QuestionType type;      // 문제 유형 (Enum)
    private String questionText;    // 문제 지문
    private String answer;          // 정답
    private String explanation;     // 해설
    private String[] options;       // 보기 (빈칸채우기용)

    // 생성자 1: 타자 게임용
    public Question(int id, String word) {
        this(id, QuestionType.KEYWORD, word, word, "키워드: " + word, null);
    }

    // 생성자 2: OX 퀴즈용
    public Question(int id, String text, String answer, String explanation) {
        this(id, QuestionType.OX, text, answer, explanation, null);
    }

    // 생성자 3: 빈칸 채우기 등 전체 필드용
    public Question(int id, QuestionType type, String text, String answer, String explanation, String[] options) {
        this.id = id;
        this.type = type;
        this.questionText = text;
        this.answer = answer;
        this.explanation = explanation;
        this.options = options;
    }

    // 정답 체크 로직
    public boolean checkAnswer(String input) {
        if (input == null) return false;
        return answer.trim().equalsIgnoreCase(input.trim());
    }

    // Getters
    public int getId() { return id; }
    public QuestionType getType() { return type; }
    public String getQuestionText() { return questionText; }
    public String getAnswer() { return answer; }
    public String getExplanation() { return explanation; }
    public String[] getOptions() { return options; }

    @Override
    public String toString() {
        return "Q" + id + "[" + type + "]: " + questionText;
    }
}