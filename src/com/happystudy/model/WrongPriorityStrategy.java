package com.happystudy.model;

import com.happystudy.dao.DbUtil;
import com.happystudy.dao.QuestionDao;

import java.util.*;

/**
 * Created by qi on 2017/5/27.
 */

//错题优先
public class WrongPriorityStrategy implements Strategy{
    private Random random = new Random();
    @Override
    public List<QuestionIdentify> generateQuestion(User user, int questionNums) {
        String sql = "select * from WrongQuestion where userid = ?";
        Object[] params = {user.getId()};
        //用户的错题
        List<Map<String,Object>> wrongQuestionsRows = DbUtil.excuteQuery(sql,params);
        List<QuestionIdentify> wrongQuestionList = new ArrayList<>();
        for(Map<String,Object> row:wrongQuestionsRows)
        {
            int questionId = Integer.parseInt(row.get("QuestionId").toString());
            QuestionType type = QuestionType.valueOf(row.get("QuestionType").toString());
            QuestionIdentify questionIdentify = new QuestionIdentify(type,questionId);
            wrongQuestionList.add(questionIdentify);
        }

        List<QuestionIdentify> questionList = new ArrayList<>();
        for (QuestionType questionType:QuestionType.values())
        {
            List<Integer> questionIds = QuestionDao.GetQuestions(questionType,user.getGrade());
            for (Integer questionId : questionIds) {
                QuestionIdentify questionIdentify = new QuestionIdentify(questionType, questionId);
                questionList.add(questionIdentify);
            }
        }
        //除去错题中的所有题目
        questionList.removeAll(wrongQuestionList);
        int total = questionList.size() + wrongQuestionList.size() > questionNums?questionNums:questionList.size()
                +wrongQuestionList.size();

        List<QuestionIdentify> allQuestions = new ArrayList<>();
        for(int i=0;i<total;i++)
        {
            //判断是从错题中取题目还是从其他题目中取题目
            int selection = random.nextInt(10);
            //如果随机生成的数大于2，且错题列表不为0，或者其他题目里面已经空了，则从错题中取题目
            if((selection > 2&&wrongQuestionList.size()>0)||questionList.size()==0)
            {
                int randomIndex = random.nextInt(wrongQuestionList.size());
                allQuestions.add(wrongQuestionList.remove(randomIndex));
            }
            //同上类似，从其他题目中取题目
            else if((selection<=2&&questionList.size()>0)||wrongQuestionList.size()==0)
            {
                int randomIndex = random.nextInt(questionList.size());
                allQuestions.add(questionList.remove(randomIndex));
            }
        }
        return  allQuestions;
    }

    @Override
    public String toString() {
        return "错题优先";
    }
}
