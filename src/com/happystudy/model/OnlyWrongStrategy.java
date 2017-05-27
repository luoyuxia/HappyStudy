package com.happystudy.model;

import com.happystudy.dao.DbUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by qi on 2017/5/27.
 */
public class OnlyWrongStrategy implements Strategy {
    private Random random = new Random();
    @Override
    public List<QuestionIdentify> generateQuestion(User user, int questionNums) {
        String sql = "select * from WrongQuestion where userid = ?";
        Object[] params = {user.getId()};
        List<Map<String,Object>> wrongQuestionsRows = DbUtil.excuteQuery(sql,params);
        //计算出总共应产生的错题数，如果用户的错题数小于用户要做的题目数，则只产生用户的错题数
        int total = wrongQuestionsRows.size() > questionNums? questionNums:wrongQuestionsRows.size();
        //用户的总共错题数
        int userWrongQuestions = wrongQuestionsRows.size();
        List<QuestionIdentify> questionIdentifyList = new ArrayList<>();
        for(int i=0;i<total;i++)
        {
            int nextQuestion = random.nextInt(userWrongQuestions);
            Map<String,Object> questionRow = wrongQuestionsRows.get(nextQuestion);
            String questionType = questionRow.get("QuestionType").toString();
            int questionId =Integer.parseInt(questionRow.get("QuestionId").toString());
            QuestionIdentify questionIdentify = new QuestionIdentify(QuestionType.valueOf(questionType),questionId);
            questionIdentifyList.add(questionIdentify);
            wrongQuestionsRows.remove(nextQuestion);
            userWrongQuestions -- ;
        }
        return questionIdentifyList;
    }

    @Override
    public String toString()
    {
        return "只做错题";
    }
}
