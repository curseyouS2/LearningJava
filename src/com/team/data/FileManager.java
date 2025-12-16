package com.team.data;

import java.io.*;
import java.util.*;
import com.team.model.Question;
import com.team.model.QuestionType;

public class FileManager {
    
    // 텍스트 파일에서 문제 목록 읽어오기
    public List<Question> loadQuestions(String filePath) {
        List<Question> list = new ArrayList<>();
        File file = new File(filePath);
        
        // 파일이 없으면 빈 리스트 반환 (나중에 Repository에서 처리)
        if (!file.exists()) {
            System.out.println("[오류] 데이터 파일을 찾을 수 없습니다: " + file.getAbsolutePath());
            return null; 
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // 빈 줄이나 주석(#) 건너뛰기
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                
                // 데이터 파싱 (파이프 '|' 로 구분)
                Question q = parseLine(line);
                if (q != null) {
                    list.add(q);
                }
            }
            System.out.println("[성공] 파일에서 " + list.size() + "개의 문제를 로드했습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 한 줄을 쪼개서 Question 객체로 만드는 메서드
    private Question parseLine(String line) {
        try {
            // split을 할 때 -1을 넣어야 빈 칸도 무시하지 않고 가져옴
            String[] parts = line.split("\\|", -1); 
            
            // 데이터 형식이 맞는지 확인 (최소 7개 필드 필요)
            if (parts.length < 7) return null;

            int id = Integer.parseInt(parts[0].trim());
            QuestionType type = QuestionType.valueOf(parts[1].trim());
            String text = parts[2].trim();
            
            // "null" 글자거나 비어있으면 진짜 null로 변환, \n은 줄바꿈으로 복원
            String code = parts[3].trim().equals("null") ? null : parts[3].replace("\\n", "\n");
            
            String answer = parts[4].trim();
            String explanation = parts[5].trim();
            
            // 보기는 콤마로 구분
            String[] options = null;
            if (!parts[6].trim().equals("null") && !parts[6].trim().isEmpty()) {
                options = parts[6].split(",");
            }

            // Question 객체 생성
            return new Question(id, type, text, code, answer, explanation, options);

        } catch (Exception e) {
            System.out.println("[파싱 오류] 잘못된 데이터 라인: " + line);
            return null;
        }
    }
}