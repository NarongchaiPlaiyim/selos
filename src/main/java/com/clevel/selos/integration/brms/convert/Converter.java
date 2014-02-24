package com.clevel.selos.integration.brms.convert;

import com.clevel.selos.integration.BRMS;
import com.clevel.selos.integration.brms.model.request.*;
import com.clevel.selos.integration.brms.model.request.data.*;
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
    Logger log;

    @Inject
    public Converter() {
    }

    protected String getDecisionID(String applicationNo, String statusCode){

        String decisionID = new StringBuilder("SELOS").append(applicationNo).append(statusCode == null ? "" : statusCode).append("_").append(simpleDateFormat.format(Calendar.getInstance().getTime())).toString();
        return decisionID;

    }


}
