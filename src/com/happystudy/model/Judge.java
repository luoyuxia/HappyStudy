package com.happystudy.model;

import com.happystudy.dao.DbUtil;
import com.happystudy.util.ConstantValue;
import com.happystudy.util.GBC;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;


public class Judge extends Question{

    private String question ;
    private boolean isRight;
    private JRadioButton[] radioButtons;
    private ButtonGroup buttonGroup;
    private JPanel resultPanel = new JPanel();
    public Judge(int questionId)
    {
        super(QuestionType.Judge,questionId);
    }
    @Override
    public void initFromDb() {
        String sql = "select * from Judge where id = ?";
        Object[] params = {questionId};
        List<Map<String,Object>> resultRows = DbUtil.excuteQuery(sql,params);
        if(resultRows.size()>0) {
            Map<String, Object> oneQuestion = resultRows.get(0);
            question = oneQuestion.get("Question").toString();
            isRight = Integer.parseInt(oneQuestion.get("IsRight").toString())==1;
        }
    }

    @Override
    public void initPanel(JPanel panel) {
        panel.setLayout(new GridBagLayout());

        JPanel qustionTitlePanel = new JPanel(new BorderLayout());
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel type = new JLabel("判断题：");
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
        buttonGroup = new ButtonGroup();
        radioButtons = new JRadioButton[2];
        radioButtons[0] = new JRadioButton("错误  ",false);
        radioButtons[1] = new JRadioButton("正确  ",false);
        for(JRadioButton button:radioButtons)
        {
            buttonGroup.add(button);
            button.setFont(ConstantValue.QuestionFont);
            choicePanel.add(button);
        }
        panel.add(choicePanel,new GBC(0,1,1,1).setAnchor(GridBagConstraints.NORTHWEST).setInsets(ConstantValue.AnswerMargin,0,0,0).setWeight(100,100).setFill(GridBagConstraints.BOTH));

        panel.add(resultPanel,new GBC(0,2,1,1).setAnchor(GridBagConstraints.NORTHWEST).setInsets(ConstantValue.AnswerMargin,0,0,0).setWeight(100,100).setFill(GridBagConstraints.BOTH));
    }

    @Override
    public boolean hasAnswer() {
        for(JRadioButton button:radioButtons)
            if(button.isSelected())
                return true;
        return false;
    }

    @Override
    public boolean isRight() {
        for(int i=0;i<radioButtons.length;i++)
        {
            if(radioButtons[i].isSelected())
                return i == (isRight?1:0);
        }
        return false;
    }

    @Override
    public void showQuestionDetail(JPanel panel) {
        String pre = "回答" + (isRight()?"正确":"错误");
        JLabel rightLabel = new JLabel(pre + " 正确答案:" +(isRight?"正确":"错误"));
        rightLabel.setFont(ConstantValue.AnswerFont);
        rightLabel.setForeground(Color.red);

        JRadioButton select = null;
        for (JRadioButton button:radioButtons)
        {
            if(button.isSelected())
                select = button;
        }
        String yourAnswer = select!=null?select.getText():"";
        JLabel yourAnswerLabel = new JLabel("你的答案: "+ yourAnswer);
        yourAnswerLabel.setFont(ConstantValue.AnswerFont);
        yourAnswerLabel.setForeground(Color.red);

        resultPanel.setLayout(new GridBagLayout());
        resultPanel.add(rightLabel,new GBC(0,0,1,1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setWeight(100,0));
        resultPanel.add(yourAnswerLabel,new GBC(0,1,1,1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.WEST).setWeight(100,0));
    }
}
