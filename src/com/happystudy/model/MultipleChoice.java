package com.happystudy.model;

import com.happystudy.dao.DbUtil;
import com.happystudy.util.ConstantValue;
import com.happystudy.util.GBC;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MultipleChoice extends Question{

    private static final int Options = 4;
    private JCheckBox[] checkBoxes;
    private List<Integer> rightChoices;
    private JPanel resultPanel = new JPanel();
    private String question ;
    private String[] choices ;
    public MultipleChoice(int questionId)
    {
        super(QuestionType.MultipleChoice,questionId);
    }
    @Override
    public void initFromDb() {
        String sql = "select * from MultipleChoice where id = ?";
        Object[] params = {questionId};
        List<Map<String,Object>> resultRows = DbUtil.excuteQuery(sql,params);
        if(resultRows.size()>0)
        {
            Map<String,Object> dbQuestion = resultRows.get(0);
            question = dbQuestion.get("Question").toString();
            choices = new String[Options];
            for(int i=0;i<choices.length;i++)
            {
                choices[i] = dbQuestion.get("Choice"+(i+1)).toString();
            }
            rightChoices = new ArrayList<>();
            String right = dbQuestion.get("RightChoice").toString();
            String[] rightOptions = right.split(" ");
            for(String option:rightOptions)
            {
                rightChoices.add(Integer.parseInt(option.trim()));
            }
        }
    }

    @Override
    public void initPanel(JPanel panel) {
        panel.setLayout(new GridBagLayout());

        JPanel qustionTitlePanel = new JPanel(new BorderLayout());
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel type = new JLabel("多项选择题：");
        type.setFont(ConstantValue.QuestionFont);
        typePanel.add(type);
        qustionTitlePanel.add(typePanel,BorderLayout.NORTH);

        JTextArea questionArea = new JTextArea(question,20,50);
        questionArea.setLineWrap(true);
        questionArea.setEditable(false);
        questionArea.setBackground(panel.getBackground());
        questionArea.setFont(ConstantValue.QuestionFont);
        qustionTitlePanel.add(questionArea,BorderLayout.CENTER);

        panel.add(qustionTitlePanel,new GBC(0,0,1,1).setAnchor(GridBagConstraints.NORTHWEST).setFill(GridBagConstraints.HORIZONTAL).setWeight(100,0));

        JPanel choicePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        checkBoxes = new JCheckBox[Options];
        for(int i=0;i<choices.length;i++)
        {
            checkBoxes[i] = new JCheckBox(""+choices[i]+"   ");
            choicePanel.add(checkBoxes[i]);
            checkBoxes[i].setFont(ConstantValue.QuestionFont);
        }

        panel.add(choicePanel,new GBC(0,1,1,1).setInsets(ConstantValue.AnswerMargin,0,0,0).setAnchor(GridBagConstraints.NORTHWEST).setFill(GridBagConstraints.BOTH).setWeight(100,100));

        resultPanel = new JPanel();
        panel.add(resultPanel,new GBC(0,2,1,1).setAnchor(GridBagConstraints.NORTHWEST).setFill(GridBagConstraints.BOTH).setWeight(100,100).setInsets(ConstantValue.AnswerMargin,0,0,0));
    }

    @Override
    public boolean hasAnswer() {
        for(JCheckBox checkBox : checkBoxes)
            if(checkBox.isSelected())
                return true;
        return false;
    }

    @Override
    public boolean isRight() {
        List<Integer> answers = new ArrayList<>();
        for(int i=0;i<checkBoxes.length;i++)
        {
            if(checkBoxes[i].isSelected())
                answers.add(i+1);
        }
        return answers.equals(rightChoices);
    }

    @Override
    public void showQuestionDetail(JPanel panel) {
        resultPanel.setLayout(new GridBagLayout());
        String pre = "回答" + (isRight()?"正确 ":"错误 ");
        StringBuilder rightAnswer = new StringBuilder();
        for(int i : rightChoices)
        {
            rightAnswer.append(choices[i]).append(" ");
        }
        JLabel rightLabel = new JLabel(pre+"正确答案："+rightAnswer);
        rightLabel.setFont(ConstantValue.AnswerFont);
        rightLabel.setForeground(Color.red);

        StringBuilder userAnswer = new StringBuilder();
        for(int i=0;i<checkBoxes.length;i++)
        {
            if(checkBoxes[i].isSelected())
                userAnswer.append(checkBoxes[i].getText().trim()).append(" ");
        }
        JLabel userAnswerLabel = new JLabel("你的答案："+userAnswer.toString());
        userAnswerLabel.setFont(ConstantValue.AnswerFont);
        userAnswerLabel.setForeground(Color.red);

        resultPanel.add(rightLabel,new GBC(0,0,1,1).setAnchor(GridBagConstraints.NORTHWEST).setFill(GridBagConstraints.HORIZONTAL).setWeight(100,0));
        resultPanel.add(userAnswerLabel,new GBC(0,1,1,1).setAnchor(GridBagConstraints.NORTHWEST).setFill(GridBagConstraints.HORIZONTAL).setWeight(100,0));


    }
}
