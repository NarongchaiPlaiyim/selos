package com.clevel.selos.integration.ncb.ncrs.models.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;

@XStreamAlias("account")
public class SubjectAccountModel implements Serializable {


    private String membercode;
    private String shortname;
    private String accounttype;
    private String accountnumber;
    private String ownership;
    private String currencycode;
    private String opendate;
    private String lastpaymentdate;
    private String closeddate;
    private String asofdate;
    private String creditlimit;
    private String amountowed;
    private String amountpastdue;
    private String defaultdate;
    private String installmentfreq;
    private String installmentamount;
    private String installmentnumofpayment;
    private String accountstatus;
    private String loanclass;

    private String paymtpattern;
    private String paymtstartdate;
    private String paymtenddate;

    private String paymt01;
    private String paymtdate01;
    private String paymt02;
    private String paymtdate02;
    private String paymt03;
    private String paymtdate03;
    private String paymt04;
    private String paymtdate04;
    private String paymt05;
    private String paymtdate05;
    private String paymt06;
    private String paymtdate06;
    private String paymt07;
    private String paymtdate07;
    private String paymt08;
    private String paymtdate08;
    private String paymt09;
    private String paymtdate09;
    private String paymt10;
    private String paymtdate10;
    private String paymt11;
    private String paymtdate11;
    private String paymt12;
    private String paymtdate12;
    private String paymt13;
    private String paymtdate13;
    private String paymt14;
    private String paymtdate14;
    private String paymt15;
    private String paymtdate15;
    private String paymt16;
    private String paymtdate16;
    private String paymt17;
    private String paymtdate17;
    private String paymt18;
    private String paymtdate18;
    private String paymt19;
    private String paymtdate19;
    private String paymt20;
    private String paymtdate20;
    private String paymt21;
    private String paymtdate21;
    private String paymt22;
    private String paymtdate22;
    private String paymt23;
    private String paymtdate23;
    private String paymt24;
    private String paymtdate24;
    private String paymt25;
    private String paymtdate25;
    private String paymt26;
    private String paymtdate26;
    private String paymt27;
    private String paymtdate27;
    private String paymt28;
    private String paymtdate28;
    private String paymt29;
    private String paymtdate29;
    private String paymt30;
    private String paymtdate30;
    private String paymt31;
    private String paymtdate31;
    private String paymt32;
    private String paymtdate32;
    private String paymt33;
    private String paymtdate33;
    private String paymt34;
    private String paymtdate34;
    private String paymt35;
    private String paymtdate35;
    private String paymt36;
    private String paymtdate36;

    private String loanobjective;
    private String collaterall1;
    private String collaterall2;
    private String collaterall3;

    private String lastrestructureddate;
    private String pctpaymt;
    private String typeofcreditcard;
    private String numberofcoborrower;
    private String unitmake;
    private String unitmodel;
    private String credittypeflag;

    @XStreamImplicit(itemFieldName = "dispute")
    private ArrayList<SubjectAccounDisputetModel> dispute = new ArrayList<SubjectAccounDisputetModel>();

    @XStreamImplicit(itemFieldName = "history")
    private ArrayList<HistoryModel> history = new ArrayList<HistoryModel>();

