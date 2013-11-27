package com.clevel.selos.model;

public enum BankType {
    BBL(1,"BBL"), KTB(2,"KTB"), BAY(3,"BAY"), KBANK(4,"KBANK"), KK(5,"KK"),
    CIMB(6,"CIMB"), TMB(7,"TMB"), TISCO(8,"TISCO"), TCRBANK(9,"TCRBANK"), SCB(10,"SCB"),
    TBANK(11,"TBANK"), SME_BANK(12,"SME BANK"), BAAC(13,"BAAC"), EXIM_BANK(14,"EXIM BANK"), UOB(15,"UOB"),
    LH_BANK(16,"LH BANK"), SCBT(17,"SCBT"), GSB(18,"GSB"), GH_BANK(19,"GH BANK"), IBANK(20,"IBANK"),
    ICBC(21,"ICBC"), OTHER(22,"Other");

    int value;
    String shortName;

    private BankType(int value, String shortName) {
        this.value = value;
        this.shortName = shortName;
    }

    public int value() {
        return this.value;
    }

    public String shortName() {
        return this.shortName;
    }
}
