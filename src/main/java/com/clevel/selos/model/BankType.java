package com.clevel.selos.model;

public enum BankType {
    BBL(1,"BBL"), KTB(2,"KTB"), BAY(3,"BAY"), KBANK(4,"KBANK"), KK(5,"KK"),JPM(6,"JPM"),CITI(7,"CITI"),
    CIMB(8,"CIMB Thai"),SMBC(9,"SMBC"),DEUTSHE(10,"DEUTSHE"),RBS(11,"RBS"),TMB(12,"TMB"), TISCO(13,"TISCO"),
    TCRBANK(14,"TCRB"), SCB(15,"SCB"),TBANK(16,"TBANK"), BNP(17,"BNP"),SME_BANK(18,"SMEB"), EXIM_BANK(19,"EXIM"),
    MHCB(20,"MHCB"),MEGA_ICBC(21,"MEGA ICBC"), UOB(22,"UOBT"),LH_BANK(23,"LHBANK"), SCBT(24,"SCBT"),MUFG(25,"MUFG"),
    BOC(26,"BOC"),AMERICA(27,"AMERICA"), GSB(28,"GSB"),GH_BANK(29,"GHBANK"),RHB(30,"RHB"), IOB(31,"IOB"),IBANK(32,"iBank"),
    AIG(33,"AIG"),OCBC(34,"OCBC"),ICBC(35,"ICBC"),HSBC(36,"HSBC"),OTHER(37,"Etc.");

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
