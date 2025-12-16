package com.team.main;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import java.awt.Font;
import java.util.Enumeration;
import com.team.ui.MainFrame;

public class Main {
    public static void main(String[] args) {
        // 1. 프로그램 시작 전에 한글 폰트 강제 적용 (가장 중요!)
        applyGlobalFont(new Font("맑은 고딕", Font.PLAIN, 14)); 

        // 2. 메인 프레임 실행
        new MainFrame(); 
    }

    // ★ 모든 UI의 폰트를 강제로 바꿔주는 마법의 코드
    private static void applyGlobalFont(Font font) {
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        FontUIResource f = new FontUIResource(font);
        
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            
            // 폰트 설정값이면 무조건 우리가 지정한 폰트로 덮어씌움
            if (value instanceof FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }
}