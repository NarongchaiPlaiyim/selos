package com.clevel.selos.integration.brms.convert;

import com.clevel.selos.integration.BRMS;
import com.clevel.selos.integration.brms.model.request.*;
import com.clevel.selos.integration.brms.model.request.data2.*;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ConvertImp implements ConvertInf, Serializable {

    private ApplicationTypeLevel applicationTypeLevel;
    private List<BorrowerTypeLevel> borrowerTypeLevel;
    private List<ProductTypeLevel> productTypeLevel;
    private List<SELOSProductProgramTypeLevel> selosProductProgramTypesLevel;
    private List<CreditFacilityTypeLevel> creditFacilityTypeLevel;
    private List<CollateralTypeLevel> collateralTypesLevel;

    @Inject
    @BRMS
    Logger log;

    @Inject
    public ConvertImp() {
    }

    @Override
    public void convertInputModelToRequestModel(PreScreenRequest inputModel) throws Exception {

    }

    @Override
    public void convertInputModelToRequestModel(FullApplicationRequest inputModel) throws Exception {

    }

    @Override
    public void convertInputModelToRequestModel(DocCustomerRequest inputModel) throws Exception {

    }

    @Override
    public void convertInputModelToRequestModel(DocAppraisalRequest inputModel) throws Exception {

    }

    @Override
    public com.clevel.selos.integration.brms.service.standardpricing.interestrules.DecisionServiceRequest convertInputModelToRequestModel(StandardPricingIntRequest inputModel) throws Exception {
        log.debug("convertInputModelToRequestModel({})", inputModel.toString());
        com.clevel.selos.integration.brms.service.standardpricing.interestrules.DecisionServiceRequest request = null;
        com.clevel.selos.integration.brms.service.standardpricing.interestrules.ApplicationType applicationType = null;
        GregorianCalendar gregory = null;
        XMLGregorianCalendar calendar = null;
        List<com.clevel.selos.integration.brms.service.standardpricing.interestrules.AttributeType> attributeTypeList = null;
        com.clevel.selos.integration.brms.service.standardpricing.interestrules.AttributeType attributeType = null;
        List<com.clevel.selos.integration.brms.service.standardpricing.interestrules.BorrowerType> borrowerTypeList = null;
        com.clevel.selos.integration.brms.service.standardpricing.interestrules.BorrowerType borrowerType = null;
        List<com.clevel.selos.integration.brms.service.standardpricing.interestrules.ProductType> productTypeList = null;
        com.clevel.selos.integration.brms.service.standardpricing.interestrules.ProductType productType = null;
        List<com.clevel.selos.integration.brms.service.standardpricing.interestrules.SELOSProductProgramType> selosProductProgramTypeList = null;
        com.clevel.selos.integration.brms.service.standardpricing.interestrules.SELOSProductProgramType selosProductProgramType = null;
        List<com.clevel.selos.integration.brms.service.standardpricing.interestrules.CreditFacilityType> creditFacilityTypeList = null;
        com.clevel.selos.integration.brms.service.standardpricing.interestrules.CreditFacilityType creditFacilityType = null;
        List<com.clevel.selos.integration.brms.service.standardpricing.interestrules.CollateralType> collateralTypeList = null;
        com.clevel.selos.integration.brms.service.standardpricing.interestrules.CollateralType collateralType = null;

        String applicationNumber = null;
        Date processDate = null;
        Date appInDate = null;
        String collateralPotertialForPricing = null;
        String personalId = null;
        String productGroup = null;
        BigDecimal totalProposedCreditLimit = null;
        String productProgram = null;
        String strCreditFacilityType = null;
        BigDecimal creditLimit = null;
        String collateralPotential = null;
        String strCollateralType = null;
        try {
            request = new com.clevel.selos.integration.brms.service.standardpricing.interestrules.DecisionServiceRequest();
            applicationType = request.getUnderwritingRequest().getUnderwritingApprovalRequest().getApplication();
            applicationTypeLevel = inputModel.getApplicationType();

            applicationNumber = applicationTypeLevel.getApplicationNumber();                            //1
            log.debug("Application Number is {}", applicationNumber);
            applicationType.setApplicationNumber(applicationNumber);

            try {
                processDate = applicationTypeLevel.getProcessDate();
                log.debug("Process Date is {}", processDate);
                gregory = new GregorianCalendar();
                gregory.setTime(processDate);                                                           //2
                calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                applicationType.setDateOfApplication(calendar);
            } catch (Exception e) {
                processDate = new Date();
                log.debug("Exception Process Date is {}", processDate);
                gregory = new GregorianCalendar();
                gregory.setTime(processDate);
                calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                applicationType.setDateOfApplication(calendar);
            }

            attributeTypeList = applicationType.getAttribute();
            attributeType = new com.clevel.selos.integration.brms.service.standardpricing.interestrules.AttributeType();

            try {
                appInDate = applicationTypeLevel.getAttribute().getAppInDate();                         //3
                log.debug("App in Date is {}", appInDate);
                gregory = new GregorianCalendar();
                gregory.setTime(appInDate);
                calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                attributeType.setName("App In Date");
                attributeType.setDateTimeValue(calendar);
                attributeTypeList.add(attributeType);
            } catch (Exception e) {
                appInDate = new Date();
                log.debug("Exception App in Date is {}", appInDate);
                gregory = new GregorianCalendar();
                gregory.setTime(new Date());
                calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                attributeType.setName("App In Date");
                attributeType.setDateTimeValue(calendar);
                attributeTypeList.add(attributeType);
            }

            attributeType = new com.clevel.selos.integration.brms.service.standardpricing.interestrules.AttributeType();
            attributeType.setName("Collateral Potential");
            collateralPotertialForPricing = applicationTypeLevel.getCollateralPotertialForPricing();      //48//Enum = CollateralPotentialEnum
            attributeType.setStringValue(collateralPotertialForPricing);
            log.debug("Enum is {}", "CollateralPotentialEnum");
            log.debug("Attribute Name : {}, String value : {} ", "Collateral Potential", collateralPotertialForPricing);
            attributeTypeList.add(attributeType);

            borrowerTypeList = applicationType.getBorrower();

            borrowerTypeLevel = applicationTypeLevel.getBorrowerType();
            for (BorrowerTypeLevel model : borrowerTypeLevel) {
                borrowerType = new com.clevel.selos.integration.brms.service.standardpricing.interestrules.BorrowerType();
                personalId = model.getIndividualType().getPersonalId();                                    //69
                log.debug("Personal Id is {}", personalId);
                borrowerType.getIndividual().setCitizenID(personalId);
                borrowerTypeList.add(borrowerType);
            }

            productTypeList = applicationType.getProduct();

            productTypeLevel = applicationTypeLevel.getProductType();
            for (ProductTypeLevel model : productTypeLevel) {
                productType = new com.clevel.selos.integration.brms.service.standardpricing.interestrules.ProductType();//110//Enum = SELOSProductGroupEnum
                productGroup = model.getProductGroup();
                log.debug("Enum is {}", "SELOSProductGroupEnum");
                productType.setProductType(productGroup);
                log.debug("Product Group is {}", productGroup);

                totalProposedCreditLimit =  model.getTotalProposedCreditLimit();                           //112
                productType.setRequestedCreditLimit(totalProposedCreditLimit);
                log.debug("Total Proposed Credit Limit is {}", totalProposedCreditLimit);

                selosProductProgramTypeList = productType.getSelosProductProgram();
                selosProductProgramTypesLevel = model.getSelosProductProgramTypes();
                for (SELOSProductProgramTypeLevel selosProduct : selosProductProgramTypesLevel) {
                    selosProductProgramType = new com.clevel.selos.integration.brms.service.standardpricing.interestrules.SELOSProductProgramType();
                    productProgram = selosProduct.getProductProgram();                                      //113//Enum = SELOSProductProgramEnum
                    log.debug("Enum is {}", "SELOSProductProgramEnum");
                    selosProductProgramType.setName(productProgram);
                    log.debug("Product Program is {}", productProgram);

                    creditFacilityTypeList = selosProductProgramType.getCreditFacility();
                    creditFacilityTypeLevel = selosProduct.getCreditFacilityType();
                    for (CreditFacilityTypeLevel creditFacility : creditFacilityTypeLevel) {
                        creditFacilityType = new com.clevel.selos.integration.brms.service.standardpricing.interestrules.CreditFacilityType();
                        strCreditFacilityType = creditFacility.getCreditFacilityType();                     //114//Enum = CreditFacilityTypeEnum
                        log.debug("Enum is {}", "CreditFacilityTypeEnum");
                        creditFacilityType.setType(strCreditFacilityType);
                        log.debug("Credit Facility Type is {}", strCreditFacilityType);
                        creditLimit = creditFacility.getCreditLimit();                                      //115
                        creditFacilityType.setCreditLimit(creditLimit);
                        log.debug("Credit Limit is {}", creditLimit);

                        creditFacility.getGuaranteeType();                                                  //117//Enum = guaranteeTypeEnum  //todo : setGuaranteeType did not found

                        creditFacilityTypeList.add(creditFacilityType);
                    }
                    selosProductProgramTypeList.add(selosProductProgramType);
                }
                collateralTypeList = productType.getCollateral();
                collateralTypesLevel = model.getCollateralTypes();
                for (CollateralTypeLevel collateralTypeLevel : collateralTypesLevel) {
                    collateralType = new com.clevel.selos.integration.brms.service.standardpricing.interestrules.CollateralType();

                    attributeType = new com.clevel.selos.integration.brms.service.standardpricing.interestrules.AttributeType();
                    collateralPotential = collateralTypeLevel.getAttributeType().getCollateralPotential();  //119//Enum = CollateralPotentialEnum
                    attributeType.setStringValue(collateralPotential);
                    attributeType.setName("Collateral Potential");
                    log.debug("Enum is {}", "CollateralPotentialEnum");
                    log.debug("Attribute Name : {}, String value : {} ", "Collateral Potential", collateralPotential);
                    collateralType.getAttribute().add(attributeType);

                    log.debug("Enum is {}", "CollateralTypeEnum");
                    strCollateralType = collateralTypeLevel.getCollateralType();                           //120//Enum = CollateralTypeEnum
                    collateralType.setCollateralType(strCollateralType);
                    log.debug("Collateral Type is {}", strCollateralType);

                    collateralTypeList.add(collateralType);
                }
                productTypeList.add(productType);
            }
            request.getUnderwritingRequest().getUnderwritingApprovalRequest().setApplication(applicationType);
            return request;
        } catch (Exception e) {
            log.error("convertInputModelToRequestModel() Exception : {}", e);
            throw e;
        }

//        return null;
    }

    @Override
    public com.clevel.selos.integration.brms.service.standardpricing.feerules.DecisionServiceRequest convertInputModelToRequestModel(StandardPricingFeeRequest inputModel) throws Exception {
        log.debug("convertInputModelToRequestModel({})", inputModel.toString());
        com.clevel.selos.integration.brms.service.standardpricing.feerules.DecisionServiceRequest request = null;
        com.clevel.selos.integration.brms.service.standardpricing.feerules.ApplicationType applicationType = null;
        GregorianCalendar gregory = null;
        XMLGregorianCalendar calendar = null;
        List<com.clevel.selos.integration.brms.service.standardpricing.feerules.AttributeType> attributeTypeList = null;
        com.clevel.selos.integration.brms.service.standardpricing.feerules.AttributeType attributeType = null;
        List<com.clevel.selos.integration.brms.service.standardpricing.feerules.BorrowerType> borrowerTypeList = null;
        com.clevel.selos.integration.brms.service.standardpricing.feerules.BorrowerType borrowerType = null;
        List<com.clevel.selos.integration.brms.service.standardpricing.feerules.ProductType> productTypeList = null;
        com.clevel.selos.integration.brms.service.standardpricing.feerules.ProductType productType = null;
        List<com.clevel.selos.integration.brms.service.standardpricing.feerules.SELOSProductProgramType> selosProductProgramTypeList = null;
        com.clevel.selos.integration.brms.service.standardpricing.feerules.SELOSProductProgramType selosProductProgramType = null;
        List<com.clevel.selos.integration.brms.service.standardpricing.feerules.CreditFacilityType> creditFacilityTypeList = null;
        com.clevel.selos.integration.brms.service.standardpricing.feerules.CreditFacilityType creditFacilityType = null;
        List<com.clevel.selos.integration.brms.service.standardpricing.feerules.CollateralType> collateralTypeList = null;
        com.clevel.selos.integration.brms.service.standardpricing.feerules.CollateralType collateralType = null;

        String applicationNumber = null;
        Date processDate = null;
        Date appInDate = null;
        String collateralPotertialForPricing = null;
        String personalId = null;
        String productGroup = null;
        BigDecimal totalProposedCreditLimit = null;
        String productProgram = null;
        String strCreditFacilityType = null;
        BigDecimal creditLimit = null;
        String collateralPotential = null;
        String strCollateralType = null;
        try {
            request = new com.clevel.selos.integration.brms.service.standardpricing.feerules.DecisionServiceRequest();
            applicationType = request.getUnderwritingRequest().getUnderwritingApprovalRequest().getApplication();
            applicationTypeLevel = inputModel.getApplicationType();

            applicationNumber = applicationTypeLevel.getApplicationNumber();                            //1
            log.debug("Application Number is {}", applicationNumber);
            applicationType.setApplicationNumber(applicationNumber);

            try {
                processDate = applicationTypeLevel.getProcessDate();
                log.debug("Process Date is {}", processDate);
                gregory = new GregorianCalendar();
                gregory.setTime(processDate);                                                           //2
                calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                applicationType.setDateOfApplication(calendar);
            } catch (Exception e) {
                processDate = new Date();
                log.debug("Exception Process Date is {}", processDate);
                gregory = new GregorianCalendar();
                gregory.setTime(processDate);
                calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                applicationType.setDateOfApplication(calendar);
            }

            attributeTypeList = applicationType.getAttribute();
            attributeType = new com.clevel.selos.integration.brms.service.standardpricing.feerules.AttributeType();

            try {
                appInDate = applicationTypeLevel.getAttribute().getAppInDate();                         //3
                log.debug("App in Date is {}", appInDate);
                gregory = new GregorianCalendar();
                gregory.setTime(appInDate);
                calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                attributeType.setName("App In Date");
                attributeType.setDateTimeValue(calendar);
                attributeTypeList.add(attributeType);
            } catch (Exception e) {
                appInDate = new Date();
                log.debug("Exception App in Date is {}", appInDate);
                gregory = new GregorianCalendar();
                gregory.setTime(new Date());
                calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregory);
                attributeType.setName("App In Date");
                attributeType.setDateTimeValue(calendar);
                attributeTypeList.add(attributeType);
            }

            attributeType = new com.clevel.selos.integration.brms.service.standardpricing.feerules.AttributeType();
            attributeType.setName("Collateral Potential");
            collateralPotertialForPricing = applicationTypeLevel.getCollateralPotertialForPricing();      //48//Enum = CollateralPotentialEnum
            attributeType.setStringValue(collateralPotertialForPricing);
            log.debug("Enum is {}", "CollateralPotentialEnum");
            log.debug("Attribute Name : {}, String value : {} ", "Collateral Potential", collateralPotertialForPricing);
            attributeTypeList.add(attributeType);

            borrowerTypeList = applicationType.getBorrower();

            borrowerTypeLevel = applicationTypeLevel.getBorrowerType();
            for (BorrowerTypeLevel model : borrowerTypeLevel) {
                borrowerType = new com.clevel.selos.integration.brms.service.standardpricing.feerules.BorrowerType();
                personalId = model.getIndividualType().getPersonalId();                                    //69
                log.debug("Personal Id is {}", personalId);
                borrowerType.getIndividual().setCitizenID(personalId);
                borrowerTypeList.add(borrowerType);
            }

            productTypeList = applicationType.getProduct();

            productTypeLevel = applicationTypeLevel.getProductType();
            for (ProductTypeLevel model : productTypeLevel) {
                productType = new com.clevel.selos.integration.brms.service.standardpricing.feerules.ProductType();//110//Enum = SELOSProductGroupEnum
                productGroup = model.getProductGroup();
                log.debug("Enum is {}", "SELOSProductGroupEnum");
                productType.setProductType(productGroup);
                log.debug("Product Group is {}", productGroup);

                totalProposedCreditLimit =  model.getTotalProposedCreditLimit();                           //112
                productType.setRequestedCreditLimit(totalProposedCreditLimit);
                log.debug("Total Proposed Credit Limit is {}", totalProposedCreditLimit);

                selosProductProgramTypeList = productType.getSelosProductProgram();
                selosProductProgramTypesLevel = model.getSelosProductProgramTypes();
                for (SELOSProductProgramTypeLevel selosProduct : selosProductProgramTypesLevel) {
                    selosProductProgramType = new com.clevel.selos.integration.brms.service.standardpricing.feerules.SELOSProductProgramType();
                    productProgram = selosProduct.getProductProgram();                                      //113//Enum = SELOSProductProgramEnum
                    log.debug("Enum is {}", "SELOSProductProgramEnum");
                    selosProductProgramType.setName(productProgram);
                    log.debug("Product Program is {}", productProgram);

                    creditFacilityTypeList = selosProductProgramType.getCreditFacility();
                    creditFacilityTypeLevel = selosProduct.getCreditFacilityType();
                    for (CreditFacilityTypeLevel creditFacility : creditFacilityTypeLevel) {
                        creditFacilityType = new com.clevel.selos.integration.brms.service.standardpricing.feerules.CreditFacilityType();
                        strCreditFacilityType = creditFacility.getCreditFacilityType();                     //114//Enum = CreditFacilityTypeEnum
                        log.debug("Enum is {}", "CreditFacilityTypeEnum");
                        creditFacilityType.setType(strCreditFacilityType);
                        log.debug("Credit Facility Type is {}", strCreditFacilityType);
                        creditLimit = creditFacility.getCreditLimit();                                      //115
                        creditFacilityType.setCreditLimit(creditLimit);
                        log.debug("Credit Limit is {}", creditLimit);

                        creditFacility.getGuaranteeType();                                                  //117//Enum = guaranteeTypeEnum  //todo : setGuaranteeType did not found

                        creditFacilityTypeList.add(creditFacilityType);
                    }
                    selosProductProgramTypeList.add(selosProductProgramType);
                }
                collateralTypeList = productType.getCollateral();
                collateralTypesLevel = model.getCollateralTypes();
                for (CollateralTypeLevel collateralTypeLevel : collateralTypesLevel) {
                    collateralType = new com.clevel.selos.integration.brms.service.standardpricing.feerules.CollateralType();

                    attributeType = new com.clevel.selos.integration.brms.service.standardpricing.feerules.AttributeType();
                    collateralPotential = collateralTypeLevel.getAttributeType().getCollateralPotential();  //119//Enum = CollateralPotentialEnum
                    attributeType.setStringValue(collateralPotential);
                    attributeType.setName("Collateral Potential");
                    log.debug("Enum is {}", "CollateralPotentialEnum");
                    log.debug("Attribute Name : {}, String value : {} ", "Collateral Potential", collateralPotential);
                    collateralType.getAttribute().add(attributeType);

                    log.debug("Enum is {}", "CollateralTypeEnum");
                    strCollateralType = collateralTypeLevel.getCollateralType();                           //120//Enum = CollateralTypeEnum
                    collateralType.setCollateralType(strCollateralType);
                    log.debug("Collateral Type is {}", strCollateralType);

                    collateralTypeList.add(collateralType);
                }
                productTypeList.add(productType);
            }
            request.getUnderwritingRequest().getUnderwritingApprovalRequest().setApplication(applicationType);
            return request;
        } catch (Exception e) {
            log.error("convertInputModelToRequestModel() Exception : {}", e);
            throw e;
        }
    }


}
