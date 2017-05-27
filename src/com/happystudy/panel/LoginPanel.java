package com.happystudy.panel;

import com.happystudy.Dialog.RegisterDialog;
import com.happystudy.dao.UserDao;
import com.happystudy.main.MainFrame;
import com.happystudy.model.User;
import com.happystudy.util.ConstantValue;
import com.happystudy.util.GBC;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LoginPanel extends JPanel{
    private JFrame parentFrame;
    private static final int totalGrade = 6;
    private static final String[] GradesLabel = {"默认年级","一年级","二年级","三年级",
            "四年级","五年级","六年级"};
    private JPanel rightPanel;
    private JTextField userName;
    private JComboBox<String> grades ;
    private JButton loginButton;
    private JButton registerButton;
    private MainFrame mainFrame ;

    public LoginPanel(MainFrame mainFrame)
    {
        this.mainFrame = mainFrame;
        initRightPanel();
    }
    private void initRightPanel()
    {
        this.setLayout(new GridBagLayout());
        Border border = BorderFactory.createRaisedBevelBorder();
        Border title = BorderFactory.createTitledBorder(border,"登录");
        rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBorder(title);
        this.add(rightPanel,new GBC(0,0,1,1).setInsets(0,0,0,20).setWeight(100,0).setAnchor(GridBagConstraints.EAST));
        JLabel label = new JLabel("你的名字：");
        label.setFont(ConstantValue.MainFont);
        userName = new JTextField(10);
        userName.setFont(ConstantValue.MainFont);
        JLabel gradeLabel = new JLabel("你的年级：");
        gradeLabel.setFont(ConstantValue.MainFont);
        grades = new JComboBox<>();
        grades.setFont(ConstantValue.MainFont);
        for(int i=0;i<GradesLabel.length;i++)
        {
            grades.addItem(GradesLabel[i]);
        }

        loginButton = new JButton("登录");
        loginButton.setFont(ConstantValue.MainFont);
        loginButton.setBackground(Color.green);
        registerButton = new JButton("注册");
        registerButton.setFont(ConstantValue.MainFont);
        registerButton.setBackground(Color.GREEN);

        rightPanel.add(label,new GBC(0,0,1,1).setAnchor(GridBagConstraints.WEST));
        rightPanel.add(userName,new GBC(1,0,1,1).setAnchor(GridBagConstraints.WEST).setWeight(100,0));
        rightPanel.add(gradeLabel,new GBC(0,1,1,1).setInsets(20,0,0,0).setAnchor(GridBagConstraints.WEST));
        rightPanel.add(grades,new GBC(1,1,1,1).setInsets(20,0,0,0).setAnchor(GridBagConstraints.WEST).setWeight(100,0));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,20,0));
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        rightPanel.add(buttonPanel,new GBC(0,2,2,1).setInsets(20,0,0,0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.WEST).setWeight(100,0));

        registerListener();
    }

    private void registerListener()
    {
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog register = new RegisterDialog(parentFrame);
                register.setLocationRelativeTo(null);
                register.pack();
                register.setVisible(true);

            }
        });
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = userName.getText().trim();
                if(name.equals(""))
                {
                    JOptionPane.showMessageDialog(LoginPanel.this,"请输入你的名字！");
                    return;
                }
                User user = UserDao.getUserByName(name);
                if(user == null)
                {
                    JOptionPane.showMessageDialog(LoginPanel.this,"不存在的用户，请检查你的名字！");
                    return;
                }
                int gradeSelect = grades.getSelectedIndex();
                //如果用户改变了年级选项，判断是否与之前记录的年级是否一致，如果不是的话，需要进行年级更改
                if(gradeSelect>=1 && gradeSelect!=user.getGrade())
                {
                    user.setGrade(gradeSelect);
                    UserDao.updateUser(user);
                }
                mainFrame.userEnter(user);
            }
        });

    }
}
