package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.StatusIdBasedOnStepId;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class StatusIdBasedOnStepIdDAO extends GenericDAO<StatusIdBasedOnStepId,String>
{
    @Inject
    @SELOS
    Logger log;

    public StatusIdBasedOnStepIdDAO()
    {

    }

    public List<StatusIdBasedOnStepId> getStatusidbasedonstepid(int stepidd)
    {
        log.info("stepid is ::::::::{}",stepidd);

        log.info("Controller comes to getStatusidbasedonstepid method of StatusIdbasedOnStepIdDAO class");


            List<StatusIdBasedOnStepId> statusIdBasedOnStepIdList = new ArrayList<StatusIdBasedOnStepId>();

            if(stepidd > 0)
            {
                log.info("controller comes in if condition of stepid value is greater than 0 ::{}",stepidd);

                Criteria criteria = getSession().createCriteria(StatusIdBasedOnStepId.class);

                criteria.setProjection(Projections.projectionList().add(Projections.property("statusid"),"statusid")) ;

                criteria.add(Restrictions.eq("stepid",stepidd)).setResultTransformer(Transformers.aliasToBean(StatusIdBasedOnStepId.class));

                statusIdBasedOnStepIdList = criteria.list();

                log.info("statusIdBasedOnStepIdListtttttttt  size is : ::::: {}",statusIdBasedOnStepIdList.size());

                statusIdBasedOnStepIdList = new ArrayList(new HashSet(statusIdBasedOnStepIdList));

                log.info("statusIdBasedOnStepIdListtttttttt  size is after : ::::: {}",statusIdBasedOnStepIdList.size());

                Iterator iterator = statusIdBasedOnStepIdList.iterator();

                while(iterator.hasNext() == true)
                {
                    StatusIdBasedOnStepId statusIdBasedOnStepId = new StatusIdBasedOnStepId();

                    statusIdBasedOnStepId = (StatusIdBasedOnStepId)iterator.next();

                    log.info("status id is ::::::::::: {}",statusIdBasedOnStepId.getStatusid());
                }

            }
            return statusIdBasedOnStepIdList;



    }

}
