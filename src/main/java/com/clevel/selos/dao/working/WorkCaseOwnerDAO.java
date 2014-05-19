package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.StepValue;
import com.clevel.selos.model.db.master.WorkCaseOwner;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WorkCaseOwnerDAO extends GenericDAO<WorkCaseOwner, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public WorkCaseOwnerDAO() {
    }

    public List<WorkCaseOwner> findByWorkCasePreScreenId(long workCasePreScreenId, String userId, int roleId)
    {

        List<WorkCaseOwner> workCaseOwnerList = createCriteria().add(Restrictions.eq("workCasePrescreenId", workCasePreScreenId))
                                                    .add(Restrictions.eq("userid",userId)).add(Restrictions.eq("roleid", roleId)).list();

        return workCaseOwnerList;
    }

    public List<WorkCaseOwner> findByWorkCaseId(long workCaseId, String userId, int roleId)
    {

        List<WorkCaseOwner> workCaseOwnerList = createCriteria().add(Restrictions.eq("workCaseId", workCaseId))
                .add(Restrictions.eq("userid",userId)).add(Restrictions.eq("roleid", roleId)).list();

        return workCaseOwnerList;
    }

    public List<String> getWorkCaseByWorkCasePrescreenId(long workCasePrescreenId){
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

    public int findWorkCaseOwner(long workCasePreScreenId, long workCaseId, String userId){
        Criteria criteria = createCriteria();
        if(workCasePreScreenId != 0) {
            criteria.add(Restrictions.eq("workCasePrescreenId", workCasePreScreenId));
        }
        if(workCaseId != 0){
            criteria.add(Restrictions.eq("workCaseId", workCaseId));
        }
        criteria.add(Restrictions.eq("userid", userId));

        List<WorkCaseOwner> workCaseOwnerList = criteria.list();

        if(workCaseOwnerList == null)
            workCaseOwnerList = new ArrayList<WorkCaseOwner>();

        return workCaseOwnerList.size();
    }

    //Function for AppHeader
    public List<String> getWorkCaseByWorkCaseId(long workCaseId){
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

        //criteria.setProjection(Projections.max("createDate"));

        criteria.addOrder(Order.desc("createDate"));

        criteria.setMaxResults(1);

        WorkCaseOwner workCaseOwner = (WorkCaseOwner)criteria.uniqueResult();

        return workCaseOwner;

    }

    public String getUW1(long stepId, long workCaseId){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("stepId", stepId));
        criteria.add(Restrictions.eq("workCaseId", workCaseId));
        WorkCaseOwner workCaseOwner = (WorkCaseOwner)criteria.uniqueResult();

        if(workCaseOwner != null){
            return workCaseOwner.getUserid();
        }
        return "";
    }
}
