package com.clevel.selos.transform.business;

import com.clevel.selos.integration.dwh.obligation.model.Obligation;
import com.clevel.selos.integration.rlos.appin.model.AppInProcess;
import com.clevel.selos.model.view.ExistingCreditDetailView;
import com.clevel.selos.util.DateTimeUtil;

import java.math.BigDecimal;


public class ExistingCreditTransform extends BusinessTransform {

    public ExistingCreditDetailView getExistingCredit(Obligation obligation){
        ExistingCreditDetailView existingCreditDetailView = new ExistingCreditDetailView();
        existingCreditDetailView.setAccountName(obligation.getAccountName());
        existingCreditDetailView.setAccountStatus(obligation.getAccountStatus());
        existingCreditDetailView.setAccountNumber(obligation.getAccountNumber());
        existingCreditDetailView.setAccountSuf(obligation.getAccountSuffix());
        existingCreditDetailView.setProductCode(obligation.getProductCode());
        existingCreditDetailView.setProjectCode(obligation.getProjectCode());
        existingCreditDetailView.setLimit(obligation.getLimit());
        existingCreditDetailView.setOutstanding(obligation.getOutstanding());
        existingCreditDetailView.setTenor((int)((DateTimeUtil.daysBetween2Dates(obligation.getMaturityDate(), obligation.getLastContractDate()))/30.4));

        return existingCreditDetailView;
    }

    public ExistingCreditDetailView getExistingCredit(AppInProcess appInProcess){
        ExistingCreditDetailView existingCreditDetailView = new ExistingCreditDetailView();

        /*existingCreditDetailView.setAccountName(appInProces);
        existingCreditDetailView.setAccountNumber(appInProcess.getAppNumber());
        existingCreditDetailView.setAccountStatus(appInProcess.getStatus());
        existingCreditDetailView.setAccountSuf("-");
        existingCreditDetailView.setProductCode(appInProcess.getProductCode());
        existingCreditDetailView.setProjectCode(appInProcess.getProjectCode());
        if(appInProcess.getFinalLimit().compareTo(BigDecimal.ZERO))
            existingCreditDetailView.setLimit(appInProcess.getFinalLimit());
        else if(appInProcess.getRequestLimit())
            existingCreditDetailView.setLimit(appInProcess.getRequestLimit());
        existingCreditDetailView.setOutstanding(obligation.getOutstanding());
        existingCreditDetailView.setTenor((int)((DateTimeUtil.daysBetween2Dates(obligation.getMaturityDate(), obligation.getLastContractDate()))/30.4));
        */
        return null;
    }
}
