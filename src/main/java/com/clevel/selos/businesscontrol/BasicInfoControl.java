package com.clevel.selos.businesscontrol;

import com.clevel.selos.businesscontrol.util.bpm.BPMExecutor;
import com.clevel.selos.dao.master.ProductGroupDAO;
import com.clevel.selos.dao.master.RequestTypeDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.BAPaymentMethodValue;
import com.clevel.selos.model.CreditCategory;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.RelationValue;
import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.db.master.ProductGroup;
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
    ProductGroupDAO productGroupDAO;
    @Inject
    RequestTypeDAO requestTypeDAO;
    @Inject
    BAPAInfoDAO bapaInfoDAO;
    @Inject
    ExistingCreditFacilityDAO existingCreditFacilityDAO;
    @Inject
    ExistingCreditDetailDAO existingCreditDetailDAO;

    @Inject
    BasicInfoTransform basicInfoTransform;
    @Inject
    OpenAccountTransform openAccountTransform;
    @Inject
    BPMExecutor bpmExecutor;

    /*@Inject
    ExSummaryControl exSummaryControl;*/

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

        //set existing loan in one year flag
        basicInfo.setHaveLoanInOneYear(1);
        ExistingCreditFacility existingCreditFacility = existingCreditFacilityDAO.findByWorkCaseId(basicInfo.getWorkCase().getId());
        if(existingCreditFacility!=null) {
            List<ExistingCreditDetail> borrowerComExistingCreditList = existingCreditDetailDAO.findByExistingCreditFacilityByTypeAndCategory(existingCreditFacility,RelationValue.BORROWER.value(), CreditCategory.COMMERCIAL);
            if(borrowerComExistingCreditList!=null && borrowerComExistingCreditList.size()>0){
                for(ExistingCreditDetail existingCreditDetail : borrowerComExistingCreditList){
                    if(existingCreditDetail.getExistProductSegment()!=null){
                        if(existingCreditDetail.getExistProductSegment().getId()==2 && existingCreditDetail.getExistProductSegment().getId()==6){
                            basicInfo.setHaveLoanInOneYear(2);
                            break;
                        }
                    }
                }
            }
        }
        log.info("haveLoanInOneYear {}", Util.isRadioTrue(basicInfo.getHaveLoanInOneYear()));

        log.info("calBasicInfo :: basicInfo {}", basicInfo);

        return basicInfo;
    }

    public void saveBasicInfo(BasicInfoView basicInfoView, long workCaseId, String queueName, String wobNumber) {
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

        ProductGroup productGroup = productGroupDAO.findById(basicInfoView.getProductGroup().getId());
        workCase.setProductGroup(productGroup);
        workCase.setRequestType(requestTypeDAO.findById(basicInfoView.getRequestType().getId()));
        //----Set update flag for require in Check Criteria----
        workCase.setCaseUpdateFlag(1);
        workCaseDAO.persist(workCase);

        //update product group to
        try {
            bpmExecutor.updateProductGroup(productGroup != null ? productGroup.getDescription() : "", queueName, wobNumber);
        }catch (Exception ex){
            log.error("Exception while update product group to BPM : ", ex);
        }

        //Delete Open Account
        for (Long openAccountId : basicInfoView.getDeleteTmpList()){
            if(openAccountId != 0){
                OpenAccount openAccount = openAccountDAO.findById(openAccountId);
                openAccountDAO.delete(openAccount);
            }
        }

        //Add new Open Account
        for (OpenAccountView bav : basicInfoView.getOpenAccountViews()) {
            OpenAccount openAccount = openAccountTransform.transformToModel(bav,workCase);
            if(openAccount.getId() != 0){
                List<OpenAccountName> openAccountNameList = openAccountNameDAO.findByOpenAccount(openAccount);
                openAccountNameDAO.delete(openAccountNameList);

                List<OpenAccountPurpose> openAccountPurposeList = openAccPurposeDAO.findByOpenAccount(openAccount);
                openAccPurposeDAO.delete(openAccountPurposeList);
            }
            openAccountDAO.persist(openAccount);
        }

        //Update BOT Class
        //exSummaryControl.calculateBOTClass(workCaseId);
        //update product group to BPM
        //bpmExecutor.updateProductGroup(basicInfo.get)
    }
}
