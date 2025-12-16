package com.team.ui.game;

import javax.swing.*;
import java.awt.*;
import com.team.ui.MainFrame;

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

        // 하단 홈 버튼
        JButton homeBtn = new JButton("🏠 홈으로");
        homeBtn.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        homeBtn.setBackground(Color.WHITE);
        homeBtn.addActionListener(e -> stopGameAndGoHome());
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        bottomPanel.add(homeBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // ★ [핵심 수정] 점수 관리 통합 메서드
    protected void updateScore(int delta) {
        this.score += delta;
        
        // 1. 점수 마이너스 방지 로직
        if (this.score < 0) {
            this.score = 0;
        }
        
        // 2. UI 즉시 동기화 (이제 불일치 문제 해결됨)
        scoreLabel.setText("점수: " + this.score);
    }

    public void startGame() {
        score = 0;
        timeLeft = 60;
        updateScore(0); // UI 초기화
        
        if (gameTimer != null) gameTimer.stop();
        
        gameTimer = new Timer(1000, e -> {
            timeLeft--;
            timeBar.setValue(timeLeft);
            timerLabel.setText("남은 시간: " + timeLeft + "초");
            if (timeLeft <= 0) gameOver();
        });
        gameTimer.start();
    }

    protected void stopGameAndGoHome() {
        if (gameTimer != null) gameTimer.stop();
        mainFrame.changePanel("Mode");
    }

    protected void gameOver() {
        if (gameTimer != null) gameTimer.stop();
        JOptionPane.showMessageDialog(this, "게임 종료! 최종 점수: " + score);
        mainFrame.changePanel("Mode");
    }
}