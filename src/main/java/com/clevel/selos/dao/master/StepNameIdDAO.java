package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.StepNameId;
import com.clevel.selos.system.Config;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StepNameIdDAO  extends GenericDAO<StepNameId,Integer>
{
    @Inject
    @SELOS
    Logger log;

    @Inject
    @Config(name = "interface.pe.sql.inbox.stepnames")
    String stepNames;

    public StepNameIdDAO()
    {

    }

    public List<StepNameId> getStepNameIds()
    {
        List<StepNameId> stepNameIdList;

        log.debug("controller comes to getStepNameIds method of StepNameIdDAO class");

        Criteria criteria = getSession().createCriteria(StepNameId.class);

        String[] numberStrs = stepNames.split(",");
        List<Integer> numbers = new ArrayList<Integer>();
        for(int i = 0;i < numberStrs.length;i++){
            numbers.add(Integer.parseInt(numberStrs[i].trim()));
        }

        criteria.setProjection( Projections.projectionList().add(Projections.property("id"),"id").add(Projections.projectionList().add(Projections.property("description"),"description")));
        criteria.add(Restrictions.not(Restrictions.in("id",numbers)));
        criteria.addOrder(Order.asc("id"));
        criteria.setResultTransformer(Transformers.aliasToBean(StepNameId.class));

        stepNameIdList = criteria.list();

        log.info("stepNameIdsList size is ::::::::::: {}", stepNameIdList != null ? stepNameIdList.size() : null);

        return stepNameIdList;
    }
}
