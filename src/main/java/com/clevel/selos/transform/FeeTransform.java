package com.clevel.selos.transform;

import com.clevel.selos.dao.master.FeePaymentMethodDAO;
import com.clevel.selos.dao.master.FeeTypeDAO;
import com.clevel.selos.exception.BRMSInterfaceException;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.brms.model.response.PricingFee;
import com.clevel.selos.model.db.master.FeePaymentMethod;
import com.clevel.selos.model.db.master.FeeType;
import com.clevel.selos.model.view.FeeDetailView;
import com.clevel.selos.model.view.FeePaymentMethodView;
import com.clevel.selos.model.view.FeeTypeView;
import com.clevel.selos.system.message.ExceptionMapping;
import com.clevel.selos.system.message.ExceptionMessage;
import com.clevel.selos.system.message.Message;
import org.slf4j.Logger;

import javax.inject.Inject;

public class FeeTransform extends Transform{

    @Inject
    @SELOS
    Logger logger;

    @Inject
    FeePaymentMethodDAO feePaymentMethodDAO;

    @Inject
    FeeTypeDAO feeTypeDAO;

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

}
