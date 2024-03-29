package com.clevel.selos.integration.ncb.nccrs.models.response;

import java.io.Serializable;

public class CreditScoreModel implements Serializable {
    private String score;
    private String scoregrade;
    private String odds;
    private String rs1;
    private String rs2;
    private String rs3;
    private String rs4;

    public String getScore() {
        return score;
    }

    public String getScoregrade() {
        return scoregrade;
    }

    public String getOdds() {
        return odds;
    }

    public String getRs1() {
        return rs1;
    }

    public String getRs2() {
        return rs2;
    }

    public String getRs3() {
        return rs3;
    }

    public String getRs4() {
        return rs4;
    }
}
