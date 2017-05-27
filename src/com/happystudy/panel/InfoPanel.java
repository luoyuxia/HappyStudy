package com.happystudy.panel;

import com.happystudy.main.MainFrame;
import com.happystudy.model.*;
import com.happystudy.util.GBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;


public class InfoPanel extends JPanel{
    private MainFrame parentFrame;
    private java.util.List<Strategy> strategies ;
    private JSlider slider;
    private JComboBox<Strategy> strategyComboBox;
    private User user;
    private JLabel levelLabel ;
    private JLabel levelNameLabel;
    private JProgressBar expBar;
    private JLabel expInfoLabel;
    public InfoPanel(MainFrame parentFrame,User user)
    {
        this.parentFrame = parentFrame;
        this.user = user;
        init();
    }

    public void setUser(User user)
    {
        this.user = user;
    }
    private void init()
    {
        strategies = new ArrayList<>();
        strategies.add(new NormalStrategy());
        strategies.add(new OnlyWrongStrategy());
        strategies.add(new WrongPriorityStrategy());

        this.setLayout(new GridBagLayout());
        JPanel header = new JPanel(new FlowLayout());
        JLabel headerLabel = new JLabel("欢迎进入智能答题教学系统");
        headerLabel.setForeground(Color.BLACK);
        Font font = new Font("宋体",Font.ITALIC,30);
        headerLabel.setFont(font);
        header.add(headerLabel);
        this.add(header,new GBC(0,0,2,1).setInsets(20,0,0,0).setWeight(100,0).setAnchor(GridBagConstraints.NORTH));

        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setSize(400,400);
        this.add(infoPanel,new GBC(0,1,1,1).setFill(GridBagConstraints.HORIZONTAL).setInsets(100,20,0,0).setWeight(100,100).setAnchor(GridBagConstraints.NORTHWEST));



        infoPanel.setLayout(new GridBagLayout());
        JLabel nameLabel = new JLabel("罗宇侠");
        Font nameFont = new Font("宋体",Font.PLAIN,40);
        nameLabel.setFont(nameFont);
        nameLabel.setForeground(Color.BLACK);
        infoPanel.add(nameLabel,new GBC(0,0,1,1).setInsets(0,30,0,0).setWeight(100,0).setAnchor(GridBagConstraints.NORTHWEST));

  //      JLabel level = new JLabel("当前等级: "+user.getCurrentLevel());
        levelLabel = new JLabel();
        Font normalFont = new Font("宋体",Font.PLAIN,30);
        levelLabel.setFont(normalFont);
        infoPanel.add(levelLabel,new GBC(0,1,1,1).setInsets(10,5,0,0).setWeight(100,0).setAnchor(GridBagConstraints.WEST));

        levelNameLabel = new JLabel();
        levelNameLabel.setFont(normalFont);
        infoPanel.add(levelNameLabel,new GBC(0,2,1,1).setInsets(10,5,0,0).setWeight(100,0).setAnchor(GridBagConstraints.WEST));

        JLabel expLabel = new JLabel("当前经验:");
        expLabel.setFont(normalFont);
        infoPanel.add(expLabel,new GBC(0,3,1,1).setInsets(10,5,0,0).setWeight(100,0).setAnchor(GridBagConstraints.WEST));

        JPanel barPanel = new JPanel(new FlowLayout());
        expBar = new JProgressBar();
        expBar.setFont(normalFont);
    //    expBar.setMaximum(user.getNextLevelExp());
    //    expBar.setValue(user.getCurrent_ep());
        expBar.setBackground(Color.red);
        expBar.setForeground(Color.green);
    //    JLabel label = new JLabel(user.getCurrent_ep()+"/"+user.getNextLevelExp());
        expInfoLabel = new JLabel();
        expInfoLabel.setFont(normalFont);
        expInfoLabel.setForeground(Color.red);
        barPanel.add(expBar);
        barPanel.add(expInfoLabel);

        infoPanel.add(barPanel,new GBC(0,4,1,1).setInsets(10,0,0,0).setWeight(100,0).setAnchor(GridBagConstraints.WEST));

        updateUserInfo();


        JPanel optionPanel = new JPanel(new GridBagLayout());
        this.add(optionPanel,new GBC(1,1,1,1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.NORTHWEST).setWeight(100,0).setInsets(150,0,0,20));

        JLabel nums = new JLabel("做题数量: ");
        nums.setFont(normalFont);
        optionPanel.add(nums,new GBC(0,0,1,1).setAnchor(GridBagConstraints.EAST).setWeight(100,100));
        slider = new JSlider(5,50,5);
        slider.setForeground(Color.red);

        Font font1 = new Font("宋体",Font.PLAIN,20);
        slider.setFont(font1);
        slider.setMajorTickSpacing(5);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        optionPanel.add(slider,new GBC(1,0,1,1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.WEST).setWeight(100,100));

        JLabel optionLabel = new JLabel("做题选项: ");
        optionLabel.setFont(normalFont);
        optionPanel.add(optionLabel,new GBC(0,1,1,1).setInsets(10,0,0,0).setAnchor(GridBagConstraints.EAST));

        strategyComboBox = new JComboBox<>();
        for(Strategy strategy:strategies)
        {
            strategyComboBox.addItem(strategy);
        }
        strategyComboBox.setFont(normalFont);
        optionPanel.add(strategyComboBox,new GBC(1,1,1,1).setInsets(10,0,0,0).setAnchor(GridBagConstraints.EAST).setWeight(100,100).setFill(GridBagConstraints.HORIZONTAL));

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton startButton = new JButton("开始做题");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startToDo();
            }
        });
        startButton.setBackground(Color.GREEN);
        startButton.setFont(normalFont);
        buttonPanel.add(startButton);
        optionPanel.add(buttonPanel,new GBC(0,2,2,1).setInsets(10,0,0,0).setAnchor(GridBagConstraints.EAST).setWeight(100,100).setFill(GridBagConstraints.HORIZONTAL));

    }

    private void startToDo()
    {
        int questionNums = slider.getValue();
        Strategy strategy = (Strategy) strategyComboBox.getSelectedItem();
        parentFrame.startDoQuestions(questionNums,strategy);
    }

    public void updateUserInfo()
    {
        levelLabel.setText("当前等级: "+user.getCurrentLevel());
        levelNameLabel.setText("当前称号: "+user.getCurrentLevelName());
        expInfoLabel.setText(user.getCurrent_ep()+"/"+user.getNextLevelExp());
        expBar.setMaximum(user.getNextLevelExp());
        expBar.setValue(user.getCurrent_ep());
    }
}
