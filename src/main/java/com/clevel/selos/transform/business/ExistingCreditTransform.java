package com.clevel.selos.transform.business;

import com.clevel.selos.integration.dwh.obligation.model.Obligation;
import com.clevel.selos.integration.rlos.appin.model.AppInProcess;
import com.clevel.selos.integration.rlos.appin.model.CreditDetail;
import com.clevel.selos.integration.rlos.appin.model.CustomerDetail;
import com.clevel.selos.model.view.ExistingCreditDetailView;
import com.clevel.selos.util.DateTimeUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    public List<ExistingCreditDetailView> getExistingCredit(AppInProcess appInProcess){
        List<ExistingCreditDetailView> existingCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();

        List<CustomerDetail> customerDetailList = appInProcess.getCustomerDetailList();
        List<CreditDetail> creditDetailList = appInProcess.getCreditDetailList();
        StringBuilder accountName = new StringBuilder();
        for(int i = 0; i < customerDetailList.size(); i++){
            CustomerDetail customerDetail = customerDetailList.get(i);
            accountName.append(customerDetail.getFirstNameTh()).append(" ").append(customerDetail.getLastNameTh());
            if(i < customerDetailList.size() - 1)
                accountName.append(",");
        }

        for(CreditDetail creditDetail : creditDetailList){
            if(creditDetail!=null){
                ExistingCreditDetailView existingCreditDetailView = new ExistingCreditDetailView();
                existingCreditDetailView.setAccountName(accountName.toString());
                existingCreditDetailView.setAccountNumber(appInProcess.getAppNumber());
                existingCreditDetailView.setAccountStatus(appInProcess.getStatus());
                existingCreditDetailView.setAccountSuf("-");
                existingCreditDetailView.setProductCode(creditDetail.getProductCode());
                existingCreditDetailView.setProjectCode(creditDetail.getProjectCode());
                if(creditDetail.getFinalLimit() != null && !creditDetail.getFinalLimit().equals(BigDecimal.ZERO)){
                    existingCreditDetailView.setLimit(creditDetail.getFinalLimit());
                    existingCreditDetailView.setTenor(creditDetail.getFinalTenors());
                    existingCreditDetailView.setInstallment(creditDetail.getFinalInstallment());
                } else {
                    existingCreditDetailView.setLimit(creditDetail.getRequestLimit());
                    existingCreditDetailView.setTenor(creditDetail.getRequestTenor());
                    existingCreditDetailView.setInstallment(BigDecimal.ZERO);
                }
                existingCreditDetailViewList.add(existingCreditDetailView);
            }
        }
        return existingCreditDetailViewList;
    }
}
