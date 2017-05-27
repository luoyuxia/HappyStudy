package com.happystudy.panel;

import com.happystudy.main.MainFrame;
import com.happystudy.model.*;
import com.happystudy.util.ConstantValue;
import com.happystudy.util.GBC;
import com.happystudy.util.MediaHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class MainPanel extends JPanel{
    private static final Dimension cardPanelSize = new Dimension(200,300);
    private Strategy strategy;
    private JPanel buttonPanel;
    private JPanel questionCardPanel;
    private CardLayout cardLayout ;
    private JButton[] buttons;
    private JButton nextButton;
    private JButton previousButton;
    private int currentIndex;
    private User user;
    private JButton submitButton ;
    private Font font = new Font("SansSerif",Font.BOLD,20);
    private QuestionPanel currentPanel;
    private Color finishColor = Color.cyan;
    private Color unFinishColor = Color.WHITE;
    private Color currentColor = Color.LIGHT_GRAY;
    private Color rightColor = Color.GREEN;
    private Color wrongColor = Color.RED;
    private QuestionPanel[] questionPanelList ;
    private JPanel scorePanel ;
    private JButton backButton ;
    private boolean hasSubmit ;
    private MainFrame parentFrame ;
    public MainPanel(User user, Strategy strategy, MainFrame parentFrame)
    {
        super();
        this.parentFrame = parentFrame;
        hasSubmit = false;
        this.user = user;
        this.strategy = strategy;
        currentIndex = -1;
        initPanel();
    }

    public MainPanel(MainFrame frame)
    {
        this(null,null,frame);
    }
    public void setStrategy(Strategy strategy)
    {
        this.strategy = strategy;
    }
    public void setUser(User user){this.user = user;}
    private void initPanel()
    {
        this.setLayout(new BorderLayout());
        scorePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this.add(scorePanel,BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridBagLayout());
        this.add(panel,BorderLayout.CENTER);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(buttonPanel,new GBC(0,0,10,1).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.NORTHWEST).setWeight(100,100));

        cardLayout = new CardLayout();
        questionCardPanel = new JPanel(cardLayout);
        questionCardPanel.setMinimumSize(cardPanelSize);
      //  questionCardPanel.setSize(new Dimension((int)(this.getSize().getWidth()*0.7),(int)(this.getSize().getHeight()*0.7)));
        System.out.println(questionCardPanel.getSize().getWidth());
        panel.add(questionCardPanel,new GBC(0,1,10,5).setFill(GridBagConstraints.BOTH).setAnchor(GridBagConstraints.NORTHWEST).setWeight(100,100));


        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        previousButton = new JButton("上一题");
        nextButton = new JButton("下一题");
        submitButton = new JButton("提交");
        backButton = new JButton("返回首页");
        previousButton.setFont(ConstantValue.AnswerFont);
        nextButton.setFont(ConstantValue.AnswerFont);
        submitButton.setFont(ConstantValue.AnswerFont);
        backButton.setFont(ConstantValue.AnswerFont);
        previousButton.setBackground(Color.GREEN);
        nextButton.setBackground(Color.GREEN);
        submitButton.setBackground(Color.GREEN);
        backButton.setBackground(Color.GREEN);
        bottomPanel.add(previousButton);
        bottomPanel.add(nextButton);
        bottomPanel.add(submitButton);
        bottomPanel.add(backButton);
        this.add(bottomPanel,BorderLayout.SOUTH);
        registerListener();
    }

    private void registerListener()
    {
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              int selection =  JOptionPane.showConfirmDialog(null,"你确定要提交题目吗？","提示",JOptionPane.YES_NO_CANCEL_OPTION);
                if(selection==JOptionPane.YES_OPTION)
                {
                    submitQuestion();
                }
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentIndex+1<buttons.length)
                     buttons[currentIndex+1].doClick();
            }
        });
        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentIndex-1>=0)
                    buttons[currentIndex-1].doClick();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parentFrame.backToInfoPanel();
            }
        });

    }

    public void GenerateQuestion(int questionNums)
    {
        hasSubmit = false;
        scorePanel.removeAll();
        questionCardPanel.removeAll();
        buttonPanel.removeAll();
        backButton.setVisible(false);
        submitButton.setEnabled(true);
        java.util.List<QuestionIdentify> questionIdentifyList = strategy.generateQuestion(user,questionNums);
        int actualNums = questionIdentifyList.size();
        if(actualNums<questionNums)
        {
            JOptionPane.showMessageDialog(this,"题目数量不足，当前只显示"+actualNums+"道题！");
        }
        buttons = new JButton[actualNums];
        questionPanelList = new QuestionPanel[actualNums];
        for(int i=0;i<questionIdentifyList.size();i++)
        {
            QuestionIdentify questionIdentify = questionIdentifyList.get(i);
            Question question = QuestionFactory.createQuestion(questionIdentify.getQuestionType(),questionIdentify.getQuestionId());
            questionPanelList[i] = new QuestionPanel(question);
            questionCardPanel.add(String.valueOf(i+1),questionPanelList[i]);
            buttons[i] = new JButton(String.valueOf(i+1));
            buttons[i].setBackground(unFinishColor);
            buttons[i].setForeground(Color.black);
            buttons[i].setFont(font);
            buttonPanel.add(buttons[i]);
        }
        if(actualNums>0) {
            registerButtonGroupListener();
            currentIndex = 0;
            buttons[currentIndex].setBackground(currentColor);
            currentPanel = questionPanelList[currentIndex];
            changeBottomButtonState();
        }
    }

    private void registerButtonGroupListener()
    {
        for (JButton button:buttons)
        {
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //如果回答了这道题，改变其颜色
                    if(!hasSubmit) {
                        if (currentPanel.hasAnswer())
                            buttons[currentIndex].setBackground(finishColor);
                        else
                            buttons[currentIndex].setBackground(unFinishColor);
                    }
                    else
                    {
                        buttons[currentIndex].setBackground(questionPanelList[currentIndex].isRight()?rightColor:wrongColor);
                    }
                    JButton clickButton = (JButton)(e.getSource());
                    clickButton.setBackground(currentColor);
                    String panelIndex = clickButton.getText();
                    cardLayout.show(questionCardPanel,panelIndex);
                    //改变当前的索引值
                    currentIndex = Integer.parseInt(panelIndex)-1;
                    currentPanel = questionPanelList[currentIndex];
                    changeBottomButtonState();
                }
            });
        }
    }

    private void submitQuestion()
    {
        submitButton.setEnabled(false);
        hasSubmit = true;
        int total = buttons.length;
        int right = 0;
        for(int i=0;i<questionPanelList.length;i++)
        {
            questionPanelList[i].showResult();
            if(questionPanelList[i].isRight())
            {
                buttons[i].setBackground(rightColor);
                right  =  right +1;
            }
            else
            {
                buttons[i].setBackground(wrongColor);
            }
        }
        backButton.setVisible(true);
        JProgressBar bar = new JProgressBar(SwingConstants.HORIZONTAL,total);
        bar.setBackground(Color.red);
        bar.setForeground(Color.GREEN);
        bar.setValue(right);

        scorePanel.add(bar);
        JLabel rateLabel = new JLabel(right+"/"+total);
        rateLabel.setFont(ConstantValue.ResultFont);
        scorePanel.add(rateLabel);

        int scoreGet = ConstantValue.EXP_Question * right;

        double rate = (right/(total*1.0));

        if(rate>0.9)
            MediaHelper.playHappyMusic();
        else
            MediaHelper.playUnHappyMusic();
        JLabel text = new JLabel(ConstantValue.getEncorage(rate));
        text.setFont(ConstantValue.ResultFont);
        scorePanel.add(text);
        JLabel scoreText = new JLabel("你总共得到了"+scoreGet+"点经验值");
        scoreText.setFont(ConstantValue.ResultFont);
        scorePanel.add(scoreText);

        //如果升级了
        int currentLevel = user.getCurrentLevel();
        user.addExp(scoreGet);
        if(currentLevel<user.getCurrentLevel())
        {
            JOptionPane.showMessageDialog(this,"恭喜你升级了！你现在的等级为"+user.getCurrentLevelName()
            +"，离下一级还差 "+(user.getNextLevelExp()-user.getCurrent_ep())+"点经验值，请继续努力哦！");
        }

        this.revalidate();
        //启动一个保存错误题目的线程
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                saveWrongQuestion();
                user.update();
            }
        });
        thread.start();
    }

    private void saveWrongQuestion()
    {
        for(QuestionPanel panel:questionPanelList)
        {
            if(!panel.isRight())
                panel.saveWrongQuestion(user.getId());
        }
    }

    private void changeBottomButtonState()
    {
        if(currentIndex == buttons.length-1)
            nextButton.setEnabled(false);
        else
            nextButton.setEnabled(true);
         if(currentIndex == 0)
            previousButton.setEnabled(false);
        else
            previousButton.setEnabled(true);
    }
}
