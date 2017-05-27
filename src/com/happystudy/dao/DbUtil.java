package com.happystudy.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;


public class DbUtil {
    private static String url;
    private static String user;
    private static String password;
    private static String driverClass;
    static
    {
        try
        {
            Properties properties = new Properties();
            try
            {
                InputStream stream = DbUtil.class.getResourceAsStream("/db.properties");
                properties.load(stream);
                url = properties.getProperty("url");
                user = properties.getProperty("user");
                password = properties.getProperty("password");
                driverClass = properties.getProperty("driverClass");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Class.forName(driverClass);
        }
        catch (ClassNotFoundException e) {
            System.out.println("驱动寻找失败！");
        }
    }
    public static Connection getConnection()
    {
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void close(Connection con, PreparedStatement statement, ResultSet resultSet)
    {
        try
        {
            if(con!=null)
                con.close();
            if(statement!=null)
                statement.close();
            if(resultSet!=null)
                resultSet.close();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    public static int executeUpdate(String sql,Object[] params)
    {
        Connection con =null;
        PreparedStatement statement=null;
        try
        {
            con = getConnection();
            statement = con.prepareStatement(sql);
            if(params!=null)
            {
                for(int i=0;i<params.length;i++)
                {
                    statement.setObject(i+1,params[i]+"");
                }
            }
            return statement.executeUpdate();

        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        finally {
            close(con,statement,null);
        }
        return  0;
    }

    public static List<Map<String,Object>> excuteQuery(String sql,Object[] params)
    {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Map<String,Object>> resultList = new ArrayList<>();
        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            if(params!=null)
            {
                for(int i=0;i<params.length;i++)
                {
                    statement.setObject(i+1,params[i]+"");
                }
            }
            resultSet = statement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int coulums = metaData.getColumnCount();
            while (resultSet.next())
            {
                Map<String,Object> rowResult = new HashMap<>();
                for(int i=0;i<coulums;i++)
                {
                    String columnName = metaData.getColumnName(i+1);
                    Object columnValue = resultSet.getObject(columnName);
                    rowResult.put(columnName,columnValue);
                }
                resultList.add(rowResult);
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        finally {
            close(connection,statement,resultSet);
        }
        return resultList;
    }





    public static void main(String args[])
    {
       Connection con = DbUtil.getConnection();
       if(con!=null)
       {
           System.out.println("conncection successful");
       }

       String sql = "select * from completion";
       List<Map<String,Object>> result = DbUtil.excuteQuery(sql,null);
        for (Map<String,Object> row:result
             ) {
            for (Map.Entry<String,Object> entry: row.entrySet()
                 ) {
                System.out.println(entry.getKey() +":  " +entry.getValue());
            }
            System.out.println("======================");
        }
    }
}
