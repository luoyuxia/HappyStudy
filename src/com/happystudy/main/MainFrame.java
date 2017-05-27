package com.happystudy.main;

import com.happystudy.model.NormalStrategy;
import com.happystudy.model.Strategy;
import com.happystudy.model.User;
import com.happystudy.panel.InfoPanel;
import com.happystudy.panel.LoginPanel;
import com.happystudy.panel.MainPanel;

import javax.swing.*;
import java.awt.*;


public class MainFrame extends JFrame{
    private static final int DEFAULT_WIDTH = 500;
    private static final int DEFAULT_HEIGHT = 500;
    private static final String MainPanelName = "MainPanel";
    private static final String InfoPanelName = "InfoPanel";
    private static final String LoginPanelName = "LoginPanel";
    private MainPanel mainPanel;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private InfoPanel infoPanel;
    private JPanel loginPanel;
    private User currentUser;

    public MainFrame()
    {
        init();
    }
    private void init()
    {
        setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
        setLocationRelativeTo(null);
        cardLayout = new CardLayout();
        cardPanel = new JPanel();
        cardPanel.setLayout(cardLayout);
        add(cardPanel);
        //添加登录界面
        loginPanel = new LoginPanel(this);
        cardPanel.add(LoginPanelName,loginPanel);
        //添加信息界面
   //     infoPanel = new InfoPanel(this);
    //    cardPanel.add(InfoPanelName,infoPanel);
        //添加答题主界面
        mainPanel = new MainPanel(this);
        cardPanel.add(MainPanelName,mainPanel);

        cardLayout.show(cardPanel,LoginPanelName);
    }
    public void userEnter(User user)
    {
        this.currentUser = user;
        infoPanel = new InfoPanel(this,this.currentUser);
        cardPanel.add(InfoPanelName,infoPanel);
        cardLayout.show(cardPanel,InfoPanelName);
        System.out.println(user);
    }

    public void startDoQuestions(int questionNums, Strategy strategy)
    {
        mainPanel.setUser(this.currentUser);
        mainPanel.setStrategy(strategy);
        mainPanel.GenerateQuestion(questionNums);
        cardLayout.show(cardPanel,MainPanelName);
    }

    public void backToInfoPanel()
    {
        cardLayout.show(cardPanel,InfoPanelName);
        infoPanel.updateUserInfo();
    }
}
