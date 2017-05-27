package com.happystudy.model;

import com.happystudy.dao.DbUtil;
import com.happystudy.util.ConstantValue;
import com.happystudy.util.GBC;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;


public class SingleChoice extends Question {

    private static final int ChoiceNums = 4;
    private String[] choices ;
    private Integer rightChoice;
    private String question;
    private ButtonGroup buttonGroup = new ButtonGroup();
    private JRadioButton[] radioButtons = new JRadioButton[ChoiceNums];
    private JPanel resultPanel = new JPanel();

    public SingleChoice(int questionId)
    {
        super(QuestionType.SingleChoice,questionId);
    }
    @Override
    public void initFromDb() {
        String sql = "select * from SingleChoice where id = ?";
        Object[] params = {this.questionId};
        List<Map<String,Object>> result = DbUtil.excuteQuery(sql,params);
        if(result.size()>0) {
            Map<String, Object> questionRow = result.get(0);
            question = questionRow.get("Question").toString();
            choices = new String[ChoiceNums];
            String pre = "Choice";
            for (int i=0;i<choices.length;i++)
            {
                choices[i] = questionRow.get(pre+(i+1)).toString();
            }
            rightChoice = (Integer)(questionRow.get("RightChoice"));
        }
    }

    @Override
    public void initPanel(JPanel panel) {
        panel.setLayout(new GridBagLayout());

        JPanel qustionTitlePanel = new JPanel(new BorderLayout());
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel type = new JLabel("单选题：");
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

        JPanel choicePanel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
        radioButtons[0]= new JRadioButton("A",false);
        buttonGroup.add(radioButtons[0]);

        radioButtons[1] = new JRadioButton("B",false);
        buttonGroup.add(radioButtons[1]);

        radioButtons[2] = new JRadioButton("C",false);
        buttonGroup.add(radioButtons[2]);

        radioButtons[3] = new JRadioButton("D",false);
        buttonGroup.add(radioButtons[3]);

        for (int i=0;i<radioButtons.length;i++)
        {
            JLabel label = new JLabel(" "+choices[i] +"  ");
            label.setFont(ConstantValue.QuestionFont);
            radioButtons[i].setFont(ConstantValue.QuestionFont);
            choicePanel.add(radioButtons[i]);
            choicePanel.add(label);
        }

        panel.add(choicePanel,new GBC(0,1,1,1).setAnchor(GridBagConstraints.NORTHWEST).setInsets(ConstantValue.AnswerMargin,0,0,0).setWeight(100,100).setFill(GridBagConstraints.BOTH));

        panel.add(resultPanel,new GBC(0,2,1,1).setAnchor(GridBagConstraints.NORTHWEST).setInsets(ConstantValue.AnswerMargin,0,0,0).setWeight(100,100).setFill(GridBagConstraints.BOTH));
    }

    @Override
    public boolean hasAnswer() {
        for(JRadioButton button:radioButtons)
        {
            if(button.isSelected())
                return true;
        }
        return false;
    }

    @Override
    public boolean isRight() {
        if(hasAnswer())
        {
            for(int i=0;i<radioButtons.length;i++)
            {
                if(radioButtons[i].isSelected())
                {
                    if(i==rightChoice-1)
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public void showQuestionDetail(JPanel panel) {
        JRadioButton selectRadioButton = null;
        for(int i=0;i<radioButtons.length;i++)
        {
            if(radioButtons[i].isSelected())
                selectRadioButton = radioButtons[i];
        }
        String yourChoice = selectRadioButton==null?"  ": selectRadioButton.getText();
        for(JRadioButton button:radioButtons)
            button.setEnabled(false);
        resultPanel.setLayout(new GridBagLayout());
        String pre = "回答"+(isRight()?"正确":"错误");
        JLabel rightAnswer = new JLabel(pre+" 正确答案：" + radioButtons[rightChoice-1].getText());
        rightAnswer.setFont(ConstantValue.AnswerFont);
        rightAnswer.setForeground(Color.RED);
        JLabel yourAnswer = new JLabel("你的答案: "+yourChoice);
        yourAnswer.setFont(ConstantValue.AnswerFont);
        yourAnswer.setForeground(Color.RED);

        resultPanel.add(rightAnswer,new GBC(0,0,1,1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setWeight(100,0));
        resultPanel.add(yourAnswer,new GBC(0,1,1,1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setWeight(100,0));
    }
}
