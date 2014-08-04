package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.StepValue;
import com.clevel.selos.model.db.working.WorkCaseOwner;
import org.hibernate.Criteria;
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


        List<WorkCaseOwner> workCaseOwnerList = createCriteria().add(Restrictions.eq("workCasePrescreen.id", workCasePreScreenId))
                                                    .add(Restrictions.eq("user.id",userId)).add(Restrictions.eq("role.id", roleId)).list();

        return workCaseOwnerList;
    }

    public List<WorkCaseOwner> findByWorkCaseId(long workCaseId, String userId, int roleId)
    {

        List<WorkCaseOwner> workCaseOwnerList = createCriteria().add(Restrictions.eq("workCase.id", workCaseId))
                .add(Restrictions.eq("user.id",userId)).add(Restrictions.eq("role.id", roleId)).list();

        return workCaseOwnerList;
    }

    public List<String> getWorkCaseByWorkCasePrescreenId(long workCasePrescreenId)
    {

        Criteria criteria = createCriteria();

        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePrescreenId));

        //criteria.setFetchMode("user", FetchMode.SELECT);

        //criteria.setFetchMode("createBy", FetchMode.SELECT);
        //criteria.setFetchMode("modifyBy", FetchMode.SELECT);

        List<WorkCaseOwner> workCaseOwnerList = criteria.list();

        Iterator<WorkCaseOwner> it = workCaseOwnerList.iterator();

        List userList = new ArrayList();

        while (it.hasNext())
        {
            WorkCaseOwner workCaseOwner = it.next();
            userList.add(workCaseOwner.getUser().getId());
        }

        return userList;
    }

    public int findWorkCaseOwner(long workCasePreScreenId, long workCaseId, String userId){
        Criteria criteria = createCriteria();
        if(workCasePreScreenId != 0) {
            criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePreScreenId));
        }
        if(workCaseId != 0){
            criteria.add(Restrictions.eq("workCase.id", workCaseId));
        }
        criteria.add(Restrictions.eq("user.id", userId));

        List<WorkCaseOwner> workCaseOwnerList = criteria.list();

        if(workCaseOwnerList == null)
            workCaseOwnerList = new ArrayList<WorkCaseOwner>();

        return workCaseOwnerList.size();
    }

    //Function for AppHeader
    public List<String> getWorkCaseByWorkCaseId(long workCaseId){
        Criteria criteria = createCriteria();

        criteria.add(Restrictions.eq("workCase.id", workCaseId));

        //criteria.setFetchMode("user.id", FetchMode.SELECT);
        //criteria.setFetchMode("createBy", FetchMode.SELECT);
        //criteria.setFetchMode("modifyBy", FetchMode.SELECT);

        List<WorkCaseOwner> workCaseOwnerList = criteria.list();

        Iterator<WorkCaseOwner> it = workCaseOwnerList.iterator();

        List userList = new ArrayList();

        while (it.hasNext())
        {
            WorkCaseOwner workCaseOwner = it.next();
            userList.add(workCaseOwner.getUser().getId());
        }

        return userList;
    }

    public WorkCaseOwner getWorkCaseOwnerByRole(long workCaseId, int roleId, String userId, long stepId){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("step.id", stepId));
        criteria.add(Restrictions.eq("role.id", roleId));
        criteria.add(Restrictions.eq("user.id", userId));
        WorkCaseOwner workCaseOwner = (WorkCaseOwner)criteria.uniqueResult();

        return workCaseOwner;
    }

    public WorkCaseOwner getWorkCaseOwnerPreScreen(long workCasePreScreenId, int roleId, String userId, long stepId){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePreScreenId));
        criteria.add(Restrictions.eq("step.id", stepId));
        criteria.add(Restrictions.eq("role.id", roleId));
        criteria.add(Restrictions.eq("user.id", userId));
        WorkCaseOwner workCaseOwner = (WorkCaseOwner)criteria.uniqueResult();

        return workCaseOwner;
    }

    public WorkCaseOwner getLatestWorkCaseOwnerByRole(long workCaseId, int roleId) {
    	Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("workCase.id", workCaseId));
        criteria.add(Restrictions.eq("role.id", roleId));
        criteria.addOrder(Order.desc("id"));
        List<WorkCaseOwner> list = criteria.list();
        if (list == null || list.isEmpty())
        	return null;
        else
        	return list.get(0);
    }

    public WorkCaseOwner getLatestUWActionDate(Long workCaseId)
    {

        Criteria criteria = createCriteria();

        List<Long> restrictionsStepList = new ArrayList<Long>();

        restrictionsStepList.add(Long.parseLong(String.valueOf(StepValue.CREDIT_DECISION_UW1.value())));

        restrictionsStepList.add(Long.parseLong(String.valueOf(StepValue.CREDIT_DECISION_UW2.value())));

        criteria.add(Restrictions.eq("workCase.id",workCaseId));

        criteria.add(Restrictions.in("step.id",restrictionsStepList));

        //criteria.setProjection(Projections.max("createDate"));

        criteria.addOrder(Order.desc("createDate"));

        criteria.setMaxResults(1);

        WorkCaseOwner workCaseOwner = (WorkCaseOwner)criteria.uniqueResult();

        return workCaseOwner;

    }

    public String getUW1(final long stepId, final long workCaseId){
        Criteria criteria = null;
        WorkCaseOwner workCaseOwner = null;
        try{
            criteria = createCriteria();
            criteria.add(Restrictions.eq("step.id", stepId));
            criteria.add(Restrictions.eq("workCase.id", workCaseId));
            workCaseOwner = (WorkCaseOwner)criteria.uniqueResult();
            if(workCaseOwner != null){
                return workCaseOwner.getUser() != null ? workCaseOwner.getUser().getId() : "";
            }
        } catch (Exception e) {
            log.debug("Error while getUW1 is processing {}", e);
        }
        return "";
    }

    public List<Integer> getWorkCaseRolesByWorkCaseId(long workCaseId){
        Criteria criteria = createCriteria();

        criteria.add(Restrictions.eq("workCase.id", workCaseId));

        List<WorkCaseOwner> workCaseOwnerList = criteria.list();

        Iterator<WorkCaseOwner> it = workCaseOwnerList.iterator();

        List roleList = new ArrayList();

        while (it.hasNext())
        {
            WorkCaseOwner workCaseOwner = it.next();
            roleList.add(workCaseOwner.getRole().getId());
        }

        return roleList;
    }

    public List<Integer> getWorkCaseRolesByWorkCasePreScreenId(long workCasePreScreenId)
    {

        Criteria criteria = createCriteria();

        criteria.add(Restrictions.eq("workCasePrescreen.id", workCasePreScreenId));

        List<WorkCaseOwner> workCaseOwnerList = criteria.list();

        Iterator<WorkCaseOwner> it = workCaseOwnerList.iterator();

        List roleList = new ArrayList();

        while (it.hasNext())
        {
            WorkCaseOwner workCaseOwner = it.next();
            roleList.add(workCaseOwner.getRole().getId());
        }

        return roleList;
    }
}
