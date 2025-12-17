package com.team.ui.intro;

import javax.swing.*;
import java.awt.*;
import com.team.ui.MainFrame;
import com.team.util.SoundManager;
import com.team.util.ImageManager;

public class ModeSelectPanel extends JPanel {
    private MainFrame mainFrame;

    public ModeSelectPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(248, 233, 233));

        // 상단 로고 (글씨 포함)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        topPanel.setOpaque(false);
        
        ImageIcon logoImg = ImageManager.getInstance().getSmallLogo();
        JLabel logo;
        if (logoImg != null) {
            // 로고를 좀 더 크게 (160x64)
            Image img = logoImg.getImage();
            Image scaledImg = img.getScaledInstance(160, 100, Image.SCALE_SMOOTH);
            logo = new JLabel(new ImageIcon(scaledImg));
        } else {
            logo = new JLabel("☕ Learning Java");
            logo.setFont(new Font("Arial", Font.BOLD, 24));
            logo.setForeground(new Color(189, 130, 80));
        }
        topPanel.add(logo);
        add(topPanel, BorderLayout.NORTH);

        // 타이틀
        JLabel titleLabel = new JLabel("모드 선택", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 40));
        titleLabel.setForeground(new Color(89, 64, 50));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));

        // 메뉴 패널
        JPanel menuPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        menuPanel.setBackground(new Color(248, 233, 233));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 80, 50));

        menuPanel.add(createModeCard("키워드타이핑", "TypingGame", ImageManager.getInstance().getTypingIcon()));
        menuPanel.add(createModeCard("문법OX퀴즈", "OXGame", ImageManager.getInstance().getOXIcon()));
        menuPanel.add(createModeCard("빈칸 채우기", "BlankGame", ImageManager.getInstance().getBlankIcon()));
        menuPanel.add(createModeCard("오답노트", "Note", ImageManager.getInstance().getNoteIcon()));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(titleLabel, BorderLayout.NORTH);
        centerPanel.add(menuPanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JButton createModeCard(String title, String targetPanelName, ImageIcon icon) {
        JButton btn = new JButton();
        btn.setLayout(new BorderLayout());
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(145, 107, 85), 3));

        // 아이콘 (이미지 또는 이모지)
        JLabel iconLabel;
        if (icon != null) {
            iconLabel = new JLabel(icon, SwingConstants.CENTER);
        } else {
            iconLabel = new JLabel("⌨️🔍", SwingConstants.CENTER);
            iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        }
        iconLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));

        // 구분선
        JPanel separator = new JPanel();
        separator.setBackground(new Color(220, 200, 180));
        separator.setPreferredSize(new Dimension(200, 2));

        // 타이틀
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        titleLabel.setForeground(new Color(89, 64, 50));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 30, 10));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.add(iconLabel);
        contentPanel.add(separator);
        contentPanel.add(titleLabel);

        btn.add(contentPanel, BorderLayout.CENTER);
        
        btn.addActionListener(e -> {
            SoundManager.getInstance().playClickSound();
            try {
                mainFrame.changePanel(targetPanelName);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "아직 구현되지 않은 모드입니다!");
            }
        });

        return btn;
    }
}