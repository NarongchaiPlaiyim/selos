package com.clevel.selos.dao.relation;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.relation.StepToStatus;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
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
        List<StepToStatus> stepToStatusList = new ArrayList<StepToStatus>();
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("currentStatus.id", statusId));
        criteria.add(Restrictions.eq("step.id", stepId));
        criteria.add(Restrictions.eq("activeRole.id", roleId));
        stepToStatusList = criteria.list();

        return stepToStatusList;
    }

}
