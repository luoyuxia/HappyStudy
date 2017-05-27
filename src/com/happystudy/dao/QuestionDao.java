package com.happystudy.dao;

import com.happystudy.model.QuestionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by qi on 2017/5/26.
 */
public class QuestionDao {
    static public List<Integer> GetQuestions(QuestionType questionType,int grade)
    {
        String sql = "select Id from "+questionType.toString() +" where level = ?";
        Object[] params = {grade};
        List<Map<String,Object>> result = DbUtil.excuteQuery(sql,params);
        List<Integer> questionNums = new ArrayList<>();
        for (Map<String,Object> row: result) {
            questionNums.add((Integer) row.get("Id"));
        }
        return questionNums;
    }
}
