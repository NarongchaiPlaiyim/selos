package com.clevel.selos.integration.brms.convert;

import com.clevel.selos.integration.BRMS;
import com.clevel.selos.integration.brms.model.BRMSField;
import com.clevel.selos.integration.brms.model.request.BRMSApplicationInfo;
import com.clevel.selos.integration.brms.service.prescreenunderwritingrules.AttributeType;
import com.clevel.selos.integration.brms.service.prescreenunderwritingrules.DecisionServiceRequest;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.xml.datatype.DatatypeFactory;
import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;

public class PrescreenConverter extends Converter{
    @Inject
    @BRMS
    Logger logger;

    @Inject
    public PrescreenConverter(){}

    public DecisionServiceRequest getDecisionServiceRequest(BRMSApplicationInfo applicationInfo){
        DecisionServiceRequest decisionServiceRequest = new DecisionServiceRequest();
        return decisionServiceRequest;
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
