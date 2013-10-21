package com.clevel.selos.model.view;

import com.clevel.selos.model.AccountInfoId;
import com.clevel.selos.model.AccountInfoName;
import com.clevel.selos.model.ActionResult;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class NcbView implements Serializable {
    private String idNumber;
    private ActionResult result;
    private String reason;
    private NCBInfoView ncbInfoView;
    private List<NCBDetailView> NCBDetailViews;
    private List<AccountInfoId> accountInfoIdList; // use for csi
    private List<AccountInfoName> accountInfoNameList; // use for csi

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public ActionResult getResult() {
        return result;
    }

    public void setResult(ActionResult result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public NCBInfoView getNcbInfoView() {
        return ncbInfoView;
    }

    public void setNcbInfoView(NCBInfoView ncbInfoView) {
        this.ncbInfoView = ncbInfoView;
    }

    public List<NCBDetailView> getNCBDetailViews() {
        return NCBDetailViews;
    }

    public void setNCBDetailViews(List<NCBDetailView> NCBDetailViews) {
        this.NCBDetailViews = NCBDetailViews;
    }

    public List<AccountInfoId> getAccountInfoIdList() {
        return accountInfoIdList;
    }

    public void setAccountInfoIdList(List<AccountInfoId> accountInfoIdList) {
        this.accountInfoIdList = accountInfoIdList;
    }

    public List<AccountInfoName> getAccountInfoNameList() {
        return accountInfoNameList;
    }

    public void setAccountInfoNameList(List<AccountInfoName> accountInfoNameList) {
        this.accountInfoNameList = accountInfoNameList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("idNumber", idNumber)
                .append("result", result)
                .append("reason", reason)
                .append("ncbInfoView", ncbInfoView)
                .append("NCBDetailViews", NCBDetailViews)
                .append("accountInfoIdList", accountInfoIdList)
                .append("accountInfoNameList", accountInfoNameList)
                .toString();
    }
}
