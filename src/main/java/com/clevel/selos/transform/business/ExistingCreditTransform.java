package com.clevel.selos.transform.business;

import com.clevel.selos.integration.dwh.obligation.model.Obligation;
import com.clevel.selos.integration.rlos.appin.model.AppInProcess;
import com.clevel.selos.integration.rlos.appin.model.CustomerDetail;
import com.clevel.selos.model.view.ExistingCreditDetailView;
import com.clevel.selos.util.DateTimeUtil;

import java.math.BigDecimal;
import java.util.List;


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
        //existingCreditDetailView.setTenor((int)((DateTimeUtil.daysBetween2Dates(obligation.getMaturityDate(), obligation.getLastContractDate()))/30.4));

        return existingCreditDetailView;
    }

    public ExistingCreditDetailView getExistingCredit(AppInProcess appInProcess){
        ExistingCreditDetailView existingCreditDetailView = new ExistingCreditDetailView();

        List<CustomerDetail> customerDetailList = appInProcess.getCustomerDetailList();
        StringBuilder accountName = new StringBuilder();
        for(int i = 0; i < customerDetailList.size(); i++){
            CustomerDetail customerDetail = customerDetailList.get(i);
            accountName.append(customerDetail.getFirstNameTh()).append(" ").append(customerDetail.getLastNameTh());
            if(i < customerDetailList.size() - 1)
                accountName.append(",");
        }

        existingCreditDetailView.setAccountName(accountName.toString());
        existingCreditDetailView.setAccountNumber(appInProcess.getAppNumber());
        existingCreditDetailView.setAccountStatus(appInProcess.getStatus());
        existingCreditDetailView.setAccountSuf("-");
        existingCreditDetailView.setProductCode(appInProcess.getProductCode());
        existingCreditDetailView.setProjectCode(appInProcess.getProjectCode());
        if(appInProcess.getFinalLimit() != null && !appInProcess.getFinalLimit().equals(BigDecimal.ZERO))
            existingCreditDetailView.setLimit(appInProcess.getFinalLimit());
        else
            existingCreditDetailView.setLimit(appInProcess.getRequestLimit());

        if(appInProcess.getFinalTenors() != null && ! appInProcess.getFinalTenors().equals(BigDecimal.ZERO))
            existingCreditDetailView.setTenor(appInProcess.getFinalTenors());

        if(appInProcess.getFinalLimit() != null && !appInProcess.getFinalLimit().equals(BigDecimal.ZERO))
            existingCreditDetailView.setLimit(appInProcess.getFinalLimit());
        else
            existingCreditDetailView.setLimit(appInProcess.getRequestLimit());

        /*existingCreditDetailView.setInstallment(appInProcess.get);
        existingCreditDetailView.setTenor((int)((DateTimeUtil.daysBetween2Dates(obligation.getMaturityDate(), obligation.getLastContractDate()))/30.4));
                          */
        return null;
    }
}
