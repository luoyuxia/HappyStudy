package com.happystudy.Dialog;

import com.happystudy.dao.UserDao;
import com.happystudy.util.ConstantValue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class RegisterDialog extends JDialog{
    private JTextField userName ;
    private JComboBox<String> gradesComboBox;
    private static final String[] gradesLabel = {
      "一年级","二年级","三年级","四年级","五年级"
    };
    private JButton registerButton ;

    public RegisterDialog(JFrame parentFrame)
    {
        super(parentFrame,"注册",true);
        init();
    }
    private void init()
    {
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        add(panel,BorderLayout.CENTER);

        panel.setLayout(new GridLayout(2,2));
        userName = new JTextField(10);
        userName.setFont(ConstantValue.MainFont);
        gradesComboBox = new JComboBox<>();
        for (String s :gradesLabel)
        {
            gradesComboBox.addItem(s);
        }
        gradesComboBox.setFont(ConstantValue.MainFont);
        JLabel nameLabel = new JLabel("你的名字：");
        nameLabel.setFont(ConstantValue.MainFont);
        JLabel gradesLabel = new JLabel("你的年级：");
        gradesLabel.setFont(ConstantValue.MainFont);
        panel.add(nameLabel);
        panel.add(userName);
        panel.add(gradesLabel);
        panel.add(gradesComboBox);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        registerButton = new JButton("注册");
        registerButton.setFont(ConstantValue.MainFont);
        buttonPanel.add(registerButton);
        add(buttonPanel,BorderLayout.SOUTH);
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userRegister();
            }
        });
    }

    private void userRegister()
    {
        String user = userName.getText().trim();
        if(user.equals(""))
        {
            JOptionPane.showMessageDialog(this,"对不起，请你输入你的名字！");
        }
        else
        {
            int grade =   gradesComboBox.getSelectedIndex() + 1;
            if(UserDao.addUser(user,grade))
            {
                JOptionPane.showMessageDialog(this,"注册成功！");
                this.dispose();
            }
        }
    }
}
