package com.team.util;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ImageManager {
    private static ImageManager instance;
    
    public static ImageManager getInstance() {
        if (instance == null) {
            instance = new ImageManager();
        }
        return instance;
    }
    
    private ImageManager() {
        // 생성자
    }
    
    // 로고 이미지 불러오기
    public ImageIcon getLogo() {
        return loadImage("resources/images/logo.png", 800, 500);
    }
    
    // 작은 로고 (상단용)
    public ImageIcon getSmallLogo() {
        return loadImage("resources/images/logo.png", 800, 400);
    }
    
    // 홈 버튼 이미지
    public ImageIcon getHomeIcon() {
        return loadImage("resources/images/home.png", 24, 24);
    }
    
    // 키워드 타이핑 아이콘
    public ImageIcon getTypingIcon() {
        return loadImage("resources/images/typing.png", 80, 80);
    }
    
    // OX 퀴즈 아이콘
    public ImageIcon getOXIcon() {
        return loadImage("resources/images/ox.png", 80, 80);
    }
    
    // 빈칸 채우기 아이콘
    public ImageIcon getBlankIcon() {
        return loadImage("resources/images/blank.png", 80, 80);
    }
    
    // 오답 노트 아이콘
    public ImageIcon getNoteIcon() {
        return loadImage("resources/images/note.png", 80, 80);
    }
    
    // 이미지 로드 (파일이 없으면 null 반환)
    private ImageIcon loadImage(String path, int width, int height) {
        try {
            File imgFile = new File(path);
            if (!imgFile.exists()) {
                System.out.println("[알림] 이미지 파일이 없습니다: " + path);
                return null;
            }
            
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage();
            Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImg);
        } catch (Exception e) {
            System.out.println("[오류] 이미지 로드 실패: " + path);
            return null;
        }
    }
}