package com.clevel.selos.integration.brms.convert;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.integration.brms.model.BRMSFieldAttributes;
import com.clevel.selos.integration.brms.model.request.BRMSAccountRequested;
import com.clevel.selos.integration.brms.model.request.BRMSApplicationInfo;
import com.clevel.selos.integration.brms.model.request.BRMSCustomerInfo;
import com.clevel.selos.integration.brms.model.response.DocCustomerResponse;
import com.clevel.selos.integration.brms.model.response.DocumentDetail;
import com.clevel.selos.model.DocLevel;
import com.ilog.rules.decisionservice.DecisionServiceRequest;
import com.ilog.rules.decisionservice.DecisionServiceResponse;
import com.ilog.rules.param.UnderwritingRequest;
import com.tmbbank.enterprise.model.*;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.xml.datatype.DatatypeFactory;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class DocCustomerConverter extends Converter{

    @Inject
    @SELOS
    Logger logger;

    @Inject
    public DocCustomerConverter(){
    }

    public DecisionServiceRequest getDecisionServiceRequest(BRMSApplicationInfo applicationInfo){
        logger.debug("-- Start getDecisionServiceRequest {}", applicationInfo);
        ApplicationType applicationType = new ApplicationType();

        applicationType.setApplicationNumber(getValueForInterface(applicationInfo.getApplicationNo()));
        try{
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(getValueForInterface(applicationInfo.getProcessDate()));
            applicationType.setDateOfApplication(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
        }catch (Exception ex){
            logger.error("Could not transform Date");
        }

        //1. Convert Value for Application Level//
        List<AttributeType> attributeTypeList = applicationType.getAttribute();
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.APP_IN_DATE, applicationInfo.getBdmSubmitDate()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.CUSTOMER_ENTITY, applicationInfo.getBorrowerType()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.EXISTING_SME_CUSTOMER, applicationInfo.isExistingSMECustomer()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.SAME_SET_OF_BORROWER, applicationInfo.isRequestLoanWithSameName()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.REFINANCE_IN_FLAG, applicationInfo.isRefinanceIN()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.REFINANCE_OUT_FLAG, applicationInfo.isRefinanceOUT()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.LENDING_REFER_TYPE, applicationInfo.getLoanRequestType()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.BA_FLAG, applicationInfo.isApplyBAwithCash()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.TOP_UP_BA_FLAG, applicationInfo.isTopupBA()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.TCG_FLAG, applicationInfo.isRequestTCG()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.STEP, applicationInfo.getStepCode()));
        if(applicationInfo.getReferredDocType() == null){
            attributeTypeList.add(getAttributeType(BRMSFieldAttributes.REFERENCE_DOCUMENT_TYPE, ""));
        }else{
            attributeTypeList.add(getAttributeType(BRMSFieldAttributes.REFERENCE_DOCUMENT_TYPE, applicationInfo.getReferredDocType()));
        }



        List<ProductType> productTypeList = applicationType.getProduct();
        ProductType productType = new ProductType();
        productType.setProductType(getValueForInterface(applicationInfo.getProductGroup()));
        productTypeList.add(productType);

        //1. Convert Value for Product Program - Acc/Requested//
        //Convert the each row of Credit Detail to be:
        // - Production Program - A
        //    - Credit Type A
        //    - Credit Type B
        List<SELOSProductProgramType> selosProductProgramTypeList = productType.getSelosProductProgram();
        List<BRMSAccountRequested> accountRequestedList = applicationInfo.getAccountRequestedList();

        SELOSProductProgramType selosProductProgramType = new SELOSProductProgramType();
        List<CreditFacilityType> creditFacilityTypeList = null;
        for(BRMSAccountRequested accountRequested: accountRequestedList){
            if(!accountRequested.getProductProgram().equals(selosProductProgramType.getName())){
                if(selosProductProgramType.getName() != null && !"".equals(selosProductProgramType.getName())){
                    selosProductProgramTypeList.add(selosProductProgramType);
                    selosProductProgramType = new SELOSProductProgramType();
                }
                selosProductProgramType.setID(getValueForInterface(accountRequested.getCreditDetailId()));
                selosProductProgramType.setName(getValueForInterface(accountRequested.getProductProgram()));
                creditFacilityTypeList = selosProductProgramType.getCreditFacility();
            }

            CreditFacilityType creditFacilityType = new CreditFacilityType();
            creditFacilityType.setID(getValueForInterface(accountRequested.getCreditDetailId()));
            creditFacilityType.setType(getValueForInterface(accountRequested.getCreditType()));

            creditFacilityTypeList.add(creditFacilityType);
        }

        //2. Convert Customer, NCB Account, Obligation Account//
        List<BRMSCustomerInfo> customerInfoList = applicationInfo.getCustomerInfoList();
        List<BorrowerType> borrowerTypeList = applicationType.getBorrower();
        for(BRMSCustomerInfo customerInfo : customerInfoList){
            BorrowerType borrowerType = new BorrowerType();
            borrowerType.setID(getValueForInterface(customerInfo.getCustomerId()));
            borrowerType.setNationality(getValueForInterface(customerInfo.getNationality()));
            borrowerType.setBotClass(getValueForInterface(customerInfo.getAdjustClass()));
            borrowerType.setKycRiskLevel(getValueForInterface(customerInfo.getKycLevel()));

            List<AttributeType> borrowerAttributeList = borrowerType.getAttribute();
            borrowerAttributeList.add(getAttributeType(BRMSFieldAttributes.CUSTOMER_ENTITY, customerInfo.getCustomerEntity()));
            borrowerAttributeList.add(getAttributeType(BRMSFieldAttributes.EXISTING_SME_CUSTOMER, customerInfo.isExistingSMECustomer()));
            borrowerAttributeList.add(getAttributeType(BRMSFieldAttributes.RELATIONSHIP_TYPE, customerInfo.getRelation()));
            borrowerAttributeList.add(getAttributeType(BRMSFieldAttributes.REFERENCE, customerInfo.getReference()));
            borrowerAttributeList.add(getAttributeType(BRMSFieldAttributes.SPOUSE_ID, customerInfo.getSpousePersonalID()));
            borrowerAttributeList.add(getAttributeType(BRMSFieldAttributes.SPOUSE_RELATIONSHIP_TYPE, customerInfo.getSpouseRelationType()));

            IndividualType individualType = new IndividualType();
            individualType.setCitizenID(getValueForInterface(customerInfo.getPersonalID()));
            individualType.setAge(getValueForInterface(customerInfo.getAgeMonths()));
            individualType.setMaritalStatus(getValueForInterface(customerInfo.getMarriageStatus()));
            borrowerType.setIndividual(individualType);

            borrowerTypeList.add(borrowerType);
        }

        UnderwritingApprovalRequestType underwritingApprovalRequestType = new UnderwritingApprovalRequestType();
        underwritingApprovalRequestType.setApplication(applicationType);

        UnderwritingRequest underwritingRequest = new UnderwritingRequest();
        underwritingRequest.setUnderwritingApprovalRequest(underwritingApprovalRequestType);

        DecisionServiceRequest decisionServiceRequest = new DecisionServiceRequest();
        decisionServiceRequest.setDecisionID(getDecisionID(applicationInfo.getApplicationNo(), applicationInfo.getStatusCode()));
        decisionServiceRequest.setUnderwritingRequest(underwritingRequest);
        return decisionServiceRequest;
    }

    public DocCustomerResponse getDocCustomerResponse(DecisionServiceResponse decisionServiceResponse){
        DocCustomerResponse docCustomerResponse = new DocCustomerResponse();
        if(decisionServiceResponse != null){

            docCustomerResponse.setDecisionID(decisionServiceResponse.getDecisionID());

            UnderwritingRequest underwritingRequest = decisionServiceResponse.getUnderwritingRequest();
            UnderwritingApprovalRequestType underwritingApprovalRequestType = underwritingRequest.getUnderwritingApprovalRequest();

            ApplicationType applicationType = underwritingApprovalRequestType.getApplication();
            docCustomerResponse.setApplicationNo(applicationType.getApplicationNumber());

            //Get Document Detail List in Borrower Level//
            List<BorrowerType> borrowerTypeList = applicationType.getBorrower();
            List<DocumentDetail> documentDetailList = new ArrayList<DocumentDetail>();
            for (BorrowerType borrowerType : borrowerTypeList){
                String docOwner = borrowerType.getID();
                /*
                if(borrowerType.getIndividual() != null){
                    docOwner = borrowerType.getIndividual().getCitizenID();
                }*/
                logger.debug("getting document owner from customer id {}", docOwner);
                List<DocumentSetType> documentSetTypeList = borrowerType.getRequiredDocumentSet();
                documentDetailList.addAll(getDocumentDetail(documentSetTypeList, docOwner, DocLevel.CUS_LEVEL));
            }

            //Get Document Detail List in Application Level//
            List<DocumentSetType> documentSetTypeList = applicationType.getRequiredDocumentSet();
            documentDetailList.addAll(getDocumentDetail(documentSetTypeList, null, DocLevel.APP_LEVEL));
            docCustomerResponse.setDocumentDetailList(documentDetailList);
        }

        return docCustomerResponse;
    }


}
