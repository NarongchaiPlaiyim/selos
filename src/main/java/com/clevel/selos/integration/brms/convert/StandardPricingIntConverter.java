package com.clevel.selos.integration.brms.convert;

import com.clevel.selos.integration.BRMS;
import com.clevel.selos.integration.brms.model.BRMSField;
import com.clevel.selos.integration.brms.model.request.BRMSAccountRequested;
import com.clevel.selos.integration.brms.model.request.BRMSApplicationInfo;
import com.clevel.selos.integration.brms.model.response.PricingIntTier;
import com.clevel.selos.integration.brms.model.response.PricingInterest;
import com.clevel.selos.integration.brms.model.response.StandardPricingResponse;
import com.clevel.selos.integration.brms.service.standardpricing.interestrules.*;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.xml.datatype.DatatypeFactory;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class StandardPricingIntConverter extends Converter {
    @Inject
    @BRMS
    Logger logger;

    @Inject
    public StandardPricingIntConverter(){}

    public DecisionServiceRequest getDecisionServiceRequest(BRMSApplicationInfo applicationInfo){
        if(applicationInfo != null){
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
            attributeTypeList.add(getAttributeType(BRMSField.APP_IN_DATE, applicationInfo.getBdmSubmitDate()));
            attributeTypeList.add(getAttributeType(BRMSField.GUARANTEE_TYPE, applicationInfo.getLoanRequestType()));
            attributeTypeList.add(getAttributeType(BRMSField.TOTAL_TCG_GUARANTEE_AMOUNT, applicationInfo.getTotalTCGGuaranteeAmount()));
            attributeTypeList.add(getAttributeType(BRMSField.NUMBER_OF_INDV_GUARANTOR, applicationInfo.getNumberOfIndvGuarantor()));
            attributeTypeList.add(getAttributeType(BRMSField.NUMBER_OF_JURIS_GUARANTOR, applicationInfo.getNumberOfJurisGuarantor()));
            attributeTypeList.add(getAttributeType(BRMSField.TOTAL_MORTGAGE_VALUE, applicationInfo.getTotalMortgageValue()));
            attributeTypeList.add(getAttributeType(BRMSField.NUMBER_OF_REDEEM_TRANSACTION, applicationInfo.getTotalRedeemTransaction()));


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
        StandardPricingResponse standardPricingIntResponse = new StandardPricingResponse();
        if(decisionServiceResponse != null){


            UnderwritingRequest underwritingRequest = decisionServiceResponse.getUnderwritingRequest();
            UnderwritingApprovalRequestType approvalRequestType = underwritingRequest.getUnderwritingApprovalRequest();
            ApplicationType applicationType = approvalRequestType.getApplication();

            //To store List of Pricing Interest Object.
            List<PricingInterest> pricingInterestList = new ArrayList<PricingInterest>();

            List<ProductType> productTypeList = applicationType.getProduct();
            for(ProductType productType : productTypeList){

                List<SELOSProductProgramType> productProgramTypeList = productType.getSelosProductProgram();
                for(SELOSProductProgramType productProgramType : productProgramTypeList){
                    List<CreditFacilityType> creditFacilityTypeList = productProgramType.getCreditFacility();
                    for(CreditFacilityType creditFacilityType : creditFacilityTypeList){

                        PricingType pricingType = creditFacilityType.getPricing();
                        PricingInterest pricingInterest = new PricingInterest();
                        pricingInterest.setCreditDetailId(creditFacilityType.getID());
                        List<PricingIntTier> pricingIntTierList = new ArrayList<PricingIntTier>();

                        List<PricingTierType> pricingTierList = pricingType.getPricingTier();
                        for(PricingTierType pricingTierType : pricingTierList){
                            PricingIntTier pricingIntTier = new PricingIntTier();
                            pricingIntTier.setRateType(pricingTierType.getRateType());
                            pricingIntTier.setSpread(pricingTierType.getSpread());
                            pricingIntTier.setRateVariance(pricingTierType.getRateVariance());

                            List<AttributeType> pricingAttrList = pricingTierType.getAttribute();
                            for(AttributeType attributeType : pricingAttrList){
                                if(attributeType.getName().equals(BRMSField.PRICE_MAXIMUM_RATE.value())){
                                    pricingIntTier.setMaxRateVariance(attributeType.getNumericValue());
                                    break;
                                }
                            }
                            pricingIntTierList.add(pricingIntTier);
                        }
                        pricingInterest.setPricingIntTierList(pricingIntTierList);
                        pricingInterestList.add(pricingInterest);
                    }
                }
            }

            standardPricingIntResponse.setDecisionID(decisionServiceResponse.getDecisionID());
            standardPricingIntResponse.setApplicationNo(applicationType.getApplicationNumber());

        }

        return standardPricingIntResponse;
    }

    private AttributeType getAttributeType(BRMSField field, Date value){
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

    private AttributeType getAttributeType(BRMSField field, String value){
        AttributeType attributeType = new AttributeType();
        attributeType.setName(field.value());
        attributeType.setStringValue(value);
        return attributeType;
    }

    private AttributeType getAttributeType(BRMSField field, BigDecimal value){
        AttributeType attributeType = new AttributeType();
        attributeType.setName(field.value());
        attributeType.setNumericValue(value);

        return attributeType;
    }

}
