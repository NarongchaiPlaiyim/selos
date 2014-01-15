package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.ServiceSegment;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ServiceSegmentDAO  extends GenericDAO<ServiceSegment, Integer> {

    @Inject
    @SELOS
    Logger log;
    @Inject
    public  ServiceSegmentDAO(){
    }

    public ServiceSegment findByCode(int code){
        Criteria criteria = getSession().createCriteria(getEntityClass())
                .add(Restrictions.eq("code", code));
        return (ServiceSegment)criteria.uniqueResult();
    }
}
