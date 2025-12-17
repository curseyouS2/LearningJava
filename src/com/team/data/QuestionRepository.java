package com.team.data;

import java.util.*;
import com.team.model.Question;
import com.team.model.QuestionType;

public class QuestionRepository {
    private static QuestionRepository instance;
    private Map<Integer, Question> questionMap;
    private Set<Integer> wrongQuestionIds;

    public static QuestionRepository getInstance() {
        if (instance == null) {
            instance = new QuestionRepository();
        }
        return instance;
    }

    private QuestionRepository() {
        questionMap = new HashMap<>();
        wrongQuestionIds = new HashSet<>();
        loadAllData();
    }

    private void loadAllData() {
        FileManager fm = new FileManager();

        List<Question> loadedList = fm.loadQuestions("/data/questions.txt");

        if (!loadedList.isEmpty()) {
            for (Question q : loadedList) {
                add(q);
            }
            System.out.println("[시스템] 문제 파일 로딩 성공: " + loadedList.size() + "문제");
        } else {
            // 파일 로드 실패 시 비상용 데이터
            System.out.println("[시스템] 파일 로드 실패. 비상용 데이터를 사용합니다.");
            loadBackupData();
        }
    }

    private void add(Question q) {
        questionMap.put(q.getId(), q);
    }

    // 파일 로드 실패 시 사용할 비상용 데이터
    private void loadBackupData() {
        add(new Question(
                101,
                QuestionType.KEYWORD,
                "모든 클래스에서 접근 가능한 접근 제어자는?",
                null,
                "public",
                "public은 어디서든 접근 가능합니다.",
                null
        ));

        add(new Question(
                102,
                QuestionType.KEYWORD,
                "인스턴스 생성 없이 사용할 수 있는 멤버?",
                null,
                "static",
                "static은 인스턴스 없이 사용 가능합니다.",
                null
        ));

        add(new Question(
                201,
                QuestionType.OX,
                "Java는 객체지향 언어이다.",
                null,
                "O",
                "Java는 OOP 언어입니다.",
                null
        ));

        add(new Question(
                301,
                QuestionType.BLANK,
                "상속 키워드?",
                "public class A ___ B {}",
                "extends",
                "상속은 extends",
                new String[]{"extends", "import"}
        ));
    }

    // ================= 조회 메서드 =================

    public List<Question> getQuestionsByType(QuestionType type) {
        List<Question> list = new ArrayList<>();

        for (Question q : questionMap.values()) {
            if (q.getType() == type) {
                list.add(q);
            }
        }

        Collections.shuffle(list);

        return list.size() > 10 ? list.subList(0, 10) : list;
    }

    public void addWrongAnswer(int id) {
        wrongQuestionIds.add(id);
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

    public Set<Integer> getWrongQuestionIdsSet() {
        return wrongQuestionIds;
    }

    public void setWrongQuestionIds(Set<Integer> loadedIds) {
        this.wrongQuestionIds = loadedIds;
        System.out.println("[시스템] 데이터 동기화 완료. 오답 수: " + wrongQuestionIds.size());
    }
}
