package com.happystudy.model;

import com.happystudy.dao.DbUtil;
import com.happystudy.util.ConstantValue;
import com.happystudy.util.GBC;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;



//选择题
public class Completion extends Question{

    private String question;
    private String rightAnwser;
    private JTextField userAnswerField;
    private Font answerFont;
    private JPanel questionPanel;

    public Completion(int questionId)
    {
        super(QuestionType.Completion,questionId);
    }


    public void initFromDb()
    {
        String sql = "select question,answer from Completion where id = ?";
        Object[] params = {this.questionId};
        List<Map<String,Object>> questionList = DbUtil.excuteQuery(sql,params);
        if(questionList.size()>0)
        {
            this.question = questionList.get(0).get("Question").toString();
            this.rightAnwser = questionList.get(0).get("Answer").toString();
        }
    }
    @Override
    public void initPanel(JPanel panel) {
        panel.setLayout(new GridBagLayout());

        questionPanel = new JPanel(new BorderLayout());

        JPanel questionTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel questionType = new JLabel("填空题：");
        questionType.setFont(ConstantValue.QuestionFont);
        questionType.setForeground(Color.BLACK);
        questionTypePanel.add(questionType);
        questionPanel.add(questionTypePanel,BorderLayout.NORTH);

        JTextArea questionArea = new JTextArea(question,20,50);
        questionArea.setLineWrap(true);
        questionArea.setEditable(false);
        questionArea.setBackground(panel.getBackground());
        questionArea.setFont(ConstantValue.QuestionFont);
        questionPanel.add(questionArea,BorderLayout.CENTER);
        panel.add(questionPanel,new GBC(0,1,1,1).setAnchor(GridBagConstraints.NORTHWEST).setWeight(100,100).setFill(GridBagConstraints.BOTH));

        JPanel answerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel answerLabel = new JLabel("你的答案：");
        answerLabel.setFont(ConstantValue.AnswerFont);
        answerLabel.setForeground(Color.BLACK);
        userAnswerField = new JTextField(20);
        userAnswerField.setFont(ConstantValue.AnswerFont);
        answerPanel.add(answerLabel);
        answerPanel.add(userAnswerField);
        panel.add(answerPanel,new GBC(0,2,1,1).setAnchor(GridBagConstraints.NORTHWEST).setWeight(100,100).setFill(GridBagConstraints.HORIZONTAL));
    }

    @Override
    public boolean hasAnswer() {
        return userAnswerField.getText().length()!=0;
    }

    @Override
    public boolean isRight() {
        return userAnswerField.getText().equals(this.rightAnwser);
    }

    @Override
    public void showQuestionDetail(JPanel panel) {
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String result = "回答" + (isRight()?"正确  ":"错误  ");
        JLabel label = new JLabel(result+"正确答案： " + rightAnwser);
        label.setFont(ConstantValue.AnswerFont);
        label.setForeground(Color.red);
        rightPanel.add(label);
        userAnswerField.setEditable(false);
        questionPanel.add(rightPanel,BorderLayout.SOUTH);
    }
}
