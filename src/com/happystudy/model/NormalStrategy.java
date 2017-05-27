package com.happystudy.model;

import com.happystudy.dao.QuestionDao;

import java.util.*;


public class NormalStrategy implements Strategy{
    private Random random;
    public NormalStrategy()
    {
        random = new Random();
    }
    @Override
    public List<QuestionIdentify> generateQuestion(User user, int questionNums) {
        //找出每种类型的某一年级的问题编号
        Map<QuestionType,List<Integer>> numsOfDifferentType = new HashMap<>();
        int totalNums = 0;
        for (QuestionType questionType:QuestionType.values())
        {
            numsOfDifferentType.put(questionType, QuestionDao.GetQuestions(questionType,user.getGrade()));
            totalNums += numsOfDifferentType.get(questionType).size();
        }
        List<QuestionIdentify> questionIdentifyList = new ArrayList<>();
        //如果总共的问题数小于应该产生的问题数，则只产生总共的问题数
        int questionShouldGenerate = totalNums < questionNums? totalNums:questionNums;
        for (int i=0;i<questionShouldGenerate;i++)
        {
            //随机产生下一个问题
            int n = random.nextInt(totalNums)+1;
            for (Map.Entry<QuestionType,List<Integer>> entry:numsOfDifferentType.entrySet())
            {
                if(n<=entry.getValue().size())
                {
                    int questionId = entry.getValue().get(n-1);
                    entry.getValue().remove(n-1);
                    QuestionIdentify questionIdentify = new QuestionIdentify(entry.getKey(),questionId);
                    questionIdentifyList.add(questionIdentify);
                    totalNums -- ;
                    break;
                }
                else
                {
                    n = n - entry.getValue().size();
                }
            }
        }
        return questionIdentifyList;
    }

    @Override
    public String toString()
    {
        return "随机选择";
    }

    public static void main(String args[])
    {
        Strategy strategy = new NormalStrategy();
        User user = new User(1,1,1,1,"1");
        List<QuestionIdentify> questionIdentifyList = strategy.generateQuestion(user,1);
        System.out.println(questionIdentifyList.size());
    }
}
