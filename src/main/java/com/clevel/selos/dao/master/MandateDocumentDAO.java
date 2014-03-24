package com.clevel.selos.dao.master;


import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.MandateDocument;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class MandateDocumentDAO extends GenericDAO<MandateDocument, Long>{

    @Inject
    @SELOS
    Logger logger;
    @Inject
    public MandateDocumentDAO(){
    }

    public List<MandateDocument> findByStep(long stepId){
        logger.debug("findByCriteria Step:{}", stepId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("step.id", stepId));

        List<MandateDocument> mandateDocumentList = criteria.list();
        logger.debug("retrun List<MandateDocument> {}", mandateDocumentList);
        return mandateDocumentList;
    }
}
