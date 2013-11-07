package com.clevel.selos.dao.working;

import com.clevel.selos.dao.GenericDAO;
import com.clevel.selos.model.db.working.CollateralHeaderDetail;
import com.clevel.selos.model.db.working.SubCollateralDetail;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

public class SubCollateralDetailDAO extends GenericDAO<SubCollateralDetail, Long> {
    @Inject
    private Logger log;

    @Inject
    public SubCollateralDetailDAO(){

    }
    public List<SubCollateralDetail> findByCollateralHeaderDetail(CollateralHeaderDetail collateralHeaderDetail ){
        log.info("findByCollateralHeaderDetail begin ");
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("collateralHeaderDetail", collateralHeaderDetail));
        criteria.addOrder(Order.asc("no"));
        List<SubCollateralDetail> subCollateralDetailList = criteria.list();
        log.info("findByCollateralHeaderDetail. (result size: {})", subCollateralDetailList.size());

        return subCollateralDetailList;
    }
}

