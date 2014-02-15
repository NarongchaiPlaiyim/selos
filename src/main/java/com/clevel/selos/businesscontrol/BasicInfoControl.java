package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.CustomerEntityDAO;
import com.clevel.selos.dao.master.ProductGroupDAO;
import com.clevel.selos.dao.master.RequestTypeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.BAPaymentMethodValue;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.RelationValue;
import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.db.master.SBFScore;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.model.view.OpenAccountView;
import com.clevel.selos.transform.BasicInfoTransform;
import com.clevel.selos.transform.OpenAccountTransform;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Stateless
public class BasicInfoControl extends BusinessControl {
    @Inject
    @SELOS
    Logger log;
    @Inject
    BasicInfoDAO basicInfoDAO;
    @Inject
    CustomerDAO customerDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    OpenAccountDAO openAccountDAO;
    @Inject
    OpenAccountNameDAO openAccountNameDAO;
    @Inject
    OpenAccountPurposeDAO openAccPurposeDAO;
    @Inject
    CustomerEntityDAO customerEntityDAO;
    @Inject
    ProductGroupDAO productGroupDAO;
    @Inject
    RequestTypeDAO requestTypeDAO;
    @Inject
    BAPAInfoDAO bapaInfoDAO;

    @Inject
    BasicInfoTransform basicInfoTransform;
    @Inject
    OpenAccountTransform openAccountTransform;
    @Inject
    CustomerInfoControl customerInfoControl;

    @Inject
    public BasicInfoControl(){

    }

    public BasicInfoView getBasicInfo(long workCaseId) {
        log.info("getBasicInfo ::: workCaseId : {}", workCaseId);
        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);

        if(basicInfo == null) {
            basicInfo = initialBasicInfo(workCase);
        } else if(basicInfo.getRetrievedFlag() == 0) {
            basicInfo = calBasicInfo(basicInfo);
            basicInfoDAO.persist(basicInfo);
        }

        BasicInfoView basicInfoView = basicInfoTransform.transformToView(basicInfo, workCase);
        List<OpenAccount> openAccountList = openAccountDAO.findByWorkCaseId(workCaseId);
        List<OpenAccountView> openAccountViewList = openAccountTransform.transformToViewList(openAccountList);
        basicInfoView.setOpenAccountViews(openAccountViewList);

