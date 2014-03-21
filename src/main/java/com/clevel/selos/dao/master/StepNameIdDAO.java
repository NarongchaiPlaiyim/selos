package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.StepNameId;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;

public class StepNameIdDAO  extends GenericDAO<StepNameId,Integer>
{
    @Inject
    @SELOS
    Logger log;

    public StepNameIdDAO()
    {

    }

    public List<StepNameId> getStepNameids()
    {
        List<StepNameId> stepNameidsList = new ArrayList<StepNameId>();

        log.info("controller comes to getStepNameids method of StepNameIdDAO class");

        Criteria criteria = getSession().createCriteria(StepNameId.class);

        criteria.setProjection( Projections.projectionList().add(Projections.property("id"),"id").add(Projections.projectionList().add(Projections.property("description"),"description"))).setResultTransformer(Transformers.aliasToBean(StepNameId.class));

        //criteria.setProjection( Projections.projectionList().add(Projections.property("description"),"description"));

        //criteria.setResultTransformer(Transformers.aliasToBean(StepNameId.class));

        stepNameidsList = criteria.list();

        log.info("stepNameIdsList size is ::::::::::: {}",stepNameidsList.size());

       Iterator iterator = stepNameidsList.iterator();

       log.info("iterator hasNext method return type : {}",iterator.hasNext());

        //List<StepNameId> stepnameidlist = new ArrayList<StepNameId>();

        while(iterator.hasNext() == true)
        {
            log.info("hasNext method is true that's y controller comes to while loop");

            StepNameId stepNameid = new StepNameId();

            stepNameid = (StepNameId)iterator.next();

            log.info("step id names : {}",stepNameid.getId());

             log.info("step desc : {}",stepNameid.getDescription());

            //stepnameidlist.add(String.valueOf(stepNameid.getId()));

        }

        //log.info("stepnameidlit string values : {}",stepnameidlist.size());

        return stepNameidsList;
    }
}
