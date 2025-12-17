package com.team.util;

import javax.sound.sampled.*;
import java.io.File;

public class SoundManager {
    private static SoundManager instance;
    private Clip bgmClip; // 배경음악
    private boolean isMuted = false;
    
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }
    
    private SoundManager() {
        // 생성자
    }
    
    // 배경음악 재생
    public void playBGM() {
        try {
            if (bgmClip != null && bgmClip.isRunning()) {
                return; // 이미 재생 중
            }
            
            File soundFile = new File("resources/sounds/bgm.wav");
            if (!soundFile.exists()) {
                System.out.println("[알림] 배경음악 파일이 없습니다.");
                return;
            }
            
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            bgmClip = AudioSystem.getClip();
            bgmClip.open(audioIn);
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY); // 반복 재생
            bgmClip.start();
            
            System.out.println("[사운드] 배경음악 재생 시작");
        } catch (Exception e) {
            System.out.println("[오류] 배경음악 재생 실패");
        }
    }
    
    // 배경음악 정지
    public void stopBGM() {
        if (bgmClip != null && bgmClip.isRunning()) {
            bgmClip.stop();
            System.out.println("[사운드] 배경음악 정지");
        }
    }
    
    // 효과음 재생 (버튼 클릭)
    public void playClickSound() {
        if (isMuted) return;
        playSound("resources/sounds/click.wav");
    }
    
    // 효과음 재생 (정답)
    public void playCorrectSound() {
        if (isMuted) return;
        playSound("resources/sounds/correct.wav");
    }
    
    // 효과음 재생 (오답)
    public void playWrongSound() {
        if (isMuted) return;
        playSound("resources/sounds/wrong.wav");
    }
    
    // 일반 사운드 재생
    private void playSound(String filePath) {
        try {
            File soundFile = new File(filePath);
            if (!soundFile.exists()) {
                System.out.println("[알림] 사운드 파일이 없습니다: " + filePath);
                return;
            }
            
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
            
            // 재생 완료 후 자동으로 닫기
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
        } catch (Exception e) {
            System.out.println("[오류] 사운드 재생 실패: " + filePath);
        }
    }
    
    // 음소거 토글
    public void toggleMute() {
        isMuted = !isMuted;
        if (isMuted) {
            stopBGM();
        } else {
            playBGM();
        }
    }
    
    public boolean isMuted() {
        return isMuted;
    }
}