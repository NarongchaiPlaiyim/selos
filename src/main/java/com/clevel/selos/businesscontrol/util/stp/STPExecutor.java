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
import java.sql.ResultSet;
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

        String result="";
        try{

            Query query=em.createNativeQuery("{call SLOS.PUSERFILEUPLOAD ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )}");

            for(int i =0;i < params.length;i++){
                System.out.println("data "+params[i]);
                query.setParameter(i+1,params[i]);

            }
//            query.setParameter(1,"'DELETE'");
//            query.setParameter(2,"'DELETE'");
//            query.setParameter(3,"'DELETE'");
//            query.setParameter(4,"'DELETE'");
//            query.setParameter(5,"'DELETE'");
//            query.setParameter(6,"'DELETE'");
//            query.setParameter(7,"'DELETE'");
//            query.setParameter(8,"'DELETE'");
//            query.setParameter(9,"'DELETE'");
//            query.setParameter(10,"'DELETE'");
//            query.setParameter(11,"'DELETE'");
//            query.setParameter(12,"'DELETE'");
//            query.setParameter(13,"'DELETE'");
//            query.setParameter(14,"'DELETE'");
//            query.setParameter(15,546);
//            query.setParameter(16,"'DELETE'");
            query.setParameter(17,result);
            query.executeUpdate();
            result= (String) query.getParameterValue(17);
            System.out.println("result : "+result);
            Object o=query.getParameterValue(17);
            System.out.println(".............    "+o.toString());

//            result= (String) query.getSingleResult();
//            System.out.println("result2 : "+result);

            result= (String) query.getParameterValue(17);
            System.out.println("result3 : "+result);

        }catch (Exception e){
                 e.printStackTrace();
            System.out.println("444444444444444444444 "+e.getMessage());
        }finally {
            try{

            }catch (Exception ex){ }
        }
        return result;
    }

}
