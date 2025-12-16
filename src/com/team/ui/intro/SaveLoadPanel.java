package com.team.ui.intro;

import javax.swing.*;
import java.awt.*;
import com.team.ui.MainFrame;
import com.team.data.SaveManager;

public class SaveLoadPanel extends JPanel {
	private static final long serialVersionUID = 1L;
    private MainFrame mainFrame;
    private SaveManager saveManager;
    private JButton[] slotBtns = new JButton[3]; // 슬롯 버튼 3개

    public SaveLoadPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.saveManager = new SaveManager();
        
        setLayout(new BorderLayout());
        setBackground(new Color(248, 233, 233));

        // 타이틀
        JLabel titleLabel = new JLabel("데이터 불러오기 / 슬롯 선택", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 30));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 30, 0));
        add(titleLabel, BorderLayout.NORTH);

        // 중앙 슬롯 버튼 영역
        JPanel slotPanel = new JPanel(new GridLayout(1, 3, 30, 0));
        slotPanel.setOpaque(false);
        slotPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 100, 100));

        for (int i = 0; i < 3; i++) {
            final int slotIndex = i + 1;
            slotBtns[i] = new JButton();
            styleSlotButton(slotBtns[i]);
            
            // 버튼 클릭 이벤트
            slotBtns[i].addActionListener(e -> selectSlot(slotIndex));
            
            slotPanel.add(slotBtns[i]);
        }
        
        // 새로고침 (날짜 표시)
        refreshSlots();
        
        add(slotPanel, BorderLayout.CENTER);
        
        // 뒤로가기 버튼
        JButton backBtn = new JButton("뒤로가기");
        backBtn.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        backBtn.addActionListener(e -> mainFrame.changePanel("Start"));
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(backBtn);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    // 화면이 켜질 때마다 파일 상태 확인해서 날짜 갱신
    public void refreshSlots() {
        for (int i = 0; i < 3; i++) {
            String date = saveManager.getSaveDate(i + 1);
            String text = "<html><center>SLOT " + (i + 1) + "<br><br>" + date + "</center></html>";
            slotBtns[i].setText(text);
        }
    }

    private void selectSlot(int slot) {
        // 로드 시도
        boolean success = saveManager.load(slot);
        
        if (success) {
            JOptionPane.showMessageDialog(this, slot + "번 슬롯 데이터를 불러왔습니다.");
        } else {
            // 파일이 없으면 새로 시작
            int choice = JOptionPane.showConfirmDialog(this, "저장된 데이터가 없습니다. 새로 시작할까요?", "알림", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                SaveManager.currentSlot = slot; // 슬롯 지정
                // 기존 데이터 초기화 필요 (선택사항)
                // QuestionRepository.getInstance().clearWrongAnswers(); 
                JOptionPane.showMessageDialog(this, slot + "번 슬롯에 자동 저장됩니다.");
            } else {
                return; // 취소
            }
        }
        mainFrame.changePanel("Mode"); // 모드 선택 화면으로 이동
    }
    
    private void styleSlotButton(JButton btn) {
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
    }
}