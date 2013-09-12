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
    public String csrService(@WebParam(name = "jobName") String jobName,
                             @WebParam(name = "caNumber") String caNumber,
                             @WebParam(name = "oldCaNumber") String oldCaNumber,
                             @WebParam(name = "accountNo1") String accountNo1,
                             @WebParam(name = "customerId") String customerId,
                             @WebParam(name = "customerName") String customerName,
                             @WebParam(name = "citizenId") String citizenId,
                             @WebParam(name = "requestType") int requestType,
                             @WebParam(name = "customerType") int customerType,
                             @WebParam(name = "bdmId") String bdmId,
                             @WebParam(name = "hubCode") String hubCode,
                             @WebParam(name = "regionCode") String regionCode,
                             @WebParam(name = "uwId") String uwId,
                             @WebParam(name = "appInDateBDM") String appInDateBDM,
                             @WebParam(name = "finalApproved") String finalApproved,
                             @WebParam(name = "parallel") String parallel,
                             @WebParam(name = "pending") String pending,
                             @WebParam(name = "caExist") String caExist,
                             @WebParam(name = "caEnd") String caEnd,
                             @WebParam(name = "accountNo2") String accountNo2,
                             @WebParam(name = "accountNo3") String accountNo3,
                             @WebParam(name = "accountNo4") String accountNo4,
                             @WebParam(name = "accountNo5") String accountNo5,
                             @WebParam(name = "accountNo6") String accountNo6,
                             @WebParam(name = "accountNo7") String accountNo7,
                             @WebParam(name = "accountNo8") String accountNo8,
                             @WebParam(name = "accountNo9") String accountNo9,
                             @WebParam(name = "accountNo10") String accountNo10,
                             @WebParam(name = "appInDateUW") String appInDateUW,
                             @WebParam(name = "createDate") Date createDate) {
        log.debug("csrService : START");
        log.debug("csrService : Check CANumber ");
        CRSData crsData = null;
        try {
            //Search Ca
            crsData = crsDataDAO.findOneByCriteria(Restrictions.eq("caNumber", caNumber));

        } catch (NonUniqueResultException x) {
            x.printStackTrace();
            log.debug("csrService Exception : {}", x.getMessage());
            return "Fail";
        }

        if (crsData != null) {
            message = "HAVA A DATA";

        } else {
            message = "NO DATA";

            CRSData savebase = new CRSData();
            savebase.setCaNumber(caNumber);

            //create database record
            log.debug("csrService : Create database record ");
            crsAuditor.add(savebase);

        }
        log.debug("csrService : END");
        return message;
    }

}
