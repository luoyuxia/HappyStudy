package com.happystudy.model;

import com.happystudy.dao.DbUtil;

import javax.swing.*;
import java.awt.*;
import java.util.*;


public abstract class Question {
    protected QuestionType questionType;
    protected int questionId;

    public Question(QuestionType questionType,int questionId)
    {
        this.questionType = questionType;
        this.questionId = questionId;
        initFromDb();
    }

    public abstract void initFromDb();

    public abstract void initPanel(JPanel panel) ;

    public  abstract boolean hasAnswer();

    public abstract boolean isRight();

    public  abstract void showQuestionDetail(JPanel panel);
    public boolean saveWrongToDb(int userId)
    {
        String selectSql = "select Quantity from WrongQuestion where Userid = ? and QuestionType =? and QuestionId = ?";
        Object[] params = {userId,this.questionType,this.questionId};

        java.util.List<Map<String,Object>> result = DbUtil.excuteQuery(selectSql,params);
        if(result.size() == 0) {
            String insertSql = "insert into WrongQuestion(QuestionType,QuestionId,UserId,Quantity)" +
                    " values(?,?,?,1)";
         Object[] params1 = {this.questionType, this.questionId, userId};
            return DbUtil.executeUpdate(insertSql, params1) != 0;
        }
        else
        {
            int current_quantity =(Integer)result.get(0).get("Quantity");
            String updateQuantity = "update WrongQuestion set Quantity = ? where userId = ? and QuestionType=? and QuestionId = ?";
            Object[] params2 = {current_quantity+1,userId,this.questionType,this.questionId};
            return DbUtil.executeUpdate(updateQuantity,params2)!=0;
        }
    }
}
