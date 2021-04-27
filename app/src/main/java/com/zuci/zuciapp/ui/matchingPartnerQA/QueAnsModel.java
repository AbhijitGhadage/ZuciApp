package com.zuci.zuciapp.ui.matchingPartnerQA;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class QueAnsModel implements Serializable {
    @SerializedName("QuestionNo")
    private long qId;
    @SerializedName("Question")
    private String question;
    @SerializedName("OptionOne")
    private String optOne;
    @SerializedName("OptionTwo")
    private String optTwo;
    @SerializedName("OptionThree")
    private String optThree;
    @SerializedName("OptionFour")
    private String optFour;

    public long getqId() {
        return qId;
    }

    public void setqId(long qId) {
        this.qId = qId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptOne() {
        return optOne;
    }

    public void setOptOne(String optOne) {
        this.optOne = optOne;
    }

    public String getOptTwo() {
        return optTwo;
    }

    public void setOptTwo(String optTwo) {
        this.optTwo = optTwo;
    }

    public String getOptThree() {
        return optThree;
    }

    public void setOptThree(String optThree) {
        this.optThree = optThree;
    }

    public String getOptFour() {
        return optFour;
    }

    public void setOptFour(String optFour) {
        this.optFour = optFour;
    }
}
