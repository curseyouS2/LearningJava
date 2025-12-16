package com.team.data;

import java.io.*;
import java.util.*;

public class FileManager {
    // 키워드 파일 읽어오기 [cite: 113]
    public List<String> loadKeywords() {
        List<String> list = new ArrayList<>();
        File file = new File("data/keywords/java_keywords.txt");
        
        if (!file.exists()) {
            System.out.println("파일 없음: " + file.getAbsolutePath());
            return Arrays.asList("java", "code", "swing"); // 비상용 더미 데이터
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) list.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}