package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.inject.Inject;
import java.util.Date;

public class ContactRecordDetailView {
    private long id;
    private int no;
    private Date callingDate;
    private String callingTime;
    private int callingResult;
    private String callingResultStr;
    private int acceptResult;
    private String acceptResultStr;
    private Date nextCallingDate;
    private String nextCallingTime;
    private Reason reason;
    private String remark;
    private String status;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    private CustomerAcceptanceView customerAcceptance;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public Date getCallingDate() {
        return callingDate;
    }

    public void setCallingDate(Date callingDate) {
        this.callingDate = callingDate;
    }

    public String getCallingTime() {
        return callingTime;
    }

    public void setCallingTime(String callingTime) {
        this.callingTime = callingTime;
    }

    public int getCallingResult() {
        return callingResult;
    }

    public void setCallingResult(int callingResult) {
        this.callingResult = callingResult;
    }

    public String getCallingResultStr() {
        System.out.println(" this.callingResult is " + this.callingResult);
        /*if(this.callingResult==0){
            System.out.println(" case 0 " + msg.get("app.customerAcceptance.radio.label.callingResult.cannotContact"));
            callingResultStr = msg.get("app.customerAcceptance.radio.label.callingResult.cannotContact");
        }else if(this.callingResult==1){
            System.out.println(" case 1 " + msg.get("app.customerAcceptance.radio.label.callingResult.canContact"));
            callingResultStr = msg.get("app.customerAcceptance.radio.label.callingResult.canContact");
        }else{
            System.out.println(" case else "+msg.get("app.customerAcceptance.radio.label.callingResult.etc"));
            callingResultStr = msg.get("app.customerAcceptance.radio.label.callingResult.etc");
        }
        System.out.println(" final callingResultStr is  " + callingResultStr);*/
        return callingResultStr;
    }

    public void setCallingResultStr(String callingResultStr) {
        this.callingResultStr = callingResultStr;
    }

    public int getAcceptResult() {
        return acceptResult;
    }

    public void setAcceptResult(int acceptResult) {
        this.acceptResult = acceptResult;
    }

    public String getAcceptResultStr() {
        /*
        if(this.acceptResult==0){
            acceptResultStr = msg.get("app.customerAcceptance.radio.label.acceptResult.notAccept");
        }else if(this.acceptResult==1){
            acceptResultStr = msg.get("app.customerAcceptance.radio.label.acceptResult.accept");
        }else{
            acceptResultStr = msg.get("app.customerAcceptance.radio.label.acceptResult.etc");
        }*/

        return acceptResultStr;
    }

    public void setAcceptResultStr(String acceptResultStr) {
        this.acceptResultStr = acceptResultStr;
    }

    public Date getNextCallingDate() {
        return nextCallingDate;
    }

    public void setNextCallingDate(Date nextCallingDate) {
        this.nextCallingDate = nextCallingDate;
    }

    public String getNextCallingTime() {
        return nextCallingTime;
    }

    public void setNextCallingTime(String nextCallingTime) {
        this.nextCallingTime = nextCallingTime;
    }

    public Reason getReason() {
        return reason;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public User getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(User modifyBy) {
        this.modifyBy = modifyBy;
    }

    public CustomerAcceptanceView getCustomerAcceptance() {
        return customerAcceptance;
    }

    public void setCustomerAcceptance(CustomerAcceptanceView customerAcceptance) {
        this.customerAcceptance = customerAcceptance;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("no", no)
                .append("callingDate", callingDate)
                .append("callingTime", callingTime)
                .append("callingResult", callingResult)
                .append("callingResultStr", callingResultStr)
                .append("acceptResult", acceptResult)
                .append("acceptResultStr", acceptResultStr)
                .append("nextCallingDate", nextCallingDate)
                .append("nextCallingTime", nextCallingTime)
                .append("reason", reason)
                .append("remark", remark)
                .append("status", status)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .append("customerAcceptance", customerAcceptance)
                .toString();
    }
}
