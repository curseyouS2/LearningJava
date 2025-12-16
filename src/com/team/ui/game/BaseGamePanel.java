package com.team.ui.game;

import javax.swing.*;
import java.awt.*;
import com.team.ui.MainFrame;

public class BaseGamePanel extends JPanel {
    protected MainFrame mainFrame;
    protected JProgressBar timeBar; // 상단 시간바 [cite: 28]
    protected JLabel scoreLabel;    // 점수판 [cite: 30]
    protected JLabel timerLabel;    // 남은 시간 텍스트
    protected int score = 0;
    protected int timeLeft = 60;    // 기본 60초
    protected Timer gameTimer;      // 타이머 객체

    public BaseGamePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(248, 233, 233)); // 배경색(분홍)

        // 1. 상단 정보창
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 시간 게이지바
        timeBar = new JProgressBar(0, 60);
        timeBar.setValue(60);
        timeBar.setForeground(new Color(134, 219, 102)); // 초록색

        // 점수 및 시간 텍스트
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);
        timerLabel = new JLabel("남은 시간: 60초");
        scoreLabel = new JLabel("점수: 0");
        infoPanel.add(timerLabel, BorderLayout.WEST);
        infoPanel.add(scoreLabel, BorderLayout.EAST);

        topPanel.add(timeBar, BorderLayout.NORTH);
        topPanel.add(infoPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // 2. 하단 홈 버튼
        JButton homeBtn = new JButton("🏠 홈으로");
        homeBtn.addActionListener(e -> stopGameAndGoHome());
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setOpaque(false);
        bottomPanel.add(homeBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // 각 자식에서 override 할 것 
    public void startGame() {
        score = 0;
        timeLeft = 60;
        scoreLabel.setText("점수: " + score);
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