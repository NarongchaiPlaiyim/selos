package com.clevel.selos.model.RMmodel;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sahawat
 * Date: 30/8/2556
 * Time: 13:54 น.
 * To change this template use File | Settings | File Templates.
 */
public class CustomerAccountModel implements Serializable {
    private String reqId;
    private String resCode;
    private String resDesc;
    private List<CustomerAccountListModel> accountBody;


    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResDesc() {
        return resDesc;
    }

    public void setResDesc(String resDesc) {
        this.resDesc = resDesc;
    }

    public List<CustomerAccountListModel> getAccountBody() {
        return accountBody;
    }

    public void setAccountBody(List<CustomerAccountListModel> accountBody) {
        this.accountBody = accountBody;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("reqId", reqId)
                .append("resCode", resCode)
                .append("resDesc", resDesc)
                .append("accountBody", accountBody)
                .toString();
    }
}