    public SubjectAccountModel(String membercode, String shortname, String accounttype, String accountnumber, String ownership, String currencycode, String opendate, String lastpaymentdate, String closeddate, String asofdate, String creditlimit, String amountowed, String amountpastdue, String defaultdate, String installmentfreq, String installmentamount, String installmentnumofpayment, String accountstatus, String loanclass, String paymtpattern, String paymtstartdate, String paymtenddate,
                               String paymt01, String paymtdate01, String paymt02, String paymtdate02, String paymt03, String paymtdate03, String paymt04, String paymtdate04, String paymt05, String paymtdate05, String paymt06, String paymtdate06, String paymt07, String paymtdate07, String paymt08, String paymtdate08, String paymt09, String paymtdate09, String paymt10, String paymtdate10, String paymt11, String paymtdate11, String paymt12, String paymtdate12, String paymt13, String paymtdate13,
                               String paymt14, String paymtdate14, String paymt15, String paymtdate15, String paymt16, String paymtdate16, String paymt17, String paymtdate17, String paymt18, String paymtdate18, String paymt19, String paymtdate19, String paymt20, String paymtdate20, String paymt21, String paymtdate21, String paymt22, String paymtdate22, String paymt23, String paymtdate23, String paymt24, String paymtdate24, String paymt25, String paymtdate25, String paymt26, String paymtdate26,
                               String paymt27, String paymtdate27, String paymt28, String paymtdate28, String paymt29, String paymtdate29, String paymt30, String paymtdate30, String paymt31, String paymtdate31, String paymt32, String paymtdate32, String paymt33, String paymtdate33, String paymt34, String paymtdate34, String paymt35, String paymtdate35, String paymt36, String paymtdate36, String loanobjective, String collaterall1, String collaterall2, String collaterall3, String lastrestructureddate,
                               String pctpaymt, String typeofcreditcard, String numberofcoborrower, String unitmake, String unitmodel, String credittypeflag, ArrayList<HistoryModel> history, ArrayList<SubjectAccounDisputetModel> dispute) {
        this.membercode = membercode;
        this.shortname = shortname;
        this.accounttype = accounttype;
        this.accountnumber = accountnumber;
        this.ownership = ownership;
        this.currencycode = currencycode;
        this.opendate = opendate;
        this.lastpaymentdate = lastpaymentdate;
        this.closeddate = closeddate;
        this.asofdate = asofdate;
        this.creditlimit = creditlimit;
        this.amountowed = amountowed;
        this.amountpastdue = amountpastdue;
        this.defaultdate = defaultdate;
        this.installmentfreq = installmentfreq;
        this.installmentamount = installmentamount;
        this.installmentnumofpayment = installmentnumofpayment;
        this.accountstatus = accountstatus;
        this.loanclass = loanclass;
        this.paymtpattern = paymtpattern;
        this.paymtstartdate = paymtstartdate;
        this.paymtenddate = paymtenddate;
        this.paymt01 = paymt01;
        this.paymtdate01 = paymtdate01;
        this.paymt02 = paymt02;
        this.paymtdate02 = paymtdate02;
        this.paymt03 = paymt03;
        this.paymtdate03 = paymtdate03;
        this.paymt04 = paymt04;
        this.paymtdate04 = paymtdate04;
        this.paymt05 = paymt05;
        this.paymtdate05 = paymtdate05;
        this.paymt06 = paymt06;
        this.paymtdate06 = paymtdate06;
        this.paymt07 = paymt07;
        this.paymtdate07 = paymtdate07;
        this.paymt08 = paymt08;
        this.paymtdate08 = paymtdate08;
        this.paymt09 = paymt09;
        this.paymtdate09 = paymtdate09;
        this.paymt10 = paymt10;
        this.paymtdate10 = paymtdate10;
        this.paymt11 = paymt11;
        this.paymtdate11 = paymtdate11;
        this.paymt12 = paymt12;
        this.paymtdate12 = paymtdate12;
        this.paymt13 = paymt13;
        this.paymtdate13 = paymtdate13;
        this.paymt14 = paymt14;
        this.paymtdate14 = paymtdate14;
        this.paymt15 = paymt15;
        this.paymtdate15 = paymtdate15;
        this.paymt16 = paymt16;
        this.paymtdate16 = paymtdate16;
        this.paymt17 = paymt17;
        this.paymtdate17 = paymtdate17;
        this.paymt18 = paymt18;
        this.paymtdate18 = paymtdate18;
        this.paymt19 = paymt19;
        this.paymtdate19 = paymtdate19;
        this.paymt20 = paymt20;
        this.paymtdate20 = paymtdate20;
        this.paymt21 = paymt21;
        this.paymtdate21 = paymtdate21;
        this.paymt22 = paymt22;
        this.paymtdate22 = paymtdate22;
        this.paymt23 = paymt23;
        this.paymtdate23 = paymtdate23;
        this.paymt24 = paymt24;
        this.paymtdate24 = paymtdate24;
        this.paymt25 = paymt25;
        this.paymtdate25 = paymtdate25;
        this.paymt26 = paymt26;
        this.paymtdate26 = paymtdate26;
        this.paymt27 = paymt27;
        this.paymtdate27 = paymtdate27;
        this.paymt28 = paymt28;
        this.paymtdate28 = paymtdate28;
        this.paymt29 = paymt29;
        this.paymtdate29 = paymtdate29;
        this.paymt30 = paymt30;
        this.paymtdate30 = paymtdate30;
        this.paymt31 = paymt31;
        this.paymtdate31 = paymtdate31;
        this.paymt32 = paymt32;
        this.paymtdate32 = paymtdate32;
        this.paymt33 = paymt33;
        this.paymtdate33 = paymtdate33;
        this.paymt34 = paymt34;
        this.paymtdate34 = paymtdate34;
        this.paymt35 = paymt35;
        this.paymtdate35 = paymtdate35;
        this.paymt36 = paymt36;
        this.paymtdate36 = paymtdate36;
        this.loanobjective = loanobjective;
        this.collaterall1 = collaterall1;
        this.collaterall2 = collaterall2;
        this.collaterall3 = collaterall3;
        this.lastrestructureddate = lastrestructureddate;
        this.pctpaymt = pctpaymt;
        this.typeofcreditcard = typeofcreditcard;
        this.numberofcoborrower = numberofcoborrower;
        this.unitmake = unitmake;
        this.unitmodel = unitmodel;
        this.credittypeflag = credittypeflag;
        this.dispute = dispute;
        this.history = history;
    }

