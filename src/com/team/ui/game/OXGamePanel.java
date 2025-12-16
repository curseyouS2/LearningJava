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
    
    private List<Question> questionQueue;
    private int currentIndex = 0;
    private Question currentQuestion;

    public OXGamePanel(MainFrame mainFrame) {
        super(mainFrame);
        
        // 1. 문제 영역
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));

        questionArea = new JTextArea("문제 로딩 중...");
        questionArea.setFont(new Font("맑은 고딕", Font.BOLD, 28));
        questionArea.setLineWrap(true);
        questionArea.setWrapStyleWord(true);
        questionArea.setEditable(false);
        questionArea.setOpaque(false);
        
        resultLabel = new JLabel(" ", SwingConstants.CENTER);
        resultLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        
        centerPanel.add(questionArea, BorderLayout.CENTER);
        centerPanel.add(resultLabel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);

        // 2. 버튼 영역
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        btnPanel.setOpaque(false);
        btnPanel.setPreferredSize(new Dimension(1280, 200));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 50, 100));

        JButton btnO = createBtn("O", Color.GREEN);
        JButton btnX = createBtn("X", Color.RED);

        btnO.addActionListener(e -> checkAnswer("O"));
        btnX.addActionListener(e -> checkAnswer("X"));

        btnPanel.add(btnO);
        btnPanel.add(btnX);
        add(btnPanel, BorderLayout.SOUTH);
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
        // 저장소에서 OX 문제 가져오기
        questionQueue = QuestionRepository.getInstance().getQuestionsByType(QuestionType.OX);
        currentIndex = 0;
        resultLabel.setText(" ");
        nextQuestion();
    }

    private void nextQuestion() {
        if (currentIndex >= questionQueue.size()) {
            gameOver();
            return;
        }
        currentQuestion = questionQueue.get(currentIndex++);
        questionArea.setText(currentQuestion.getQuestionText());
        resultLabel.setText(" ");
    }

    private void checkAnswer(String userAns) {
        if (currentQuestion.checkAnswer(userAns)) {
            score += 20;
            scoreLabel.setText("점수: " + score);
            resultLabel.setText("정답입니다! ⭕");
            resultLabel.setForeground(new Color(0, 150, 0));
        } else {
            score -= 10;
            // ★ 틀린 문제 ID 신고
            QuestionRepository.getInstance().addWrongAnswer(currentQuestion.getId());
            resultLabel.setText("오답! ❌ 해설: " + currentQuestion.getExplanation());
            resultLabel.setForeground(Color.RED);
        }
        
        // 1.5초 뒤 다음 문제 (해설 읽을 시간 줌)
        Timer t = new Timer(1500, e -> nextQuestion());
        t.setRepeats(false);
        t.start();
    }
}