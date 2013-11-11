package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.CollateralType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class CollateralTypeDAO extends GenericDAO<CollateralType, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public CollateralTypeDAO() {
    }

    public CollateralType findByCollateralCode(CollateralType collateralType) {
        log.info("getListByCollateralCode. (businessGroup: {})", collateralType);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("code", collateralType.getCode()));
        CollateralType collateralTypeResult = (CollateralType)criteria.uniqueResult();
        return collateralTypeResult;
    }
}
