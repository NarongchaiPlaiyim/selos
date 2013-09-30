package com.clevel.selos.dao.stp;

import com.clevel.selos.dao.audit.BPMActivityDAO;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class STPExecutor {
    @Inject
    Logger log;
    @Inject
    BPMActivityDAO bpmActivityDAO;
    @PersistenceContext
    protected EntityManager em;

    @Inject
    public STPExecutor() {
    }

    public String getApplicationNumber() {
        String applicationNumber = "";
        try {
            Query query = em.createNativeQuery("select SLOS.GETAPPLICATIONNUMBER(:param) from dual");
            query.setParameter("param", "XX");
            applicationNumber = (String) query.getSingleResult();

            log.debug("applicationNumber: {}",applicationNumber);
        } catch (Exception e) {
            log.error("Exception getApplicationNumber! (message: {})", e.getMessage());
        }
        return applicationNumber;
    }

}
