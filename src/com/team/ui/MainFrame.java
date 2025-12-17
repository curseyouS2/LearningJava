package com.team.ui;

import javax.swing.*;
import java.awt.*;

import com.team.ui.intro.*;
import com.team.ui.game.*;
import com.team.data.*;

public class MainFrame extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel mainContainer;

    // 패널들을 멤버 변수로 선언 (메서드에서 접근하기 위해)
    private StartPanel startPanel;
    private ModeSelectPanel modePanel;
    private TypingGamePanel typingPanel;
    private OXGamePanel oxPanel;
    private FillBlankPanel blankPanel;
    private NotePanel notePanel;
    private SaveLoadPanel saveLoadPanel;
    private SaveManager saveManager; 
    
    public MainFrame() {
        // 1. 기본 프레임 설정
        setTitle("Java Learning Game");
        setSize(1280, 720); // 기획서 해상도
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 화면 정중앙 배치
        setResizable(false); // 창 크기 조절 막기 (디자인 깨짐 방지)

        // 2. 레이아웃 설정
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        // 3. 패널 생성 (this를 넘겨줘서 메인프레임을 조작할 수 있게 함)
        startPanel = new StartPanel(this);
        modePanel = new ModeSelectPanel(this);
        typingPanel = new TypingGamePanel(this);
        oxPanel = new OXGamePanel(this);
        blankPanel = new FillBlankPanel(this);
        notePanel = new NotePanel(this);
        saveLoadPanel = new SaveLoadPanel(this);
        saveManager = new SaveManager();
        
        
        // 4. 패널을 카드 레이아웃에 추가 (이름표 붙이기)
        mainContainer.add(startPanel, "Start");
        mainContainer.add(modePanel, "Mode");
        mainContainer.add(typingPanel, "TypingGame"); 
        mainContainer.add(oxPanel, "OXGame");         
        mainContainer.add(blankPanel, "BlankGame");
        mainContainer.add(notePanel, "Note");
        mainContainer.add(saveLoadPanel, "SaveLoad"); 
        
        // 5. 프레임에 컨테이너 부착
        add(mainContainer);
        
        // 6. 첫 화면 보여주기
        cardLayout.show(mainContainer, "Start");
        
        setVisible(true);
    }

    //  화면 전환 메서드
    public void changePanel(String panelName) {
    	
        cardLayout.show(mainContainer, panelName);
        
        
        if (panelName.equals("TypingGame"))
        {
            typingPanel.startGame(); 
        } 
        else if (panelName.equals("OXGame")) 
        {
            oxPanel.startGame();     
        } 
        else if (panelName.equals("BlankGame")) 
        { 
            blankPanel.startGame();
        }
        else if (panelName.equals("Note")) 
        { 
            notePanel.startGame(); 
        }
        
        if (panelName.equals("Mode")) {
            // 현재 선택된 슬롯이 있다면 저장 실행
            if (SaveManager.currentSlot != -1) 
            {
                saveManager.save(SaveManager.currentSlot);
            }
        }

        // 3. 키보드 입력을 위해 포커스 맞추기
        mainContainer.requestFocusInWindow();
    }
}