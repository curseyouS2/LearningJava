package com.team.ui.game;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import com.team.ui.MainFrame;
import com.team.data.QuestionRepository;
import com.team.model.Question;
import com.team.model.QuestionType;
import com.team.util.SoundManager;

public class TypingGamePanel extends BaseGamePanel {
    private JLabel wordLabel;
    private JTextField inputField;
    private JLabel comboLabel; // 콤보 표시
    
    private List<Question> questionQueue;
    private int currentIndex = 0;
    private Question currentQuestion;
    private int combo = 0; // 연속 정답 수

    public TypingGamePanel(MainFrame mainFrame) {
        super(mainFrame);
        
        // 중앙 화면
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(80, 0, 0, 0));

        wordLabel = new JLabel("Ready?");
        wordLabel.setFont(new Font("맑은 고딕", Font.BOLD, 40));
        wordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 콤보 표시
        comboLabel = new JLabel("");
        comboLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        comboLabel.setForeground(new Color(255, 140, 0));
        comboLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        inputField = new JTextField(15);
        inputField.setMaximumSize(new Dimension(400, 50));
        inputField.setFont(new Font("Arial", Font.PLAIN, 24));
        inputField.setHorizontalAlignment(JTextField.CENTER);
        inputField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(145, 107, 85), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // 엔터키 이벤트
        inputField.addActionListener(e -> checkAnswer());

        centerPanel.add(wordLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(comboLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        centerPanel.add(inputField);

        add(centerPanel, BorderLayout.CENTER);
    }

    @Override
    public void startGame() {
        super.startGame();
        questionQueue = QuestionRepository.getInstance().getQuestionsByType(QuestionType.KEYWORD);
        currentIndex = 0;
        combo = 0;
        comboLabel.setText("");
        nextQuestion();
    }

    private void nextQuestion() {
        if (currentIndex >= questionQueue.size()) {
            gameOver();
            return;
        }
        currentQuestion = questionQueue.get(currentIndex++);
        wordLabel.setText(currentQuestion.getQuestionText());
        inputField.setText("");
        inputField.requestFocus();
    }

    private void checkAnswer() {
        String input = inputField.getText().trim();
        
        if (currentQuestion.checkAnswer(input)) {
            updateScore(10); // 정답
            combo++; // 콤보 증가
            updateComboDisplay();
            inputField.setBackground(new Color(200, 255, 200));
            SoundManager.getInstance().playCorrectSound(); // 정답 사운드
        } else {
            updateScore(-5); // 오답
            combo = 0; // 콤보 초기화
            comboLabel.setText("");
            QuestionRepository.getInstance().addWrongAnswer(currentQuestion.getId());
            inputField.setBackground(new Color(255, 200, 200));
            SoundManager.getInstance().playWrongSound(); // 오답 사운드
        }
        
        // 색 원래대로
        Timer t = new Timer(200, e -> inputField.setBackground(Color.WHITE));
        t.setRepeats(false);
        t.start();
        
        nextQuestion();
    }

    private void updateComboDisplay() {
        if (combo >= 3) {
            comboLabel.setText(combo + " Combo!");
        } else {
            comboLabel.setText("");
        }
    }
}