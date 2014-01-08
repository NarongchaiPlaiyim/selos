package com.clevel.selos.transform.business;

import com.clevel.selos.dao.master.BankAccountStatusDAO;
import com.clevel.selos.dao.master.BankAccountTypeDAO;
import com.clevel.selos.dao.master.DWHBankDataSourceDAO;
import com.clevel.selos.dao.working.ExistingCreditDetailDAO;
import com.clevel.selos.dao.working.ExistingCreditFacilityDAO;
import com.clevel.selos.integration.dwh.obligation.model.Obligation;
import com.clevel.selos.integration.dwh.obligation.model.ObligationResult;
import com.clevel.selos.integration.rlos.appin.model.AppInProcess;
import com.clevel.selos.integration.rlos.appin.model.CreditDetail;
import com.clevel.selos.integration.rlos.appin.model.CustomerDetail;
import com.clevel.selos.model.CreditCategory;
import com.clevel.selos.model.CreditRelationType;
import com.clevel.selos.model.QualitativeClass;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.BankAccountStatus;
import com.clevel.selos.model.db.master.BankAccountType;
import com.clevel.selos.model.db.master.DWHBankDataSource;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.ExistingCreditDetail;
import com.clevel.selos.model.db.working.ExistingCreditFacility;
import com.clevel.selos.model.view.BankAccountStatusView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.ExistingCreditDetailView;
import com.clevel.selos.model.view.ExistingCreditFacilityView;
import com.clevel.selos.transform.BankAccountStatusTransform;
import com.clevel.selos.transform.SBFScoreTransform;
import com.clevel.selos.transform.ServiceSegmentTransform;
import com.clevel.selos.util.DateTimeUtil;
import com.clevel.selos.util.Util;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ObligationBizTransform extends BusinessTransform {

    @Inject
    ExistingCreditFacilityDAO existingCreditFacilityDAO;

    @Inject
    ExistingCreditDetailDAO existingCreditDetailDAO;

    @Inject
    DWHBankDataSourceDAO dwhBankDataSourceDAO;

    @Inject
    BankAccountStatusDAO bankAccountStatusDAO;

    @Inject
    BankAccountStatusTransform bankAccountStatusTransform;

    @Inject
    SBFScoreTransform sbfScoreTransform;

    @Inject
    ServiceSegmentTransform serviceSegmentTransform;

    @Inject
    BankAccountTypeDAO bankAccountTypeDAO;

    @Inject
    public ObligationBizTransform() {

    }

    public ExistingCreditDetailView getExistingCredit(Obligation obligation) {
        ExistingCreditDetailView existingCreditDetailView = new ExistingCreditDetailView();
        existingCreditDetailView.setAccountName(obligation.getAccountName());
        //existingCreditDetailView.setAccountStatus(obligation.getAccountStatus());   //code
        //use dataSource to find bankAccountType
        if(!Util.isEmpty(obligation.getDataSource())){
            DWHBankDataSource dwhBankDataSource = dwhBankDataSourceDAO.findByDataSource(obligation.getDataSource().trim());
            if(dwhBankDataSource != null){
                String code = obligation.getAccountStatus();
                BankAccountStatus bankAccountStatus = bankAccountStatusDAO.findByCodeAndType(code, dwhBankDataSource.getBankAccountType().getId());
                if(bankAccountStatus != null){
                    BankAccountStatusView bankAccountStatusView = bankAccountStatusTransform.getBankAccountStatusView(bankAccountStatus);
                    existingCreditDetailView.setAccountStatus(bankAccountStatusView);
                } else {
                    existingCreditDetailView.setAccountStatus(new BankAccountStatusView());
                }
            } else {
                existingCreditDetailView.setAccountStatus(new BankAccountStatusView());
            }
        }
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
        existingCreditDetailView.setAccountRef(obligation.getAccountRef());
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
                if(!Util.isEmpty(appInProcess.getStatus())){
                    BankAccountType bankAccountType = bankAccountTypeDAO.getAccountTypeRLOS();
                    if(bankAccountType != null){
                        BankAccountStatus bankAccountStatus = bankAccountStatusDAO.findByCodeAndType(appInProcess.getStatus(), bankAccountType.getId());
                        BankAccountStatusView bankAccountStatusView = bankAccountStatusTransform.getBankAccountStatusView(bankAccountStatus);
                        existingCreditDetailView.setAccountStatus(bankAccountStatusView);
                    } else {
                        existingCreditDetailView.setAccountStatus(new BankAccountStatusView());
                    }
                }
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

    public ExistingCreditFacility getExistingCreditFacility(ExistingCreditFacilityView existingCreditFacilityView, ExistingCreditFacility existingCreditFacility, User user) {
        log.info("Transform ExistingCreditFacility with ExistingCreditFacilityView{}", existingCreditFacilityView);
        Date now = new Date();
        if (existingCreditFacility != null) {
            existingCreditFacility.setModifyBy(user);
            existingCreditFacility.setModifyDate(now);
        } else {
            existingCreditFacility = new ExistingCreditFacility();
            existingCreditFacility.setCreateBy(user);
            existingCreditFacility.setCreateDate(now);
            existingCreditFacility.setModifyBy(user);
            existingCreditFacility.setModifyDate(now);
        }

        existingCreditFacility.setTotalBorrowerAppInRLOSLimit(existingCreditFacilityView.getTotalBorrowerAppInRLOSLimit());
        existingCreditFacility.setTotalBorrowerComLimit(existingCreditFacilityView.getTotalBorrowerComLimit());
        existingCreditFacility.setTotalBorrowerRetailLimit(existingCreditFacilityView.getTotalBorrowerRetailLimit());

        existingCreditFacility.setTotalRelatedAppInRLOSLimit(existingCreditFacilityView.getTotalRelatedAppInRLOSLimit());
        existingCreditFacility.setTotalRelatedComLimit(existingCreditFacilityView.getTotalRelatedComLimit());
        existingCreditFacility.setTotalRelatedRetailLimit(existingCreditFacilityView.getTotalRelatedRetailLimit());

        List<ExistingCreditDetail> existingCreditDetailList = new ArrayList<ExistingCreditDetail>();
        existingCreditDetailList.addAll(getExistingCreditDetail(existingCreditFacilityView.getBorrowerComExistingCredit(), existingCreditFacility, user));
        existingCreditDetailList.addAll(getExistingCreditDetail(existingCreditFacilityView.getBorrowerRetailExistingCredit(), existingCreditFacility, user));
        existingCreditDetailList.addAll(getExistingCreditDetail(existingCreditFacilityView.getBorrowerAppInRLOSCredit(), existingCreditFacility, user));
        existingCreditDetailList.addAll(getExistingCreditDetail(existingCreditFacilityView.getRelatedComExistingCredit(), existingCreditFacility, user));
        existingCreditDetailList.addAll(getExistingCreditDetail(existingCreditFacilityView.getRelatedRetailExistingCredit(), existingCreditFacility, user));
        existingCreditDetailList.addAll(getExistingCreditDetail(existingCreditFacilityView.getRelatedAppInRLOSCredit(), existingCreditFacility, user));

        existingCreditFacility.setExistingCreditDetailList(existingCreditDetailList);

        log.info("Transform Result ExistingCreditFacility {}", existingCreditFacility);

        return existingCreditFacility;
    }

    public List<ExistingCreditDetail> getExistingCreditDetail(List<ExistingCreditDetailView> existingCreditDetailViewList, ExistingCreditFacility existingCreditFacility, User user) {
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
                log.debug("transform ExistingCreditTransform ::: getAccountStatus {}", existingCreditDetailView.getAccountStatus());
                if(existingCreditDetailView.getAccountStatus() != null){
                    BankAccountStatus bankAccountStatus = bankAccountStatusTransform.getBankAccountStatus(existingCreditDetailView.getAccountStatus());
                    log.debug("transform ExistingCreditTransform ::: bankAccountStatus : {}", bankAccountStatus);
                    if(bankAccountStatus.getId() != 0){
                        existingCreditDetail.setAccountstatus(bankAccountStatus);
                    }else{
                        existingCreditDetail.setAccountstatus(null);
                    }
                }else{
                    existingCreditDetail.setAccountstatus(null);
                }
                existingCreditDetail.setInstallment(existingCreditDetailView.getInstallment());
                existingCreditDetail.setExistingCreditFacility(existingCreditFacility);
                existingCreditDetail.setCreditCategory(existingCreditDetailView.getCreditCategory().value());
                existingCreditDetail.setCreditRelationType(existingCreditDetailView.getCreditRelationType().value());

                existingCreditDetail.setLimit(existingCreditDetailView.getLimit());
                existingCreditDetail.setOutstanding(existingCreditDetailView.getOutstanding());
                existingCreditDetail.setIntFee(existingCreditDetailView.getIntFeePercent());

                existingCreditDetail.setTenor(existingCreditDetailView.getTenor());
                existingCreditDetailList.add(existingCreditDetail);
                log.debug("transform ExistingCreditTransform ::: existingCreditDetailList : {}", existingCreditDetail);
            }
        }
        return existingCreditDetailList;
    }

    public ExistingCreditFacilityView getExistingCreditView(ExistingCreditFacility existingCreditFacility) {
        ExistingCreditFacilityView existingCreditFacilityView = new ExistingCreditFacilityView();

        if (existingCreditFacility != null) {
            existingCreditFacilityView.setId(existingCreditFacility.getId());
            existingCreditFacilityView.setTotalBorrowerAppInRLOSLimit(existingCreditFacility.getTotalBorrowerAppInRLOSLimit());
            existingCreditFacilityView.setTotalBorrowerComLimit(existingCreditFacility.getTotalBorrowerComLimit());
            existingCreditFacilityView.setTotalBorrowerRetailLimit(existingCreditFacility.getTotalBorrowerRetailLimit());
            existingCreditFacilityView.setTotalRelatedAppInRLOSLimit(existingCreditFacility.getTotalRelatedAppInRLOSLimit());
            existingCreditFacilityView.setTotalRelatedComLimit(existingCreditFacility.getTotalRelatedComLimit());
            existingCreditFacilityView.setTotalRelatedRetailLimit(existingCreditFacility.getTotalRelatedRetailLimit());

            List<ExistingCreditDetailView> _borrowerComList = new ArrayList<ExistingCreditDetailView>();
            List<ExistingCreditDetailView> _borrowerRetList = new ArrayList<ExistingCreditDetailView>();
            List<ExistingCreditDetailView> _borrowerRLOSList = new ArrayList<ExistingCreditDetailView>();

            List<ExistingCreditDetailView> _relatedComList = new ArrayList<ExistingCreditDetailView>();
            List<ExistingCreditDetailView> _relatedRetList = new ArrayList<ExistingCreditDetailView>();
            List<ExistingCreditDetailView> _relatedRLOSList = new ArrayList<ExistingCreditDetailView>();


            List<ExistingCreditDetail> existingCreditDetailList = existingCreditFacility.getExistingCreditDetailList();
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



            existingCreditFacilityView.setBorrowerComExistingCredit(_borrowerComList);
            existingCreditFacilityView.setBorrowerRetailExistingCredit(_borrowerRetList);
            existingCreditFacilityView.setBorrowerAppInRLOSCredit(_borrowerRLOSList);

            existingCreditFacilityView.setRelatedComExistingCredit(_relatedComList);
            existingCreditFacilityView.setRelatedRetailExistingCredit(_relatedRetList);
            existingCreditFacilityView.setRelatedAppInRLOSCredit(_relatedRLOSList);
        }

        return existingCreditFacilityView;
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
        log.debug("getExistingCreditDetailView ::: getAccountstatus : {}", existingCreditDetail.getAccountstatus());
        existingCreditDetailView.setAccountStatus(bankAccountStatusTransform.getBankAccountStatusView(existingCreditDetail.getAccountstatus()));
        existingCreditDetailView.setTenor(existingCreditDetail.getTenor());
        //existingCreditDetailView.setAccountStatus(new BankAccountStatusView());     //TODO Remove this line
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

    public CustomerInfoView getCustomerInfoView(List<Obligation> obligationList, CustomerInfoView customerInfoView){
        if(obligationList != null && obligationList.size() > 0){
            String serviceSegment = null;
            BigDecimal unpaidFeeInsurance = new BigDecimal(0);
            BigDecimal pendingClaimLG = new BigDecimal(0);
            Date lastReviewDate = null;
            Date extendedReviewDate = null;
            Date nextReviewDate = null;
            int scfScore = -1;
            String covenantFlag = null;
            String reviewFlag = null;
            QualitativeClass qualitativeClass = null;

            for(Obligation obligation : obligationList){
                //Service Segment, which should be the same for all obligation record
                serviceSegment = obligation.getServiceSegment();

                //Unpaid fee balance = Sum(|COM_AMOUNT| + |TMB_PAID_EXPENSE_AMOUNT|);
                unpaidFeeInsurance = unpaidFeeInsurance.add(obligation.getComAmount().abs());
                unpaidFeeInsurance = unpaidFeeInsurance.add(obligation.getTmbPaidExpenseAmount().abs());

                //Pending Claim LG = Sum(|CLAIM_AMOUNT|)
                pendingClaimLG = pendingClaimLG.add(obligation.getClaimAmount().abs());

                //Latest Review Date, get the one which latest than other account.
                if(obligation.getLastReviewDate() != null){
                    if(lastReviewDate == null || lastReviewDate.before(obligation.getLastReviewDate()))
                        lastReviewDate = obligation.getLastReviewDate();
                }

                //Extended Review Date, get earliest Date than other account.
                if(obligation.getExtendedReviewDate() != null){
                    if(extendedReviewDate == null || extendedReviewDate.after(obligation.getExtendedReviewDate()))
                        extendedReviewDate = obligation.getExtendedReviewDate();
                }

                //Next Review Date, get the earliest date than other account
                if(obligation.getNextReviewDate() != null){
                    if(nextReviewDate == null || nextReviewDate.after(obligation.getNextReviewDate()))
                        nextReviewDate = obligation.getNextReviewDate();
                }

                //SCFScore, get worst score (Max is the worst) of final rate
                if(obligation.getScfScoreFinalRate() != null && !"".equals(obligation.getScfScoreFinalRate().trim())){
                    int tempSBFScore = Integer.parseInt(obligation.getScfScoreFinalRate());
                    log.info("tempSBFScore int score : {}", tempSBFScore);
                    if (tempSBFScore > scfScore){
                        scfScore = tempSBFScore;
                    }
                }

                //Covenant Flag
                if(obligation.getCovenantFlag() != null && !"".equals(obligation.getCovenantFlag())){
                    covenantFlag = obligation.getCovenantFlag();
                }

                //Review Flag
                if(obligation.getReviewFlag() != null && !"".equals(obligation.getReviewFlag())){
                    reviewFlag = obligation.getReviewFlag();
                }

                //Adjust Class (BOT Class to BRMS)
                if(obligation.getAdjustClass() != null && !"".equals(obligation.getAdjustClass())){
                    try{
                        QualitativeClass tempQualitativeClass = QualitativeClass.valueOf(obligation.getAdjustClass());
                        if(qualitativeClass == null || qualitativeClass.getValue() < tempQualitativeClass.value()){
                            qualitativeClass = tempQualitativeClass;
                        }
                    } catch (IllegalArgumentException iaex) {
                        log.debug("Adjust Class");
                    }
                }
            }

            customerInfoView.setServiceSegmentView(serviceSegmentTransform.transformToView(Util.isEmpty(serviceSegment)? 0 : Integer.parseInt(serviceSegment)));
            customerInfoView.setPendingClaimLG(pendingClaimLG);
            customerInfoView.setUnpaidFeeInsurance(unpaidFeeInsurance);
            if(qualitativeClass != null)
                customerInfoView.setAdjustClass(qualitativeClass.toString());

            if(lastReviewDate != null){
                customerInfoView.setLastReviewDate(lastReviewDate);
                customerInfoView.setReviewFlag(RadioValue.YES.value());
            } else {
                customerInfoView.setReviewFlag(RadioValue.NO.value());
            }

            if(extendedReviewDate != null){
                customerInfoView.setNextReviewDate(nextReviewDate);
                customerInfoView.setNextReviewDateFlag(RadioValue.YES.value());
            } else {
                customerInfoView.setNextReviewDateFlag(RadioValue.NO.value());
            }

            if(nextReviewDate != null){
                customerInfoView.setNextReviewDate(nextReviewDate);
                customerInfoView.setNextReviewDateFlag(RadioValue.YES.value());
            } else {
                customerInfoView.setNextReviewDateFlag(RadioValue.NO.value());
            }

            customerInfoView.setRatingFinal(sbfScoreTransform.transformToView(scfScore));

            if(covenantFlag == null){
                customerInfoView.setCovenantFlag(RadioValue.NOT_SELECTED.value());
            } else if(covenantFlag.equalsIgnoreCase("Y")){
                customerInfoView.setCovenantFlag(RadioValue.YES.value());
            } else if (covenantFlag.equalsIgnoreCase("N")){
                customerInfoView.setCovenantFlag(RadioValue.NO.value());
            }

            if(reviewFlag == null){
                customerInfoView.setReviewFlag(RadioValue.NOT_SELECTED.value());
            } else if(reviewFlag.equalsIgnoreCase("Y")){
                customerInfoView.setReviewFlag(RadioValue.YES.value());
            } else if (covenantFlag.equalsIgnoreCase("N")){
                customerInfoView.setReviewFlag(RadioValue.NO.value());
            }

        }
        return customerInfoView;
    }
}
