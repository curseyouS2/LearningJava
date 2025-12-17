package com.team.data;

import java.util.*;
import com.team.model.Question;
import com.team.model.QuestionType;

public class QuestionRepository {
    private static QuestionRepository instance;
    private Map<Integer, Question> questionMap;
    private Set<Integer> wrongQuestionIds;

    public static QuestionRepository getInstance() {
        if (instance == null) instance = new QuestionRepository();
        return instance;
    }

    private QuestionRepository() {
        questionMap = new HashMap<>();
        wrongQuestionIds = new HashSet<>();
        
        // ★ 여기서 파일 로딩 시도!
        loadAllData();
    }

    private void loadAllData() {
        FileManager fm = new FileManager();
        // 프로젝트 폴더 안의 data/questions.txt 경로
        List<Question> loadedList = fm.loadQuestions("/questions.txt");

        if (loadedList != null && !loadedList.isEmpty()) {
            // 파일 로드 성공 시
            for (Question q : loadedList) {
                add(q);
            }
        } else {
            // ★ 비상 사태: 파일이 없거나 에러나면 하드코딩 데이터 사용 (발표 사고 방지용)
            System.out.println("[시스템] 파일 로드 실패. 비상용 데이터를 사용합니다.");
            loadBackupData();
        }
    }

    private void add(Question q) {
        questionMap.put(q.getId(), q);
    }
    
    // 파일 로드 실패 시 사용할 비상용 데이터 (기존 하드코딩 코드)
    private void loadBackupData() {
        add(new Question(101, QuestionType.KEYWORD, "모든 클래스에서 접근 가능한 접근 제어자는?", null, "public", "public은 어디서든 접근 가능합니다.", null));
        add(new Question(102, QuestionType.KEYWORD, "인스턴스 생성 없이 사용할 수 있는 멤버?", null, "static", "static은 인스턴스 없이 사용 가능합니다.", null));
        // ... (필요하다면 기존 하드코딩 데이터를 여기에 쭉 복사해두세요)
        // 일단 최소한의 테스트용 데이터만 넣어둡니다.
        add(new Question(201, QuestionType.OX, "Java는 객체지향 언어이다.", null, "O", "Java는 OOP 언어입니다.", null));
        add(new Question(301, QuestionType.BLANK, "상속 키워드?", "public class A ___ B {}", "extends", "상속은 extends", new String[]{"extends", "import"}));
    }

    // ... 아래 메서드들은 기존과 동일 ...
    public List<Question> getQuestionsByType(QuestionType type) {
        List<Question> list = new ArrayList<>();
        
        // 1. 해당 타입의 모든 문제 수집
        for (Question q : questionMap.values()) {
            if (q.getType() == type) list.add(q);
        }
        
        // 2. 랜덤 섞기
        Collections.shuffle(list);
        
        // 3. 10개만 자르기 (문제가 10개보다 적으면 다 줌)
        if (list.size() > 10) {
            return list.subList(0, 10);
        } else {
            return list;
        }
    }

    public void addWrongAnswer(int id) {
        wrongQuestionIds.add(id);
        // (선택) 여기서 오답 ID도 파일에 저장하면 완벽함 (일단은 메모리에만)
    }
    
    public List<Question> getWrongQuestions() {
        List<Question> list = new ArrayList<>();
        for (int id : wrongQuestionIds) {
            if (questionMap.containsKey(id)) list.add(questionMap.get(id));
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