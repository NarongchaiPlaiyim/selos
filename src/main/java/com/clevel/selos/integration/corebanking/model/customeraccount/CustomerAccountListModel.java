package com.clevel.selos.integration.corebanking.model.customeraccount;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;


public class CustomerAccountListModel implements Serializable {

    private String rel;
    private String cd;
    private String pSO;
    private String appl;
    private String accountNo;
    private String trlr;
    private BigDecimal balance;
    private String dir;
    private String prod;
    private String ctl1;
    private String ctl2;
    private String ctl3;
    private String ctl4;
    private String status;
    private String date;
    private String name;
    private String citizenId;
    private String curr;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCd() {
        return cd;
    }

    public void setCd(String cd) {
        this.cd = cd;
    }

    public String getpSO() {
        return pSO;
    }

    public void setpSO(String pSO) {
        this.pSO = pSO;
    }

    public String getAppl() {
        return appl;
    }

    public void setAppl(String appl) {
        this.appl = appl;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getTrlr() {
        return trlr;
    }

    public void setTrlr(String trlr) {
        this.trlr = trlr;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getProd() {
        return prod;
    }

    public void setProd(String prod) {
        this.prod = prod;
    }

    public String getCtl1() {
        return ctl1;
    }

    public void setCtl1(String ctl1) {
        this.ctl1 = ctl1;
    }

    public String getCtl2() {
        return ctl2;
    }

    public void setCtl2(String ctl2) {
        this.ctl2 = ctl2;
    }

    public String getCtl3() {
        return ctl3;
    }

    public void setCtl3(String ctl3) {
        this.ctl3 = ctl3;
    }

    public String getCtl4() {
        return ctl4;
    }

    public void setCtl4(String ctl4) {
        this.ctl4 = ctl4;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getCurr() {
        return curr;
    }

    public void setCurr(String curr) {
        this.curr = curr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("rel", rel)
                .append("cd", cd)
                .append("pSO", pSO)
                .append("appl", appl)
                .append("accountNo", accountNo)
                .append("trlr", trlr)
                .append("balance", balance)
                .append("dir", dir)
                .append("prod", prod)
                .append("ctl1", ctl1)
                .append("ctl2", ctl2)
                .append("ctl3", ctl3)
                .append("ctl4", ctl4)
                .append("status", status)
                .append("date", date)
                .append("name", name)
                .append("citizenId", citizenId)
                .append("curr", curr)
                .toString();
    }
}
