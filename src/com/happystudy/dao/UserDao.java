package com.happystudy.dao;

import com.happystudy.model.User;

import java.util.List;
import java.util.Map;


public class UserDao {
    public static boolean addUser(String name,int grade)
    {
        String sql = "insert into user(name,grade,current_ep) values(?,?,?)";
        Object[] params = {name,grade,0};
        return DbUtil.executeUpdate(sql,params)!=0;
    }

    public static User getUserByName(String name)
    {
        String sql = "select * from user where name = ?";
        Object[] params = {name};
        User user = null;
        List<Map<String,Object>> users = DbUtil.excuteQuery(sql,params);
        if(users.size()>0)
        {
            user = new User();
            Map<String,Object> dbUser = users.get(0);
            user.setId(Integer.parseInt(dbUser.get("Id").toString()));
            user.setGrade(Integer.parseInt(dbUser.get("Grade").toString()));
            user.setCurrentLevel(Integer.parseInt(dbUser.get("currentLevel").toString()));
            user.setName(dbUser.get("Name").toString());
            user.setCurrent_ep(Integer.parseInt(dbUser.get("current_ep").toString()));
        }
        return user;
    }

    public static boolean updateUser(User user)
    {
        String sql = "update user set Grade = ?,currentLevel=?,current_ep=?" +
                " where id = ?";
        Object[] params = {user.getGrade(),user.getCurrentLevel(),user.getCurrent_ep(),user.getId()};
        return DbUtil.executeUpdate(sql,params)!=0;
    }
}
