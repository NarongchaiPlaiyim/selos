package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.MandateDocAccess;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class MandateDocAccessDAO extends GenericDAO<MandateDocAccess, Long>{

    @Inject
    @SELOS
    Logger log;

    @Inject
    public MandateDocAccessDAO(){}

    public MandateDocAccess findByStepAndRole(long stepId, long roleId){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("step.id", stepId));
        criteria.add(Restrictions.eq("role.id", roleId));
        MandateDocAccess mandateDocAccess = null;
        try{
            mandateDocAccess = (MandateDocAccess)criteria.uniqueResult();
        }catch (Exception ex){
            log.warn("The result is not unique: ", ex);
            mandateDocAccess = (MandateDocAccess)criteria.list().get(0);
        }
        return mandateDocAccess;
    }
}
