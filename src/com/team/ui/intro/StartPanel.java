package com.team.ui.intro;

import javax.swing.*;
import java.awt.*;
import com.team.ui.MainFrame;
import com.team.util.SoundManager;
import com.team.util.ImageManager;

public class StartPanel extends JPanel {
    private MainFrame mainFrame;

    public StartPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(248, 233, 233));

        // 배경음악 시작
        SoundManager.getInstance().playBGM();

        // 로고 이미지만 크게 표시 (글씨 포함되어 있음)
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setOpaque(false);
        
        ImageIcon logoImg = ImageManager.getInstance().getLogo();
        JLabel logoLabel;
        if (logoImg != null) {
            // 로고 이미지를 크게 표시 (400x160)
            Image img = logoImg.getImage();
            Image scaledImg = img.getScaledInstance(400, -1, Image.SCALE_SMOOTH);
            logoLabel = new JLabel(new ImageIcon(scaledImg));
        } else {
            // 이미지가 없으면 이모지 + 텍스트
            logoLabel = new JLabel("☕ Learning Java");
            logoLabel.setFont(new Font("Arial", Font.BOLD, 50));
            logoLabel.setForeground(new Color(189, 130, 80));
        }
        
        logoPanel.add(logoLabel);

        // 버튼들
        JButton startBtn = createStyledButton("시작하기");
        JButton helpBtn = createStyledButton("도움말");

        startBtn.addActionListener(e -> {
            SoundManager.getInstance().playClickSound();
            mainFrame.changePanel("SaveLoad");
        });
        
        helpBtn.addActionListener(e -> {
            SoundManager.getInstance().playClickSound();
            showHelpDialog();
        });

        add(Box.createVerticalGlue());
        add(logoPanel);
        add(Box.createRigidArea(new Dimension(0, 80)));
        add(startBtn);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(helpBtn);
        add(Box.createVerticalGlue());
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 26));
        btn.setForeground(Color.BLACK);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(145, 107, 85), 3),
            BorderFactory.createEmptyBorder(15, 40, 15, 40)
        ));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(250, 70));
        return btn;
    }

    private void showHelpDialog() {
        String helpText = "자바 문법을 게임으로 배워보세요!\n\n" +
                         "1. 키워드 타이핑 - 자바 키워드 빠르게 입력\n" +
                         "2. 문법 O/X - 참/거짓 퀴즈\n" +
                         "3. 빈칸 채우기 - 코드 빈칸 맞추기\n" +
                         "4. 오답 노트 - 틀린 문제 복습";
        JOptionPane.showMessageDialog(this, helpText, "도움말", JOptionPane.INFORMATION_MESSAGE);
    }
}