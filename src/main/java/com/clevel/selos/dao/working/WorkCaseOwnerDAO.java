package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.StepValue;
import com.clevel.selos.model.db.master.WorkCaseOwner;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class WorkCaseOwnerDAO extends GenericDAO<WorkCaseOwner, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public WorkCaseOwnerDAO() {
    }

    public List<String> getWorkCaseByWorkCasePrescreenId(Integer workCasePrescreenId){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreenId", workCasePrescreenId));
        criteria.setFetchMode("userid", FetchMode.SELECT);
        //criteria.setFetchMode("createBy", FetchMode.SELECT);
        //criteria.setFetchMode("modifyBy", FetchMode.SELECT);
        List<WorkCaseOwner> workCaseOwnerList = criteria.list();

        Iterator<WorkCaseOwner> it = workCaseOwnerList.iterator();

        List userList = new ArrayList();

        while (it.hasNext())
        {
            WorkCaseOwner workCaseOwner = it.next();
            userList.add(workCaseOwner.getUserid());
        }

        return userList;
    }

    //Function for AppHeader
    public List<String> getWorkCaseByWorkCaseId(Integer workCaseId){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCaseId", workCaseId));
        criteria.setFetchMode("userid", FetchMode.SELECT);
        //criteria.setFetchMode("createBy", FetchMode.SELECT);
        //criteria.setFetchMode("modifyBy", FetchMode.SELECT);
        List<WorkCaseOwner> workCaseOwnerList = criteria.list();

        Iterator<WorkCaseOwner> it = workCaseOwnerList.iterator();

        List userList = new ArrayList();

        while (it.hasNext())
        {
            WorkCaseOwner workCaseOwner = it.next();
            userList.add(workCaseOwner.getUserid());
        }

        return userList;
    }

    public WorkCaseOwner getLatestUWActionDate(Long workCaseId)
    {

        Criteria criteria = createCriteria();

        List<Integer> restrictionsStepList = new ArrayList<Integer>();

        restrictionsStepList.add(StepValue.CREDIT_DECISION_UW1.value());

        restrictionsStepList.add(StepValue.CREDIT_DECISION_UW2.value());

        criteria.add(Restrictions.eq("workCaseId",workCaseId.intValue()));

        criteria.add(Restrictions.in("stepId",restrictionsStepList));

        criteria.setProjection(Projections.max("createDate"));

        WorkCaseOwner workCaseOwner = (WorkCaseOwner)criteria.uniqueResult();

        return workCaseOwner;

    }
}
