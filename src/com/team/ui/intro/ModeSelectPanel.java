package com.team.ui.intro;

import javax.swing.*;
import java.awt.*;
import com.team.ui.MainFrame;

public class ModeSelectPanel extends JPanel {
    private MainFrame mainFrame;

    public ModeSelectPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(248, 233, 233)); // 배경색

        // 1. 상단 타이틀
        JLabel titleLabel = new JLabel("모드 선택", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 36));
        titleLabel.setForeground(new Color(89, 64, 50)); // 진한 갈색
        titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // 2. 중앙 카드 메뉴 (4개 배치)
        JPanel menuPanel = new JPanel(new GridLayout(1, 4, 20, 0)); // 1줄 4칸, 간격 20
        menuPanel.setBackground(new Color(248, 233, 233));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 80, 50)); // 전체 여백

        // 각 모드 버튼 생성 (버튼명, 이동할 패널 이름)
        menuPanel.add(createModeCard("키워드 타이핑", "TypingGame"));
        menuPanel.add(createModeCard("문법 O/X", "OXGame"));
        menuPanel.add(createModeCard("빈칸 채우기", "BlankGame"));
        menuPanel.add(createModeCard("오답 노트", "Note"));

        add(menuPanel, BorderLayout.CENTER);
    }

    // 카드 형태의 버튼 만들기
    private JButton createModeCard(String title, String targetPanelName) {
        JButton btn = new JButton(title);
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        btn.setBackground(Color.WHITE); // 카드 배경 흰색
        btn.setForeground(new Color(89, 64, 50));
        btn.setFocusPainted(false);
        
        // 아이콘 이미지가 있다면 여기에 추가 (btn.setIcon(...))
        
        // 클릭 이벤트
        btn.addActionListener(e -> {
            // 해당 게임 화면으로 이동
            // (주의: MainFrame에 해당 패널이 add 되어 있어야 이동함)
            try {
                mainFrame.changePanel(targetPanelName);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "아직 구현되지 않은 모드입니다: " + title);
            }
        });

        return btn;
    }
}