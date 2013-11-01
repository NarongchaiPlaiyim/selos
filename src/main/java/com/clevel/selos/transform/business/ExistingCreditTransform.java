package com.clevel.selos.transform.business;

import com.clevel.selos.dao.master.BankAccountStatusDAO;
import com.clevel.selos.dao.master.BankAccountTypeDAO;
import com.clevel.selos.dao.master.DWHBankDataSourceDAO;
import com.clevel.selos.dao.working.ExistingCreditDetailDAO;
import com.clevel.selos.dao.working.ExistingCreditSummaryDAO;
import com.clevel.selos.integration.dwh.obligation.model.Obligation;
import com.clevel.selos.integration.rlos.appin.model.AppInProcess;
import com.clevel.selos.integration.rlos.appin.model.CreditDetail;
import com.clevel.selos.integration.rlos.appin.model.CustomerDetail;
import com.clevel.selos.model.CreditCategory;
import com.clevel.selos.model.CreditRelationType;
import com.clevel.selos.model.db.master.BankAccountStatus;
import com.clevel.selos.model.db.master.BankAccountType;
import com.clevel.selos.model.db.master.DWHBankDataSource;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.ExistingCreditDetail;
import com.clevel.selos.model.db.working.ExistingCreditSummary;
import com.clevel.selos.model.view.BankAccountStatusView;
import com.clevel.selos.model.view.ExistingCreditDetailView;
import com.clevel.selos.model.view.ExistingCreditView;
import com.clevel.selos.transform.BankAccountStatusTransform;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.Util;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ExistingCreditTransform extends BusinessTransform {

    @Inject
    ExistingCreditSummaryDAO existingCreditSummaryDAO;

    @Inject
    ExistingCreditDetailDAO existingCreditDetailDAO;

    @Inject
    DWHBankDataSourceDAO dwhBankDataSourceDAO;

    @Inject
    BankAccountStatusDAO bankAccountStatusDAO;

    @Inject
    BankAccountStatusTransform bankAccountStatusTransform;

    @Inject
    BankAccountTypeDAO bankAccountTypeDAO;

    @Inject
    public ExistingCreditTransform() {

    }

    public ExistingCreditDetailView getExistingCredit(Obligation obligation) {
        ExistingCreditDetailView existingCreditDetailView = new ExistingCreditDetailView();
        existingCreditDetailView.setAccountName(obligation.getAccountName());
        //existingCreditDetailView.setAccountStatus(obligation.getAccountStatus());   //code
        //use dataSource to find bankAccountType
        /*if(!Util.isEmpty(obligation.getDataSource())){
            DWHBankDataSource dwhBankDataSource = dwhBankDataSourceDAO.findByDataSource(obligation.getDataSource());
            String code = obligation.getAccountStatus();
            BankAccountStatus bankAccountStatus = bankAccountStatusDAO.findByCodeAndType(code, dwhBankDataSource.getBankAccountType().getId());
            BankAccountStatusView bankAccountStatusView = bankAccountStatusTransform.getBankAccountStatusView(bankAccountStatus);
            existingCreditDetailView.setAccountStatus(bankAccountStatusView);
        }*/
        existingCreditDetailView.setAccountStatus(new BankAccountStatusView());
        existingCreditDetailView.setAccountNumber(obligation.getAccountNumber());
        existingCreditDetailView.setAccountSuf(obligation.getAccountSuffix());
        existingCreditDetailView.setProductCode(obligation.getProductCode());
        existingCreditDetailView.setProjectCode(obligation.getProjectCode());
        existingCreditDetailView.setLimit(obligation.getLimit());
        existingCreditDetailView.setOutstanding(obligation.getOutstanding());
        int days = DateTimeUtil.daysBetween2Dates(obligation.getLastContractDate(), obligation.getMaturityDate());
        existingCreditDetailView.setTenor(new BigDecimal(days / 30.4));
        existingCreditDetailView.setCreditCategory(CreditCategory.COMMERCIAL);
        return existingCreditDetailView;
    }

    public List<ExistingCreditDetailView> getExistingCredit(AppInProcess appInProcess) {
        List<ExistingCreditDetailView> existingCreditDetailViewList = new ArrayList<ExistingCreditDetailView>();

        List<CustomerDetail> customerDetailList = appInProcess.getCustomerDetailList();
        List<CreditDetail> creditDetailList = appInProcess.getCreditDetailList();
        StringBuilder accountName = new StringBuilder();
        for (int i = 0; i < customerDetailList.size(); i++) {
            CustomerDetail customerDetail = customerDetailList.get(i);
            accountName.append(customerDetail.getFirstNameTh()).append(" ").append(customerDetail.getLastNameTh());
            if (i < customerDetailList.size() - 1)
                accountName.append(",");
        }

        for (CreditDetail creditDetail : creditDetailList) {
            if (creditDetail != null) {
                ExistingCreditDetailView existingCreditDetailView = new ExistingCreditDetailView();
                existingCreditDetailView.setAccountName(accountName.toString());
                existingCreditDetailView.setAccountNumber(appInProcess.getAppNumber());
                //existingCreditDetailView.setAccountStatus(appInProcess.getStatus());  delete
                /*if(!Util.isEmpty(appInProcess.getStatus())){
                    BankAccountType bankAccountType = bankAccountTypeDAO.getAccountTypeRLOS();
                    BankAccountStatus bankAccountStatus = bankAccountStatusDAO.findByCodeAndType(appInProcess.getStatus(), bankAccountType.getId());
                    BankAccountStatusView bankAccountStatusView = bankAccountStatusTransform.getBankAccountStatusView(bankAccountStatus);
                    existingCreditDetailView.setAccountStatus(bankAccountStatusView);
                }*/
                existingCreditDetailView.setAccountSuf("-");
                existingCreditDetailView.setProductCode(creditDetail.getProductCode());
                existingCreditDetailView.setProjectCode(creditDetail.getProjectCode());
                existingCreditDetailView.setCreditCategory(CreditCategory.RLOS_APP_IN);

                if (creditDetail.getFinalLimit() != null && !creditDetail.getFinalLimit().equals(BigDecimal.ZERO)) {
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

    public ExistingCreditSummary getExistingCreditSummary(ExistingCreditView existingCreditView, ExistingCreditSummary existingCreditSummary, User user) {
        log.info("Transform ExistingCreditSummary with ExistingCreditView{}", existingCreditView);
        Date now = new Date();
        if (existingCreditSummary != null) {
            existingCreditSummary.setModifyBy(user);
            existingCreditSummary.setModifyDate(now);
        } else {
            existingCreditSummary = new ExistingCreditSummary();
            existingCreditSummary.setCreateBy(user);
            existingCreditSummary.setCreateDate(now);
            existingCreditSummary.setModifyBy(user);
            existingCreditSummary.setModifyDate(now);
        }

        existingCreditSummary.setTotalBorrowerAppInRLOSLimit(existingCreditView.getTotalBorrowerAppInRLOSLimit());
        existingCreditSummary.setTotalBorrowerComLimit(existingCreditView.getTotalBorrowerComLimit());
        existingCreditSummary.setTotalBorrowerRetailLimit(existingCreditView.getTotalBorrowerRetailLimit());

        existingCreditSummary.setTotalRelatedAppInRLOSLimit(existingCreditView.getTotalRelatedAppInRLOSLimit());
        existingCreditSummary.setTotalRelatedComLimit(existingCreditView.getTotalRelatedComLimit());
        existingCreditSummary.setTotalRelatedRetailLimit(existingCreditView.getTotalRelatedRetailLimit());

        List<ExistingCreditDetail> existingCreditDetailList = new ArrayList<ExistingCreditDetail>();
        existingCreditDetailList.addAll(getExistingCreditDetail(existingCreditView.getBorrowerComExistingCredit(), existingCreditSummary, user));
        existingCreditDetailList.addAll(getExistingCreditDetail(existingCreditView.getBorrowerRetailExistingCredit(), existingCreditSummary, user));
        existingCreditDetailList.addAll(getExistingCreditDetail(existingCreditView.getBorrowerAppInRLOSCredit(), existingCreditSummary, user));
        existingCreditDetailList.addAll(getExistingCreditDetail(existingCreditView.getRelatedComExistingCredit(), existingCreditSummary, user));
        existingCreditDetailList.addAll(getExistingCreditDetail(existingCreditView.getRelatedRetailExistingCredit(), existingCreditSummary, user));
        existingCreditDetailList.addAll(getExistingCreditDetail(existingCreditView.getRelatedAppInRLOSCredit(), existingCreditSummary, user));

        existingCreditSummary.setExistingCreditDetailList(existingCreditDetailList);

        log.info("Transform Result ExistingCreditSummary {}", existingCreditSummary);

        return existingCreditSummary;
    }

    public List<ExistingCreditDetail> getExistingCreditDetail(List<ExistingCreditDetailView> existingCreditDetailViewList, ExistingCreditSummary existingCreditSummary, User user) {
        List<ExistingCreditDetail> existingCreditDetailList = new ArrayList<ExistingCreditDetail>();
        if (existingCreditDetailViewList != null) {
            for (ExistingCreditDetailView existingCreditDetailView : existingCreditDetailViewList) {
                ExistingCreditDetail existingCreditDetail = null;
                Date now = new Date();
                if (existingCreditDetailView.getId() != 0) {
                    existingCreditDetail = existingCreditDetailDAO.findById(existingCreditDetailView.getId());
                    existingCreditDetail.setModifyBy(user);
                    existingCreditDetail.setModifyDate(now);
                } else {
                    existingCreditDetail = new ExistingCreditDetail();
                    existingCreditDetail.setCreateBy(user);
                    existingCreditDetail.setCreateDate(now);
                    existingCreditDetail.setModifyBy(user);
                    existingCreditDetail.setModifyDate(now);
                }
                existingCreditDetail.setAccountName(existingCreditDetailView.getAccountName());
                existingCreditDetail.setAccountNumber(existingCreditDetailView.getAccountNumber());
                existingCreditDetail.setInstallment(existingCreditDetailView.getInstallment());
                existingCreditDetail.setExistingCreditSummary(existingCreditSummary);
                existingCreditDetail.setCreditCategory(existingCreditDetailView.getCreditCategory().value());
                existingCreditDetail.setCreditRelationType(existingCreditDetailView.getCreditRelationType().value());

                existingCreditDetail.setLimit(existingCreditDetailView.getLimit());
                existingCreditDetail.setOutstanding(existingCreditDetailView.getOutstanding());
                existingCreditDetail.setIntFee(existingCreditDetailView.getIntFeePercent());
                existingCreditDetailList.add(existingCreditDetail);
            }
        }
        return existingCreditDetailList;
    }

    public ExistingCreditView getExistingCreditView(ExistingCreditSummary existingCreditSummary) {
        ExistingCreditView existingCreditView = new ExistingCreditView();

        if (existingCreditSummary != null) {
            existingCreditView.setId(existingCreditSummary.getId());
            existingCreditView.setTotalBorrowerAppInRLOSLimit(existingCreditSummary.getTotalBorrowerAppInRLOSLimit());
            existingCreditView.setTotalBorrowerComLimit(existingCreditSummary.getTotalBorrowerComLimit());
            existingCreditView.setTotalBorrowerRetailLimit(existingCreditSummary.getTotalBorrowerRetailLimit());
            existingCreditView.setTotalRelatedAppInRLOSLimit(existingCreditSummary.getTotalRelatedAppInRLOSLimit());
            existingCreditView.setTotalRelatedComLimit(existingCreditSummary.getTotalRelatedComLimit());
            existingCreditView.setTotalRelatedRetailLimit(existingCreditSummary.getTotalRelatedRetailLimit());

            List<ExistingCreditDetailView> _borrowerComList = new ArrayList<ExistingCreditDetailView>();
            List<ExistingCreditDetailView> _borrowerRetList = new ArrayList<ExistingCreditDetailView>();
            List<ExistingCreditDetailView> _borrowerRLOSList = new ArrayList<ExistingCreditDetailView>();

            List<ExistingCreditDetailView> _relatedComList = new ArrayList<ExistingCreditDetailView>();
            List<ExistingCreditDetailView> _relatedRetList = new ArrayList<ExistingCreditDetailView>();
            List<ExistingCreditDetailView> _relatedRLOSList = new ArrayList<ExistingCreditDetailView>();


            List<ExistingCreditDetail> existingCreditDetailList = existingCreditSummary.getExistingCreditDetailList();
            for (ExistingCreditDetail existingCreditDetail : existingCreditDetailList) {
                ExistingCreditDetailView existingCreditDetailView = getExistingCreditDetailView(existingCreditDetail);
                if (existingCreditDetailView.getCreditCategory().equals(CreditCategory.COMMERCIAL)) {
                    if (existingCreditDetailView.getCreditRelationType().equals(CreditRelationType.BORROWER))
                        _borrowerComList.add(existingCreditDetailView);
                    else
                        _relatedComList.add(existingCreditDetailView);
                } else if (existingCreditDetailView.getCreditCategory().equals(CreditCategory.RETAIL)) {
                    if (existingCreditDetailView.getCreditRelationType().equals(CreditRelationType.BORROWER))
                        _borrowerRetList.add(existingCreditDetailView);
                    else
                        _relatedRetList.add(existingCreditDetailView);
                } else {
                    if (existingCreditDetailView.getCreditRelationType().equals(CreditRelationType.BORROWER))
                        _borrowerRLOSList.add(existingCreditDetailView);
                    else
                        _relatedRLOSList.add(existingCreditDetailView);
                }
            }

            existingCreditView.setBorrowerComExistingCredit(_borrowerComList);
            existingCreditView.setBorrowerRetailExistingCredit(_borrowerRetList);
            existingCreditView.setBorrowerAppInRLOSCredit(_borrowerRLOSList);

            existingCreditView.setRelatedComExistingCredit(_relatedComList);
            existingCreditView.setRelatedRetailExistingCredit(_relatedRetList);
            existingCreditView.setRelatedAppInRLOSCredit(_relatedRLOSList);
        }

        return existingCreditView;
    }

    public ExistingCreditDetailView getExistingCreditDetailView(ExistingCreditDetail existingCreditDetail) {
        ExistingCreditDetailView existingCreditDetailView = new ExistingCreditDetailView();
        existingCreditDetailView.setId(existingCreditDetail.getId());
        existingCreditDetailView.setInstallment(existingCreditDetail.getInstallment());
        existingCreditDetailView.setCreditType(existingCreditDetail.getCreditType());
        existingCreditDetailView.setAccountName(existingCreditDetail.getAccountName());
        existingCreditDetailView.setProductCode(existingCreditDetail.getProductCode());
        existingCreditDetailView.setAccountNumber(existingCreditDetail.getAccountNumber());

        //existingCreditDetailView.setAccountStatus();
        //existingCreditDetailView.setAccountStatus(bankAccountStatusTransform.getBankAccountStatusView(existingCreditDetail.getAccountstatus()));
        existingCreditDetailView.setAccountStatus(new BankAccountStatusView());     //TODO Remove this line
        existingCreditDetailView.setAccountSuf(existingCreditDetail.getAccountSuf());
        existingCreditDetailView.setIntFeePercent(existingCreditDetail.getIntFee());
        existingCreditDetailView.setOutstanding(existingCreditDetail.getOutstanding());

        if (existingCreditDetail.getCreditCategory() == CreditCategory.RETAIL.value())
            existingCreditDetailView.setCreditCategory(CreditCategory.RETAIL);
        else if (existingCreditDetail.getCreditCategory() == CreditCategory.COMMERCIAL.value())
            existingCreditDetailView.setCreditCategory(CreditCategory.COMMERCIAL);
        else
            existingCreditDetailView.setCreditCategory(CreditCategory.RLOS_APP_IN);

        if (existingCreditDetail.getCreditRelationType() == CreditRelationType.BORROWER.value())
            existingCreditDetailView.setCreditRelationType(CreditRelationType.BORROWER);
        else
            existingCreditDetailView.setCreditRelationType(CreditRelationType.RELATED);
        existingCreditDetailView.setLimit(existingCreditDetail.getLimit());
        return existingCreditDetailView;
    }
}
