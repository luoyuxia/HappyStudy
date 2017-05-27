package com.happystudy.model;


public class QuestionIdentify {
    private QuestionType questionType;
    private int questionId;

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public int getQuestionId() {
        return questionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuestionIdentify that = (QuestionIdentify) o;

        return getQuestionId() == that.getQuestionId() && getQuestionType() == that.getQuestionType();

    }

    @Override
    public int hashCode() {
        int result = getQuestionType().hashCode();
        result = 31 * result + getQuestionId();
        return result;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public QuestionIdentify(QuestionType questionType, int questionId)
    {
        this.questionType = questionType;
        this.questionId = questionId;
    }
}
