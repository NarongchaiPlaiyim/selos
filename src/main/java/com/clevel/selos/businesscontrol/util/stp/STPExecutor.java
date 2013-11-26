package com.clevel.selos.businesscontrol.util.stp;

import com.clevel.selos.integration.SELOS;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.SQLException;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class STPExecutor {
    @Inject
    @SELOS
    Logger log;
    @PersistenceContext
    protected EntityManager em;

    @Inject
    public STPExecutor() {
    }

    public String getApplicationNumber(String segmentCode) {
        String applicationNumber = "";
        try {
            Query query = em.createNativeQuery("select SLOS.GETAPPLICATIONNUMBER(:param) from dual");
            query.setParameter("param", segmentCode);
            applicationNumber = (String) query.getSingleResult();

            log.debug("applicationNumber: {}", applicationNumber);
        } catch (Exception e) {
            log.error("Exception getApplicationNumber! (message: {})", e.getMessage());
        }
        return applicationNumber;
    }



    public String addUserFromFile( Object... params) {

        String result=null;
        try{

            Query query=em.createNativeQuery("{CALL SLOS.PUSERFILEUPLOAD ( ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ? )}");

            for(int i =0;i < params.length;i++){
                System.out.println("oooooooooo "+params[i]);
                query.setParameter(i+1,params[i]);
            }
            query.setParameter(17,result);
            result=(String)query.getSingleResult();
//                query.executeUpdate();


        }catch (Exception e){
                 e.printStackTrace();
        }finally {
            try{

            }catch (Exception ex){ }
        }
        return result;
    }

}
