package com.happystudy.util;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConstantValue {
    //得分及其对应的标语
    private static Map<Integer,String> ScoreMap ;
    public static final int MaxLevel = 10;
    public static Font ResultFont = new Font("SansSerif",Font.PLAIN,20);
    public static Font QuestionFont = new Font("Monospaced",Font.BOLD,30);
    public static Font AnswerFont = new Font("Monospaced",Font.BOLD,20);
    public static Font MainFont = new Font("宋体",Font.PLAIN,25);
    public static final int QuestionMarginLeft = 20;
    public static final int AnswerMargin = 30;
    //每道题对应的经验
    public static int EXP_Question ;
    //级别与对应的升级经验的对应关系
    public static Map<Integer,Integer> Level_Exp = new HashMap<>();

    //级别与级别称号的对应关系
    public static Map<Integer,String> Level_Name = new HashMap<>();
    static
    {
        ScoreMap = new HashMap<>();
        ScoreMap.put(0,"真遗憾，还得继续努力哦！");
        ScoreMap.put(1,"加油哦！");
        ScoreMap.put(2,"真厉害，继续努力哦!");
        ScoreMap.put(3,"真厉害，还差一点点哦！");
        ScoreMap.put(4,"你太棒了，答对了所有的题目！");
    }
    static
    {
        Properties properties = new Properties();
        try
        {
            InputStream inputStream = ConstantValue.class.getResourceAsStream("/level.properties");
            properties.load(new InputStreamReader(inputStream,"UTF-8"));
            String pre = "level";
            for(int i=1;i<=10;i++)
            {
                String key = pre + i;
                String exp = properties.getProperty(key);
                int expNeed =exp!=null?Integer.parseInt(exp):0;
                Level_Exp.put(i,expNeed);

                String levelName = properties.getProperty(pre+i+"Name");
                Level_Name.put(i,levelName);
            }
            EXP_Question = Integer.parseInt(properties.getProperty("expOfQuestion"));
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    public static String getEncorage(double rate)
    {
        int score = (int)(rate * 100);
        int level = 1;
        if(score == 100)
            level = 4;
        else if(score>=90)
            level = 3;
        else if(score>=80)
            level = 2;
        else if(score>=60)
            level = 1;
        else
            level = 0;
        return ScoreMap.get(level);
    }
}
