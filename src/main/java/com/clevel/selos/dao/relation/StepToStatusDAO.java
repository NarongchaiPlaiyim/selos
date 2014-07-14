package com.clevel.selos.dao.relation;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.relation.StepToStatus;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class StepToStatusDAO extends GenericDAO<StepToStatus, Long> {
    @Inject
    @SELOS
    Logger log;

    @Inject
    public StepToStatusDAO(){

    }

    public List<StepToStatus> getActionListByRole(long stepId, long statusId, int roleId){
        log.debug("getActionListByRole ::: stepId : {}, statusId : {}, roleId : {}", stepId, statusId, roleId);
        List<StepToStatus> stepToStatusList = null;
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("step.id", stepId));
        criteria.add(Restrictions.eq("currentStatus.id", statusId));
        criteria.add(Restrictions.eq("activeRole.id", roleId));
        stepToStatusList = (List<StepToStatus>)criteria.list();
        return stepToStatusList;
    }

    public StepToStatus getNextStep(long stepId, long statusId, long actionId){
        log.debug("getNextStep ::: stepId : {}, statusId : {}, actionId : {}", stepId, statusId, actionId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("step.id", stepId));
        criteria.add(Restrictions.eq("currentStatus.id", statusId));
        criteria.add(Restrictions.eq("action.id", actionId));
        StepToStatus stepToStatus = (StepToStatus) criteria.uniqueResult();

        log.debug("getNextStep ::: stepToStatus : {}", stepToStatus);

        return stepToStatus;
    }

}
