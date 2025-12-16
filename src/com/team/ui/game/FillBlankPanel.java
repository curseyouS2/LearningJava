package com.team.ui.game;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import com.team.ui.MainFrame;
import com.team.data.QuestionRepository;
import com.team.model.Question;
import com.team.model.QuestionType;

public class FillBlankPanel extends BaseGamePanel {
    // 상단 및 중앙 영역
    private JLabel questionTextLabel; 
    private JLabel codeLabel;         
    
    // 하단 영역 (CardLayout 사용: 보기 버튼들 <-> 해설+다음버튼)
    private JPanel bottomContainer;
    private JPanel optionsPanel;      // 보기 버튼 4개 있는 곳
    private JPanel feedbackPanel;     // 결과, 해설, 다음버튼 있는 곳
    
    // 피드백 영역 구성요소
    private JLabel resultLabel;       // "정답! ⭕" 또는 "오답! ❌"
    private JTextArea explanationArea;// 해설 텍스트
    private JButton nextBtn;          // [다음 문제] 버튼
    
    private List<Question> questionQueue;
    private int currentIndex = 0;
    private Question currentQuestion;

    public FillBlankPanel(MainFrame mainFrame) {
        super(mainFrame);

        // 1. 중앙 영역 (질문 + 코드 박스)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 10, 50));

        // 질문 텍스트
        questionTextLabel = new JLabel("문제 로딩 중...");
        questionTextLabel.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        questionTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // 코드 박스 (HTML로 스타일링)
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

        // 2. 하단 영역 (CardLayout 설정)
        bottomContainer = new JPanel(new CardLayout());
        bottomContainer.setOpaque(false);
        bottomContainer.setPreferredSize(new Dimension(1280, 250)); // 높이 확보
        bottomContainer.setBorder(BorderFactory.createEmptyBorder(10, 100, 30, 100));

        // [카드 1] 보기 버튼 패널
        optionsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        optionsPanel.setOpaque(false);

        // [카드 2] 피드백 패널 (결과 + 해설 + 다음 버튼)
        feedbackPanel = new JPanel(new BorderLayout());
        feedbackPanel.setOpaque(false);
        
        // 피드백 상단: 결과 멘트
        resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        
        // 피드백 중앙: 해설 (내용이 길 수 있으니 TextArea)
        explanationArea = new JTextArea();
        explanationArea.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
        explanationArea.setLineWrap(true);
        explanationArea.setWrapStyleWord(true);
        explanationArea.setEditable(false);
        explanationArea.setOpaque(false);
        explanationArea.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // 피드백 하단: 다음 버튼
        JPanel nextBtnPanel = new JPanel(new FlowLayout());
        nextBtnPanel.setOpaque(false);
        nextBtn = new JButton("다음 문제 >");
        nextBtn.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        nextBtn.setPreferredSize(new Dimension(200, 50));
        nextBtn.setBackground(new Color(145, 107, 85));
        nextBtn.setForeground(Color.WHITE);
        nextBtn.setFocusPainted(false);
        nextBtn.addActionListener(e -> nextQuestion()); // 클릭 시 다음 문제로

        nextBtnPanel.add(nextBtn);

        feedbackPanel.add(resultLabel, BorderLayout.NORTH);
        feedbackPanel.add(explanationArea, BorderLayout.CENTER);
        feedbackPanel.add(nextBtnPanel, BorderLayout.SOUTH);

        // 컨테이너에 카드 추가
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
        
        // 1. 질문 및 코드 표시
        questionTextLabel.setText(currentQuestion.getQuestionText());
        
        String rawCode = currentQuestion.getCode();
        if (rawCode == null) rawCode = "";
        
        // 코드 하이라이팅 (빈칸 빨간줄)
        String htmlContent = "<html><body style='text-align:center;'>" 
                           + rawCode.replace("\n", "<br>").replace("______", "<u><b style='color:red;'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</b></u>") 
                           + "</body></html>";
        codeLabel.setText(htmlContent);

        // 2. 보기 버튼 업데이트
        updateOptions(currentQuestion.getOptions());
        
        // 3. 화면 전환 -> 보기 선택 화면으로
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
        
        // 1. 점수 처리
        if (isCorrect) {
            updateScore(30); 
            resultLabel.setText("정답입니다! ⭕");
            resultLabel.setForeground(new Color(0, 150, 0));
        } else {
            updateScore(-10);
            QuestionRepository.getInstance().addWrongAnswer(currentQuestion.getId());
            resultLabel.setText("오답! ❌   정답: " + currentQuestion.getAnswer());
            resultLabel.setForeground(Color.RED);
        }

        // 2. 해설 내용 세팅
        explanationArea.setText(currentQuestion.getExplanation());

        // 3. 화면 전환 -> 피드백(해설+다음버튼) 화면으로
        CardLayout cl = (CardLayout) bottomContainer.getLayout();
        cl.show(bottomContainer, "FEEDBACK");
        
        // 다음 버튼에 포커스 (엔터키로 넘어갈 수 있게)
        nextBtn.requestFocus();
    }
}