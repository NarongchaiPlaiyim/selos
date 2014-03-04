package com.clevel.selos.integration.brms.convert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.clevel.selos.integration.BRMS;
import com.clevel.selos.integration.brms.model.BRMSFieldAttributes;
import com.clevel.selos.integration.brms.model.request.BRMSAccountRequested;
import com.clevel.selos.integration.brms.model.request.BRMSApplicationInfo;
import com.clevel.selos.integration.brms.model.response.PricingFee;
import com.clevel.selos.integration.brms.model.response.StandardPricingResponse;
import com.clevel.selos.integration.brms.service.standardpricing.feerules.*;
import com.clevel.selos.integration.brms.service.standardpricing.feerules.ApplicationType;
import com.clevel.selos.integration.brms.service.standardpricing.feerules.AttributeType;
import com.clevel.selos.integration.brms.service.standardpricing.feerules.CreditFacilityType;
import com.clevel.selos.integration.brms.service.standardpricing.feerules.DecisionServiceRequest;
import com.clevel.selos.integration.brms.service.standardpricing.feerules.ProductType;
import com.clevel.selos.integration.brms.service.standardpricing.feerules.SELOSProductProgramType;
import com.clevel.selos.integration.brms.service.standardpricing.feerules.UnderwritingApprovalRequestType;
import com.clevel.selos.integration.brms.service.standardpricing.feerules.UnderwritingRequest;
import com.clevel.selos.model.FeeLevel;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.xml.datatype.DatatypeFactory;

public class StandardPricingFeeConverter extends Converter{

    @Inject
    @BRMS
    Logger logger;

    @Inject
    public StandardPricingFeeConverter(){
    }

