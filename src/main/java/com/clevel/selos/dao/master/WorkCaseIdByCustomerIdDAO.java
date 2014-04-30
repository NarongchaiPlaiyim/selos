package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.WorkCaseIdByCustomerId;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Prashanth Reddy B
 * Date: 3/25/14
 * Time: 12:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkCaseIdByCustomerIdDAO extends GenericDAO<WorkCaseIdByCustomerId,String> {

    @Inject
    @SELOS
    Logger log;

    public WorkCaseIdByCustomerIdDAO()
    {

    }

    public List<WorkCaseIdByCustomerId> getWorkCaseIdByCustomerId(int customerId)
    {
        List<WorkCaseIdByCustomerId> workCaseIdList = new ArrayList<WorkCaseIdByCustomerId>();

        log.info("controller comes to getApplicationNoByWorkCaseId method of SearchApplicationNoDAO class");

        Criteria criteria = getSession().createCriteria(WorkCaseIdByCustomerId.class);

        criteria.setProjection( Projections.projectionList().add(Projections.property("workCaseId"),"workCaseId").add(Projections.property("wrokCasePreScreenId"),"wrokCasePreScreenId"));

        criteria.add(Restrictions.eq("id", customerId)).setResultTransformer(Transformers.aliasToBean(WorkCaseIdByCustomerId.class));

        workCaseIdList = criteria.list();

        log.info("applicationNumber list size is : {}",workCaseIdList.size());

        return workCaseIdList;

    }
}
