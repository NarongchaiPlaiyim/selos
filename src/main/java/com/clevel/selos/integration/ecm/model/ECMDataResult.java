package com.clevel.selos.integration.ecm.model;

import com.clevel.selos.integration.ecm.db.ECMDetail;
import com.clevel.selos.integration.ecm.db.ECMTypeName;
import com.clevel.selos.model.ActionResult;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ECMDataResult implements Serializable {
    private ActionResult actionResult;
    private String reason;
    private List<ECMDetail> ecmDetailList;
    private Map<String, ECMTypeName> ecmTypeNameMap;

    public ActionResult getActionResult() {
        return actionResult;
    }

    public void setActionResult(ActionResult actionResult) {
        this.actionResult = actionResult;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<ECMDetail> getEcmDetailList() {
        return ecmDetailList;
    }

    public void setEcmDetailList(List<ECMDetail> ecmDetailList) {
        this.ecmDetailList = ecmDetailList;
    }

    public Map<String, ECMTypeName> getEcmTypeNameMap() {
        return ecmTypeNameMap;
    }

    public void setEcmTypeNameMap(Map<String, ECMTypeName> ecmTypeNameMap) {
        this.ecmTypeNameMap = ecmTypeNameMap;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("actionResult", actionResult)
                .append("reason", reason)
                .append("ecmDetailList", ecmDetailList)
                .append("ecmTypeNameMap", ecmTypeNameMap)
                .toString();
    }
}
