package com.clevel.selos.dao.master;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.CollateralType;
import com.clevel.selos.model.db.master.SubCollateralType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class SubCollateralTypeDAO extends GenericDAO<SubCollateralType, Integer> {
    @Inject
    @SELOS
    Logger log;
    @Inject
    public SubCollateralTypeDAO() {}


    public SubCollateralType findByHeadAndSubColCode(CollateralType collateralType, String subCode) {
        log.info("findByHeadCodeAndSubColCode. (collateralType: {}, subCode: {})", collateralType,subCode);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("collateralType.id", collateralType.getId()));
        criteria.add(Restrictions.eq("code", subCode));
        SubCollateralType subCollateralTypeResult = (SubCollateralType)criteria.uniqueResult();
        return subCollateralTypeResult;
    }

    public List<SubCollateralType> findByCollateralType(CollateralType collateralType) {
        log.info("findByCollateralType. (collateralType) :: {}",collateralType.getId());
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("collateralType.id", collateralType.getId()));
        criteria.addOrder(Order.asc("id"));
        List<SubCollateralType> list = (List<SubCollateralType>)criteria.list();
        log.info("getList. (result size: {})", list.size());
        return list;
    }

    public List<SubCollateralType> findByHeadAndSubColDefaultType(CollateralType collateralType) {
        log.info("findByHeadAndSubColDefaultType collateralType ::: {})", collateralType);
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("collateralType.id", collateralType.getId()));
        criteria.add(Restrictions.eq("defaultType", 1));
        List<SubCollateralType>  subCollateralTypeResult = (List<SubCollateralType>)criteria.list();
        log.info("subCollateralTypeResult getList :: {}",subCollateralTypeResult.size());
        return subCollateralTypeResult;
    }
}
