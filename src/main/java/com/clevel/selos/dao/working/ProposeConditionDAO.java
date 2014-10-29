package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.DecisionType;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.working.ProposeConditionInfo;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ProposeConditionDAO extends GenericDAO<ProposeConditionInfo, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public ProposeConditionDAO() {
    }

    /*public List<ProposeConditionInfo> findByWorkCaseAndProposeType(long workCaseId, ProposeType proposeType){
        Criteria criteria = createCriteria();
        criteria.createAlias("proposeLine", "propose");
        criteria.createAlias("propose.workCase", "wrkCase");
        criteria.add(Restrictions.eq("wrkCase.id", workCaseId));
        criteria.add(Restrictions.eq("proposeType",proposeType));
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }*/
}
