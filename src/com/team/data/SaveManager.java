package com.team.data;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import com.team.model.SaveData;

public class SaveManager {
    private static final String SAVE_FOLDER = "save/";
    
    // 현재 플레이 중인 슬롯 번호 (-1이면 슬롯 선택 안 함)
    public static int currentSlot = -1; 

    public SaveManager() {
        // save 폴더가 없으면 생성
        File folder = new File(SAVE_FOLDER);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    // 데이터 저장 (현재 상태 -> 파일)
    public void save(int slot) {
        try {
            // 1. 현재 저장소에서 오답 데이터 가져오기
            Set<Integer> wrongIds = QuestionRepository.getInstance().getWrongQuestionIdsSet();
            
            // 2. 날짜 생성
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
            
            // 3. 데이터 객체 생성
            SaveData data = new SaveData(wrongIds, date);
            
            // 4. 파일 쓰기
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FOLDER + "slot_" + slot + ".dat"));
            oos.writeObject(data);
            oos.close();
            
            System.out.println("[시스템] 슬롯 " + slot + "번에 저장 완료 (" + date + ")");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 데이터 로드 (파일 -> 현재 상태)
    public boolean load(int slot) {
        try {
            File file = new File(SAVE_FOLDER + "slot_" + slot + ".dat");
            if (!file.exists()) return false;

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            SaveData data = (SaveData) ois.readObject();
            ois.close();

            // 가져온 데이터를 게임 저장소(Repository)에 적용
            QuestionRepository.getInstance().setWrongQuestionIds(data.getWrongQuestionIds());
            
            currentSlot = slot; // 현재 슬롯 설정
            System.out.println("[시스템] 슬롯 " + slot + "번 불러오기 완료");
            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 슬롯 정보 확인 (날짜만 가져오기 - 버튼 표시용)
    public String getSaveDate(int slot) {
        try {
            File file = new File(SAVE_FOLDER + "slot_" + slot + ".dat");
            if (!file.exists()) return "비어 있음";

            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            SaveData data = (SaveData) ois.readObject();
            ois.close();
            
            return data.getSaveDate();
        } catch (Exception e) {
            return "비어 있음";
        }
    }
}