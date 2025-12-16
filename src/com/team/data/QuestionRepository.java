package com.team.data;

import java.util.*;
import com.team.model.Question;
import com.team.model.QuestionType;

public class QuestionRepository {
    private static QuestionRepository instance;
    private Map<Integer, Question> questionMap; // 전체 문제 저장소 (ID로 조회)
    private Set<Integer> wrongQuestionIds;      // 틀린 문제 ID 목록 (중복 방지 Set 사용)

    // 싱글톤 패턴: 프로그램 내에서 데이터통은 딱 하나만 존재
    public static QuestionRepository getInstance() {
        if (instance == null) instance = new QuestionRepository();
        return instance;
    }

    private QuestionRepository() {
        questionMap = new HashMap<>();
        wrongQuestionIds = new HashSet<>();
        loadSampleData(); // 프로그램 시작 시 데이터 로드
    }

    // ★ 여기에 모든 문제를 등록합니다 (나중에 파일 읽기로 교체될 부분)
    private void loadSampleData() {
        // 1. 타자 연습 문제 (ID: 100번대)
        add(new Question(101, "public"));
        add(new Question(102, "static"));
        add(new Question(103, "void"));
        add(new Question(104, "class"));
        add(new Question(105, "interface"));
        add(new Question(106, "extends"));

        // 2. OX 퀴즈 문제 (ID: 200번대)
        add(new Question(201, "Java는 객체지향 언어이다.", "O", "Java는 대표적인 OOP 언어입니다."));
        add(new Question(202, "int는 참조형 변수이다.", "X", "int는 기본형(Primitive) 변수입니다."));
        add(new Question(203, "생성자는 리턴 타입이 없다.", "O", "생성자는 클래스명과 같으며 리턴 타입이 없습니다."));
        add(new Question(204, "배열의 인덱스는 1부터 시작한다.", "X", "배열은 0부터 시작합니다."));
        add(new Question(205, "String은 변경 불가능(Immutable)한 객체다.", "O", "String 값을 바꾸면 새로운 객체가 생성됩니다."));
        
        add(new Question(301, QuestionType.BLANK, 
        	    "다음 코드는 상속을 구현하는 부분이다. 빈칸에 알맞은 키워드는?", 
        	    "extends", 
        	    "클래스 상속은 extends, 인터페이스 구현은 implements를 사용합니다.", 
        	    new String[]{"extends", "implements", "import", "package"}));

        	add(new Question(302, QuestionType.BLANK, 
        	    "객체의 인스턴스를 생성할 때 사용하는 키워드는?", 
        	    "new", 
        	    "객체 생성 시에는 new 키워드를 사용합니다.", 
        	    new String[]{"create", "new", "make", "object"}));

        	add(new Question(303, QuestionType.BLANK, 
        	    "값을 반환하지 않는 메서드의 리턴 타입은?", 
        	    "void", 
        	    "리턴값이 없을 때는 void를 명시해야 합니다.", 
        	    new String[]{"null", "void", "empty", "zero"}));
    }

    private void add(Question q) {
        questionMap.put(q.getId(), q);
    }

    // 특정 타입의 문제 리스트 가져오기 (랜덤 섞어서)
    public List<Question> getQuestionsByType(QuestionType type) {
        List<Question> list = new ArrayList<>();
        for (Question q : questionMap.values()) {
            if (q.getType() == type) list.add(q);
        }
        Collections.shuffle(list);
        return list;
    }

    // 틀린 문제 신고하기
    public void addWrongAnswer(int id) {
        wrongQuestionIds.add(id);
        System.out.println("[시스템] 오답 노트에 추가됨. ID: " + id); // 확인용 로그
        // TODO: 나중에 파일(wrong_notes.txt) 저장 코드 추가
    }
    public List<Question> getWrongQuestions() {
        List<Question> list = new ArrayList<>();
        for (int id : wrongQuestionIds) {
            if (questionMap.containsKey(id)) {
                list.add(questionMap.get(id));
            }
        }
        return list;
    }

    // (선택사항) 오답 노트 초기화 기능
    public void clearWrongAnswers() {
        wrongQuestionIds.clear();
    }
}