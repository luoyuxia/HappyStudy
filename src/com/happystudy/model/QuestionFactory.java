package com.happystudy.model;


public class QuestionFactory {
    public static Question createQuestion(QuestionType questionType,int quesionId)
    {
        switch (questionType)
        {
            case Completion:
                return new Completion(quesionId);
            case SingleChoice:
                return new SingleChoice(quesionId);
            case Judge:
                return new Judge(quesionId);
            case MultipleChoice:
                return new MultipleChoice(quesionId);
            default:
                return null;
        }
    }

    public static void main(String args[])
    {
        Question q = createQuestion(QuestionType.Completion,1);
    }
}
