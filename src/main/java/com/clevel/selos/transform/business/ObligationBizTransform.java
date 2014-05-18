package com.clevel.selos.transform.business;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.ExistingCreditDetailDAO;
import com.clevel.selos.dao.working.ExistingCreditFacilityDAO;
import com.clevel.selos.integration.dwh.obligation.model.Obligation;
import com.clevel.selos.integration.rlos.appin.model.AppInProcess;
import com.clevel.selos.integration.rlos.appin.model.CreditDetail;
import com.clevel.selos.integration.rlos.appin.model.CustomerDetail;
import com.clevel.selos.model.*;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.ExistingCreditDetail;
import com.clevel.selos.model.db.working.ExistingCreditFacility;
import com.clevel.selos.model.view.*;
import com.clevel.selos.transform.BankAccountStatusTransform;
import com.clevel.selos.transform.ProductTransform;
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
    ProductTransform productTransform;

    @Inject
    BankAccountTypeDAO bankAccountTypeDAO;

    @Inject
    ExistingProductFormulaDAO existingProductFormulaDAO;

    @Inject
    ProductProgramDAO productProgramDAO;

    @Inject
    CreditTypeDAO creditTypeDAO;

    @Inject
    public ObligationBizTransform() {

    }

    public ExistingCreditDetailView getExistingCredit(Obligation obligation) {
        ExistingCreditDetailView existingCreditDetailView = new ExistingCreditDetailView();
        existingCreditDetailView.setAccountName(obligation.getAccountName());
        //existingCreditDetailView.setAccountStatus(obligation.getAccountStatus());   //code
        existingCreditDetailView.setExistAccountStatusView(new BankAccountStatusView());
        //use dataSource to find bankAccountType
        if(!Util.isEmpty(obligation.getDataSource())){
            String dataSource = null;
            if(obligation.getDataSource()!=null) {
                dataSource = obligation.getDataSource().trim();
            }
            String code = obligation.getAccountStatus();
            BankAccountStatus bankAccountStatus = bankAccountStatusDAO.findByCodeAndDataSource(code,dataSource);
            if(bankAccountStatus != null){
                BankAccountStatusView bankAccountStatusView = bankAccountStatusTransform.getBankAccountStatusView(bankAccountStatus);
                existingCreditDetailView.setExistAccountStatusView(bankAccountStatusView);
            } else {
                existingCreditDetailView.setExistAccountStatusView(new BankAccountStatusView());
            }
        }
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

        //get ProductProgram
        ExistingProductFormula existingProductFormula = existingProductFormulaDAO.findProductFormula(obligation.getProductCode(), obligation.getProjectCode(), obligation.getTmbExtProductTypeCD());
        if(existingProductFormula==null || (existingProductFormula!=null && existingProductFormula.getId()==0)){
            existingProductFormula = new ExistingProductFormula();
            List<ExistingProductFormula> existingProductFormulas = existingProductFormulaDAO.findProductFormula(obligation.getProductCode(), obligation.getProjectCode());
            if(existingProductFormulas!=null && existingProductFormulas.size()>0){
                existingProductFormula = existingProductFormulas.get(0);
            }
        }

        if(existingProductFormula!=null && existingProductFormula.getId()!=0){
            existingCreditDetailView.setExistProductProgramView(productTransform.transformToView(existingProductFormula.getProductProgram()));
            existingCreditDetailView.setExistCreditTypeView(productTransform.transformToView(existingProductFormula.getCreditType()));
            existingCreditDetailView.setProductSegment(existingProductFormula.getProductSegment());
        } else {
            ProductProgram productProgram = productProgramDAO.getNonProductProgram();
            existingCreditDetailView.setExistProductProgramView(productTransform.transformToView(productProgram));
        }

        //get for existing credit in full app  (Tier List)
        List<ExistingCreditTierDetailView> existingCreditTierDetailViewList = new ArrayList<ExistingCreditTierDetailView>();
        ExistingCreditTierDetailView existingCreditTierDetailView = new ExistingCreditTierDetailView();

        existingCreditTierDetailView.setInstallment(obligation.getTmbInstallmentAmt());
        //calculate tenors ((tmb_maturity_date - tmb_origination_date) / 30.4) , tmb_origination_date = last_contact_date
        int diffDate = DateTimeUtil.daysBetween2Dates(obligation.getLastContractDate(),obligation.getMaturityDate());
        BigDecimal dividend = new BigDecimal(diffDate);
        BigDecimal divisor = new BigDecimal("30.4");
        BigDecimal tenors = BigDecimal.ZERO;
        if(dividend.compareTo(BigDecimal.ZERO)!=0){
            tenors = dividend.divide(divisor,0,BigDecimal.ROUND_HALF_UP);
        }
        existingCreditTierDetailView.setTenor(tenors.intValue());
        //TODO: confirm which field used to be int/fee from OBLIGATION

        existingCreditDetailView.setInstallment(obligation.getTmbInstallmentAmt());
        existingCreditDetailView.setTenor(tenors);

        existingCreditTierDetailViewList.add(existingCreditTierDetailView);

        existingCreditDetailView.setExistingCreditTierDetailViewList(existingCreditTierDetailViewList);

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
                existingCreditDetailView.setExistAccountStatusView(new BankAccountStatusView());
                if(!Util.isEmpty(appInProcess.getStatus())){
                    String dataSource = "RLOS"; //TODO: change to master or enum
                    String code = appInProcess.getStatus();
                    BankAccountStatus bankAccountStatus = bankAccountStatusDAO.findByCodeAndDataSource(code,dataSource);
                    if(bankAccountStatus != null){
                        BankAccountStatusView bankAccountStatusView = bankAccountStatusTransform.getBankAccountStatusView(bankAccountStatus);
                        existingCreditDetailView.setExistAccountStatusView(bankAccountStatusView);
                    } else {
                        existingCreditDetailView.setExistAccountStatusView(new BankAccountStatusView());
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
            int no = 0;
            for (ExistingCreditDetailView existingCreditDetailView : existingCreditDetailViewList) {
                ExistingCreditDetail existingCreditDetail = null;
                no = no+1;
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
                log.debug("transform ExistingCreditTransform ::: getAccountStatus {}", existingCreditDetailView.getExistAccountStatusView());
                if(existingCreditDetailView.getExistAccountStatusView() != null){
                    BankAccountStatus bankAccountStatus = bankAccountStatusTransform.getBankAccountStatus(existingCreditDetailView.getExistAccountStatusView());
                    log.debug("transform ExistingCreditTransform ::: bankAccountStatus : {}", bankAccountStatus);
                    if(bankAccountStatus.getId() != 0){
                        existingCreditDetail.setExistAccountStatus(bankAccountStatus);
                    }else{
                        existingCreditDetail.setExistAccountStatus(null);
                    }
                }else{
                    existingCreditDetail.setExistAccountStatus(null);
                }

                if(existingCreditDetailView.getExistCreditTypeView()!=null && existingCreditDetailView.getExistCreditTypeView().getId()!=0){
                    CreditType creditType = creditTypeDAO.findById(existingCreditDetailView.getExistCreditTypeView().getId());
                    existingCreditDetail.setExistCreditType(creditType);
                }

                if(existingCreditDetailView.getExistProductProgramView()!=null && existingCreditDetailView.getExistProductProgramView().getId()!=0){
                    ProductProgram productProgram = productProgramDAO.findById(existingCreditDetailView.getExistProductProgramView().getId());
                    existingCreditDetail.setExistProductProgram(productProgram);
                }

                existingCreditDetail.setInstallment(existingCreditDetailView.getInstallment());
                existingCreditDetail.setExistingCreditFacility(existingCreditFacility);
                existingCreditDetail.setCreditCategory(existingCreditDetailView.getCreditCategory().value());
                existingCreditDetail.setCreditRelationType(existingCreditDetailView.getCreditRelationType().value());

                existingCreditDetail.setLimit(existingCreditDetailView.getLimit());
                existingCreditDetail.setOutstanding(existingCreditDetailView.getOutstanding());
                existingCreditDetail.setIntFee(existingCreditDetailView.getIntFeePercent());

                existingCreditDetail.setTenor(existingCreditDetailView.getTenor());

                existingCreditDetail.setBorrowerType(existingCreditDetailView.getBorrowerType());
                existingCreditDetail.setAccountSuf(existingCreditDetailView.getAccountSuf());
                existingCreditDetail.setProductCode(existingCreditDetailView.getProductCode());
                existingCreditDetail.setProjectCode(existingCreditDetailView.getProjectCode());
                existingCreditDetail.setExistProductSegment(existingCreditDetailView.getProductSegment());
                existingCreditDetail.setNo(no);
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


            //Set for prescreen
            //Set Existing Credit for PreScreen
            List<ExistingCreditDetailView> borrowerComExistingCredit = existingCreditFacilityView.getBorrowerComExistingCredit();
            List<ExistingCreditDetailView> borrowerRetailExistingCredit = existingCreditFacilityView.getBorrowerRetailExistingCredit();
            List<ExistingCreditDetailView> relatedComExistingCredit = existingCreditFacilityView.getRelatedComExistingCredit();
            List<ExistingCreditDetailView> relatedRetailExistingCredit = existingCreditFacilityView.getRelatedRetailExistingCredit();
            BigDecimal totalBorrowerComLimit = existingCreditFacilityView.getTotalBorrowerComLimit();
            BigDecimal totalBorrowerRetailLimit = existingCreditFacilityView.getTotalBorrowerRetailLimit();
            BigDecimal totalRelatedComLimit = existingCreditFacilityView.getTotalRelatedComLimit();
            BigDecimal totalRelatedRetailLimit = existingCreditFacilityView.getTotalRelatedRetailLimit();

            List<ExistingCreditDetailView> borrowerExistingCreditPreScreen = new ArrayList<ExistingCreditDetailView>();
            List<ExistingCreditDetailView> relateExistingCreditPresScreen = new ArrayList<ExistingCreditDetailView>();
            BigDecimal totalBorrowerLimitPreScreen = BigDecimal.ZERO;
            BigDecimal totalRelatedLimitPreScreen = BigDecimal.ZERO;

            if(borrowerComExistingCredit!=null && borrowerComExistingCredit.size()>0){
                for(ExistingCreditDetailView existingCreditDetailView : borrowerComExistingCredit) {
                    borrowerExistingCreditPreScreen.add(existingCreditDetailView);
                }
            }
            if(borrowerRetailExistingCredit!=null && borrowerRetailExistingCredit.size()>0){
                for(ExistingCreditDetailView existingCreditDetailView : borrowerRetailExistingCredit) {
                    borrowerExistingCreditPreScreen.add(existingCreditDetailView);
                }
            }

            if(relatedComExistingCredit!=null && relatedComExistingCredit.size()>0){
                for(ExistingCreditDetailView existingCreditDetailView : relatedComExistingCredit) {
                    relateExistingCreditPresScreen.add(existingCreditDetailView);
                }
            }
            if(relatedRetailExistingCredit!=null && relatedRetailExistingCredit.size()>0){
                for(ExistingCreditDetailView existingCreditDetailView : relatedRetailExistingCredit) {
                    relateExistingCreditPresScreen.add(existingCreditDetailView);
                }
            }

            //add total
            if(totalBorrowerComLimit!=null && totalBorrowerComLimit.compareTo(BigDecimal.ZERO)>0){
                totalBorrowerLimitPreScreen = totalBorrowerLimitPreScreen.add(totalBorrowerComLimit);
            }
            if(totalBorrowerRetailLimit!=null && totalBorrowerRetailLimit.compareTo(BigDecimal.ZERO)>0){
                totalBorrowerLimitPreScreen = totalBorrowerLimitPreScreen.add(totalBorrowerRetailLimit);
            }

            if(totalRelatedComLimit!=null && totalRelatedComLimit.compareTo(BigDecimal.ZERO)>0){
                totalRelatedLimitPreScreen = totalRelatedLimitPreScreen.add(totalRelatedComLimit);
            }
            if(totalRelatedRetailLimit!=null && totalRelatedRetailLimit.compareTo(BigDecimal.ZERO)>0){
                totalRelatedRetailLimit = totalRelatedRetailLimit.add(totalRelatedRetailLimit);
            }

            existingCreditFacilityView.setBorrowerExistingCreditPreScreen(borrowerExistingCreditPreScreen);
            existingCreditFacilityView.setRelateExistingCreditPresScreen(relateExistingCreditPresScreen);
            existingCreditFacilityView.setTotalBorrowerLimitPreScreen(totalBorrowerLimitPreScreen);
            existingCreditFacilityView.setTotalRelatedLimitPreScreen(totalRelatedRetailLimit);
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
        existingCreditDetailView.setExistProductProgramView(productTransform.transformToView(existingCreditDetail.getExistProductProgram()));
        existingCreditDetailView.setExistCreditTypeView(productTransform.transformToView(existingCreditDetail.getExistCreditType()));

        //existingCreditDetailView.setAccountStatus();
        log.debug("getExistingCreditDetailView ::: getAccountstatus : {}", existingCreditDetail.getExistAccountStatus());
        existingCreditDetailView.setExistAccountStatusView(bankAccountStatusTransform.getBankAccountStatusView(existingCreditDetail.getExistAccountStatus()));
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
        existingCreditDetailView.setProductSegment(existingCreditDetail.getExistProductSegment());
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
            List<CustomerOblAccountInfoView> customerOblAccountInfoViewList = new ArrayList<CustomerOblAccountInfoView>();

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
                    //TODO Change to remove scfScore to 2 length
                    //int tempSBFScore = Integer.parseInt(obligation.getScfScoreFinalRate());
                    int tempSBFScore = 0;
                    if(obligation.getScfScoreFinalRate().length() > 2){
                        tempSBFScore = Integer.parseInt(obligation.getScfScoreFinalRate().substring(obligation.getScfScoreFinalRate().length()-2, obligation.getScfScoreFinalRate().length()));
                    }
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

                //Set Customfer Obl Account.
                customerOblAccountInfoViewList.add(getCustomerOblAccountInfo(obligation));
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
            customerInfoView.setCustomerOblAccountInfoViewList(customerOblAccountInfoViewList);

        }
        return customerInfoView;
    }

    public CustomerOblAccountInfoView getCustomerOblAccountInfo(Obligation obligation){
        log.debug("-- begin getCustomerOblAccountInfo obligation: {}", obligation);
        if(obligation == null)
            return null;

        CustomerOblAccountInfoView customerOblAccountInfo = new CustomerOblAccountInfoView();
        boolean accountActiveFlag = true;

        /*if("04".equals(obligation.getDataSource())){
            int compareResult = BigDecimal.ZERO.compareTo(obligation.getOutstanding());
            if(compareResult < 0){
                accountActiveFlag = true;
            } else if(compareResult == 0){
                boolean isMatch = false;
                StringTokenizer tokenizer = new StringTokenizer("_blank|C|H|J|P|T","|");
                while(tokenizer.hasMoreTokens()){
                    String _value = tokenizer.nextToken();

                    if(_value.equals(obligation.getCardBlockCode())){

                    }
                }
                if(isMatch){
                    accountActiveFlag = true;
                }
            }
        } else if("11".equals(obligation.getDataSource())){
            StringTokenizer tokenizer = new StringTokenizer("CL|PF", "|");
            boolean isMatch = false;
            while (tokenizer.hasMoreTokens()){
                String _value = tokenizer.nextToken();
                if(_value.equals(obligation.getAccountStatus())){
                    isMatch = true;
                }
            }

            if(isMatch){

            } else {
                accountActiveFlag = true;
            }

        }*/
        customerOblAccountInfo.setAccountActiveFlag(accountActiveFlag);
        customerOblAccountInfo.setAccountRef(obligation.getAccountRef());
        customerOblAccountInfo.setDataSource(obligation.getDataSource());
        customerOblAccountInfo.setCusRelAccount(obligation.getCusRelAccount());
        customerOblAccountInfo.setTdrFlag(TMBTDRFlag.lookup(obligation.getTdrFlag()));
        customerOblAccountInfo.setNumMonthIntPastDue(obligation.getNumMonthIntPastDue());
        customerOblAccountInfo.setNumMonthIntPastDueTDRAcc(obligation.getNumMonthIntPastDueTDRAcc());
        customerOblAccountInfo.setTmbDelPriDay(obligation.getTmbDelPriDay());
        customerOblAccountInfo.setTmbDelIntDay(obligation.getTmbDelIntDay());
        customerOblAccountInfo.setCardBlockCode(obligation.getCardBlockCode());
        log.debug("-- end getCustomerOblAccountInfo return customerOblAccountInfo: {}", customerOblAccountInfo);
        return customerOblAccountInfo;
    }
}
