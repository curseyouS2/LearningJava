package com.team.ui.game;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import com.team.ui.MainFrame;
import com.team.data.QuestionRepository;
import com.team.model.Question;
import com.team.model.QuestionType;

public class TypingGamePanel extends BaseGamePanel {
    private JLabel wordLabel;
    private JTextField inputField;
    
    private List<Question> questionQueue; // 풀 문제 목록
    private int currentIndex = 0;
    private Question currentQuestion;

    public TypingGamePanel(MainFrame mainFrame) {
        super(mainFrame);
        
        // 중앙 화면 구성
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(100, 0, 0, 0));

        wordLabel = new JLabel("Ready?");
        wordLabel.setFont(new Font("Arial", Font.BOLD, 40));
        wordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        inputField = new JTextField(15);
        inputField.setMaximumSize(new Dimension(400, 50));
        inputField.setFont(new Font("Arial", Font.PLAIN, 24));
        inputField.setHorizontalAlignment(JTextField.CENTER);
        
        inputField.addActionListener(e -> checkAnswer()); // 엔터키 이벤트

        centerPanel.add(wordLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        centerPanel.add(inputField);

        add(centerPanel, BorderLayout.CENTER);
    }

    @Override
    public void startGame() {
        super.startGame();
        // 저장소에서 타자 문제만 가져오기
        questionQueue = QuestionRepository.getInstance().getQuestionsByType(QuestionType.KEYWORD);
        currentIndex = 0;
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
            score += 10;
            scoreLabel.setText("점수: " + score);
            inputField.setBackground(new Color(200, 255, 200)); // 초록
        } else {
            score -= 5;
            // ★ 틀렸으니 저장소에 ID 신고
            QuestionRepository.getInstance().addWrongAnswer(currentQuestion.getId());
            inputField.setBackground(new Color(255, 200, 200)); // 빨강
        }
        
        Timer t = new Timer(200, e -> inputField.setBackground(Color.WHITE));
        t.setRepeats(false);
        t.start();
        
        nextQuestion();
    }
}