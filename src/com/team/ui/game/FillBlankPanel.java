package com.team.ui.game;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import com.team.ui.MainFrame;
import com.team.data.QuestionRepository;
import com.team.model.Question;
import com.team.model.QuestionType;
import com.team.util.SoundManager;

public class FillBlankPanel extends BaseGamePanel {
    private JLabel questionTextLabel; 
    private JLabel codeLabel;         
    
    private JPanel bottomContainer;
    private JPanel optionsPanel;
    private JPanel feedbackPanel;
    
    private JLabel resultLabel;
    private JTextArea explanationArea;
    private JButton nextBtn;
    
    private List<Question> questionQueue;
    private int currentIndex = 0;
    private Question currentQuestion;

    public FillBlankPanel(MainFrame mainFrame) {
        super(mainFrame);

        // 중앙 영역
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 10, 50));

        questionTextLabel = new JLabel("문제 로딩 중...");
        questionTextLabel.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        questionTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        codeLabel = new JLabel();
        codeLabel.setFont(new Font("Consolas", Font.PLAIN, 16)); 
        codeLabel.setOpaque(true);
        codeLabel.setBackground(new Color(230, 230, 230)); 
        codeLabel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        codeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(questionTextLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        centerPanel.add(codeLabel);

        add(centerPanel, BorderLayout.CENTER);

        // 하단 영역
        bottomContainer = new JPanel(new CardLayout());
        bottomContainer.setOpaque(false);
        bottomContainer.setPreferredSize(new Dimension(1280, 250));
        bottomContainer.setBorder(BorderFactory.createEmptyBorder(10, 100, 30, 100));

        // 보기 버튼
        optionsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        optionsPanel.setOpaque(false);

        // 피드백 패널
        feedbackPanel = new JPanel(new BorderLayout());
        feedbackPanel.setOpaque(false);
        
        resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        
        explanationArea = new JTextArea();
        explanationArea.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
        explanationArea.setLineWrap(true);
        explanationArea.setWrapStyleWord(true);
        explanationArea.setEditable(false);
        explanationArea.setOpaque(false);
        explanationArea.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JPanel nextBtnPanel = new JPanel(new FlowLayout());
        nextBtnPanel.setOpaque(false);
        nextBtn = new JButton("다음 문제 >");
        nextBtn.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        nextBtn.setPreferredSize(new Dimension(200, 50));
        nextBtn.setBackground(new Color(145, 107, 85));
        nextBtn.setForeground(Color.WHITE);
        nextBtn.setFocusPainted(false);
        nextBtn.addActionListener(e -> nextQuestion());

        nextBtnPanel.add(nextBtn);

        feedbackPanel.add(resultLabel, BorderLayout.NORTH);
        feedbackPanel.add(explanationArea, BorderLayout.CENTER);
        feedbackPanel.add(nextBtnPanel, BorderLayout.SOUTH);

        bottomContainer.add(optionsPanel, "OPTIONS");
        bottomContainer.add(feedbackPanel, "FEEDBACK");

        add(bottomContainer, BorderLayout.SOUTH);
    }

    @Override
    public void startGame() {
        super.startGame();
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
        
        questionTextLabel.setText(currentQuestion.getQuestionText());
        
        String rawCode = currentQuestion.getCode();
        if (rawCode == null) rawCode = "";
        
        String htmlContent = "<html><body style='text-align:center;'>" 
                           + rawCode.replace("\n", "<br>").replace("______", 
                             "<u><b style='color:red;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></u>") 
                           + "</body></html>";
        codeLabel.setText(htmlContent);

        updateOptions(currentQuestion.getOptions());
        
        CardLayout cl = (CardLayout) bottomContainer.getLayout();
        cl.show(bottomContainer, "OPTIONS");
    }

    private void updateOptions(String[] options) {
        optionsPanel.removeAll(); 
        
        if (options != null) {
            for (String opt : options) {
                JButton btn = new JButton(opt);
                btn.setFont(new Font("Arial", Font.BOLD, 20));
                btn.setBackground(Color.WHITE);
                btn.setFocusPainted(false);
                
                btn.addActionListener(e -> checkAnswer(opt));
                optionsPanel.add(btn);
            }
        }
        optionsPanel.revalidate();
        optionsPanel.repaint();
    }

    private void checkAnswer(String selected) {
        boolean isCorrect = currentQuestion.checkAnswer(selected);
        
        if (isCorrect) {
            updateScore(30);
            resultLabel.setText("정답입니다!");
            resultLabel.setForeground(new Color(0, 150, 0));
            SoundManager.getInstance().playCorrectSound();
        } else {
            updateScore(-10);
            QuestionRepository.getInstance().addWrongAnswer(currentQuestion.getId());
            resultLabel.setText("오답!   정답: " + currentQuestion.getAnswer());
            resultLabel.setForeground(Color.RED);
            SoundManager.getInstance().playWrongSound();
        }

        explanationArea.setText(currentQuestion.getExplanation());

        CardLayout cl = (CardLayout) bottomContainer.getLayout();
        cl.show(bottomContainer, "FEEDBACK");
        nextBtn.requestFocus();
    }
}