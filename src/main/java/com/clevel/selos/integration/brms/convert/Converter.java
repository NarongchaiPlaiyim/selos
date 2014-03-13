package com.clevel.selos.integration.brms.convert;

import com.clevel.selos.integration.BRMS;
import com.clevel.selos.integration.brms.model.BRMSFieldAttributes;
import com.clevel.selos.integration.brms.model.request.*;
import com.clevel.selos.integration.brms.model.response.DocumentDetail;
import com.clevel.selos.model.DocLevel;
import com.tmbbank.enterprise.model.AttributeType;
import com.tmbbank.enterprise.model.DocumentSetType;
import com.tmbbank.enterprise.model.DocumentType;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class Converter implements Serializable {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");

    @Inject
    @BRMS
    Logger logger;

    @Inject
    public Converter() {
    }

    protected String getDecisionID(String applicationNo, String statusCode){
        logger.debug("getDecisionID with applicationNo {} and statusCode",applicationNo, statusCode);
        String decisionID = new StringBuilder("SELOS").append(applicationNo).append(statusCode == null ? "" : statusCode).append("_").append(simpleDateFormat.format(Calendar.getInstance().getTime())).toString();
        logger.debug("return decisionID", decisionID);
        return decisionID;
    }

    protected AttributeType getAttributeType(BRMSFieldAttributes field, Date value){
        AttributeType attributeType = new AttributeType();

        try{
            attributeType.setName(field.value());
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(value);
            attributeType.setDateTimeValue(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
        } catch (Exception ex){
            logger.warn("Cannot convert Date {}",value);
        }
        return attributeType;
    }

    protected AttributeType getAttributeType(BRMSFieldAttributes field, String value){
        AttributeType attributeType = new AttributeType();
        attributeType.setName(field.value());
        attributeType.setStringValue(value);
        return attributeType;
    }

    protected AttributeType getAttributeType(BRMSFieldAttributes field, BigDecimal value){
        AttributeType attributeType = new AttributeType();
        attributeType.setName(field.value());
        attributeType.setNumericValue(value);
        return attributeType;
    }

    protected AttributeType getAttributeType(BRMSFieldAttributes field, boolean existingSMECustomer){
        AttributeType attributeType = new AttributeType();
        attributeType.setName(field.value());
        attributeType.setBooleanValue(existingSMECustomer);
        return attributeType;
    }

    protected List<DocumentDetail> getDocumentDetail(List<DocumentSetType> documentSetTypeList, String owner, DocLevel docLevel){
        logger.debug("-- begin getDocumentDetail from List<DocumentSetType>{}", documentSetTypeList);

        List<DocumentDetail> documentDetailList = new ArrayList<DocumentDetail>();

        for(DocumentSetType documentSetType : documentSetTypeList){
            List<DocumentType> documentTypeList = documentSetType.getDocument();
            for(DocumentType documentType : documentTypeList){
                logger.debug("transform document type {}", documentType);
                DocumentDetail documentDetail = new DocumentDetail();
                documentDetail.setId(documentType.getID());
                documentDetail.setDescription(documentType.getDescription());
                documentDetail.setMandateFlag(documentType.isMandatoryFlag());

                List<AttributeType> attributeTypeList = documentType.getAttribute();
                for(AttributeType attributeType : attributeTypeList){
                    if(BRMSFieldAttributes.DOCUMENT_GROUP.value().equals(attributeType.getName())){
                        documentDetail.setDocumentGroup(attributeType.getStringValue());
                    } else if(BRMSFieldAttributes.CONDITION.value().endsWith(attributeType.getName())){
                        documentDetail.setCondition(attributeType.getStringValue());
                    } else if(BRMSFieldAttributes.STEP.value().equals(attributeType.getName())){
                        documentDetail.setStep(attributeType.getStringValue());
                    } else if(BRMSFieldAttributes.SHOW_FLAG.value().equals(attributeType.getName())){
                        documentDetail.setShowFlag(attributeType.getStringValue());
                    } else if(BRMSFieldAttributes.OPER_STEP.value().equals(attributeType.getName())){
                        documentDetail.setOperStep(attributeType.getStringValue());
                    } else if(BRMSFieldAttributes.OPER_SHOW_FLAG.value().equals(attributeType.getName())){
                        documentDetail.setOperShowFlag(attributeType.getStringValue());
                    } else if(BRMSFieldAttributes.OPER_MANDATORY_Flag.value().equals(attributeType.getName())){
                        documentDetail.setOperMandateFlag(attributeType.isBooleanValue());
                    }
                }
                documentDetail.setDocLevel(docLevel);
                documentDetail.setDocOwner(owner);
                logger.debug("to DocumentDetail {}", documentDetail);
                documentDetailList.add(documentDetail);
            }
        }

        logger.debug("-- end getDocumentDetail return {}", documentDetailList);
        return documentDetailList;
    }
}
