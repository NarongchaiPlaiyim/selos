package com.clevel.selos.integration.brms.convert;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.clevel.selos.integration.BRMS;
import com.clevel.selos.integration.brms.model.BRMSField;
import com.clevel.selos.integration.brms.model.request.BRMSAccountRequested;
import com.clevel.selos.integration.brms.model.request.BRMSApplicationInfo;
import com.clevel.selos.integration.brms.service.standardpricing.feerules.*;
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

            SELOSProductProgramType selosProductProgramType = null;
            List<CreditFacilityType> creditFacilityTypeList = null;
            for(BRMSAccountRequested accountRequested: accountRequestedList){
                if(!accountRequested.getProductProgram().equals(selosProductProgramType.getName())){
                    selosProductProgramTypeList.add(selosProductProgramType);
                    selosProductProgramType = new SELOSProductProgramType();
                    selosProductProgramType.setID(accountRequested.getProposeId());
                    selosProductProgramType.setName(accountRequested.getProductProgram());
                    selosProductProgramType.setFrontEndDiscount(accountRequested.getFontEndFeeDiscountRate());
                    creditFacilityTypeList = selosProductProgramType.getCreditFacility();
                }

                CreditFacilityType creditFacilityType = new CreditFacilityType();
                creditFacilityType.setID(accountRequested.getProposeId());
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

    private AttributeType getAttributeType(BRMSField field, Date value){
        AttributeType attributeType = new AttributeType();

        try{
            attributeType.setName(field.value());
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(value);
            attributeType.setDateTimeValue(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
        } catch (Exception ex){
            logger.error("Cannot convert XML");
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
