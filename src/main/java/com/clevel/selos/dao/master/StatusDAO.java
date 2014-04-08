package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.Status;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class StatusDAO extends GenericDAO<Status, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public StatusDAO() {
    }

    public String statusNameById(int id)
    {

        long longId = new Integer(id).longValue();

        Criteria criteria = getSession().createCriteria(getEntityClass()).add(Restrictions.eq("id",longId));

        Status status = (Status)criteria.uniqueResult();

        return status.getDescription();

    }
}
