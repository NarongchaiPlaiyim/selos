package com.clevel.selos.integration.brms.convert;

import com.clevel.selos.integration.brms.model.BRMSFieldAttributes;
import com.clevel.selos.integration.brms.model.request.BRMSApplicationInfo;
import com.clevel.selos.integration.brms.model.response.DocAppraisalResponse;
import com.clevel.selos.integration.brms.model.response.DocCustomerResponse;
import com.clevel.selos.integration.brms.model.response.DocumentDetail;
import com.clevel.selos.model.DocLevel;
import com.ilog.rules.decisionservice.DecisionServiceRequest;
import com.ilog.rules.decisionservice.DecisionServiceResponse;
import com.ilog.rules.param.UnderwritingRequest;
import com.tmbbank.enterprise.model.*;

import javax.inject.Inject;
import javax.xml.datatype.DatatypeFactory;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class DocAppraisalConverter extends Converter{

    @Inject
    public DocAppraisalConverter() {

    }

    public DecisionServiceRequest getDecisionServiceRequest(BRMSApplicationInfo applicationInfo){
        logger.debug("-- begin getDecisionServiceRequest {}", applicationInfo);
        ApplicationType applicationType = new ApplicationType();

        applicationType.setApplicationNumber(applicationInfo.getApplicationNo());
        try{
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(applicationInfo.getProcessDate());
            applicationType.setDateOfApplication(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
        }catch (Exception ex){
            logger.error("Could not transform Date");
        }

        //1. Convert Value for Application Level//
        List<AttributeType> attributeTypeList = applicationType.getAttribute();
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.APP_IN_DATE, applicationInfo.getBdmSubmitDate()));
        attributeTypeList.add(getAttributeType(BRMSFieldAttributes.STEP, applicationInfo.getStepCode()));

        List<ProductType> productTypeList = applicationType.getProduct();
        ProductType productType = new ProductType();
        productType.setProductType(applicationInfo.getProductGroup());
        productTypeList.add(productType);

        UnderwritingApprovalRequestType underwritingApprovalRequestType = new UnderwritingApprovalRequestType();
        underwritingApprovalRequestType.setApplication(applicationType);

        UnderwritingRequest underwritingRequest = new UnderwritingRequest();
        underwritingRequest.setUnderwritingApprovalRequest(underwritingApprovalRequestType);

        DecisionServiceRequest decisionServiceRequest = new DecisionServiceRequest();
        decisionServiceRequest.setDecisionID(getDecisionID(applicationInfo.getApplicationNo(), applicationInfo.getStatusCode()));
        decisionServiceRequest.setUnderwritingRequest(underwritingRequest);

        logger.debug("-- end getDecisionServiceRequest return {}", decisionServiceRequest);
        return decisionServiceRequest;
    }

    public DocAppraisalResponse getDocAppraisalResponse(DecisionServiceResponse decisionServiceResponse){
        DocAppraisalResponse docAppraisalResponse = new DocAppraisalResponse();
        if(decisionServiceResponse != null){
            docAppraisalResponse.setDecisionID(decisionServiceResponse.getDecisionID());

            UnderwritingRequest underwritingRequest = decisionServiceResponse.getUnderwritingRequest();
            UnderwritingApprovalRequestType underwritingApprovalRequestType = underwritingRequest.getUnderwritingApprovalRequest();

            ApplicationType applicationType = underwritingApprovalRequestType.getApplication();
            docAppraisalResponse.setApplicationNo(applicationType.getApplicationNumber());

            //Get Document Detail List in Application Level//
            List<DocumentDetail> documentDetailList = new ArrayList<DocumentDetail>();

            List<DocumentSetType> documentSetTypeList = applicationType.getRequiredDocumentSet();
            documentDetailList.addAll(getDocumentDetail(documentSetTypeList, null, DocLevel.APP_LEVEL));
            docAppraisalResponse.setDocumentDetailList(documentDetailList);
        }
        logger.debug("-- end getDocAppraisalResponse return {}", docAppraisalResponse);
        return docAppraisalResponse;
    }
}
