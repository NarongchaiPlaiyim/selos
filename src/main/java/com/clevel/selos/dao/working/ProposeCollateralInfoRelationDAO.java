package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ProposeType;
import com.clevel.selos.model.db.working.ProposeCollateralInfoRelation;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class ProposeCollateralInfoRelationDAO extends GenericDAO<ProposeCollateralInfoRelation, Long> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public ProposeCollateralInfoRelationDAO() {
    }

    public List<ProposeCollateralInfoRelation> findByProposeLine(long proposeLineId, ProposeType proposeType) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("proposeLine.id", proposeLineId));
        criteria.add(Restrictions.eq("proposeType", proposeType));
        List<ProposeCollateralInfoRelation> proposeCollateralInfoRelationList = (List<ProposeCollateralInfoRelation>)criteria.list();
        return proposeCollateralInfoRelationList;
    }

    public List<ProposeCollateralInfoRelation> findByCollateralId(long collateralId, ProposeType proposeType) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("proposeCollateral.id", collateralId));
        criteria.add(Restrictions.eq("proposeType", proposeType));
        List<ProposeCollateralInfoRelation> proposeCollateralInfoRelationList = (List<ProposeCollateralInfoRelation>)criteria.list();
        return proposeCollateralInfoRelationList;
    }

    public List<ProposeCollateralInfoRelation> getListByExistingCreditDetailId(long existingCreditDetailId) {
        log.info("getListByExistingCreditDetailId. (existingCreditDetailId: {})", existingCreditDetailId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("existingCreditDetail.id", existingCreditDetailId));
        return  criteria.list();
    }

    public List<ProposeCollateralInfoRelation> findByNewCollateralId(final long newCollateralId) {
        log.info("-- findByNewCollateralId NewCollateral.id[{}]", newCollateralId);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("proposeCollateral.id", newCollateralId));
        criteria.addOrder(Order.asc("id"));
        List<ProposeCollateralInfoRelation> newCollateralCreditList = (List<ProposeCollateralInfoRelation>) criteria.list();
        log.debug("-- NewCollateralCreditList[{}]", newCollateralCreditList);
        return newCollateralCreditList;
    }
}
