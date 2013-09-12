package com.clevel.selos.integration.crs;


import com.clevel.selos.dao.ext.crs.CRSDataDAO;
import com.clevel.selos.exception.ValidationException;
import com.clevel.selos.model.db.ext.crs.CRSData;
import com.clevel.selos.system.ext.CRSAuditor;
import org.hibernate.NonUniqueResultException;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@WebService
public class CrsWebservice {
    private String message = new String("Hello , ");


    @Inject
    Logger log;

    @Inject
    CRSDataDAO crsDataDAO;

    @Inject
    CRSAuditor crsAuditor;

    @Inject
    public CrsWebservice() {

    }


    @WebMethod
    public String csrService(@WebParam String jobName,
                             @WebParam String caNumber,
                             @WebParam String oldCaNumber,
                             @WebParam String accountNo1,
                             @WebParam String customerId,
                             @WebParam String customerName,
                             @WebParam String citizenId,
                             @WebParam int requestType,
                             @WebParam int customerType,
                             @WebParam String bdmId,
                             @WebParam String hubCode,
                             @WebParam String regionCode,
                             @WebParam String uwId,
                             @WebParam String appInDateBDM,
                             @WebParam String finalApproved,
                             @WebParam String parallel,
                             @WebParam String pending,
                             @WebParam String caExist,
                             @WebParam String caEnd,
                             @WebParam String accountNo2,
                             @WebParam String accountNo3,
                             @WebParam String accountNo4,
                             @WebParam String accountNo5,
                             @WebParam String accountNo6,
                             @WebParam String accountNo7,
                             @WebParam String accountNo8,
                             @WebParam String accountNo9,
                             @WebParam String accountNo10,
                             @WebParam String appInDateUW,
                             @WebParam Date createDate) {
        log.debug("csrService : START");
        log.debug("csrService : Check CANumber ");
        CRSData crsData = crsDataDAO.findOneByCriteria(Restrictions.eq("caNumber", caNumber));


        if (crsData != null) {
            message = "HAVA A DATA";

        } else {
            message = "NO DATA";

            CRSData savebase = new CRSData();
            savebase.setCaNumber(caNumber);
            try {
                //create database record
                log.debug("csrService : Create database record ");
                crsAuditor.add(savebase);

            } catch (NonUniqueResultException x) {
                x.printStackTrace();
                log.debug("csrService Exception : {}",x.getMessage());
                return "Fail";
            }

        }
        log.debug("csrService : END");
        return message;
    }

}
