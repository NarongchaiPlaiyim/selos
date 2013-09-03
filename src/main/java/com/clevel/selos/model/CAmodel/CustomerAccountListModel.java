package com.clevel.selos.model.CAmodel;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: sahawat
 * Date: 2/9/2556
 * Time: 11:07 น.
 * To change this template use File | Settings | File Templates.
 */
public class CustomerAccountListModel implements Serializable{

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


}
