package com.team.ui.game;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import com.team.ui.MainFrame;
import com.team.data.QuestionRepository;
import com.team.model.Question;

public class NotePanel extends BaseGamePanel {
    private JTextArea questionArea; // 문제 보여주는 곳
    private JTextArea answerArea;   // 정답/해설 보여주는 곳
    private JLabel infoLabel;       // "총 3개 중 1번째" 표시
    private JButton checkBtn;       // 정답 확인 버튼
    private JButton nextBtn;        // 다음 문제 버튼
    
    private List<Question> wrongList;
    private int currentIndex = 0;

    public NotePanel(MainFrame mainFrame) {
        super(mainFrame);
        // 오답노트에서는 상단 타이머/점수가 필요 없으니 숨김 처리
        timeBar.setVisible(false);
        scoreLabel.setText("오답 복습 모드 📝");
        
        // 1. 중앙 영역 (카드 형태)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // 문제 박스
        questionArea = createTextArea(20); // 글자 크기 20
        questionArea.setText("틀린 문제가 없습니다.");
        
        // 해설 박스 (처음엔 숨김? 내용은 비워둠)
        answerArea = createTextArea(18);
        answerArea.setForeground(new Color(0, 100, 0)); // 초록색 글씨
        answerArea.setText("");

        centerPanel.add(new JLabel("=== 문제 ==="));
        centerPanel.add(questionArea);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(new JLabel("=== 정답 및 해설 ==="));
        centerPanel.add(answerArea);

        add(centerPanel, BorderLayout.CENTER);

        // 2. 하단 버튼 영역
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setOpaque(false);
        
        infoLabel = new JLabel("0 / 0");
        infoLabel.setFont(new Font("Arial", Font.BOLD, 20));

        checkBtn = new JButton("정답 확인");
        styleButton(checkBtn);
        checkBtn.addActionListener(e -> showAnswer());

        nextBtn = new JButton("다음 문제 >");
        styleButton(nextBtn);
        nextBtn.addActionListener(e -> nextQuestion());
        nextBtn.setEnabled(false); // 처음엔 비활성

        bottomPanel.add(infoLabel);
        bottomPanel.add(Box.createHorizontalStrut(20)); // 간격
        bottomPanel.add(checkBtn);
        bottomPanel.add(nextBtn);

        // BaseGamePanel의 남쪽(SOUTH)에 이미 홈 버튼이 있으니, 그 위에 얹거나 교체
        // 여기서는 기존 홈버튼 패널 위에 하나 더 얹는 방식으로 처리
        JPanel southContainer = new JPanel(new BorderLayout());
        southContainer.setOpaque(false);
        southContainer.add(bottomPanel, BorderLayout.CENTER);
        
        // 원래 있던 홈 버튼 패널 가져오기 (꼼수)
        Component oldBottom = ((BorderLayout)getLayout()).getLayoutComponent(BorderLayout.SOUTH);
        if(oldBottom != null) southContainer.add(oldBottom, BorderLayout.SOUTH);
        
        add(southContainer, BorderLayout.SOUTH);
    }
    
    @Override
    public void startGame() {
        wrongList = QuestionRepository.getInstance().getWrongQuestions();
        currentIndex = 0;
        
        if (wrongList.isEmpty()) {
            questionArea.setText("축하합니다! 틀린 문제가 없습니다. \n완벽해요! 🎉");
            answerArea.setText("");
            checkBtn.setEnabled(false);
            nextBtn.setEnabled(false);
            infoLabel.setText("0 / 0");
        } else {
            checkBtn.setEnabled(true);
            showQuestion();
        }
    }

    private void showQuestion() {
        Question q = wrongList.get(currentIndex);
        questionArea.setText(q.getQuestionText());
        answerArea.setText("?"); // 정답 가림
        nextBtn.setEnabled(false); // 아직 못 넘어감
        checkBtn.setEnabled(true);
        infoLabel.setText((currentIndex + 1) + " / " + wrongList.size());
    }

    private void showAnswer() {
        Question q = wrongList.get(currentIndex);
        String content = "정답: " + q.getAnswer() + "\n\n[해설]\n" + q.getExplanation();
        answerArea.setText(content);
        
        checkBtn.setEnabled(false);
        nextBtn.setEnabled(true); // 이제 넘어갈 수 있음
    }

    private void nextQuestion() {
        currentIndex++;
        if (currentIndex >= wrongList.size()) {
            JOptionPane.showMessageDialog(this, "복습을 완료했습니다!");
            mainFrame.changePanel("Mode"); // 메뉴로 복귀
            return;
        }
        showQuestion();
    }

    // 스타일 헬퍼
    private JTextArea createTextArea(int fontSize) {
        JTextArea ta = new JTextArea(3, 20);
        ta.setFont(new Font("맑은 고딕", Font.BOLD, fontSize));
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setEditable(false);
        ta.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return ta;
    }
    
    private void styleButton(JButton btn) {
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
    }
}