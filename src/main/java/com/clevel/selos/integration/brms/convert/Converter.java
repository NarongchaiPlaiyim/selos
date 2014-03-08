package com.clevel.selos.integration.brms.convert;

import com.clevel.selos.integration.BRMS;
import com.clevel.selos.integration.brms.model.BRMSFieldAttributes;
import com.clevel.selos.integration.brms.model.request.*;
import com.tmbbank.enterprise.model.AttributeType;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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
            logger.error("Cannot convert XML");
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
}
