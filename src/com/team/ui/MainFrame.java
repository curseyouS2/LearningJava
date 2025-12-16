package com.team.ui;

import javax.swing.*;
import java.awt.*;

// 패키지 경로 확인 (파일 위치에 따라 다를 수 있음)
import com.team.ui.intro.ModeSelectPanel;
import com.team.ui.intro.StartPanel;
import com.team.ui.game.TypingGamePanel;
import com.team.ui.game.OXGamePanel;
// import com.team.ui.game.FillBlankPanel; // 아직 안 만들었으면 주석 처리
// import com.team.ui.game.NotePanel;      // 오답노트 패널 (나중에 추가)

public class MainFrame extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel mainContainer;

    // 패널들을 멤버 변수로 선언 (메서드에서 접근하기 위해)
    private StartPanel startPanel;
    private ModeSelectPanel modePanel;
    private TypingGamePanel typingPanel;
    private OXGamePanel oxPanel;
    
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
        
        // 4. 패널을 카드 레이아웃에 추가 (이름표 붙이기)
        mainContainer.add(startPanel, "Start");
        mainContainer.add(modePanel, "Mode");
        mainContainer.add(typingPanel, "TypingGame"); // ModeSelectPanel의 버튼 이름과 같아야 함
        mainContainer.add(oxPanel, "OXGame");         // ModeSelectPanel의 버튼 이름과 같아야 함
        
        // 나중에 빈칸 채우기나 오답노트 만들면 여기에 추가
        // mainContainer.add(new FillBlankPanel(this), "GameBlank");
        // mainContainer.add(new NotePanel(this), "Note");

        // 5. 프레임에 컨테이너 부착
        add(mainContainer);
        
        // 6. 첫 화면 보여주기
        cardLayout.show(mainContainer, "Start");
        
        setVisible(true);
    }

    // ★ 화면 전환 메서드 (가장 중요)
    public void changePanel(String panelName) {
        // 1. 화면 바꾸기
        cardLayout.show(mainContainer, panelName);
        
        // 2. 게임 화면으로 갈 때마다 '게임 시작(초기화)' 시켜주기
        if (panelName.equals("TypingGame")) {
            typingPanel.startGame(); // 점수 0점, 시간 리셋
        } else if (panelName.equals("OXGame")) {
            oxPanel.startGame();     // 문제 리셋
        } 
        /* 나중에 추가
        else if (panelName.equals("GameBlank")) {
            blankPanel.startGame();
        } else if (panelName.equals("Note")) {
            notePanel.loadWrongAnswers(); // 오답노트는 데이터를 새로고침 해야 함
        }
        */

        // 3. 키보드 입력을 위해 포커스 맞추기
        mainContainer.requestFocusInWindow();
    }
}