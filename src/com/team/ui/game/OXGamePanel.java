package com.team.ui.game;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import com.team.ui.MainFrame;
import com.team.data.QuestionRepository;
import com.team.model.Question;
import com.team.model.QuestionType;

public class OXGamePanel extends BaseGamePanel {
    private JTextArea questionArea;
    private JLabel resultLabel;
    
    // 버튼들을 관리할 패널들
    private JPanel buttonContainer; // 하단 영역 전체
    private JPanel oxButtonPanel;   // O, X 버튼 있는 패널
    private JButton nextBtn;        // [다음 문제] 버튼

    private List<Question> questionQueue;
    private int currentIndex = 0;
    private Question currentQuestion;

    public OXGamePanel(MainFrame mainFrame) {
        super(mainFrame);
        
        // 1. 문제 영역 (중앙)
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 20, 100));

        questionArea = new JTextArea("문제 로딩 중...");
        questionArea.setFont(new Font("맑은 고딕", Font.BOLD, 28));
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        questionArea.setEditable(false);
        questionArea.setOpaque(false);
        
        // 결과 및 해설 표시 (처음엔 빈칸)
        resultLabel = new JLabel(" ", SwingConstants.CENTER);
        resultLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        
        centerPanel.add(questionArea, BorderLayout.CENTER);
        centerPanel.add(resultLabel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);

        // 2. 하단 버튼 영역 (CardLayout 느낌으로 교체)
        buttonContainer = new JPanel(new CardLayout());
        buttonContainer.setOpaque(false);
        buttonContainer.setPreferredSize(new Dimension(1280, 200));
        buttonContainer.setBorder(BorderFactory.createEmptyBorder(20, 100, 50, 100));

        // [상태 A] O/X 버튼 패널
        oxButtonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        oxButtonPanel.setOpaque(false);
        JButton btnO = createBtn("O", Color.GREEN);
        JButton btnX = createBtn("X", Color.RED);
        
        btnO.addActionListener(e -> checkAnswer("O"));
        btnX.addActionListener(e -> checkAnswer("X"));
        
        oxButtonPanel.add(btnO);
        oxButtonPanel.add(btnX);

        // [상태 B] 다음 문제 버튼 패널
        JPanel nextButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        nextButtonPanel.setOpaque(false);
        
        nextBtn = new JButton("다음 문제 >");
        nextBtn.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        nextBtn.setPreferredSize(new Dimension(300, 80));
        nextBtn.setBackground(new Color(145, 107, 85));
        nextBtn.setForeground(Color.WHITE);
        nextBtn.setFocusPainted(false);
        nextBtn.addActionListener(e -> nextQuestion()); // 클릭 시 다음 문제로

        nextButtonPanel.add(nextBtn);

        // 컨테이너에 두 패널 추가
        buttonContainer.add(oxButtonPanel, "OX");
        buttonContainer.add(nextButtonPanel, "NEXT");

        add(buttonContainer, BorderLayout.SOUTH);
    }

    private JButton createBtn(String text, Color c) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 80));
        btn.setForeground(c);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    @Override
    public void startGame() {
        super.startGame();
        questionQueue = QuestionRepository.getInstance().getQuestionsByType(QuestionType.OX);
        currentIndex = 0;
        nextQuestion();
    }

    private void nextQuestion() {
        // 문제 다 풀었는지 체크
        if (currentIndex >= questionQueue.size()) {
            gameOver();
            return;
        }

        // 데이터 로드
        currentQuestion = questionQueue.get(currentIndex++);
        questionArea.setText(currentQuestion.getQuestionText());
        
        // UI 초기화 (해설 지우고, O/X 버튼 보여주기)
        resultLabel.setText(" "); 
        CardLayout cl = (CardLayout) buttonContainer.getLayout();
        cl.show(buttonContainer, "OX"); // O/X 버튼 보이기
    }

    private void checkAnswer(String userAns) {
        boolean isCorrect = currentQuestion.checkAnswer(userAns);

        if (isCorrect) {
        	updateScore(20); 
            
            resultLabel.setText("정답입니다! ⭕");
            resultLabel.setForeground(new Color(0, 150, 0));
        } else {
        	updateScore(-10); 
            QuestionRepository.getInstance().addWrongAnswer(currentQuestion.getId());
            //해설 
            resultLabel.setText("<html><center>오답! ❌<br>해설: " + currentQuestion.getExplanation() + "</center></html>");
            resultLabel.setForeground(Color.RED);
        }
        
        // 버튼을 [다음 문제]로 교체 
        CardLayout cl = (CardLayout) buttonContainer.getLayout();
        cl.show(buttonContainer, "NEXT");
        nextBtn.requestFocus(); // 엔터 치면 넘어가게 포커스
    }
}