        log.info("getBasicInfo ::: basicInfoView : {}", basicInfoView);
        return basicInfoView;
    }

    public CustomerEntity getCustomerEntityByWorkCaseId(long workCaseId) {
        log.info("getCustomerEntityByWorkCaseId ::: workCaseId : {}", workCaseId);
        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
        CustomerEntity customerEntity = new CustomerEntity();
        if(basicInfo != null){
            if(basicInfo.getBorrowerType() != null){
                customerEntity = basicInfo.getBorrowerType();
            } else {
                log.error("[WorkCase] - Borrower Type is Null !!");
            }
        }

        log.debug("customerEntity : {}",customerEntity);

        return customerEntity;
    }

    private BasicInfo initialBasicInfo(WorkCase workCase){
        log.info("initialBasicInfo ::: workCase : {}", workCase);
        BasicInfo basicInfo = new BasicInfo();
        Date now = Calendar.getInstance().getTime();
        if(workCase != null){
            basicInfo.setWorkCase(workCase);
            basicInfo.setCreateBy(workCase.getCreateBy());
            basicInfo.setCreateDate(now);
            basicInfo = calBasicInfo(basicInfo);
            basicInfoDAO.persist(basicInfo);
        }
        return basicInfo;
    }

    private BasicInfo calBasicInfo(BasicInfo basicInfo){
        log.info("start calBasicInfo for basicInfo {} :: ", basicInfo);
        BigDecimal totalUnpaidFeeInsurance = BigDecimal.ZERO;
        BigDecimal totalPendingClaimLG = BigDecimal.ZERO;
        Date lastReviewDate = null;
        Date extendedReviewDate = null;
        SBFScore sbfScore = new SBFScore();
        int countNonExistingSME = 0;
        int countExistingSME = 0;

        List<Customer> customerList = customerDAO.findByWorkCaseId(basicInfo.getWorkCase().getId());

        for(Customer customer : customerList){
            if(customer.getTmbCustomerId() != null && !"".equals(customer.getTmbCustomerId())){
                CustomerOblInfo customerOblInfo = customer.getCustomerOblInfo();
                if(customerOblInfo != null){
                    if(customer.getReference() != null && Util.isTrue(customer.getReference().getPendingClaimLG()))
                        totalPendingClaimLG = totalPendingClaimLG.add(customerOblInfo.getPendingClaimLG());

                    if(customer.getReference() != null && Util.isTrue(customer.getReference().getUnpaidInsurance()))
                        totalUnpaidFeeInsurance = totalUnpaidFeeInsurance.add(customerOblInfo.getUnpaidFeeInsurance());

                    if(customer.getRelation() != null && customer.getRelation().getId() == RelationValue.BORROWER.value()) {
                        if(customerOblInfo.getLastReviewDate() != null){
                            if(lastReviewDate == null || customerOblInfo.getLastReviewDate().after(lastReviewDate)){
                                lastReviewDate = customerOblInfo.getLastReviewDate();
                            }
                        }

                        if(customerOblInfo.getExtendedReviewDate() != null){
                            if(extendedReviewDate == null || customerOblInfo.getExtendedReviewDate().before(extendedReviewDate)){
                                extendedReviewDate = customerOblInfo.getExtendedReviewDate();
                            }
                        }

                        //SCFScore, get worst score (Max is the worst) of final rate
                        if(customerOblInfo.getRatingFinal() != null && customerOblInfo.getRatingFinal().getId() != 0){
                            if(sbfScore.getScore() < customerOblInfo.getRatingFinal().getScore()){
                                sbfScore = customerOblInfo.getRatingFinal();
                            }
                        }

                        if(customerOblInfo.getServiceSegment() != null && Util.isTrue(customerOblInfo.getServiceSegment().getExistingSME())){
                            countExistingSME++;
                            log.debug("plus countExistingSME", countExistingSME);
                        }
                    }

                    if(customer.getReference() != null && Util.isTrue(customer.getReference().getSll())){
                        if(customerOblInfo.getServiceSegment() != null && Util.isTrue(customerOblInfo.getServiceSegment().getNonExistingSME())){
                            countNonExistingSME++;
                            log.debug("plus countNonExistingSME", countNonExistingSME);
                        }
                    }
                }
            }
        }
        log.info("value :: totalUnpaidFeeInsurance : {}, totalPendingClaimLG {}", totalUnpaidFeeInsurance, totalPendingClaimLG);

        basicInfo.setNoUnpaidFeeInsurance(totalUnpaidFeeInsurance.compareTo(BigDecimal.ZERO) == 0? RadioValue.YES.value() : RadioValue.NO.value());
        basicInfo.setNoPendingClaimLG(totalPendingClaimLG.compareTo(BigDecimal.ZERO) == 0? RadioValue.YES.value(): RadioValue.NO.value());
        basicInfo.setLastReviewDate(lastReviewDate);
        if(lastReviewDate == null)
            basicInfo.setPassAnnualReview(RadioValue.NO.value());
        else
            basicInfo.setPassAnnualReview(RadioValue.YES.value());

        if(sbfScore != null && sbfScore.getId() == 0){
            sbfScore = null;
        }
        basicInfo.setSbfScore(sbfScore);

        log.info("countExistingSME {}", countExistingSME);
        log.info("countNonExistingSME {}", countNonExistingSME);

        if(countExistingSME > 0){
            basicInfo.setRequestLoanWithSameName(RadioValue.YES.value());

        } else {
            basicInfo.setRequestLoanWithSameName(RadioValue.NO.value());

        }

        if(countExistingSME > 0 && countNonExistingSME == 0){
            basicInfo.setExistingSMECustomer(RadioValue.YES.value());
        } else {
            basicInfo.setExistingSMECustomer(RadioValue.NO.value());
        }

        basicInfo.setExtendedReviewDate(extendedReviewDate);

        basicInfo.setRetrievedFlag(1);

        log.info("calBasicInfo :: basicInfo {}", basicInfo);

        return basicInfo;
    }

    public void saveBasicInfo(BasicInfoView basicInfoView, long workCaseId) {
        log.info("saveBasicInfo ::: workCaseId : {} , basicInfoView : {}", workCaseId,basicInfoView);
        User user = getCurrentUser();

        WorkCase workCase = workCaseDAO.findById(workCaseId);

        if (basicInfoView.getQualitative() == 0) {
            basicInfoView.setQualitative(2);
        }

        BasicInfo basicInfo = basicInfoTransform.transformToModel(basicInfoView, workCase, user);
        basicInfoDAO.persist(basicInfo);

        BAPAInfo bapaInfo = bapaInfoDAO.findByWorkCase(workCase);
        if(bapaInfo == null) {
            bapaInfo = new BAPAInfo();
            bapaInfo.setCreateBy(user);
            bapaInfo.setCreateDate(new Date());
        }
        
        bapaInfo.setApplyBA(RadioValue.lookup(basicInfoView.getApplyBA()));
        if(basicInfoView.getApplyBA() == 2){ // apply ba = yes
            bapaInfo.setBaPaymentMethod(basicInfoView.getBaPaymentMethodValue());
        } else {
            bapaInfo.setBaPaymentMethod(BAPaymentMethodValue.NA);
        }
        bapaInfo.setWorkCase(workCase);
        bapaInfo.setModifyBy(user);
        bapaInfo.setModifyDate(new Date());
        bapaInfoDAO.persist(bapaInfo);

        basicInfo.setProductGroup(productGroupDAO.findById(basicInfoView.getProductGroup().getId()));
        basicInfo.setRequestType(requestTypeDAO.findById(basicInfoView.getRequestType().getId()));
        workCaseDAO.persist(workCase);

        //for new Open Account
        //delete
        for (Long openAccountId : basicInfoView.getDeleteTmpList()){
            if(openAccountId != 0){
                log.debug("Delete Item ( openAccountId ) : {}",openAccountId);
                OpenAccount openAccount = openAccountDAO.findById(openAccountId);

                List<OpenAccountName> openAccountNameList = openAccountNameDAO.findByOpenAccount(openAccount);
                openAccountNameDAO.delete(openAccountNameList);

                List<OpenAccountPurpose> openAccountPurposeList = openAccPurposeDAO.findByOpenAccount(openAccount);
                openAccPurposeDAO.delete(openAccountPurposeList);

                openAccountDAO.delete(openAccount);
            }
        }

        //add new
        for (OpenAccountView bav : basicInfoView.getOpenAccountViews()) {
            OpenAccount openAccount = openAccountTransform.transformToModel(bav,workCase);
            //remove all open account name in open account
            if(openAccount.getId() != 0){
                List<OpenAccountName> openAccountNameList = openAccountNameDAO.findByOpenAccount(openAccount);
                openAccountNameDAO.delete(openAccountNameList);

                List<OpenAccountPurpose> openAccountPurposeList = openAccPurposeDAO.findByOpenAccount(openAccount);
                openAccPurposeDAO.delete(openAccountPurposeList);
            }
            openAccountDAO.persist(openAccount);

            if(openAccount.getOpenAccountNameList() != null){
                for (OpenAccountName oan : openAccount.getOpenAccountNameList()){
                    OpenAccountName openAccountName = new OpenAccountName();
                    openAccountName.setCustomer(oan.getCustomer());
                    openAccountName.setOpenAccount(openAccount);
                    openAccountNameDAO.persist(openAccountName);
                }
            }

            if(openAccount.getOpenAccountPurposeList() != null){
                for (OpenAccountPurpose oap : openAccount.getOpenAccountPurposeList()){
                    OpenAccountPurpose openAccountPurpose = new OpenAccountPurpose();
                    openAccountPurpose.setAccountPurpose(oap.getAccountPurpose());
                    openAccountPurpose.setOpenAccount(openAccount);
                    openAccPurposeDAO.persist(openAccountPurpose);
                }
            }
        }
    }
}
