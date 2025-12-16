package com.team.ui.game;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

import com.team.ui.MainFrame;
import com.team.data.QuestionRepository;
import com.team.model.Question;
import com.team.model.QuestionType;

public class FillBlankPanel extends BaseGamePanel {
    private JLabel questionTextLabel; // 상단 질문
    private JLabel codeLabel;         // 중앙 코드 (HTML 사용)
    private JPanel optionsPanel;      // 하단 보기 버튼 4개
    
    private List<Question> questionQueue;
    private int currentIndex = 0;
    private Question currentQuestion;

    public FillBlankPanel(MainFrame mainFrame) {
        super(mainFrame);

        // 1. 중앙 영역 (질문 + 코드 박스)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 20, 50));

        // 질문 텍스트
        questionTextLabel = new JLabel("문제가 여기에 나옵니다.");
        questionTextLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        questionTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // 코드 박스 (HTML로 배경색과 스타일 입히기)
        codeLabel = new JLabel();
        codeLabel.setFont(new Font("Consolas", Font.PLAIN, 18)); // 코드용 폰트
        codeLabel.setOpaque(true);
        codeLabel.setBackground(new Color(230, 230, 230)); // 회색 박스
        codeLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        codeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(questionTextLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20))); // 간격
        centerPanel.add(codeLabel);

        add(centerPanel, BorderLayout.CENTER);

        // 2. 하단 보기 영역 (버튼 4개)
        optionsPanel = new JPanel(new GridLayout(2, 2, 20, 20)); // 2행 2열
        optionsPanel.setOpaque(false);
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 50, 100));
        optionsPanel.setPreferredSize(new Dimension(1280, 200));

        add(optionsPanel, BorderLayout.SOUTH);
    }

    @Override
    public void startGame() {
        super.startGame();
        // 저장소에서 빈칸 문제 가져오기
        questionQueue = QuestionRepository.getInstance().getQuestionsByType(QuestionType.BLANK);
        currentIndex = 0;
        nextQuestion();
    }

    private void nextQuestion() {
        if (currentIndex >= questionQueue.size()) {
            gameOver();
            return;
        }
        currentQuestion = questionQueue.get(currentIndex++);
        
        // 질문 표시
        questionTextLabel.setText(currentQuestion.getQuestionText());
        
        // 코드 표시 (HTML을 써서 빈칸을 밑줄로 강조)
        // 실제로는 문제에 'code' 필드가 따로 없어서, 일단 질문 텍스트를 응용하거나 
        // Question 클래스에 codeText 필드를 추가해야 하지만, 
        // 여기서는 임시로 "_____" 빈칸이 있는 형태라고 가정하고 꾸며줍니다.
        String codeHtml = "<html><body style='text-align:center; width: 400px;'>" 
                        + "public class Test {<br>"
                        + "&nbsp;&nbsp;&nbsp;&nbsp;public static <b>_______</b> main(String[] args) {<br>"
                        + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println(\"Hello\");<br>"
                        + "&nbsp;&nbsp;&nbsp;&nbsp;}<br>"
                        + "}" 
                        + "</body></html>";
        // (참고: 실제로는 Question 객체에 코드 텍스트가 있어야 정확히 바뀝니다. 
        // 지금은 데모를 위해 하드코딩된 HTML을 보여줍니다.)
        codeLabel.setText(codeHtml);

        // 보기 버튼 업데이트
        updateOptions(currentQuestion.getOptions());
    }

    private void updateOptions(String[] options) {
        optionsPanel.removeAll(); // 기존 버튼 제거
        
        if (options != null) {
            for (String opt : options) {
                JButton btn = new JButton(opt);
                btn.setFont(new Font("Arial", Font.BOLD, 20));
                btn.setBackground(Color.WHITE);
                btn.setFocusPainted(false);
                
                btn.addActionListener(e -> checkAnswer(opt, btn));
                optionsPanel.add(btn);
            }
        }
        optionsPanel.revalidate();
        optionsPanel.repaint();
    }

    private void checkAnswer(String selected, JButton btn) {
        boolean isCorrect = currentQuestion.checkAnswer(selected);
        
        if (isCorrect) {
            score += 30; // 빈칸은 어려우니까 고득점
            scoreLabel.setText("점수: " + score);
            btn.setBackground(Color.GREEN);
        } else {
            score -= 10;
            QuestionRepository.getInstance().addWrongAnswer(currentQuestion.getId());
            btn.setBackground(Color.RED);
            JOptionPane.showMessageDialog(this, "오답! 정답은: " + currentQuestion.getAnswer() 
                + "\n해설: " + currentQuestion.getExplanation());
        }

        // 잠시 후 다음 문제
        Timer t = new Timer(1000, e -> nextQuestion());
        t.setRepeats(false);
        t.start();
    }
}