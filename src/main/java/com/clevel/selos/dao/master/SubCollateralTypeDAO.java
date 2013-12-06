package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.CollateralType;
import com.clevel.selos.model.db.master.SubCollateralType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;

public class SubCollateralTypeDAO extends GenericDAO<SubCollateralType, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public SubCollateralTypeDAO() {
    }

    public SubCollateralType findByBySubColCode(SubCollateralType subCollateralType) {
        log.info("getListBySubColCode. (subCollateralType: {})", subCollateralType);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("collateralType", subCollateralType.getCollateralType()));
        criteria.add(Restrictions.eq("code", subCollateralType.getCode()));
        SubCollateralType subCollateralTypeResult = (SubCollateralType)criteria.uniqueResult();
        return subCollateralTypeResult;
    }

    public SubCollateralType findByHeadAndSubColCode(CollateralType collateralType, String subCode) {
        log.info("findByHeadCodeAndSubColCode. (collateralType: {}, subCode: {})", collateralType,subCode);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("collateralType.id", collateralType.getId()));
        criteria.add(Restrictions.eq("code", subCode));
        SubCollateralType subCollateralTypeResult = (SubCollateralType)criteria.uniqueResult();
        return subCollateralTypeResult;
    }
}
