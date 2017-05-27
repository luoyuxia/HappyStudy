package com.happystudy.main;

import javax.swing.*;
import java.awt.*;


public class HappyStudy {
    public static void main(String args[])
    {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame mainFrame = new MainFrame();
                int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
                int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
                mainFrame.setTitle("快乐学习");
                mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                mainFrame.setSize((int)(screenWidth*0.6),(int)(screenHeight*0.9));
                mainFrame.setVisible(true);
                mainFrame.setLocationRelativeTo(null);
            }
        });
    }
}
