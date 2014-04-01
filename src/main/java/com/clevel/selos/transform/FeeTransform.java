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
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
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

    public List<FeeDetail> transformToDB(List<PricingFee> pricingFeeList , long workCaseId){
        List<FeeDetail> feeDetailList = new ArrayList<FeeDetail>();
        FeeDetail feeDetail;

        WorkCase workCase = workCaseDAO.findById(workCaseId);
        for(PricingFee pricingFee :pricingFeeList){
            feeDetail = new FeeDetail();
            NewCreditDetail newCreditDetail = newCreditDetailDAO.findById(Long.valueOf(pricingFee.getCreditDetailId()));
            FeeType feeType = feeTypeDAO.findByBRMSCode(pricingFee.getType());
            feeDetail.setNewCreditDetail(newCreditDetail);
            feeDetail.setFeeType(feeType);
            feeDetail.setDescription(pricingFee.getDescription());
            feeDetail.setFeeLevel(pricingFee.getFeeLevel());
            feeDetail.setPercentFee(pricingFee.getFeePercent());
            feeDetail.setPercentFeeAfter(pricingFee.getFeePercentAfterDiscount());
            feeDetail.setFeeYear(pricingFee.getYear());
            feeDetail.setAmount(pricingFee.getAmount());
            feeDetail.setWorkCase(workCase);
            feeDetailList.add(feeDetail);
        }
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
