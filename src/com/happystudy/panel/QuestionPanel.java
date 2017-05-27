package com.happystudy.panel;

import com.happystudy.model.Question;

import javax.swing.*;
import java.awt.*;

public class QuestionPanel extends JPanel {
    private Question question;
    public QuestionPanel(Question question)
    {
        super();
        this.question = question;
        initComponent();
        question.initPanel(this);
    }

    //在问题面板中显示完整答题信息
    public void showResult()
    {
        question.showQuestionDetail(this);
    }

    public void saveWrongQuestion(int userId)
    {
        question.saveWrongToDb(userId);
    }

    //答案是否正确
    public boolean isRight()
    {
        return question.isRight();
    }

    //是否回答了题目
    public boolean hasAnswer()
    {
        return question.hasAnswer();
    }

    private void initComponent()
    {

    }
}
