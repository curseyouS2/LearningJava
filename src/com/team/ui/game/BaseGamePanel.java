package com.team.ui.game;

import javax.swing.*;
import java.awt.*;
import com.team.ui.MainFrame;
import com.team.util.SoundManager;
import com.team.util.ImageManager;

public class BaseGamePanel extends JPanel {
    protected MainFrame mainFrame;
    protected JProgressBar timeBar;
    protected JLabel scoreLabel;
    protected JLabel timerLabel;
    protected int score = 0;
    protected int timeLeft = 60;
    protected Timer gameTimer;

    public BaseGamePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(248, 233, 233));

        // 상단 패널
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        timeBar = new JProgressBar(0, 60);
        timeBar.setValue(60);
        timeBar.setForeground(new Color(134, 219, 102));

        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);
        timerLabel = new JLabel("남은 시간: 60초");
        timerLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        
        scoreLabel = new JLabel("점수: 0");
        scoreLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        
        infoPanel.add(timerLabel, BorderLayout.WEST);
        infoPanel.add(scoreLabel, BorderLayout.EAST);

        topPanel.add(timeBar, BorderLayout.NORTH);
        topPanel.add(infoPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // 하단 홈 버튼 (이미지 또는 텍스트)
        JButton homeBtn = new JButton();
        ImageIcon homeIcon = ImageManager.getInstance().getHomeIcon();
        if (homeIcon != null) {
            homeBtn.setIcon(homeIcon);
            homeBtn.setText(" 홈으로");
        } else {
            homeBtn.setText("🏠 홈으로");
        }
        homeBtn.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        homeBtn.setBackground(Color.WHITE);
        homeBtn.setFocusPainted(false);
        homeBtn.addActionListener(e -> {
            SoundManager.getInstance().playClickSound();
            stopGameAndGoHome();
        });
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        bottomPanel.add(homeBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // 점수 업데이트
    protected void updateScore(int delta) {
        this.score += delta;
        
        // 마이너스 방지
        if (this.score < 0) {
            this.score = 0;
        }
        
        scoreLabel.setText("점수: " + this.score);
    }

    public void startGame() {
        score = 0;
        timeLeft = 60;
        updateScore(0);
        
        // 타이머 시작 (기존 타이머 정리 안함 - 학생 실수)
        gameTimer = new Timer(1000, e -> {
        	timeLeft--;
            timeBar.setValue(timeLeft);
            timerLabel.setText("남은 시간: " + timeLeft + "초");
            
            // ⭐ 시간에 따라 색상 변경
            if (timeLeft <= 10) {
                // 10초 이하 - 빨간색 (긴박!)
                timeBar.setForeground(new Color(255, 82, 82));
                timerLabel.setForeground(new Color(255, 82, 82));
            } else if (timeLeft <= 20) {
                // 20초 이하 - 주황색 (경고)
                timeBar.setForeground(new Color(255, 159, 64));
                timerLabel.setForeground(new Color(255, 100, 0));
            } else if (timeLeft <= 30) {
                // 30초 이하 - 노란색 (주의)
                timeBar.setForeground(new Color(255, 205, 86));
                timerLabel.setForeground(new Color(180, 130, 0));
            } else {
                // 30초 이상 - 초록색 (여유)
                timeBar.setForeground(new Color(134, 219, 102));
                timerLabel.setForeground(Color.BLACK);
            }
            
            if (timeLeft <= 0) {
                gameOver();
            }
        });
        gameTimer.start();
    }

    protected void stopGameAndGoHome() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        mainFrame.changePanel("Mode");
    }

    protected void gameOver() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        JOptionPane.showMessageDialog(this, "게임 종료! 최종 점수: " + score);
        mainFrame.changePanel("Mode");
    }
}