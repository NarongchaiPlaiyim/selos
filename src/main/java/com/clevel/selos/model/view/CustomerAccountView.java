package com.clevel.selos.model.view;

import com.clevel.selos.model.ActionResult;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class CustomerAccountView implements Serializable {
    private ActionResult responseStatus;
    private String responseDesc;
    private List<String> accountNo;
    private String customerId;

    public CustomerAccountView(){

    }

    public ActionResult getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ActionResult responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getResponseDesc() {
        return responseDesc;
    }

    public void setResponseDesc(String responseDesc) {
        this.responseDesc = responseDesc;
    }

    public List<String> getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(List<String> accountNo) {
        this.accountNo = accountNo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("responseStatus", responseStatus)
                .append("responseDesc", responseDesc)
                .append("accountNo", accountNo)
                .append("customerId", customerId)
                .toString();
    }
}
