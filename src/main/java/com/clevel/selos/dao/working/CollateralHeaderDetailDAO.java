package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.CollateralDetail;
import com.clevel.selos.model.db.working.CollateralHeaderDetail;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class CollateralHeaderDetailDAO extends GenericDAO<CollateralHeaderDetail, Long> {
    @Inject
    private Logger log;

    @Inject
    public CollateralHeaderDetailDAO(){

    }

    public List<CollateralHeaderDetail> findByCollateralDetail(CollateralDetail collateralDetail ){
        log.info("findByCollateralDetail begin ");
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("collateralDetail", collateralDetail));
        criteria.addOrder(Order.asc("no"));
        List<CollateralHeaderDetail> collateralHeaderDetailList = criteria.list();
        log.info("findByCollateralDetail. (result size: {})", collateralHeaderDetailList.size());

        return collateralHeaderDetailList;
    }
}
