package com.team.ui.intro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.team.ui.MainFrame;

public class StartPanel extends JPanel {
    private MainFrame mainFrame;

    public StartPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // 세로 정렬
        setBackground(new Color(248, 233, 233)); // 배경색 (분홍)

        // 1. 로고 영역 (이미지가 없으면 텍스트로 대체)
        JLabel logoLabel = new JLabel("☕ Learning Java");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 40));
        logoLabel.setForeground(new Color(145, 107, 85)); // 갈색
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 2. 버튼 영역
        JButton startBtn = createStyledButton("시작하기");
        JButton helpBtn = createStyledButton("도움말");

        // 이벤트: 시작 버튼 누르면 모드 선택으로 이동
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.changePanel("Mode"); 
            }
        });
        
        // 도움말 버튼 (팝업 띄우기)
        helpBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "자바 문법을 게임으로 배워보세요!"));

        // 3. 화면에 붙이기 (여백 조정)
        add(Box.createVerticalGlue()); // 위쪽 여백 자동
        add(logoLabel);
        add(Box.createRigidArea(new Dimension(0, 50))); // 로고와 버튼 사이 간격
        add(startBtn);
        add(Box.createRigidArea(new Dimension(0, 20))); // 버튼 사이 간격
        add(helpBtn);
        add(Box.createVerticalGlue()); // 아래쪽 여백 자동
    }

    // 버튼 스타일을 예쁘게 만드는 헬퍼 메서드
    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(145, 107, 85)); // 버튼색 (갈색)
        btn.setFocusPainted(false); // 포커스 테두리 제거
        btn.setBorderPainted(false); // 외곽선 제거
        btn.setAlignmentX(Component.CENTER_ALIGNMENT); // 중앙 정렬
        btn.setMaximumSize(new Dimension(200, 60)); // 버튼 크기 고정
        return btn;
    }
}