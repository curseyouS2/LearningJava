package com.team.data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.team.model.Question;
import com.team.model.QuestionType;

public class FileManager {

    public List<Question> loadQuestions(String resourcePath) {
        List<Question> list = new ArrayList<>();

        
        InputStream is = getClass().getResourceAsStream(resourcePath);
        if (is == null) {
            System.out.println("[오류] 문제 리소스를 찾을 수 없습니다: " + resourcePath);
            return list;
        }

        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(is, "UTF-8"))) {

            String line;
            while ((line = br.readLine()) != null) {

                // 빈 줄, 주석(#) 무시
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }

                // 한 줄 파싱
                Question question = parseLine(line);
                if (question != null) {
                    list.add(question);
                }
            }

            System.out.println("[성공] 문제 " + list.size() + "개 로드 완료");

        } catch (Exception e) {
            System.out.println("[오류] 문제 파일 읽기 실패");
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 한 줄 데이터를 Question 객체로 변환
     * 형식:
     * id | type | text | code | answer | explanation | options
     */
    private Question parseLine(String line) {
        try {
            // 빈 필드도 유지하기 위해 -1 옵션
            String[] parts = line.split("\\|", -1);

            // 최소 필드 수 체크
            if (parts.length < 7) {
                return null;
            }

            int id = Integer.parseInt(parts[0].trim());
            QuestionType type = QuestionType.valueOf(parts[1].trim());
            String text = parts[2].trim();

            // code 처리 ("null" 문자열 → 실제 null)
            String code = parts[3].trim().equals("null")
                    ? null
                    : parts[3].replace("\\n", "\n");

            String answer = parts[4].trim();
            String explanation = parts[5].trim();

            // 보기 처리
            String[] options = null;
            if (!parts[6].trim().equals("null") && !parts[6].trim().isEmpty()) {
                options = parts[6].split(",");
            }

            return new Question(
                    id,
                    type,
                    text,
                    code,
                    answer,
                    explanation,
                    options
            );

        } catch (Exception e) {
            System.out.println("[파싱 오류] 잘못된 문제 데이터: " + line);
            return null;
        }
    }
}