    public DecisionServiceRequest getDecisionServiceRequest(BRMSApplicationInfo applicationInfo){

        if(applicationInfo != null){
            //Initial Decision Service Request//

            ApplicationType applicationType = new ApplicationType();


            applicationType.setApplicationNumber(applicationInfo.getApplicationNo());
            try{
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTime(applicationInfo.getProcessDate());
                applicationType.setDateOfApplication(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
            }catch (Exception ex){
                logger.error("Could not transform Date");
            }

            List<AttributeType> attributeTypeList = applicationType.getAttribute();
            attributeTypeList.add(getAttributeType(BRMSFieldAttributes.APP_IN_DATE, applicationInfo.getBdmSubmitDate()));
            attributeTypeList.add(getAttributeType(BRMSFieldAttributes.GUARANTEE_TYPE, applicationInfo.getLoanRequestType()));
            attributeTypeList.add(getAttributeType(BRMSFieldAttributes.TOTAL_TCG_GUARANTEE_AMOUNT, applicationInfo.getTotalTCGGuaranteeAmount()));
            attributeTypeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_INDV_GUARANTOR, applicationInfo.getNumberOfIndvGuarantor()));
            attributeTypeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_JURIS_GUARANTOR, applicationInfo.getNumberOfJurisGuarantor()));
            attributeTypeList.add(getAttributeType(BRMSFieldAttributes.TOTAL_MORTGAGE_VALUE, applicationInfo.getTotalMortgageValue()));
            attributeTypeList.add(getAttributeType(BRMSFieldAttributes.NUM_OF_REDEEM_TRANSACTION, applicationInfo.getTotalRedeemTransaction()));


            List<ProductType> productTypeList = applicationType.getProduct();
            List<BRMSAccountRequested> accountRequestedList = applicationInfo.getAccountRequestedList();
            ProductType productType = new ProductType();
            productType.setProductType(applicationInfo.getProductGroup());
            productType.setRequestedCreditLimit(applicationInfo.getTotalApprovedCredit());

            List<SELOSProductProgramType> selosProductProgramTypeList = productType.getSelosProductProgram();

            SELOSProductProgramType selosProductProgramType = new SELOSProductProgramType();
            List<CreditFacilityType> creditFacilityTypeList = null;
            for(BRMSAccountRequested accountRequested: accountRequestedList){
                if(!accountRequested.getProductProgram().equals(selosProductProgramType.getName())){
                    if(selosProductProgramType.getName() != null && !"".equals(selosProductProgramType.getName())){
                        selosProductProgramTypeList.add(selosProductProgramType);
                        selosProductProgramType = new SELOSProductProgramType();
                    }
                    selosProductProgramType.setID(accountRequested.getCreditDetailId());
                    selosProductProgramType.setName(accountRequested.getProductProgram());
                    selosProductProgramType.setFrontEndDiscount(accountRequested.getFontEndFeeDiscountRate());
                    creditFacilityTypeList = selosProductProgramType.getCreditFacility();
                }

                CreditFacilityType creditFacilityType = new CreditFacilityType();
                creditFacilityType.setID(accountRequested.getCreditDetailId());
                creditFacilityType.setType(accountRequested.getCreditType());
                creditFacilityType.setCreditLimit(accountRequested.getLimit());

                creditFacilityTypeList.add(creditFacilityType);
            }
            if(selosProductProgramType != null){
                selosProductProgramTypeList.add(selosProductProgramType);
            }

            productTypeList.add(productType);

            UnderwritingApprovalRequestType underwritingApprovalRequestType = new UnderwritingApprovalRequestType();
            underwritingApprovalRequestType.setApplication(applicationType);

            UnderwritingRequest underwritingRequest = new UnderwritingRequest();
            underwritingRequest.setUnderwritingApprovalRequest(underwritingApprovalRequestType);

            DecisionServiceRequest decisionServiceRequest = new DecisionServiceRequest();
            decisionServiceRequest.setUnderwritingRequest(underwritingRequest);
            decisionServiceRequest.setDecisionID(getDecisionID(applicationInfo.getApplicationNo(), applicationInfo.getStatusCode()));

            return decisionServiceRequest;
        }

        return null;
    }

    public StandardPricingResponse getStandardPricingResponse(DecisionServiceResponse decisionServiceResponse){
        StandardPricingResponse standardPricingResponse = new StandardPricingResponse();
        if(decisionServiceResponse != null){

            standardPricingResponse.setDecisionID(decisionServiceResponse.getDecisionID());
            UnderwritingRequest underwritingRequest = decisionServiceResponse.getUnderwritingRequest();
            UnderwritingApprovalRequestType underwritingApprovalRequestType = underwritingRequest.getUnderwritingApprovalRequest();
            ApplicationType applicationType = underwritingApprovalRequestType.getApplication();

            List<PricingFee> pricingFeeList = new ArrayList<PricingFee>();

            List<FeeType> feeTypeList = applicationType.getFee();
            for(FeeType feeType : feeTypeList){
                pricingFeeList.add(getPricingFee(feeType, null));
            }

            List<ProductType> productTypeList = applicationType.getProduct();
            for(ProductType productType : productTypeList){
                List<SELOSProductProgramType> productProgramTypeList = productType.getSelosProductProgram();
                for(SELOSProductProgramType productProgramType : productProgramTypeList){
                    List<CreditFacilityType> creditFacilityTypeList = productProgramType.getCreditFacility();
                    for(CreditFacilityType creditFacilityType : creditFacilityTypeList){
                        feeTypeList = creditFacilityType.getFee();
                        for(FeeType feeType : feeTypeList){
                            pricingFeeList.add(getPricingFee(feeType, creditFacilityType.getID()));
                        }
                    }
                }
            }
            standardPricingResponse.setPricingFeeList(pricingFeeList);

        }
        return standardPricingResponse;
    }

    private AttributeType getAttributeType(BRMSFieldAttributes field, Date value){
        logger.debug("-- getAttributeType()");
        AttributeType attributeType = new AttributeType();
        try{
            attributeType.setName(field.value());
            logger.debug("-- field.value()[{}]", field.value());
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(value);
            logger.debug("-- value()[{}]", value);
            attributeType.setDateTimeValue(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
        } catch (Exception ex){
            logger.error("Cannot convert XML");
            logger.error("-- Exception : {}", ex.getMessage());
        }
        return attributeType;
    }

    private AttributeType getAttributeType(BRMSFieldAttributes field, String value){
        AttributeType attributeType = new AttributeType();
        attributeType.setName(field.value());
        attributeType.setStringValue(value);
        return attributeType;
    }

    private AttributeType getAttributeType(BRMSFieldAttributes field, BigDecimal value){
        AttributeType attributeType = new AttributeType();
        attributeType.setName(field.value());
        attributeType.setNumericValue(value);

        return attributeType;
    }

    private PricingFee getPricingFee(FeeType feeType, String creditTypeId){
        PricingFee pricingFee = new PricingFee();
        pricingFee.setType(feeType.getType());
        if(creditTypeId == null){
            pricingFee.setFeeLevel(FeeLevel.APP_LEVEL);
        } else {
            pricingFee.setFeeLevel(FeeLevel.CREDIT_LEVEL);
        }
        pricingFee.setCreditDetailId(creditTypeId);
        pricingFee.setAmount(feeType.getAmount());
        pricingFee.setDescription(feeType.getDescription());

        List<AttributeType> attributeTypeList = feeType.getAttribute();
        for(AttributeType attributeType : attributeTypeList){
            if(attributeType.getName().equals(BRMSFieldAttributes.PRICE_FEE_PERCENT.value())){
                pricingFee.setFeePercent(attributeType.getNumericValue());
            } else if(attributeType.getName().equals(BRMSFieldAttributes.PRICE_FEE_PERCENT_AFT_DISCOUNT.value())){
                pricingFee.setFeePercentAfterDiscount(attributeType.getNumericValue());
            } else if(attributeType.getName().equals(BRMSFieldAttributes.PRICE_PAYMENT_METHOD.value())){
                pricingFee.setPaymentMethod(attributeType.getStringValue());
            } else if(attributeType.getName().equals(BRMSFieldAttributes.PRICE_YEAR)){
                pricingFee.setYear(attributeType.getNumericValue());
            }
        }
        return pricingFee;
    }

}
