package com.zuci.zuciapp.ui.matchingPartnerQA;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MatchingPartnerQueAnsModel implements Serializable {
    @SerializedName("QuestionNo")
    private long qId;
    @SerializedName("RegistrationId")
    private long regId;
    @SerializedName("Answer")
    private String answer;
    @SerializedName("PartnerAnswer")
    private String partnerAnswer;
    private int AnswerId = -1;
    private int PartnerAnswerId = -1;

    public long getqId() {
        return qId;
    }

    public void setqId(long qId) {
        this.qId = qId;
    }

    public long getRegId() {
        return regId;
    }

    public void setRegId(long regId) {
        this.regId = regId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getPartnerAnswer() {
        return partnerAnswer;
    }

    public void setPartnerAnswer(String partnerAnswer) {
        this.partnerAnswer = partnerAnswer;
    }

    public int getAnswerId() {
        return AnswerId;
    }

    public void setAnswerId(int answerId) {
        AnswerId = answerId;
    }

    public int getPartnerAnswerId() {
        return PartnerAnswerId;
    }

    public void setPartnerAnswerId(int partnerAnswerId) {
        PartnerAnswerId = partnerAnswerId;
    }
}