    public String getMembercode() {
        return membercode;
    }

    public String getShortname() {
        return shortname;
    }

    public String getAccounttype() {
        return accounttype;
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public String getOwnership() {
        return ownership;
    }

    public String getCurrencycode() {
        return currencycode;
    }

    public String getOpendate() {
        return opendate;
    }

    public String getLastpaymentdate() {
        return lastpaymentdate;
    }

    public String getCloseddate() {
        return closeddate;
    }

    public String getAsofdate() {
        return asofdate;
    }

    public String getCreditlimit() {
        return creditlimit;
    }

    public String getAmountowed() {
        return amountowed;
    }

    public String getAmountpastdue() {
        return amountpastdue;
    }

    public String getDefaultdate() {
        return defaultdate;
    }

    public String getInstallmentfreq() {
        return installmentfreq;
    }

    public String getInstallmentamount() {
        return installmentamount;
    }

    public String getInstallmentnumofpayment() {
        return installmentnumofpayment;
    }

    public String getAccountstatus() {
        return accountstatus;
    }

    public String getLoanclass() {
        return loanclass;
    }

    public String getPaymtpattern() {
        return paymtpattern;
    }

    public String getPaymtstartdate() {
        return paymtstartdate;
    }

    public String getPaymtenddate() {
        return paymtenddate;
    }

    public String getPaymt01() {
        return paymt01;
    }

    public String getPaymtdate01() {
        return paymtdate01;
    }

    public String getPaymt02() {
        return paymt02;
    }

    public String getPaymtdate02() {
        return paymtdate02;
    }

    public String getPaymt03() {
        return paymt03;
    }

    public String getPaymtdate03() {
        return paymtdate03;
    }

    public String getPaymt04() {
        return paymt04;
    }

    public String getPaymtdate04() {
        return paymtdate04;
    }

    public String getPaymt05() {
        return paymt05;
    }

    public String getPaymtdate05() {
        return paymtdate05;
    }

    public String getPaymt06() {
        return paymt06;
    }

    public String getPaymtdate06() {
        return paymtdate06;
    }

    public String getPaymt07() {
        return paymt07;
    }

    public String getPaymtdate07() {
        return paymtdate07;
    }

    public String getPaymt08() {
        return paymt08;
    }

    public String getPaymtdate08() {
        return paymtdate08;
    }

    public String getPaymt09() {
        return paymt09;
    }

    public String getPaymtdate09() {
        return paymtdate09;
    }

    public String getPaymt10() {
        return paymt10;
    }

    public String getPaymtdate10() {
        return paymtdate10;
    }

    public String getPaymt11() {
        return paymt11;
    }

    public String getPaymtdate11() {
        return paymtdate11;
    }

    public String getPaymt12() {
        return paymt12;
    }

    public String getPaymtdate12() {
        return paymtdate12;
    }

    public String getPaymt13() {
        return paymt13;
    }

    public String getPaymtdate13() {
        return paymtdate13;
    }

    public String getPaymt14() {
        return paymt14;
    }

    public String getPaymtdate14() {
        return paymtdate14;
    }

    public String getPaymt15() {
        return paymt15;
    }

    public String getPaymtdate15() {
        return paymtdate15;
    }

    public String getPaymt16() {
        return paymt16;
    }

    public String getPaymtdate16() {
        return paymtdate16;
    }

    public String getPaymt17() {
        return paymt17;
    }

    public String getPaymtdate17() {
        return paymtdate17;
    }

    public String getPaymt18() {
        return paymt18;
    }

    public String getPaymtdate18() {
        return paymtdate18;
    }

    public String getPaymt19() {
        return paymt19;
    }

    public String getPaymtdate19() {
        return paymtdate19;
    }

    public String getPaymt20() {
        return paymt20;
    }

    public String getPaymtdate20() {
        return paymtdate20;
    }

    public String getPaymt21() {
        return paymt21;
    }

    public String getPaymtdate21() {
        return paymtdate21;
    }

    public String getPaymt22() {
        return paymt22;
    }

    public String getPaymtdate22() {
        return paymtdate22;
    }

    public String getPaymt23() {
        return paymt23;
    }

    public String getPaymtdate23() {
        return paymtdate23;
    }

    public String getPaymt24() {
        return paymt24;
    }

    public String getPaymtdate24() {
        return paymtdate24;
    }

    public String getPaymt25() {
        return paymt25;
    }

    public String getPaymtdate25() {
        return paymtdate25;
    }

    public String getPaymt26() {
        return paymt26;
    }

    public String getPaymtdate26() {
        return paymtdate26;
    }

    public String getPaymt27() {
        return paymt27;
    }

    public String getPaymtdate27() {
        return paymtdate27;
    }

    public String getPaymt28() {
        return paymt28;
    }

    public String getPaymtdate28() {
        return paymtdate28;
    }

    public String getPaymt29() {
        return paymt29;
    }

    public String getPaymtdate29() {
        return paymtdate29;
    }

    public String getPaymt30() {
        return paymt30;
    }

    public String getPaymtdate30() {
        return paymtdate30;
    }

    public String getPaymt31() {
        return paymt31;
    }

    public String getPaymtdate31() {
        return paymtdate31;
    }

    public String getPaymt32() {
        return paymt32;
    }

    public String getPaymtdate32() {
        return paymtdate32;
    }

    public String getPaymt33() {
        return paymt33;
    }

    public String getPaymtdate33() {
        return paymtdate33;
    }

    public String getPaymt34() {
        return paymt34;
    }

    public String getPaymtdate34() {
        return paymtdate34;
    }

    public String getPaymt35() {
        return paymt35;
    }

    public String getPaymtdate35() {
        return paymtdate35;
    }

    public String getPaymt36() {
        return paymt36;
    }

    public String getPaymtdate36() {
        return paymtdate36;
    }

    public String getLoanobjective() {
        return loanobjective;
    }

    public String getCollaterall1() {
        return collaterall1;
    }

    public String getCollaterall2() {
        return collaterall2;
    }

    public String getCollaterall3() {
        return collaterall3;
    }

    public String getLastrestructureddate() {
        return lastrestructureddate;
    }

    public String getPctpaymt() {
        return pctpaymt;
    }

    public String getTypeofcreditcard() {
        return typeofcreditcard;
    }

    public String getNumberofcoborrower() {
        return numberofcoborrower;
    }

    public String getUnitmake() {
        return unitmake;
    }

    public String getUnitmodel() {
        return unitmodel;
    }

    public String getCredittypeflag() {
        return credittypeflag;
    }

    public ArrayList<SubjectAccounDisputetModel> getDispute() {
        return dispute;
    }

    public ArrayList<HistoryModel> getHistory() {
        return history;
    }


}
