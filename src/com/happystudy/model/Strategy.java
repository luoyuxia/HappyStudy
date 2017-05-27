package com.happystudy.model;

import java.util.List;


//产生题目的策略
public interface Strategy {
     List<QuestionIdentify> generateQuestion(User user,int questionNums);
}
