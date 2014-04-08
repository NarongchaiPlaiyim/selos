package com.clevel.selos.transform;

import com.clevel.selos.dao.master.FeePaymentMethodDAO;
import com.clevel.selos.dao.master.FeeTypeDAO;
import com.clevel.selos.dao.working.NewCreditDetailDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.exception.BRMSInterfaceException;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.brms.model.response.PricingFee;
import com.clevel.selos.model.db.master.FeePaymentMethod;
import com.clevel.selos.model.db.master.FeeType;
import com.clevel.selos.model.db.working.FeeDetail;
import com.clevel.selos.model.db.working.NewCreditDetail;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.FeeDetailView;
import com.clevel.selos.model.view.FeePaymentMethodView;
import com.clevel.selos.model.view.FeeTypeView;
import com.clevel.selos.model.view.NewFeeDetailView;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.util.Util;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class FeeTransform extends Transform{

    @Inject
    @SELOS
    Logger logger;

    @Inject
    FeePaymentMethodDAO feePaymentMethodDAO;

    @Inject
    FeeTypeDAO feeTypeDAO;

    @Inject
    NewCreditDetailDAO newCreditDetailDAO;

    @Inject
    WorkCaseDAO workCaseDAO;

    @Inject
    @ExceptionMessage
    Message exceptionMsg;

    @Inject
    public FeeTransform(){}

    public FeeDetailView transformToView(PricingFee pricingFee){
        FeeDetailView feeDetailView = new FeeDetailView();
        if(pricingFee != null){
            feeDetailView.setCreditDetailViewId(Long.valueOf(pricingFee.getCreditDetailId()));
            feeDetailView.setDescription(pricingFee.getDescription());
            feeDetailView.setFeeLevel(pricingFee.getFeeLevel());
            feeDetailView.setFeePaymentMethodView(getFeePaymentMethodView(pricingFee.getPaymentMethod()));
            feeDetailView.setFeeTypeView(getFeeTypeView(pricingFee.getType()));
            feeDetailView.setPercentFee(pricingFee.getFeePercent());
            feeDetailView.setPercentFeeAfter(pricingFee.getFeePercentAfterDiscount());
            feeDetailView.setFeeYear(pricingFee.getYear());
            feeDetailView.setFeeAmount(pricingFee.getAmount());
        }
        return feeDetailView;
    }

    public FeePaymentMethodView getFeePaymentMethodView(String brmsPaymentMethod) {
        FeePaymentMethodView feePaymentMethodView = new FeePaymentMethodView();
        if(brmsPaymentMethod != null){
            try {
                FeePaymentMethod feePaymentMethod = feePaymentMethodDAO.findByBRMSCode(brmsPaymentMethod);
                feePaymentMethodView.setId(feePaymentMethod.getId());
                feePaymentMethodView.setDescription(feePaymentMethod.getDescription());
                feePaymentMethodView.setBrmsCode(feePaymentMethod.getBrmsCode());
                feePaymentMethodView.setActive(feePaymentMethod.getActive());
                feePaymentMethodView.setDebitFromCustomer(feePaymentMethod.isDebitFromCustomer());
                feePaymentMethodView.setIncludeInAgreementSign(feePaymentMethod.isIncludeInAgreementSign());

            } catch (Exception ex){
                logger.debug("Cannot Find feePaymentMethod - '{}' Payment Method is not configured in Database", brmsPaymentMethod);
                throw new BRMSInterfaceException(null, ExceptionMapping.BRMS_INVALID_RETURN_DATA, exceptionMsg.get(ExceptionMapping.BRMS_INVALID_RETURN_DATA, "Fee Payment Method was not found " + brmsPaymentMethod));
            }

        }
        return feePaymentMethodView;
    }

    public FeeTypeView getFeeTypeView(String type){
        FeeTypeView feeTypeView = new FeeTypeView();
        if(type != null){
            try{
                FeeType feeType = feeTypeDAO.findByBRMSCode(type);
                feeTypeView.setId(feeType.getId());
                feeTypeView.setBrmsCode(type);
                feeTypeView.setDescription(feeType.getDescription());
                feeTypeView.setFrontend(feeType.isFrontend());
                feeTypeView.setActive(feeType.getActive());
            } catch (Exception ex){
                logger.debug("Cannot Find feeType - '{}' Fee Type is not configured correctly in Database", type);
                throw new BRMSInterfaceException(null, ExceptionMapping.BRMS_INVALID_RETURN_DATA, exceptionMsg.get(ExceptionMapping.BRMS_INVALID_RETURN_DATA, "FeeType was not found " + type));
            }
        }
        return feeTypeView;
    }
    //TODO transform to save
    public List<FeeDetail> transformToDB(List<NewFeeDetailView> newFeeDetailViewList  , long workCaseId){
        List<FeeDetail> feeDetailList = new ArrayList<FeeDetail>();
        FeeDetail feeDetail;
        WorkCase workCase = workCaseDAO.findById(workCaseId);

        for(NewFeeDetailView newFeeDetailView : newFeeDetailViewList){
            if(!Util.isNull(newFeeDetailView.getCommitmentFee())){ // CommitmentFee
                feeDetail = new FeeDetail();
                NewCreditDetail newCreditDetail = newCreditDetailDAO.findById(newFeeDetailView.getNewCreditDetailView().getId());
                FeeType feeType = feeTypeDAO.findByBRMSCode(newFeeDetailView.getCommitmentFee().getFeeTypeView().getBrmsCode());
                FeePaymentMethod feePaymentMethod = feePaymentMethodDAO.findByBRMSCode(newFeeDetailView.getCommitmentFee().getFeePaymentMethodView().getBrmsCode());
                feeDetail.setPercentFee(newFeeDetailView.getCommitmentFee().getPercentFee());
                feeDetail.setPercentFeeAfter(newFeeDetailView.getCommitmentFee().getPercentFeeAfter());
                feeDetail.setFeeYear(newFeeDetailView.getCommitmentFee().getFeeYear());
                feeDetail.setAmount(newFeeDetailView.getCommitmentFee().getFeeAmount());
                feeDetail.setFeeLevel(newFeeDetailView.getCommitmentFee().getFeeLevel());
                feeDetail.setNewCreditDetail(newCreditDetail);
                feeDetail.setFeeType(feeType);
                feeDetail.setPaymentMethod(feePaymentMethod);
                feeDetail.setDescription(newFeeDetailView.getCommitmentFee().getDescription());
                feeDetail.setWorkCase(workCase);
                feeDetailList.add(feeDetail);
            }else if(!Util.isNull(newFeeDetailView.getCancellationFee())){ // CancellationFee
                feeDetail = new FeeDetail();
                NewCreditDetail newCreditDetail = newCreditDetailDAO.findById(newFeeDetailView.getNewCreditDetailView().getId());
                FeeType feeType = feeTypeDAO.findByBRMSCode(newFeeDetailView.getCancellationFee().getFeeTypeView().getBrmsCode());
                FeePaymentMethod feePaymentMethod = feePaymentMethodDAO.findByBRMSCode(newFeeDetailView.getCancellationFee().getFeePaymentMethodView().getBrmsCode());
                feeDetail.setPercentFee(newFeeDetailView.getCancellationFee().getPercentFee());
                feeDetail.setPercentFeeAfter(newFeeDetailView.getCancellationFee().getPercentFeeAfter());
                feeDetail.setFeeYear(newFeeDetailView.getCancellationFee().getFeeYear());
                feeDetail.setAmount(newFeeDetailView.getCancellationFee().getFeeAmount());
                feeDetail.setFeeLevel(newFeeDetailView.getCancellationFee().getFeeLevel());
                feeDetail.setNewCreditDetail(newCreditDetail);
                feeDetail.setFeeType(feeType);
                feeDetail.setPaymentMethod(feePaymentMethod);
                feeDetail.setDescription(newFeeDetailView.getCancellationFee().getDescription());
                feeDetail.setWorkCase(workCase);
                feeDetailList.add(feeDetail);
            }else if(!Util.isNull(newFeeDetailView.getExtensionFee())){ // ExtensionFee
                feeDetail = new FeeDetail();
                NewCreditDetail newCreditDetail = newCreditDetailDAO.findById(newFeeDetailView.getNewCreditDetailView().getId());
                FeeType feeType = feeTypeDAO.findByBRMSCode(newFeeDetailView.getExtensionFee().getFeeTypeView().getBrmsCode());
                FeePaymentMethod feePaymentMethod = feePaymentMethodDAO.findByBRMSCode(newFeeDetailView.getExtensionFee().getFeePaymentMethodView().getBrmsCode());
                feeDetail.setPercentFee(newFeeDetailView.getExtensionFee().getPercentFee());
                feeDetail.setPercentFeeAfter(newFeeDetailView.getExtensionFee().getPercentFeeAfter());
                feeDetail.setFeeYear(newFeeDetailView.getExtensionFee().getFeeYear());
                feeDetail.setAmount(newFeeDetailView.getExtensionFee().getFeeAmount());
                feeDetail.setFeeLevel(newFeeDetailView.getExtensionFee().getFeeLevel());
                feeDetail.setNewCreditDetail(newCreditDetail);
                feeDetail.setFeeType(feeType);
                feeDetail.setPaymentMethod(feePaymentMethod);
                feeDetail.setDescription(newFeeDetailView.getExtensionFee().getDescription());
                feeDetail.setWorkCase(workCase);
                feeDetailList.add(feeDetail);
            }else if(!Util.isNull(newFeeDetailView.getPrepaymentFee())){ // PrepaymentFee
                feeDetail = new FeeDetail();
                NewCreditDetail newCreditDetail = newCreditDetailDAO.findById(newFeeDetailView.getNewCreditDetailView().getId());
                FeeType feeType = feeTypeDAO.findByBRMSCode(newFeeDetailView.getPrepaymentFee().getFeeTypeView().getBrmsCode());
                FeePaymentMethod feePaymentMethod = feePaymentMethodDAO.findByBRMSCode(newFeeDetailView.getPrepaymentFee().getFeePaymentMethodView().getBrmsCode());
                feeDetail.setPercentFee(newFeeDetailView.getPrepaymentFee().getPercentFee());
                feeDetail.setPercentFeeAfter(newFeeDetailView.getPrepaymentFee().getPercentFeeAfter());
                feeDetail.setFeeYear(newFeeDetailView.getPrepaymentFee().getFeeYear());
                feeDetail.setAmount(newFeeDetailView.getPrepaymentFee().getFeeAmount());
                feeDetail.setFeeLevel(newFeeDetailView.getPrepaymentFee().getFeeLevel());
                feeDetail.setNewCreditDetail(newCreditDetail);
                feeDetail.setFeeType(feeType);
                feeDetail.setPaymentMethod(feePaymentMethod);
                feeDetail.setDescription(newFeeDetailView.getPrepaymentFee().getDescription());
                feeDetail.setWorkCase(workCase);
                feeDetailList.add(feeDetail);
            }else if(!Util.isNull(newFeeDetailView.getStandardFrontEndFee())){ // StandardFrontEndFee
                feeDetail = new FeeDetail();
                NewCreditDetail newCreditDetail = newCreditDetailDAO.findById(newFeeDetailView.getNewCreditDetailView().getId());
                FeeType feeType = feeTypeDAO.findByBRMSCode(newFeeDetailView.getStandardFrontEndFee().getFeeTypeView().getBrmsCode());
                FeePaymentMethod feePaymentMethod = feePaymentMethodDAO.findByBRMSCode(newFeeDetailView.getStandardFrontEndFee().getFeePaymentMethodView().getBrmsCode());
                feeDetail.setPercentFee(newFeeDetailView.getStandardFrontEndFee().getPercentFee());
                feeDetail.setPercentFeeAfter(newFeeDetailView.getStandardFrontEndFee().getPercentFeeAfter());
                feeDetail.setFeeYear(newFeeDetailView.getStandardFrontEndFee().getFeeYear());
                feeDetail.setAmount(newFeeDetailView.getStandardFrontEndFee().getFeeAmount());
                feeDetail.setFeeLevel(newFeeDetailView.getStandardFrontEndFee().getFeeLevel());
                feeDetail.setNewCreditDetail(newCreditDetail);
                feeDetail.setFeeType(feeType);
                feeDetail.setPaymentMethod(feePaymentMethod);
                feeDetail.setDescription(newFeeDetailView.getStandardFrontEndFee().getDescription());
                feeDetail.setWorkCase(workCase);
                feeDetailList.add(feeDetail);
            }

        }

        logger.debug("feeDetailList size ::: {}",feeDetailList.size());
        return feeDetailList;
    }


    public List<FeeDetailView> transformToView( List<FeeDetail> feeDetailList){
        List<FeeDetailView> feeDetailViewList = new ArrayList<FeeDetailView>();
        FeeDetailView feeDetailView;
       for(FeeDetail feeDetail:feeDetailList){
           feeDetailView = new FeeDetailView();
           feeDetailView.setCreditDetailViewId(feeDetail.getNewCreditDetail().getId());
           feeDetailView.setDescription(feeDetail.getDescription());
           feeDetailView.setFeeLevel(feeDetail.getFeeLevel());
           feeDetailView.setFeePaymentMethodView(getFeePaymentMethodView(feeDetail.getPaymentMethod().getBrmsCode()));
           feeDetailView.setFeeTypeView(getFeeTypeView(feeDetail.getFeeType().getBrmsCode()));
           feeDetailView.setPercentFee(feeDetail.getPercentFee());
           feeDetailView.setPercentFeeAfter(feeDetail.getPercentFeeAfter());
           feeDetailView.setFeeYear(feeDetail.getFeeYear());
           feeDetailView.setFeeAmount(feeDetail.getAmount());
           feeDetailViewList.add(feeDetailView);
        }
        return feeDetailViewList;
    }
}
