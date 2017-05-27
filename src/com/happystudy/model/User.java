package com.happystudy.model;

import com.happystudy.dao.UserDao;
import com.happystudy.util.ConstantValue;

public class User {
    @Override
    public String toString() {
        return "User{" +
                "Id=" + Id +
                ", grade=" + grade +
                ", currentLevel=" + currentLevel +
                ", current_ep=" + current_ep +
                ", name='" + name + '\'' +
                '}';
    }

    private int Id;
    private int grade;
    private int currentLevel;
    private int current_ep;
    private String name;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getCurrent_ep() {
        return current_ep;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCurrent_ep(int current_ep) {
        this.current_ep = current_ep;
    }

    public User()
    {

    }
    public User(int Id, int grade, int currentLevel, int current_ep,String name)
    {
        this.Id = Id;
        this.grade = grade;
        this.name = name;
        this.currentLevel = currentLevel;
        this.current_ep = current_ep;
    }


    public boolean addExp(int addedExp)
    {
        //如果当前的等级已经为最高级了，且加上这些经验后会超出最高级对应的经验
        //不再进行升级了
        if(currentLevel==ConstantValue.MaxLevel &&
                currentLevel+addedExp>ConstantValue.Level_Exp.get(ConstantValue.MaxLevel))
        {
            this.current_ep = ConstantValue.Level_Exp.get(ConstantValue.MaxLevel);
        }
        //已经是最高级了，但还没到经验的最大值
        else if(currentLevel==ConstantValue.MaxLevel)
        {
            this.current_ep += addedExp;
        }
        else {
            //下一级所需要的经验
            int nextLevelEp = ConstantValue.Level_Exp.get(currentLevel+1);
            //计算溢出经验，如果大于或等于0表示可以进行升级
            int overExp = this.current_ep+addedExp - nextLevelEp;
            if(overExp<0)
            {
                this.current_ep += addedExp;
            }
            else
            {
                this.currentLevel += 1;
                this.current_ep = 0;
                this.addExp(overExp);
            }
        }
        return true;
    }

    //得到下一级所需的经验
    public int getNextLevelExp()
    {
        if(currentLevel == ConstantValue.MaxLevel)
            return ConstantValue.Level_Exp.get(ConstantValue.MaxLevel);
        return ConstantValue.Level_Exp.get(currentLevel+1);
    }

    public boolean update()
    {
        return UserDao.updateUser(this);
    }


    //获得当前称号
    public String getCurrentLevelName()
    {
        return ConstantValue.Level_Name.get(currentLevel);
    }

    public static void main(String args[])
    {
        User user = new User();
        user.setCurrent_ep(0);
        user.setCurrentLevel(2);

        user.addExp(2500000);
    }
}
