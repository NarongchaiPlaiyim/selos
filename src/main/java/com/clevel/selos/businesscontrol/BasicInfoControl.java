package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.*;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.RelationValue;
import com.clevel.selos.model.db.master.*;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.db.working.OpenAccountPurpose;
import com.clevel.selos.model.view.*;
import com.clevel.selos.transform.BasicInfoAccPurposeTransform;
import com.clevel.selos.transform.BasicInfoAccountTransform;
import com.clevel.selos.transform.BasicInfoTransform;
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
    OpenAccountPurposeDAO openAccPurposeDAO;
    @Inject
    CustomerEntityDAO customerEntityDAO;
    @Inject
    ProductGroupDAO productGroupDAO;
    @Inject
    RequestTypeDAO requestTypeDAO;


    @Inject
    BasicInfoTransform basicInfoTransform;
    @Inject
    BasicInfoAccountTransform basicInfoAccountTransform;
    @Inject
    BasicInfoAccPurposeTransform basicInfoAccPurposeTransform;
    @Inject
    CustomerInfoControl customerInfoControl;

    @Inject
    public BasicInfoControl(){

    }

    public BasicInfoView getBasicInfo(long workCaseId) {
        log.info("getBasicInfo ::: workCaseId : {}", workCaseId);
        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
        WorkCase workCase = workCaseDAO.findById(workCaseId);

        if (basicInfo == null) {
            basicInfo = initialBasicInfo(workCase);
        }

        BasicInfoView basicInfoView = basicInfoTransform.transformToView(basicInfo, workCase);
        log.info("getBasicInfo ::: basicInfoView : {}", basicInfoView);
        return basicInfoView;
    }

    private BasicInfo initialBasicInfo(WorkCase workCase){
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

    public CustomerEntity getCustomerEntityByWorkCaseId(long workCaseId) {
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

    public BasicInfo calBasicInfo(long workCaseId){
        BasicInfo basicInfo = basicInfoDAO.findByWorkCaseId(workCaseId);
        if(basicInfo != null){
            basicInfo = calBasicInfo(basicInfo);
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

        log.info("calBasicInfo :: basicInfo {}", basicInfo);

        return basicInfo;
    }

    public void saveBasicInfo(BasicInfoView basicInfoView, long workCaseId) {
        User user = getCurrentUser();

        WorkCase workCase = workCaseDAO.findById(workCaseId);

        if (basicInfoView.getQualitative() == 0) {
            basicInfoView.setQualitative(2);
        }

        if (basicInfoView.getApplyBA() == 0) {
            basicInfoView.setBaPaymentMethod(new BAPaymentMethod());
        }

        BasicInfo basicInfo = basicInfoTransform.transformToModel(basicInfoView, workCase, user);
        basicInfoDAO.persist(basicInfo);

        basicInfo.setProductGroup(productGroupDAO.findById(basicInfoView.getProductGroup().getId()));
        basicInfo.setRequestType(requestTypeDAO.findById(basicInfoView.getRequestType().getId()));
        workCaseDAO.persist(workCase);

        List<OpenAccount> openAccountList = openAccountDAO.findByBasicInfoId(basicInfo.getId());
        for (OpenAccount oa : openAccountList) {
            List<OpenAccountPurpose> openAccPurposeList = openAccPurposeDAO.findByOpenAccountId(oa.getId());
            openAccPurposeDAO.delete(openAccPurposeList);
        }
        openAccountDAO.delete(openAccountList);

        if (basicInfoView.getBasicInfoAccountViews() != null && basicInfoView.getBasicInfoAccountViews().size() > 0) {
            for (BasicInfoAccountView biav : basicInfoView.getBasicInfoAccountViews()) {
                System.out.println("BasicInfoAccountView [ ID ] : "+ biav.getId() +" [ Account Name ] : " +biav.getAccountName());
                OpenAccount openAccount = basicInfoAccountTransform.transformToModel(biav, basicInfo);
                openAccountDAO.save(openAccount);

                for (BasicInfoAccountPurposeView biapv : biav.getBasicInfoAccountPurposeView()) {
                    if (biapv.isSelected()) {
                        OpenAccountPurpose openAccPurpose = basicInfoAccPurposeTransform.transformToModel(biapv, openAccount);
                        openAccPurposeDAO.save(openAccPurpose);
                    }
                }
            }
        }

    }
}
