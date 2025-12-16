package com.team.model;

import java.io.Serializable;
import java.util.Set;

public class SaveData implements Serializable {
    private static final long serialVersionUID = 1L;

    private Set<Integer> wrongQuestionIds; // 오답 노트 데이터
    private String saveDate;               // 저장 날짜 (yyyy-MM-dd HH:mm)

    public SaveData(Set<Integer> wrongQuestionIds, String saveDate) {
        this.wrongQuestionIds = wrongQuestionIds;
        this.saveDate = saveDate;
    }

    public Set<Integer> getWrongQuestionIds() {
        return wrongQuestionIds;
    }

    public String getSaveDate() {
        return saveDate;
    }
}