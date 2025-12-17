package com.team.util;

import javax.sound.sampled.*;
import java.net.URL;

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
            if (isMuted) return;
            if (bgmClip != null && bgmClip.isRunning()) return;

            URL url = getClass().getResource("/sounds/bgm.wav");
            if (url == null) {
                System.out.println("[알림] 배경음악 리소스를 찾을 수 없습니다.");
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            bgmClip = AudioSystem.getClip();
            bgmClip.open(audioIn);
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
            bgmClip.start();

            System.out.println("[사운드] 배경음악 재생 시작");
        } catch (Exception e) {
            System.out.println("[오류] 배경음악 재생 실패");
            e.printStackTrace();
        }
    }

    // 배경음악 정지
    public void stopBGM() {
        if (bgmClip != null) {
            bgmClip.stop();
            bgmClip.close();
            bgmClip = null;
            System.out.println("[사운드] 배경음악 정지");
        }
    }

    // 효과음 재생 (버튼 클릭)
    public void playClickSound() {
        if (isMuted) return;
        playSound("/sounds/click.wav");
    }

    // 효과음 재생 (정답)
    public void playCorrectSound() {
        if (isMuted) return;
        playSound("/sounds/correct.wav");
    }

    // 효과음 재생 (오답)
    public void playWrongSound() {
        if (isMuted) return;
        playSound("/sounds/wrong.wav");
    }

    // 일반 사운드 재생 (효과음)
    private void playSound(String resourcePath) {
        try {
            URL url = getClass().getResource(resourcePath);
            if (url == null) {
                System.out.println("[알림] 사운드 리소스를 찾을 수 없습니다: " + resourcePath);
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();

            // 재생 완료 후 자동 해제
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });

        } catch (Exception e) {
            System.out.println("[오류] 사운드 재생 실패: " + resourcePath);
            e.printStackTrace();
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
